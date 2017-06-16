package apps.smartme.coolspot.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

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
import apps.smartme.coolspot.domain.Coolpoint;
import apps.smartme.coolspot.domain.UserCoolspot;

public class FavouritesActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    private FavouriteAdapter mAdapter;
    private List<UserCoolspot> userCoolspotList = new ArrayList<>();
    DatabaseReference databaseReference;
    DatabaseReference userCoolspotReference;
    ChildEventListener userCoolspotChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userCoolspotReference = databaseReference.child("UserLocation");
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
        mRecyclerView = (RecyclerView) findViewById(R.id.favourites_recycler_view);
        mAdapter = new FavouriteAdapter(userCoolspotList);
        prepareStyleData();
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

    private void prepareStyleData() {

//        Coolpoint style = new Coolpoint("Cool", 2015);
//        styleList.add(style);
//        Coolpoint style1 = new Coolpoint("Cheap", 2015);
//        styleList.add(style1);
//        Coolpoint style2 = new Coolpoint("Expensive", 2015);
//        styleList.add(style2);
//        Coolpoint style3 = new Coolpoint("Cool girls", 2015);
//        styleList.add(style3);
//        Coolpoint style4 = new Coolpoint("Nerds", 2015);
//        styleList.add(style4);
//        Coolpoint style5 = new Coolpoint("Shots", 2015);
//        styleList.add(style5);
//        Coolpoint style6 = new Coolpoint("Concert", 2015);
//        styleList.add(style6);
//        Coolpoint style7 = new Coolpoint("Lemne", 2015);
//        styleList.add(style7);
//        Coolpoint style8 = new Coolpoint("Acadele", 2015);
//        styleList.add(style8);


//       mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
