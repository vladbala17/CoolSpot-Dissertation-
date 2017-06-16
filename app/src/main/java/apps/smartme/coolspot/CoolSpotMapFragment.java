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
import android.text.format.DateFormat;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import apps.smartme.coolspot.dialogs.PlaceDefineDialog;
import apps.smartme.coolspot.dialogs.PlaceDetailsDialog;
import apps.smartme.coolspot.dialogs.PlacePickerDialog;
import apps.smartme.coolspot.domain.Coolpoint;
import apps.smartme.coolspot.domain.Coolspot;
import apps.smartme.coolspot.domain.CoolspotLocation;
import apps.smartme.coolspot.domain.UserCoolspot;

/**
 * Created by vlad on 26.03.2017.
 */

public class CoolSpotMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.

    private static final String TAG = CoolSpotMapFragment.class.getSimpleName();
    public static final int PLACE_PICKER_DIALOG = 1;
    public static final int PLACE_DEFINE_DIALOG = 2;
    public static final int PLACE_DEFINE_DIALOG_MARKER = 3;
    public static final String SELECTED_FIRST_COOLPOINT = "selected_first_coolpoint";
    public static final String SELECTED_SECOND_COOLPOINT = "selected_second_coolpoint";
    private static final long TWO_HOURS = 120 * 60 * 1000;

    //Firebase
    DatabaseReference databaseReference;
    DatabaseReference coolPointReference;
    DatabaseReference coolspotReference;
    DatabaseReference populateMapDrinkReference;
    DatabaseReference populateMapNerdReference;
    DatabaseReference populateMapGirlReference;
    DatabaseReference populateMapMusicReference;
    DatabaseReference coolSpotReference;
    DatabaseReference coolPointDrinkReference;
    DatabaseReference coolPointNerdReference;
    DatabaseReference coolPointGirlReference;
    DatabaseReference coolPointMusicReference;
    ValueEventListener coolSpotValueEventListener;
    ValueEventListener mapDrinkValueEventListener;
    ValueEventListener mapNerdValueEventListener;
    ValueEventListener mapGirlValueEventListener;
    ValueEventListener mapMusicValueEventListener;
    ChildEventListener coolPointChildEventListener;
    ChildEventListener coolPointDrinkChildEventListener;
    ChildEventListener coolPointNerdChildEventListener;
    ChildEventListener coolPointGirlChildEventListener;
    ChildEventListener coolPointMusicChildEventListener;


    List<Coolpoint> coolpointDrinkList = new ArrayList<>();
    List<Coolpoint> coolpointNerdList = new ArrayList<>();
    List<Coolpoint> coolpointGirlList = new ArrayList<>();
    List<Coolpoint> coolpointMusicList = new ArrayList<>();
    List<Coolpoint> coolpointFunList = new ArrayList<>();
    List<Coolpoint> coolpointGirlsList = new ArrayList<>();
    ImageButton recommendButton;


    private Location mLastKnownLocation;
    private CameraPosition mCameraPosition;
    LocationRequest mLocationRequest;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 12;
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
    private final DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");

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
        Log.d(TAG, "onCreate");
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();

        databaseReference = FirebaseDatabase.getInstance().getReference();
//        coolPointReference = databaseReference.child("Coolpoint");
        coolPointDrinkReference = databaseReference.child("CoolpointDrink");
        populateMapDrinkReference = databaseReference.child("CoolpointDrink");
        coolPointNerdReference = databaseReference.child("CoolpointNerd");
        populateMapNerdReference = databaseReference.child("CoolpointNerd");
        coolPointGirlReference = databaseReference.child("CoolpointGirl");
        populateMapGirlReference = databaseReference.child("CoolpointGirl");
        coolPointMusicReference = databaseReference.child("CoolpointMusic");
        populateMapMusicReference = databaseReference.child("CoolpointMusic");
        coolSpotReference = databaseReference.child("Coolspot");
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
//        getCoolspotsFromFirebase();
        getCoolspotNerdDb();
        getCoolspotDrinkDb();
        getCoolspotGirlDb();
        getCoolspotMusicDb();
        populateMapWithDrinkCoolspots();
        populateMapWithNerdCoolspots();
        populateMapWithGirlCoolspots();
        populateMapWithMusicCoolspots();
    }

    private void getCoolspotNerdDb() {
        ChildEventListener childEventCoolpointNerdListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Coolpoint coolpoint = dataSnapshot.getValue(Coolpoint.class);
                if (isRecentEnough(coolpoint.getTimestamp())) {
                    coolpointNerdList.add(dataSnapshot.getValue(Coolpoint.class));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        coolPointNerdReference.addChildEventListener(childEventCoolpointNerdListener);
        coolPointNerdChildEventListener = childEventCoolpointNerdListener;
    }

    private void getCoolspotMusicDb() {
        ChildEventListener childEventCoolpointMusicListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Coolpoint coolpoint = dataSnapshot.getValue(Coolpoint.class);
                if (isRecentEnough(coolpoint.getTimestamp())) {
                    coolpointMusicList.add(dataSnapshot.getValue(Coolpoint.class));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        coolPointMusicReference.addChildEventListener(childEventCoolpointMusicListener);
        coolPointMusicChildEventListener = childEventCoolpointMusicListener;
    }

    private void getCoolspotGirlDb() {
        ChildEventListener childEventCoolpointGirlListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Coolpoint coolpoint = dataSnapshot.getValue(Coolpoint.class);
                if (isRecentEnough(coolpoint.getTimestamp())) {
                    coolpointGirlList.add(dataSnapshot.getValue(Coolpoint.class));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        coolPointGirlReference.addChildEventListener(childEventCoolpointGirlListener);
        coolPointGirlChildEventListener = childEventCoolpointGirlListener;
    }

    private void getCoolspotDrinkDb() {
        ChildEventListener childEventCoolpointDrinkListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Coolpoint coolpoint = dataSnapshot.getValue(Coolpoint.class);
                if (isRecentEnough(coolpoint.getTimestamp())) {
                    coolpointDrinkList.add(dataSnapshot.getValue(Coolpoint.class));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        coolPointDrinkReference.addChildEventListener(childEventCoolpointDrinkListener);
        coolPointDrinkChildEventListener = childEventCoolpointDrinkListener;
    }

    private boolean isRecentEnough(long timestamp) {
        Long currentDateTimestamp = System.currentTimeMillis() / 1000;
        String fetchedDateTime = getDateFromTimestamp(timestamp);
        String currentTime = getDateFromTimestamp(currentDateTimestamp);
        DateTime lastModified = dtf.parseDateTime(fetchedDateTime);
        DateTime present = dtf.parseDateTime(currentTime);

        boolean result = Hours.hoursBetween(lastModified, present)
                .isLessThan(Hours.hours(2));
        Log.d(TAG, "db timestamp is " + getDateFromTimestamp(timestamp) + " difference is " + Hours.hoursBetween(lastModified, present).getHours() % 24 + " hours");
        return true;
    }

    private String getDateFromTimestamp(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("MM/dd/yyyy HH:mm:ss", cal).toString();
        Log.d(TAG, "CURRENT TIME FROM LONG TIMESTAMP" + date);
        return date;
    }

    private void populateMapWithDrinkCoolspots() {
        ValueEventListener populateMapValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                populateMapWithDrinkLocations();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        populateMapDrinkReference.addValueEventListener(populateMapValueEventListener);
        mapDrinkValueEventListener = populateMapValueEventListener;
    }

    private void populateMapWithNerdCoolspots() {
        ValueEventListener populateMapValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                populateMapWithNerdLocations();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        populateMapNerdReference.addValueEventListener(populateMapValueEventListener);
        mapNerdValueEventListener = populateMapValueEventListener;
    }

    private void populateMapWithGirlCoolspots() {
        ValueEventListener populateMapValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                populateMapWithGirlLocations();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        populateMapGirlReference.addValueEventListener(populateMapValueEventListener);
        mapGirlValueEventListener = populateMapValueEventListener;
    }

    private void populateMapWithMusicCoolspots() {
        ValueEventListener populateMapValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                populateMapWithMusicLocations();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        populateMapMusicReference.addValueEventListener(populateMapValueEventListener);
        mapMusicValueEventListener = populateMapValueEventListener;
    }

//    private void getCoolspotsFromFirebase() {
//        ChildEventListener childEventCoolspotListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Log.d(TAG, "====FIREBASE IN ACTION======");
//                switch (dataSnapshot.getKey()) {
//                    case "drink":
//                        collectDrinkCoolSpots((Map<String, Object>) dataSnapshot.getValue());
//                        break;
//                    case "girls":
//                        collectGirlsCoolSpots((Map<String, Object>) dataSnapshot.getValue());
//                        break;
//                    case "fun":
//                        collectFunCoolSpots((Map<String, Object>) dataSnapshot.getValue());
//                        break;
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        coolPointReference.addChildEventListener(childEventCoolspotListener);
//        coolPointChildEventListener = childEventCoolspotListener;
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//
//        if (coolPointChildEventListener != null) {
//            coolPointReference.removeEventListener(coolPointChildEventListener);
//        }
        if (coolPointDrinkChildEventListener != null) {
            coolPointDrinkReference.removeEventListener(coolPointDrinkChildEventListener);
        }

        if (coolPointNerdChildEventListener != null) {
            coolPointNerdReference.removeEventListener(coolPointNerdChildEventListener);
        }

        if (coolPointGirlChildEventListener != null) {
            coolPointGirlReference.removeEventListener(coolPointGirlChildEventListener);
        }

        if (coolPointMusicChildEventListener != null) {
            coolPointMusicReference.removeEventListener(coolPointMusicChildEventListener);
        }

        if (mapDrinkValueEventListener != null) {
            populateMapDrinkReference.removeEventListener(mapDrinkValueEventListener);
        }

        if (mapNerdValueEventListener != null) {
            populateMapNerdReference.removeEventListener(mapNerdValueEventListener);
        }

        if (mapGirlValueEventListener != null) {
            populateMapGirlReference.removeEventListener(mapGirlValueEventListener);
        }

        if (mapMusicValueEventListener != null) {
            populateMapMusicReference.removeEventListener(mapMusicValueEventListener);
        }

        if (coolSpotValueEventListener != null) {
            coolSpotReference.removeEventListener(coolSpotValueEventListener);
        }
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
        Log.d(TAG, "onMapReady()");
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
        Log.d(TAG, "updateLocationUI");
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
        Log.d(TAG, "onConnected()");
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
        mMap.setOnMarkerClickListener(this);
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(5000); //5 seconds
//        mLocationRequest.setFastestInterval(3000); //3 seconds
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter
//
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        //putLocationsOnMap(coolspotLocations);

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

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged()");
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

    private void collectDrinkCoolSpots(Map<String, Object> drinkCoolspots) {

        for (Map.Entry<String, Object> entry : drinkCoolspots.entrySet()) {
            Map singleCoolPoint = (Map) entry.getValue();
            Coolpoint coolpoint = new Coolpoint();
            coolpoint.setName((String) singleCoolPoint.get("name"));
            coolpoint.setLatitude((Double) singleCoolPoint.get("latitude"));
            coolpoint.setLongitude((Double) singleCoolPoint.get("longitude"));
            coolpoint.setCount((long) singleCoolPoint.get("count"));
            coolpointDrinkList.add(coolpoint);
            Log.d(TAG, "list1 is loaded");
        }
    }

    private void collectGirlsCoolSpots(Map<String, Object> drinkCoolspots) {

        for (Map.Entry<String, Object> entry : drinkCoolspots.entrySet()) {
            Map singleCoolPoint = (Map) entry.getValue();
            Coolpoint coolpoint = new Coolpoint();
            coolpoint.setName((String) singleCoolPoint.get("name"));
            coolpoint.setLatitude((Double) singleCoolPoint.get("latitude"));
            coolpoint.setLongitude((Double) singleCoolPoint.get("longitude"));
            coolpoint.setCount((long) singleCoolPoint.get("count"));
            coolpointGirlsList.add(coolpoint);
            Log.d(TAG, "list2 is loaded");
        }
    }

    private void collectFunCoolSpots(Map<String, Object> drinkCoolspots) {

        for (Map.Entry<String, Object> entry : drinkCoolspots.entrySet()) {
            Map singleCoolPoint = (Map) entry.getValue();
            Coolpoint coolpoint = new Coolpoint();
            coolpoint.setName((String) singleCoolPoint.get("name"));
            coolpoint.setLatitude((Double) singleCoolPoint.get("latitude"));
            coolpoint.setLongitude((Double) singleCoolPoint.get("longitude"));
            coolpoint.setCount((long) singleCoolPoint.get("count"));
            coolpointFunList.add(coolpoint);
            Log.d(TAG, "list3 is loaded");
        }
    }

    public void populateMapWithDrinkLocations() {
        Log.d(TAG, "populateMapWithDrinkLocations");
        for (Coolpoint coolpointDrink : coolpointDrinkList) {
            mMap.addMarker(new MarkerOptions()
                    .title(coolpointDrink.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_free_drinks))
                    .position(new LatLng(coolpointDrink.getLatitude(), coolpointDrink.getLongitude())));
            Log.d(TAG, "DRINK PLACE ADDED");
        }
    }

    private void populateMapWithNerdLocations() {
        for (Coolpoint coolpointNerd : coolpointNerdList) {
            mMap.addMarker(new MarkerOptions()
                    .title(coolpointNerd.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_nerd))
                    .position(new LatLng(coolpointNerd.getLatitude(), coolpointNerd.getLongitude())));
            Log.d(TAG, "NERD PLACE ADDED");
        }
    }

    private void populateMapWithGirlLocations() {
        for (Coolpoint coolpointGirl : coolpointGirlList) {
            mMap.addMarker(new MarkerOptions()
                    .title(coolpointGirl.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_girls))
                    .position(new LatLng(coolpointGirl.getLatitude(), coolpointGirl.getLongitude())));
            Log.d(TAG, "GIRL PLACE ADDED");
        }
    }

    private void populateMapWithMusicLocations() {
        for (Coolpoint coolpointMusic : coolpointMusicList) {
            mMap.addMarker(new MarkerOptions()
                    .title(coolpointMusic.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_music))
                    .position(new LatLng(coolpointMusic.getLatitude(), coolpointMusic.getLongitude())));
            Log.d(TAG, "MUSIC PLACE ADDED");
        }
    }

    public void populateDrinkFilter() {
        mMap.clear();
        for (Coolpoint coolpointDrink : coolpointDrinkList) {
            mMap.addMarker(new MarkerOptions()
                    .title(coolpointDrink.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_free_drinks))
                    .position(new LatLng(coolpointDrink.getLatitude(), coolpointDrink.getLongitude())));
            Log.d(TAG, "DRINK PLACE ADDED");
        }
    }

    private void populateNerdFilter() {
        mMap.clear();
        for (Coolpoint coolpointNerd : coolpointNerdList) {
            mMap.addMarker(new MarkerOptions()
                    .title(coolpointNerd.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_nerd))
                    .position(new LatLng(coolpointNerd.getLatitude(), coolpointNerd.getLongitude())));
            Log.d(TAG, "FUN PLACE ADDED");
        }
    }

    public void populateGirlsFilter() {
        mMap.clear();
        for (Coolpoint coolpointGirl : coolpointGirlList) {
            mMap.addMarker(new MarkerOptions()
                    .title(coolpointGirl.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_girls))
                    .position(new LatLng(coolpointGirl.getLatitude(), coolpointGirl.getLongitude())));
            Log.d(TAG, "GIRLS PLACE ADDED");
        }
    }

    public void populateMapWithFilter(String filter) {
        if (filter.equals("girls")) {
            populateGirlsFilter();
        } else if (filter.equals("nerd")) {
            populateNerdFilter();
        } else if (filter.equals("drink")) {
            populateDrinkFilter();
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
        final String coolPointFirst = data.getStringExtra(SELECTED_FIRST_COOLPOINT);
        final String coolPointSecond = data.getStringExtra(SELECTED_SECOND_COOLPOINT);
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
                    final String placeName = mLikelyPlaceNames[position];
                    if (mLikelyPlaceAttributions[position] != null) {
                        markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[position];
                    }
                    // Add a marker for the selected place, with an info window
                    // showing information about that place.
                    if (coolPointFirst.equals("drink")) {
                        mMap.addMarker(new MarkerOptions()
                                .title(mLikelyPlaceNames[position])
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_free_drinks))
                                .position(markerLatLng)
                                .snippet(markerSnippet));
                    } else {
                        mMap.addMarker(new MarkerOptions()
                                .title(mLikelyPlaceNames[position])
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bored))
                                .position(markerLatLng)
                                .snippet(markerSnippet));
                    }


                    // Position the map's camera at the location of the marker.
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                            DEFAULT_ZOOM));
                    final long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
                    final Coolspot coolspot = new Coolspot(placeName, timestamp, markerLatLng.latitude, markerLatLng.longitude, 1);
                    final UserCoolspot userCoolspot = new UserCoolspot(placeName, 1, timestamp, coolPointFirst, coolPointSecond);
                    coolSpotReference.child(placeName).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.getValue() != null) {
                                updateCoolspotChild(timestamp, placeName);
                                updateCoolPointDrinkChild(timestamp, placeName);
                                updateCoolpointFirst(placeName, coolPointFirst);
                                updateCoolspotUserChild(placeName);
                                updateUserLocationChild(placeName, userCoolspot);
//                                updateUserLocationDateChild(timestamp, placeName);
                            } else {
                                addCoolspotChild(placeName, coolspot);
                                addCoolspotDrinkChild(placeName, coolspot);
                                addCoolspotCoolpoint(placeName, coolPointFirst, coolPointSecond);
                                addCoolspotUsers(placeName, "Vlad Bala");
                                addUserLocationChild(placeName,userCoolspot);
//                                addUserLocationDateChild(placeName, timestamp);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });

                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // After Cancel code.
                }
                break;
        }
    }

    private void addCoolspotChild(final String placeName, Coolspot coolspot) {
        Map<String, Object> map = new HashMap<>();
        map.put(placeName, coolspot);
        DatabaseReference dbref = databaseReference.child("Coolspot");
        dbref.updateChildren(map);
    }

    private void addCoolspotDrinkChild(String placeName, Coolspot coolspot) {
        Map<String, Object> map = new HashMap<>();
        map.put(placeName, coolspot);
        DatabaseReference dbref = databaseReference.child("CoolpointDrink");
        dbref.updateChildren(map);
    }

    private void addCoolspotCoolpoint(String placeName, String coolspotFirst, String coolspotSecond) {
        Map<String, Object> map = new HashMap<>();
        map.put(coolspotFirst, 1);
        map.put(coolspotSecond, 1);
        DatabaseReference coolspotCoolpointFirstReferenceNotDefined = databaseReference.child("CoolspotCoolpoints").child(placeName);
        coolspotCoolpointFirstReferenceNotDefined.updateChildren(map);
    }

    private void addCoolspotUsers(String placeName, String user) {
        DatabaseReference coolspotUsersReferenceDefine = databaseReference.child("CoolspotUsers").child(placeName);
        Map<String, Object> map = new HashMap<>();
        map.put(user, true);
        coolspotUsersReferenceDefine.updateChildren(map);

    }

    private void addUserLocationChild(String placeName,UserCoolspot userCoolspot) {
        DatabaseReference usersLocationReferenceDefine = databaseReference.child("UserLocation");
        Map<String, Object> map = new HashMap<>();
        map.put(placeName, userCoolspot);
        usersLocationReferenceDefine.updateChildren(map);
    }

    private void addUserLocationDateChild(String placeName, long timestamp) {
        DatabaseReference usersLocationDateReferenceDefine = databaseReference.child("UserLocationDate");
        Map<String, Object> map = new HashMap<>();
        map.put(placeName, timestamp);
        usersLocationDateReferenceDefine.updateChildren(map);
    }

    private void updateUserLocationDateChild(long timestamp, String placeName) {
        DatabaseReference usersLocationDateReference = databaseReference.child("UserLocationDate");
        Map<String, Object> coolspotMap = new HashMap<>();
        coolspotMap.put(placeName, timestamp);
        usersLocationDateReference.updateChildren(coolspotMap);
    }

    private void updateUserLocationChild(String placeName, UserCoolspot userCoolspot) {
        DatabaseReference usersLocationReference = databaseReference.child("UserLocation").child(placeName);
        DatabaseReference usersLocationHitsReference = databaseReference.child("UserLocation").child(placeName).child("hits");
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", userCoolspot.getTimestamp());
        map.put("coolpointFirst", userCoolspot.getCoolpointFirst());
        map.put("coolpointSecond", userCoolspot.getCoolpointSecond());
        map.put("name",userCoolspot.getName());
        usersLocationReference.updateChildren(map);
        usersLocationHitsReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer currentCoolpointValue = mutableData.getValue(Integer.class);
                if (currentCoolpointValue == null) {
                    mutableData.setValue(1);
                } else {
                    mutableData.setValue(currentCoolpointValue + 1);
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    private void updateCoolspotUserChild(String placeName) {
        DatabaseReference coolspotUsersReference = databaseReference.child("CoolspotUsers").child(placeName);
        Map<String, Object> coolspotUserMap = new HashMap<>();
        coolspotUserMap.put("Vlad Bala", true);
        coolspotUsersReference.updateChildren(coolspotUserMap);
    }

    private void updateCoolpointFirst(String placeName, String coolPointFirst) {
        DatabaseReference coolspotCoolpointFirstReference = databaseReference.child("CoolspotCoolpoints").child(placeName).child(coolPointFirst);
        coolspotCoolpointFirstReference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer currentCoolpointValue = mutableData.getValue(Integer.class);
                if (currentCoolpointValue == null) {
                    mutableData.setValue(1);
                } else {
                    mutableData.setValue(currentCoolpointValue + 1);
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

            }
        });
    }

    private void updateCoolPointDrinkChild(long timestamp, String placeName) {
        Log.d(TAG, "updateCoolPointDrinkChild child is updated");
        final DatabaseReference popularityReferenceDrink = coolPointDrinkReference.child(placeName).child("popularity");
        Map<String, Object> coolspotMap = new HashMap<>();
        coolspotMap.put("timestamp", timestamp);
        coolPointDrinkReference.child(placeName).updateChildren(coolspotMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                popularityReferenceDrink.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Long currentPopularityValue = mutableData.getValue(Long.class);
                        if (currentPopularityValue == null) {
                            mutableData.setValue(1);
                        } else {
                            mutableData.setValue(currentPopularityValue + 1);
                        }

                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                    }
                });
            }
        });
    }

    private void updateCoolspotChild(long timestamp, String placeName) {
        Log.d(TAG, "updateCoolspotChild child is updated");

        final DatabaseReference popularityReference = databaseReference.child("Coolspot").child(placeName).child("popularity");

        Map<String, Object> coolspotMap = new HashMap<>();
        coolspotMap.put("timestamp", timestamp);
        coolSpotReference.child(placeName).updateChildren(coolspotMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                popularityReference.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Long currentPopularityValue = mutableData.getValue(Long.class);
                        if (currentPopularityValue == null) {
                            mutableData.setValue(1);
                        } else {
                            mutableData.setValue(currentPopularityValue + 1);
                        }

                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                    }
                });
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        coolSpotReference = databaseReference.child("Coolspot").child(marker.getTitle());
        ValueEventListener singleCoolSpotValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Coolspot coolSpot = dataSnapshot.getValue(Coolspot.class);
                PlaceDetailsDialog.newInstance(coolSpot.getName(), coolSpot.getTimestamp(), Long.toString(coolSpot.getPopularity())).show(getActivity().getSupportFragmentManager(), "placeDetails");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        coolSpotReference.addListenerForSingleValueEvent(singleCoolSpotValueListener);
        coolSpotValueEventListener = singleCoolSpotValueListener;
        return false;
    }
}
