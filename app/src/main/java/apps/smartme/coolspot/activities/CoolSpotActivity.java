package apps.smartme.coolspot.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import apps.smartme.coolspot.CoolSpotMapFragment;
import apps.smartme.coolspot.helpers.CustomTypefaceSpan;
import apps.smartme.coolspot.helpers.FontTypeface;
import apps.smartme.coolspot.R;
import apps.smartme.coolspot.adapters.CoolPointAdapter;

public class CoolSpotActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, SearchView.OnSuggestionListener, SearchView.OnCloseListener {

    private static final String TAG = "CoolSpotActivity";

    public static final String COOLPOINT = "Coolpoint";
    public static final String USER_ID_TOKEN = "userId";

    private ImageButton logoutButton;
    private CoolSpotMapFragment coolSpotMapFragment;
    private SearchManager manager;
    private SearchView coolPointSearchView;
    private List<String> coolpointList = new ArrayList<>();
    private List<String> arraylist = new ArrayList<>();
    private String userID;

    private DatabaseReference databaseReference;
    private DatabaseReference coolPointReference;
    private ChildEventListener coolPointChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cool_spot);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        coolPointReference = databaseReference.child(COOLPOINT);

        coolPointSearchView = (SearchView) findViewById(R.id.sv_coolpoint);
        coolPointSearchView.setOnQueryTextListener(this);
        coolPointSearchView.setOnSuggestionListener(this);
        coolPointSearchView.setOnCloseListener(this);
        manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        coolPointSearchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                userID = null;
            } else {
                userID = extras.getString(USER_ID_TOKEN);
            }
        } else {
            userID = (String) savedInstanceState.getSerializable(USER_ID_TOKEN);
        }
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.filter_bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.getMenu().getItem(3).setChecked(true);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        ImageView profilePictureView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        Picasso.with(this).load("https://graph.facebook.com/" + userID + "/picture").into(profilePictureView);
        logoutButton = (ImageButton) navigationView.getHeaderView(0).findViewById(R.id.btn_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                AccessToken.setCurrentAccessToken(null);
                Intent intent = new Intent(CoolSpotActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        coolSpotMapFragment = new CoolSpotMapFragment();
        changeTypeface(navigationView);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.map_fragment_container, coolSpotMapFragment).commit();
    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed was pressed");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ChildEventListener childEventCoolspotListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildAdded search list populated");
                coolpointList.add(dataSnapshot.getKey());
                arraylist.add(dataSnapshot.getKey());
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
        coolPointReference.addChildEventListener(childEventCoolspotListener);
        coolPointChildEventListener = childEventCoolspotListener;

    }

    private void applyFontToItem(MenuItem item, Typeface font) {
        SpannableString mNewTitle = new SpannableString(item.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font, 16), 0,
                mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        item.setTitle(mNewTitle);
    }

    private void changeTypeface(NavigationView navigationView) {
        FontTypeface fontTypeface = new FontTypeface(this);
        Typeface typeface = fontTypeface.getTypefaceAndroid();

        MenuItem item;

        item = navigationView.getMenu().findItem(R.id.nav_fav_places);
        item.setTitle("Favorites");
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.nav_history_places);
        item.setTitle("History");
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.nav_settings);
        item.setTitle("Preferences");
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.nav_get_premium);
        item.setTitle("Get premium");
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.nav_rate_app);
        item.setTitle("Rate the app");
        applyFontToItem(item, typeface);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_fav_places:
                intent = new Intent(this, FavouritesActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_history_places:
                intent = new Intent(this, HistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_get_premium:
                break;
            case R.id.nav_rate_app:
                break;
            case R.id.boys_filter:
                break;
            case R.id.girls_filter:
                coolSpotMapFragment.populateMapWithFilter("girls");
                break;
            case R.id.nerd_filter:
                coolSpotMapFragment.populateMapWithFilter("nerd");
                break;
            case R.id.drink_filter:
                coolSpotMapFragment.populateMapWithFilter("drink");
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase(Locale.getDefault());
        coolpointList.clear();
        if (newText.length() == 0) {
            coolpointList.addAll(arraylist);
        } else {
            for (String style : arraylist) {
                if (style.toLowerCase(Locale.getDefault()).contains(newText)) {
                    coolpointList.add(style);
                }
            }
        }
        loadHistory();
        return false;
    }

    private void loadHistory() {
        // Cursor
        String[] columns = new String[]{"_id", "text"};
        Object[] temp = new Object[]{0, "default"};
        MatrixCursor cursor = new MatrixCursor(columns);
        for (int i = 0; i < coolpointList.size(); i++) {
            temp[0] = i;
            temp[1] = coolpointList.get(i);
            cursor.addRow(temp);
        }

        CoolPointAdapter adapter = new CoolPointAdapter(this, cursor, coolpointList);
        coolPointSearchView.setSuggestionsAdapter(adapter);
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return true;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        Cursor cursor = coolPointSearchView.getSuggestionsAdapter().getCursor();
        cursor.moveToPosition(position);
        String suggestion = cursor.getString(1);//1 is the index of col containing suggestion name.
        coolPointSearchView.setQuery(suggestion, true);//setting suggestion
        coolSpotMapFragment.populateMapWithFilter(suggestion);
        return true;
    }

    @Override
    public boolean onClose() {
        coolSpotMapFragment.populateMapWithDrinkLocations();
        return false;
    }
}
