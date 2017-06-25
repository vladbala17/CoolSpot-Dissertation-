package apps.smartme.coolspot.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import apps.smartme.coolspot.R;
import apps.smartme.coolspot.adapters.RecyclerTouchListener;
import apps.smartme.coolspot.adapters.StyleDialogAdapter;

/**
 * Created by vlad on 09.05.2017.
 */

public class PlaceDefineDialog extends DialogFragment implements SearchView.OnQueryTextListener {

    private static final String TAG = PlaceDetailsDialog.class.getSimpleName();

    public static final String PLACE_NAME = "place_name";
    public static final String SELECTED_ITEM_POSITION = "selected_item_position";
    public static final String SELECTED_FIRST_COOLPOINT = "selected_first_coolpoint";
    public static final String SELECTED_SECOND_COOLPOINT = "selected_second_coolpoint";

    private RecyclerView recyclerView;
    private SearchView searchView;
    private List<String> styleList = new ArrayList<>();
    private List<String> selectedCoolpoints = new ArrayList<>();
    private StyleDialogAdapter mAdapter;
    private TextView placeDefineTextView;
    private Button markPlaceButton;
    private ImageView coolPointImageViewFirst;
    private ImageView coolPointImageViewSecond;
    private TextView coolPointTextViewFirst;
    private TextView coolPointTextViewSecond;

    private DatabaseReference databaseReference;
    private DatabaseReference coolPointReference;
    private ChildEventListener coolPointChildEventListener;

    public static PlaceDefineDialog newInstance(String placeName, int position) {
        PlaceDefineDialog t = new PlaceDefineDialog();
        Bundle args = new Bundle();
        args.putString(PLACE_NAME, placeName);
        args.putInt(SELECTED_ITEM_POSITION, position);
        t.setArguments(args);
        return t;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        coolPointReference = databaseReference.child("Coolpoint");
        Log.d(TAG, "onCreate()");
        ChildEventListener childEventCoolspotListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "====FIREBASE IN ACTION======");
                styleList.add(dataSnapshot.getKey());
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
        coolPointReference.addChildEventListener(childEventCoolspotListener);
        coolPointChildEventListener = childEventCoolspotListener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (coolPointChildEventListener != null) {
            coolPointReference.removeEventListener(coolPointChildEventListener);
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_place_define_dialog, null, false);
        String placeDefineTitle = getArguments().getString(PLACE_NAME);
        final int position = getArguments().getInt(SELECTED_ITEM_POSITION);
        coolPointImageViewFirst = (ImageView) v.findViewById(R.id.iv_coolpoint1);
        coolPointImageViewSecond = (ImageView) v.findViewById(R.id.iv_coolpoint2);
        coolPointTextViewFirst = (TextView) v.findViewById(R.id.tv_coolpoint1);
        coolPointTextViewSecond = (TextView) v.findViewById(R.id.tv_coolpoint2);
        placeDefineTextView = (TextView) v.findViewById(R.id.place_define_name);
        placeDefineTextView.setText(placeDefineTitle);
        markPlaceButton = (Button) v.findViewById(R.id.mark_place_there_btn);
        markPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getActivity().getIntent();
                intent.putExtra(SELECTED_ITEM_POSITION, position);
                intent.putExtra(SELECTED_FIRST_COOLPOINT, selectedCoolpoints.get(0));
                intent.putExtra(SELECTED_SECOND_COOLPOINT, selectedCoolpoints.get(1));
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                getDialog().dismiss();
            }
        });
        searchView = (SearchView) v.findViewById(R.id.search_view);
        recyclerView = (RecyclerView) v.findViewById(R.id.styles_galery);
        mAdapter = new StyleDialogAdapter(styleList);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String coolPoint = styleList.get(position);
                switch (coolPoint) {
                    case "drink":
                        coolPointImageViewFirst.setImageResource(R.drawable.ic_free_drinks);
                        coolPointTextViewFirst.setText(coolPoint);
                        selectedCoolpoints.add(coolPoint);
                        break;
                    case "fun":
                        coolPointImageViewFirst.setImageResource(R.drawable.ic_free_entrance);
                        break;
                    case "girls":
                        coolPointImageViewSecond.setImageResource(R.drawable.ic_girls);
                        coolPointTextViewSecond.setText(coolPoint);
                        selectedCoolpoints.add(coolPoint);
                        break;
                }
                styleList.remove(coolPoint);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        searchView.setOnQueryTextListener(this);
        searchView.setFocusable(true);
//               .setPositiveButton("Mark place", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
        return v;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        mAdapter.filter(text);
        return false;
    }
}
