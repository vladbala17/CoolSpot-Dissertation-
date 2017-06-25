package apps.smartme.coolspot.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
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
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import apps.smartme.coolspot.R;
import apps.smartme.coolspot.adapters.StyleDialogAdapter;
import apps.smartme.coolspot.domain.Coolpoint;
import apps.smartme.coolspot.domain.CoolspotCoolpoint;

/**
 * Created by vlad on 10.05.2017.
 */

public class PlaceDetailsDialog extends DialogFragment {

    private static final String TAG = PlaceDetailsDialog.class.getSimpleName();

    public static final String PLACE_NAME = "placeName";
    public static final String PLACE_POPULARITY = "placePopularity";
    public static final String PLACE_TIMESTAMP = "placeTimestamp";
    public static final String PLACE_USERS = "placeUsers";

    private final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private List<String> coolspotCoolPointList;
    private ArrayList<String> coolspotUsers = new ArrayList<>();
    private ArrayList<String> userFriends = new ArrayList<>();
    private List<String> completeCoolpointList;
    private RecyclerView recyclerView;
    private TextView placeDefineTextView;
    private TextView timeLastVisitedTextView;
    private TextView coolPointsNumberTextView;
    private TextView mutualFriendsNumberTextView;
    private StyleDialogAdapter mAdapter;

    private DatabaseReference databaseReference;
    private DatabaseReference coolSpotCoolpointsReference;
    private DatabaseReference coolSpotUsersReference;
    private DatabaseReference userFriendsReference;
    private ChildEventListener coolSpotCoolpointChildEventListener;
    private ChildEventListener coolSpotUsersChildEventListener;
    private ChildEventListener userFriendsChildEventListener;

    public static PlaceDetailsDialog newInstance(String coolSpotName, long timeStamp, String popularity) {
        PlaceDetailsDialog f = new PlaceDetailsDialog();
        Bundle args = new Bundle();
        args.putString(PLACE_NAME, coolSpotName);
        args.putLong(PLACE_TIMESTAMP, timeStamp);
        args.putString(PLACE_POPULARITY, popularity);

        f.setArguments(args);
        return f;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        String placeDefineTitle = getArguments().getString(PLACE_NAME);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        coolSpotCoolpointsReference = databaseReference.child("CoolspotCoolpoints").child(placeDefineTitle);

        ChildEventListener childEventListener2 = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                coolspotCoolPointList.add(dataSnapshot.getKey());
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

//        ValueEventListener valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                CoolspotCoolpoint coolpoint = dataSnapshot.getValue(CoolspotCoolpoint.class);
//                if (isRecentEnough(coolpoint.getTimestamp())) {
//                    coolspotCoolPointList.add(coolpoint.getCoolpointFirst());
//                    coolspotCoolPointList.add(coolpoint.getCoolpointSecond());
//                    mAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };

        coolSpotCoolpointsReference.orderByValue().limitToLast(2).addChildEventListener(childEventListener2);
        coolSpotCoolpointChildEventListener = childEventListener2;

        coolSpotUsersReference = databaseReference.child("CoolspotUsers").child(placeDefineTitle);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onAttach child added " + dataSnapshot.getKey());
                coolspotUsers.add(dataSnapshot.getKey());
//                mutualFriendsNumberTextView.setText(String.valueOf(coolspotUsers.size()));
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
        coolSpotUsersReference.addChildEventListener(childEventListener);
        coolSpotUsersChildEventListener = childEventListener;

        userFriendsReference = databaseReference.child("UserFriends");
        ChildEventListener childEventListener1 = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userFriends.add(dataSnapshot.getKey());
                mutualFriendsNumber(userFriends, coolspotUsers);
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
        userFriendsReference.addChildEventListener(childEventListener1);
        userFriendsChildEventListener = childEventListener1;
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
        long placeTimestamp = getArguments().getLong(PLACE_TIMESTAMP);
        String lastActivity = getTimeSinceLastVisit(placeTimestamp);
        String placePopularity = getArguments().getString(PLACE_POPULARITY);
        coolspotCoolPointList = new ArrayList<>();
        placeDefineTextView = (TextView) placeDefineDialog.findViewById(R.id.place_details_name);
        timeLastVisitedTextView = (TextView) placeDefineDialog.findViewById(R.id.tv_last_visited_value);
        coolPointsNumberTextView = (TextView) placeDefineDialog.findViewById(R.id.tv_coolpoints_number);
        mutualFriendsNumberTextView = (TextView) placeDefineDialog.findViewById(R.id.tv_mutual_friends_value);
        coolPointsNumberTextView.setText(String.valueOf(placePopularity));
        timeLastVisitedTextView.setText(lastActivity);
        placeDefineTextView.setText(placeDefineTitle);
        recyclerView = (RecyclerView) placeDefineDialog.findViewById(R.id.cool_points_recycler_view);
        mAdapter = new StyleDialogAdapter(coolspotCoolPointList);
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

        if (coolSpotUsersChildEventListener != null) {
            coolSpotUsersReference.removeEventListener(coolSpotUsersChildEventListener);
        }

        if (userFriendsChildEventListener != null) {
            userFriendsReference.removeEventListener(userFriendsChildEventListener);
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

    private String getTimeSinceLastVisit(long timestamp) {
        String longAgo = "";
//        new Timestamp().to
//        String fetchedDateTime = getDateFromTimestamp();
//        String currentTime = getDateFromTimestamp(currentDateTimestamp);
        DateTime lastModified = dtf.parseDateTime(new Timestamp(timestamp).toString());
        DateTime present = dtf.parseDateTime(new Timestamp(System.currentTimeMillis()).toString());

        if (Hours.hoursBetween(lastModified, present)
                .isLessThan(Hours.hours(2))) {
            longAgo = String.valueOf(Hours.hoursBetween(present, lastModified).getHours()) + "hours ";
            longAgo = longAgo + " " + String.valueOf(Minutes.minutesBetween(lastModified, present).getMinutes()) + " minutes ";
        } else {
            longAgo = longAgo + " " + String.valueOf(Minutes.minutesBetween(lastModified, present).getMinutes()) + " minutes ";
        }
        Log.d(TAG, "db timestamp is " + getDateFromTimestamp(timestamp) + " difference is " + Hours.hoursBetween(lastModified, present).getHours() % 24 + " hours");
        return longAgo;
    }

    private String getDateFromTimestamp(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("MM/dd/yyyy HH:mm:ss", cal).toString();
        Log.d(TAG, "CURRENT TIME FROM LONG TIMESTAMP" + date);
        return date;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void mutualFriendsNumber(ArrayList<String> userFriends, ArrayList<String> coolspotUsers) {
        int count = 0;
        for (String st : userFriends) {
            if (coolspotUsers.contains(st)) {
                count = count + 1;
            }
        }
        mutualFriendsNumberTextView.setText(String.valueOf(count));
        return;
    }
}
