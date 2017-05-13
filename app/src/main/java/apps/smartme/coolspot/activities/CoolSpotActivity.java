package apps.smartme.coolspot.activities;

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
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import apps.smartme.coolspot.CoolSpotMapFragment;
import apps.smartme.coolspot.CustomTypefaceSpan;
import apps.smartme.coolspot.FontTypeface;
import apps.smartme.coolspot.R;

public class CoolSpotActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "CoolSpotActivity";
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cool_spot);
        SearchView toolbar = (SearchView) findViewById(R.id.toolbar);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                userID= null;
            } else {
                userID= extras.getString("userId");
            }
        } else {
            userID= (String) savedInstanceState.getSerializable("userId");
        }
        Log.d(TAG,"============================"+userID+"===================");
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.filter_bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        ImageView profilePictureView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        Picasso.with(this).load("https://graph.facebook.com/" + userID + "/picture").into(profilePictureView);


        changeTypeface(navigationView);
        CoolSpotMapFragment coolSpotMapFragment = new CoolSpotMapFragment();
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

        item = navigationView.getMenu().findItem(R.id.nav_camera);
        item.setTitle("Favorites");
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.nav_gallery);
        item.setTitle("History");
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.nav_slideshow);
        item.setTitle("Sync with facebook");
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.nav_manage);
        item.setTitle("Preferences");
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.nav_share);
        item.setTitle("Get premium");
        applyFontToItem(item, typeface);

        item = navigationView.getMenu().findItem(R.id.nav_send);
        item.setTitle("Rate the app");
        applyFontToItem(item, typeface);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

}
