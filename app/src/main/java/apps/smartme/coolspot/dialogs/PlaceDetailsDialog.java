package apps.smartme.coolspot.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import apps.smartme.coolspot.R;
import apps.smartme.coolspot.adapters.StyleDialogAdapter;

/**
 * Created by vlad on 10.05.2017.
 */

public class PlaceDetailsDialog extends DialogFragment {
    private static final String TAG = PlaceDetailsDialog.class.getSimpleName();

    DatabaseReference databaseReference;
    DatabaseReference coolSpotCoolpointsReference;
    ChildEventListener coolSpotCoolpointChildEventListener;

    public static final String PLACE_NAME = "placeName";
    public static final String PLACE_POPULARITY = "placePopularity";
    public static final String PLACE_TIMESTAMP = "placeTimestamp";
    private List<String> coolPointList;
    private RecyclerView recyclerView;
    private TextView placeDefineTextView;
    private TextView timeLastVisitedTextView;
    private TextView coolPointsNumberTextView;
    private StyleDialogAdapter mAdapter;

    public static PlaceDetailsDialog newInstance(String coolSpotName, String timeStamp, String popularity) {
        PlaceDetailsDialog f = new PlaceDetailsDialog();
        Bundle args = new Bundle();
        args.putString(PLACE_NAME, coolSpotName);
        args.putString(PLACE_TIMESTAMP, timeStamp);
        args.putString(PLACE_POPULARITY, popularity);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        String placeDefineTitle = getArguments().getString(PLACE_NAME);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("CoolSpotCoolpoints");
        coolSpotCoolpointsReference = databaseReference.child(placeDefineTitle);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "Coolpoint added to the list" + dataSnapshot.getKey());
                coolPointList.add(dataSnapshot.getKey());
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
        coolSpotCoolpointsReference.orderByKey().addChildEventListener(childEventListener);
        coolSpotCoolpointChildEventListener = childEventListener;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        prepareStyleData();
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View placeDefineDialog = inflater.inflate(R.layout.custom_place_details_dialog, null, false);
        String placeDefineTitle = getArguments().getString(PLACE_NAME);
        String placeTimestamp = getArguments().getString(PLACE_TIMESTAMP);
        String placePopularity = getArguments().getString(PLACE_POPULARITY);
        coolPointList = new ArrayList<>();
        placeDefineTextView = (TextView) placeDefineDialog.findViewById(R.id.place_details_name);
        timeLastVisitedTextView = (TextView) placeDefineDialog.findViewById(R.id.tv_last_visited_value);
        coolPointsNumberTextView = (TextView) placeDefineDialog.findViewById(R.id.tv_coolpoints_number);
        coolPointsNumberTextView.setText(String.valueOf(placePopularity));
        timeLastVisitedTextView.setText(placeTimestamp);
        placeDefineTextView.setText(placeDefineTitle);
        recyclerView = (RecyclerView) placeDefineDialog.findViewById(R.id.cool_points_recycler_view);
        mAdapter = new StyleDialogAdapter(coolPointList);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        return placeDefineDialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (coolSpotCoolpointChildEventListener != null) {
            coolSpotCoolpointsReference.removeEventListener(coolSpotCoolpointChildEventListener);
        }
    }

    private void prepareStyleData() {
//        Coolpoint style = new Coolpoint("Caca", 2015);
//        coolPointList.add(style);
//        Coolpoint style1 = new Coolpoint("Maca", 2015);
//        coolPointList.add(style1);
//        Coolpoint style2 = new Coolpoint("Para", 2015);
//        coolPointList.add(style2);


//        mAdapter.notifyDataSetChanged();
    }
}
