package apps.smartme.coolspot.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import apps.smartme.coolspot.R;
import apps.smartme.coolspot.adapters.FavouriteAdapter;
import apps.smartme.coolspot.adapters.RecyclerTouchListener;
import apps.smartme.coolspot.domain.UserCoolspot;

public class FavouritesActivity extends AppCompatActivity {

    public static final String USER_LOCATION = "UserLocation";

    private RecyclerView mRecyclerView;
    private FavouriteAdapter mAdapter;
    private List<UserCoolspot> userCoolspotList = new ArrayList<>();
    private TextView favPlacesTextView;
    private Typeface typeface;


    private DatabaseReference databaseReference;
    private DatabaseReference userCoolspotReference;
    private ChildEventListener userCoolspotChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userCoolspotReference = databaseReference.child(USER_LOCATION);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userCoolspotList.add(dataSnapshot.getValue(UserCoolspot.class));
                mAdapter.notifyDataSetChanged();
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
        userCoolspotReference.orderByChild("hits").addChildEventListener(childEventListener);
        userCoolspotChildEventListener = childEventListener;

        setContentView(R.layout.activity_favourites);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
        favPlacesTextView = (TextView) findViewById(R.id.favourite_places_name);
        favPlacesTextView.setTypeface(typeface);
        mRecyclerView = (RecyclerView) findViewById(R.id.favourites_recycler_view);
        mAdapter = new FavouriteAdapter(userCoolspotList, typeface);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Coolpoint style = styleList.get(position);
//                Toast.makeText(FavouritesActivity.this, style.getPointName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userCoolspotChildEventListener != null) {
            userCoolspotReference.removeEventListener(userCoolspotChildEventListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
