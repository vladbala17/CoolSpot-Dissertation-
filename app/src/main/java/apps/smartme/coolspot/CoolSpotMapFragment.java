package apps.smartme.coolspot;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.ImageView;

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

import apps.smartme.coolspot.dialogs.CoolspotRecommendationDialog;
import apps.smartme.coolspot.dialogs.CoolspotDefineDialog;
import apps.smartme.coolspot.dialogs.CoolspotDetailsDialog;
import apps.smartme.coolspot.dialogs.CoolspotPickerDialog;
import apps.smartme.coolspot.domain.Coolpoint;
import apps.smartme.coolspot.domain.Coolspot;
import apps.smartme.coolspot.domain.Recommendation;
import apps.smartme.coolspot.domain.UserCoolspot;
import apps.smartme.coolspot.helpers.Helper;

/**
 * Created by vlad on 26.03.2017.
 */

public class CoolSpotMapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnMarkerClickListener {

    private static final String TAG = CoolSpotMapFragment.class.getSimpleName();

    public static final int PLACE_PICKER_DIALOG = 1;
    public static final int PLACE_DEFINE_DIALOG = 2;
    public static final int PLACE_DEFINE_DIALOG_MARKER = 3;
    public static final String SELECTED_FIRST_COOLPOINT = "selected_first_coolpoint";
    public static final String SELECTED_SECOND_COOLPOINT = "selected_second_coolpoint";
    private static final long TWO_HOURS = 120 * 60 * 1000;

    //Firebase nodes
    public static final String COOLPOINT_DRINK = "CoolpointDrink";
    public static final String COOLPOINT_NERD = "CoolpointNerd";
    public static final String COOLPOINT_GIRL = "CoolpointGirl";
    public static final String COOLPOINT_MUSIC = "CoolpointMusic";
    public static final String COOLPOINT_SPORT = "CoolpointSport";
    public static final String COOLPOINT_CHEAP = "CoolpointCheap";
    public static final String COOLPOINT_COMPUTER = "CoolpointComputer";
    public static final String COOLPOINT_EXPENSIVE = "CoolpointExpensive";
    public static final String COOLSPOT = "Coolspot";
    public static final String USER_LIKES = "UserLikes";

    //Firebase references
    private DatabaseReference databaseReference;
    private DatabaseReference coolPointReference;
    private DatabaseReference coolspotReference;
    private DatabaseReference populateMapDrinkReference;
    private DatabaseReference populateMapCheapReference;
    private DatabaseReference populateMapSportReference;
    private DatabaseReference populateMapComputerReference;
    private DatabaseReference populateMapExpensiveReference;
    private DatabaseReference coolSpotReference;
    private DatabaseReference coolPointDrinkReference;
    private DatabaseReference coolPointSportReference;
    private DatabaseReference coolPointCheapReference;
    private DatabaseReference coolPointComputerReference;
    private DatabaseReference coolPointExpensiveReference;
    private DatabaseReference coolPointRecommendationReference;
    private ValueEventListener coolSpotValueEventListener;
    private ValueEventListener mapDrinkValueEventListener;
    private ValueEventListener mapSportValueEventListener;
    private ValueEventListener mapCheapValueEventListener;
    private ValueEventListener mapComputerValueEventListener;
    private ValueEventListener mapExpensiveValueEventListener;
    private ChildEventListener coolPointChildEventListener;
    private ChildEventListener coolPointDrinkChildEventListener;
    private ChildEventListener coolPointSportChildEventListener;
    private ChildEventListener coolPointCheapChildEventListener;
    private ChildEventListener coolPointComputerChildEventListener;
    private ChildEventListener coolPointExpensiveChildEventListener;
    private ChildEventListener recomendationChildEventListener;


    private List<Coolpoint> coolpointDrinkList = new ArrayList<>();
    private List<Coolpoint> coolpointSportList = new ArrayList<>();
    private List<Coolpoint> coolpointCheapList = new ArrayList<>();
    private List<Coolpoint> coolpointComputerList = new ArrayList<>();
    private List<Coolpoint> coolpointExpensiveList = new ArrayList<>();
    private List<String> userLikesList = new ArrayList<>();
    private ImageView recommendButton;


    private Location mLastKnownLocation;
    private CameraPosition mCameraPosition;
    private LocationRequest mLocationRequest;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 12;
    private LatLng latLng;

    private GoogleMap mMap;
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
        initFirebaseReferences();
    }

    private void initFirebaseReferences() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        coolPointDrinkReference = databaseReference.child(COOLPOINT_DRINK);
        populateMapDrinkReference = databaseReference.child(COOLPOINT_DRINK);
        coolPointSportReference = databaseReference.child(COOLPOINT_SPORT);
        populateMapSportReference = databaseReference.child(COOLPOINT_SPORT);
        coolPointCheapReference = databaseReference.child(COOLPOINT_CHEAP);
        populateMapCheapReference = databaseReference.child(COOLPOINT_CHEAP);
        coolPointComputerReference = databaseReference.child(COOLPOINT_COMPUTER);
        populateMapComputerReference = databaseReference.child(COOLPOINT_COMPUTER);
        coolPointExpensiveReference = databaseReference.child(COOLPOINT_EXPENSIVE);
        populateMapExpensiveReference = databaseReference.child(COOLPOINT_EXPENSIVE);
        coolPointRecommendationReference = databaseReference.child(USER_LIKES);
        coolSpotReference = databaseReference.child(COOLSPOT);
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
//        getCoolspotsFromFirebase();
        getCoolspotDrinkDb();
        getCoolspotSportDb();
        getCoolspotCheapDb();
        getCoolspotComputerDb();
        getCoolspotExpensiveDb();
        populateMapWithDrinkCoolspots();
        populateMapWithSportCoolspots();
        populateMapWithCheapCoolspots();
        populateMapWithComputerCoolspots();
        populateMapWithExpensiveCoolspots();
    }

    private void getCoolspotSportDb() {
        ChildEventListener childEventCoolpointSportListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Coolpoint coolpoint = dataSnapshot.getValue(Coolpoint.class);
                if (isRecentEnough(coolpoint.getTimestamp())) {
                    coolpointSportList.add(dataSnapshot.getValue(Coolpoint.class));
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
        coolPointSportReference.addChildEventListener(childEventCoolpointSportListener);
        coolPointSportChildEventListener = childEventCoolpointSportListener;
    }

    private void getCoolspotExpensiveDb() {
        ChildEventListener childEventCoolpointExpensiveListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Coolpoint coolpoint = dataSnapshot.getValue(Coolpoint.class);
                if (isRecentEnough(coolpoint.getTimestamp())) {
                    coolpointExpensiveList.add(dataSnapshot.getValue(Coolpoint.class));
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
        coolPointExpensiveReference.addChildEventListener(childEventCoolpointExpensiveListener);
        coolPointExpensiveChildEventListener = childEventCoolpointExpensiveListener;
    }

    private void getCoolspotCheapDb() {
        ChildEventListener childEventCoolpointCheapListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Coolpoint coolpoint = dataSnapshot.getValue(Coolpoint.class);
                if (isRecentEnough(coolpoint.getTimestamp())) {
                    coolpointCheapList.add(dataSnapshot.getValue(Coolpoint.class));
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
        coolPointCheapReference.addChildEventListener(childEventCoolpointCheapListener);
        coolPointCheapChildEventListener = childEventCoolpointCheapListener;
    }

    private void getCoolspotComputerDb() {
        ChildEventListener childEventCoolpointComputerListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Coolpoint coolpoint = dataSnapshot.getValue(Coolpoint.class);
                if (isRecentEnough(coolpoint.getTimestamp())) {
                    coolpointComputerList.add(dataSnapshot.getValue(Coolpoint.class));
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
        coolPointComputerReference.addChildEventListener(childEventCoolpointComputerListener);
        coolPointComputerChildEventListener = childEventCoolpointComputerListener;
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

    private void getUserLikes() {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userLikesList.add(dataSnapshot.getValue(String.class));
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
        coolPointRecommendationReference.addChildEventListener(childEventListener);
        recomendationChildEventListener = childEventListener;
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

    public void populateMapWithSportCoolspots() {
        ValueEventListener populateMapValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                populateMapWithSportLocations();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        populateMapSportReference.addValueEventListener(populateMapValueEventListener);
        mapSportValueEventListener = populateMapValueEventListener;
    }

    private void populateMapWithExpensiveCoolspots() {
        ValueEventListener populateMapValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                populateMapWithExpensiveLocations();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        populateMapExpensiveReference.addValueEventListener(populateMapValueEventListener);
        mapExpensiveValueEventListener = populateMapValueEventListener;
    }

    private void populateMapWithCheapCoolspots() {
        ValueEventListener populateMapValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                populateMapWithCheapLocations();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        populateMapCheapReference.addValueEventListener(populateMapValueEventListener);
        mapCheapValueEventListener = populateMapValueEventListener;
    }

    private void populateMapWithComputerCoolspots() {
        ValueEventListener populateMapValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                populateMapWithComputerLocations();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        populateMapComputerReference.addValueEventListener(populateMapValueEventListener);
        mapComputerValueEventListener = populateMapValueEventListener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeFirebaseListeners();
    }

    private void removeFirebaseListeners() {
        if (coolPointDrinkChildEventListener != null) {
            coolPointDrinkReference.removeEventListener(coolPointDrinkChildEventListener);
        }

        if (coolPointSportChildEventListener != null) {
            coolPointSportReference.removeEventListener(coolPointSportChildEventListener);
        }

        if (coolPointCheapChildEventListener != null) {
            coolPointCheapReference.removeEventListener(coolPointCheapChildEventListener);
        }

        if (coolPointComputerChildEventListener != null) {
            coolPointComputerReference.removeEventListener(coolPointComputerChildEventListener);
        }

        if (coolPointExpensiveChildEventListener != null) {
            coolPointExpensiveReference.removeEventListener(coolPointExpensiveChildEventListener);
        }

        if (mapSportValueEventListener != null) {
            populateMapSportReference.removeEventListener(mapSportValueEventListener);
        }

        if (mapCheapValueEventListener != null) {
            populateMapCheapReference.removeEventListener(mapCheapValueEventListener);
        }

        if (mapComputerValueEventListener != null) {
            populateMapComputerReference.removeEventListener(mapComputerValueEventListener);
        }

        if (mapExpensiveValueEventListener != null) {
            populateMapExpensiveReference.removeEventListener(mapExpensiveValueEventListener);
        }

        if (mapDrinkValueEventListener != null) {
            populateMapDrinkReference.removeEventListener(mapDrinkValueEventListener);
        }

        if (coolSpotValueEventListener != null) {
            coolSpotReference.removeEventListener(coolSpotValueEventListener);
        }

        if (recomendationChildEventListener != null) {
            coolPointRecommendationReference.removeEventListener(recomendationChildEventListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.map_fragment, viewGroup, false);
        recommendButton = (ImageView) view.findViewById(R.id.recommend_btn);
        recommendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Proba", Toast.LENGTH_SHORT).show();
                Helper reccomendationHelper = new Helper(userLikesList);
                Recommendation recommendation = new Recommendation();
                CoolspotRecommendationDialog coolspotRecommendationDialog = CoolspotRecommendationDialog.newInstance(recommendation);
                coolspotRecommendationDialog.show(getActivity().getSupportFragmentManager(), "placeRecommendation");
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
//                Toast.makeText(getActivity(), "You are here", Toast.LENGTH_SHORT).show();
                showCurrentPlace();

            }
        });
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady()");
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
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

    public void populateMapWithSportLocations() {
        Log.d(TAG, "populateMapWithSportLocations");
        for (Coolpoint coolpointSport : coolpointSportList) {
            mMap.addMarker(new MarkerOptions()
                    .title(coolpointSport.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_basketball))
                    .position(new LatLng(coolpointSport.getLatitude(), coolpointSport.getLongitude())));
            Log.d(TAG, "SPORT PLACE ADDED");
        }
    }

    public void populateMapWithExpensiveLocations() {
        Log.d(TAG, "populateMapWithExpensiveLocations");
        for (Coolpoint coolpointExpensive : coolpointExpensiveList) {
            mMap.addMarker(new MarkerOptions()
                    .title(coolpointExpensive.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_expensive))
                    .position(new LatLng(coolpointExpensive.getLatitude(), coolpointExpensive.getLongitude())));
            Log.d(TAG, "EXPENSIVE PLACE ADDED");
        }
    }

    public void populateMapWithCheapLocations() {
        Log.d(TAG, "populateMapWithCheapLocations");
        for (Coolpoint coolpointCheap : coolpointCheapList) {
            mMap.addMarker(new MarkerOptions()
                    .title(coolpointCheap.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_cheap))
                    .position(new LatLng(coolpointCheap.getLatitude(), coolpointCheap.getLongitude())));
            Log.d(TAG, "CHEAP PLACE ADDED");
        }
    }

    public void populateMapWithComputerLocations() {
        Log.d(TAG, "populateMapWithComputerLocations");
        for (Coolpoint coolpointComputer : coolpointComputerList) {
            mMap.addMarker(new MarkerOptions()
                    .title(coolpointComputer.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_computer))
                    .position(new LatLng(coolpointComputer.getLatitude(), coolpointComputer.getLongitude())));
            Log.d(TAG, "COMPUTER PLACE ADDED");
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

    public void populateCheapFilter() {
        mMap.clear();
        for (Coolpoint coolpointCheap : coolpointCheapList) {
            mMap.addMarker(new MarkerOptions()
                    .title(coolpointCheap.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_cheap))
                    .position(new LatLng(coolpointCheap.getLatitude(), coolpointCheap.getLongitude())));
            Log.d(TAG, "CHEAP PLACE ADDED");
        }
    }

    public void populateComputerFilter() {
        mMap.clear();
        for (Coolpoint coolpointComputer : coolpointComputerList) {
            mMap.addMarker(new MarkerOptions()
                    .title(coolpointComputer.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_computer))
                    .position(new LatLng(coolpointComputer.getLatitude(), coolpointComputer.getLongitude())));
            Log.d(TAG, "CHEAP PLACE ADDED");
        }
    }


    public void populateMapWithFilter(String filter) {
        if (filter.equals("computer")) {
            populateComputerFilter();
        } else if (filter.equals("cheap")) {
            populateCheapFilter();
        } else if (filter.equals("drink")) {
            populateDrinkFilter();
        }
    }


    private void openSuggestedPlacesDialog() {
        CoolspotPickerDialog coolspotPickerDialog = CoolspotPickerDialog.newInstance(mLikelyPlaceNames);
        coolspotPickerDialog.setTargetFragment(this, PLACE_PICKER_DIALOG);
        coolspotPickerDialog.show(getActivity().getSupportFragmentManager(), "placePicker");
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
                    CoolspotDefineDialog coolspotDefineDialog = CoolspotDefineDialog.newInstance(selectedItemName, position);
                    coolspotDefineDialog.setTargetFragment(this, PLACE_DEFINE_DIALOG);
                    coolspotDefineDialog.show(getActivity().getSupportFragmentManager(), "placeDefine");
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
                                updateUserLocationDateChild(timestamp, placeName);
                            } else {
                                addCoolspotChild(placeName, coolspot);
                                addCoolspotDrinkChild(placeName, coolspot);
                                addCoolspotCoolpoint(placeName, coolPointFirst, coolPointSecond);
                                addCoolspotUsers(placeName, "Vlad Bala");
                                addUserLocationChild(placeName, userCoolspot);
                                addUserLocationDateChild(placeName, timestamp);
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
        DatabaseReference dbref = databaseReference.child(COOLSPOT);
        dbref.updateChildren(map);
    }

    private void addCoolspotDrinkChild(String placeName, Coolspot coolspot) {
        Map<String, Object> map = new HashMap<>();
        map.put(placeName, coolspot);
        DatabaseReference dbref = databaseReference.child(COOLPOINT_DRINK);
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

    private void addUserLocationChild(String placeName, UserCoolspot userCoolspot) {
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
        map.put("name", userCoolspot.getName());
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

        final DatabaseReference popularityReference = databaseReference.child(COOLSPOT).child(placeName).child("popularity");

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
        coolSpotReference = databaseReference.child(COOLSPOT).child(marker.getTitle());
        ValueEventListener singleCoolSpotValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Coolspot coolSpot = dataSnapshot.getValue(Coolspot.class);
                CoolspotDetailsDialog.newInstance(coolSpot.getName(), coolSpot.getTimestamp(), Long.toString(coolSpot.getPopularity())).show(getActivity().getSupportFragmentManager(), "placeDetails");
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
