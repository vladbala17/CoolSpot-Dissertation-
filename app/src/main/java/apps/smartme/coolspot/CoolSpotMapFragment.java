package apps.smartme.coolspot;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.smartme.coolspot.dialogs.PlaceDefineDialog;
import apps.smartme.coolspot.dialogs.PlaceDetailsDialog;
import apps.smartme.coolspot.dialogs.PlacePickerDialog;
import apps.smartme.coolspot.domain.CoolspotLocation;

/**
 * Created by vlad on 26.03.2017.
 */

public class CoolSpotMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.

    private static final String TAG = CoolSpotMapFragment.class.getSimpleName();
    public static final int PLACE_PICKER_DIALOG = 1;
    public static final int PLACE_DEFINE_DIALOG = 2;
    public static final int PLACE_DEFINE_DIALOG_MARKER = 3;


    List<CoolspotLocation> coolspotLocations = new ArrayList<CoolspotLocation>();
    ImageButton recommendButton;


    private Location mLastKnownLocation;
    private CameraPosition mCameraPosition;
    LocationRequest mLocationRequest;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 13;
    LatLng latLng;

    GoogleMap mMap;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // Used for selecting the current place.
    private final int mMaxEntries = 5;
    private String[] mLikelyPlaceNames = new String[mMaxEntries];
    private String[] mLikelyPlaceAddresses = new String[mMaxEntries];
    private String[] mLikelyPlaceAttributions = new String[mMaxEntries];
    private LatLng[] mLikelyPlaceLatLngs = new LatLng[mMaxEntries];

    // The entry point to Google Play services, used by the Places API and Fused Location Provider.
    private GoogleApiClient mGoogleApiClient;
    public static final String SELECTED_ITEM_POSITION = "selected_item_position";
    public static final String SELECTED_ITEM_NAME = "selected_item_name";


    public static CoolSpotMapFragment newInstance(List<CoolspotLocation> list) {

        Bundle args = new Bundle();
        CoolSpotMapFragment fragment = new CoolSpotMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);


        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("CoolspotLocation");
        ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        Map<String, Object> objectMap = (HashMap<String, Object>)
                                dataSnapshot.getValue();
                        for (Object obj : objectMap.values()) {
                            if (obj instanceof Map) {
                                Map<String, Object> mapObj = (Map<String, Object>) obj;
                                CoolspotLocation match = new CoolspotLocation();
                                match.setName((String) mapObj.get("name"));
                                match.setLatitude((double) mapObj.get("latitude"));
                                match.setLongitude((double) mapObj.get("longitude"));
                                coolspotLocations.add(match);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.map_fragment, viewGroup, false);
        recommendButton = (ImageButton) view.findViewById(R.id.recommend_btn);
        recommendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Proba", Toast.LENGTH_SHORT).show();
            }
        });

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        SupportMapFragment fragment = new SupportMapFragment();
        transaction.add(R.id.mapView, fragment);
        transaction.commit();

        fragment.getMapAsync(this);

        FloatingActionButton floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.btn_coolspot);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "You are here", Toast.LENGTH_SHORT).show();
                showCurrentPlace();

            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //  mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.style_map));

            if (!success) {
                Log.e(TAG, "Coolpoint parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }

        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;

        }

    }

    private void putLocationsOnMap(List<CoolspotLocation> coolspotLocations){
        for (CoolspotLocation coolspotLocation : coolspotLocations) {
            mMap.addMarker(new MarkerOptions()
                    .title(coolspotLocation.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_dance))
                    .position(new LatLng(coolspotLocation.getLatitude(), coolspotLocation.getLongitude())));

        }
    }


    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    public void getDeviceLocation() {
         /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }


        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        Toast.makeText(getActivity(), "onConnected", Toast.LENGTH_SHORT).show();
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
//            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(latLng);
//            markerOptions.title("Current Position");
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//            currLocationMarker = mGoogleMap.addMarker(markerOptions);
        }


        latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(5000); //5 seconds
//        mLocationRequest.setFastestInterval(3000); //3 seconds
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter
//
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        putLocationsOnMap(coolspotLocations);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Prompts the user to select the current place from a coolspotLocations of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    private void showCurrentPlace() {
        if (mMap == null) {
            return;
        }


        if (mLocationPermissionGranted) {
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission")
            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                    .getCurrentPlace(mGoogleApiClient, null);
            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {
                    int i = 0;
                    mLikelyPlaceNames = new String[mMaxEntries];
                    mLikelyPlaceAddresses = new String[mMaxEntries];
                    mLikelyPlaceAttributions = new String[mMaxEntries];
                    mLikelyPlaceLatLngs = new LatLng[mMaxEntries];
                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                        // Build a coolspotLocations of likely places to show the user. Max 5.
                        mLikelyPlaceNames[i] = (String) placeLikelihood.getPlace().getName();
                        mLikelyPlaceAddresses[i] = (String) placeLikelihood.getPlace().getAddress();
                        mLikelyPlaceAttributions[i] = (String) placeLikelihood.getPlace()
                                .getAttributions();
                        mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                        i++;
                        if (i > (mMaxEntries - 1)) {
                            break;
                        }
                    }
                    // Release the place likelihood buffer, to avoid memory leaks.
                    likelyPlaces.release();

                    // Show a dialog offering the user the coolspotLocations of likely places, and add a
                    // marker at the selected place.
                    openSuggestedPlacesDialog();
                }
            });
        }
    }

    private void openSuggestedPlacesDialog() {
        PlacePickerDialog placePickerDialog = PlacePickerDialog.newInstance(mLikelyPlaceNames);
        placePickerDialog.setTargetFragment(this, PLACE_PICKER_DIALOG);
        placePickerDialog.show(getActivity().getSupportFragmentManager(), "placePicker");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final String selectedItemName = data.getStringExtra(SELECTED_ITEM_NAME);
        final int position = data.getIntExtra(SELECTED_ITEM_POSITION, 0);
        Log.d(TAG, "//////////////////////" + position + "//////////////");
        switch (requestCode) {
            case PLACE_PICKER_DIALOG:
                if (resultCode == Activity.RESULT_OK) {
                    // After Ok code.
                    PlaceDefineDialog placeDefineDialog = PlaceDefineDialog.newInstance(selectedItemName, position);
                    placeDefineDialog.setTargetFragment(this, PLACE_DEFINE_DIALOG);
                    placeDefineDialog.show(getActivity().getSupportFragmentManager(), "placeDefine");
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // After Cancel code.
                }
                break;
            case PLACE_DEFINE_DIALOG:
                if (resultCode == Activity.RESULT_OK) {
                    // After Ok code.
                    LatLng markerLatLng = mLikelyPlaceLatLngs[position];
                    String markerSnippet = mLikelyPlaceAddresses[position];
                    if (mLikelyPlaceAttributions[position] != null) {
                        markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[position];
                    }
                    // Add a marker for the selected place, with an info window
                    // showing information about that place.
                    mMap.addMarker(new MarkerOptions()
                            .title(mLikelyPlaceNames[position])
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_dance))
                            .position(markerLatLng)
                            .snippet(markerSnippet));


                    // Position the map's camera at the location of the marker.
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                            DEFAULT_ZOOM));

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            PlaceDetailsDialog.newInstance(mLikelyPlaceNames[position]).show(getActivity().getSupportFragmentManager(), "placeDetails");

                            return false;
                        }
                    });
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // After Cancel code.
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //place marker at current position
        //mGoogleMap.clear();
//        if (currLocationMarker != null) {
//            currLocationMarker.remove();
//        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        markerOptions.title("Current Position");
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
//        currLocationMarker = mGoogleMap.addMarker(markerOptions);
//
//        Toast.makeText(this,"Location Changed",Toast.LENGTH_SHORT).show();

        //zoom to current position:
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    private void setPlaceMarkerOnTheMap(int requestCode) {
        switch (requestCode) {
            case PLACE_DEFINE_DIALOG_MARKER:
                break;
        }
    }
}
