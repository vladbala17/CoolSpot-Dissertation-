package apps.smartme.coolspot;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class CoolSpotActivity extends AppCompatActivity {

    private static final String TAG = "CoolSpotActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cool_spot);
        SearchView toolbar = (SearchView) findViewById(R.id.toolbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.filter_bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        CoolSpotMapFragment coolSpotMapFragment = new CoolSpotMapFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.map_fragment_container, coolSpotMapFragment).commit();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG,"onBackPressed was pressed");
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
}
