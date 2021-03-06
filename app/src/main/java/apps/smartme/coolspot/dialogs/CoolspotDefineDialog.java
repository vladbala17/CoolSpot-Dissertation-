package apps.smartme.coolspot.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
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

public class CoolspotDefineDialog extends DialogFragment implements SearchView.OnQueryTextListener {

    private static final String TAG = CoolspotDetailsDialog.class.getSimpleName();

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
    private Button cancelPlaceButton;
    private ImageView coolPointImageViewFirst;
    private ImageView coolPointImageViewSecond;
    private TextView coolPointTextViewFirst;
    private TextView coolPointTextViewSecond;
    private TextView whatIsCoolTextView;
    private Typeface tf;

    private boolean pickedFirst = false;

    public static CoolspotDefineDialog newInstance(String placeName, int position) {
        CoolspotDefineDialog t = new CoolspotDefineDialog();
        Bundle args = new Bundle();
        args.putString(PLACE_NAME, placeName);
        args.putInt(SELECTED_ITEM_POSITION, position);
        t.setArguments(args);
        return t;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        styleList.add("drink");
        styleList.add("fun");
        styleList.add("girls");
        styleList.add("nerd");
        styleList.add("cheap");
        styleList.add("crowded");
        styleList.add("dance");
        styleList.add("computer");
        styleList.add("expensive");
        styleList.add("bored");
        styleList.add("sport");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        styleList.clear();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_place_define_dialog, null, false);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/android.ttf");
        String placeDefineTitle = getArguments().getString(PLACE_NAME);
        final int position = getArguments().getInt(SELECTED_ITEM_POSITION);
        whatIsCoolTextView = (TextView) v.findViewById(R.id.place_define_tv);
        whatIsCoolTextView.setTypeface(tf);
        coolPointImageViewFirst = (ImageView) v.findViewById(R.id.iv_coolpoint1);
        coolPointImageViewSecond = (ImageView) v.findViewById(R.id.iv_coolpoint2);
        coolPointTextViewFirst = (TextView) v.findViewById(R.id.tv_coolpoint1);
        coolPointTextViewFirst.setTypeface(tf);
        coolPointTextViewSecond = (TextView) v.findViewById(R.id.tv_coolpoint2);
        coolPointTextViewSecond.setTypeface(tf);
        placeDefineTextView = (TextView) v.findViewById(R.id.place_define_name);
        placeDefineTextView.setText(placeDefineTitle);
        placeDefineTextView.setTypeface(tf);
        markPlaceButton = (Button) v.findViewById(R.id.mark_place_there_btn);
        markPlaceButton.setTypeface(tf);
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
        cancelPlaceButton = (Button) v.findViewById(R.id.cancel_btn);
        cancelPlaceButton.setTypeface(tf);
        cancelPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        searchView = (SearchView) v.findViewById(R.id.search_view);
        recyclerView = (RecyclerView) v.findViewById(R.id.styles_galery);
        mAdapter = new StyleDialogAdapter(styleList, tf);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String coolPoint = styleList.get(position);
                if (!pickedFirst) {
                    pickedFirst = true;
                    switch (coolPoint) {
                        case "drink":
                            coolPointImageViewFirst.setImageResource(R.drawable.ic_free_drinks);
                            coolPointTextViewFirst.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                        case "fun":
                            coolPointImageViewFirst.setImageResource(R.drawable.ic_free_entrance);
                            coolPointTextViewFirst.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                        case "girls":
                            coolPointImageViewFirst.setImageResource(R.drawable.ic_girls);
                            coolPointTextViewFirst.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                        case "nerd":
                            coolPointImageViewFirst.setImageResource(R.drawable.ic_nerd);
                            coolPointTextViewFirst.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                        case "cheap":
                            coolPointImageViewFirst.setImageResource(R.drawable.ic_cheap);
                            coolPointTextViewFirst.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                        case "crowded":
                            coolPointImageViewFirst.setImageResource(R.drawable.ic_crowded);
                            coolPointTextViewFirst.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                        case "dance":
                            coolPointImageViewFirst.setImageResource(R.drawable.ic_karaoke);
                            coolPointTextViewFirst.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                        case "computer":
                            coolPointImageViewFirst.setImageResource(R.drawable.ic_computer);
                            coolPointTextViewFirst.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                        case "expensive":
                            coolPointImageViewFirst.setImageResource(R.drawable.ic_expensive);
                            coolPointTextViewFirst.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                        case "bored":
                            coolPointImageViewFirst.setImageResource(R.drawable.ic_bored);
                            coolPointTextViewFirst.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                        default:
                            coolPointImageViewFirst.setImageResource(R.drawable.ic_free_drinks);
                            coolPointTextViewFirst.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                    }
                    styleList.remove(coolPoint);
                    mAdapter.notifyDataSetChanged();
                } else {
                    switch (coolPoint) {

                        case "drink":
                            coolPointImageViewSecond.setImageResource(R.drawable.ic_free_drinks);
                            coolPointTextViewSecond.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                        case "fun":
                            coolPointImageViewSecond.setImageResource(R.drawable.ic_free_entrance);
                            coolPointTextViewSecond.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                        case "girls":
                            coolPointImageViewSecond.setImageResource(R.drawable.ic_girls);
                            coolPointTextViewSecond.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                        case "nerd":
                            coolPointImageViewSecond.setImageResource(R.drawable.ic_nerd);
                            coolPointTextViewSecond.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                        case "cheap":
                            coolPointImageViewSecond.setImageResource(R.drawable.ic_cheap);
                            coolPointTextViewSecond.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                        case "crowded":
                            coolPointImageViewSecond.setImageResource(R.drawable.ic_crowded);
                            coolPointTextViewSecond.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                        case "dance":
                            coolPointImageViewSecond.setImageResource(R.drawable.ic_karaoke);
                            coolPointTextViewSecond.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                        case "computer":
                            coolPointImageViewSecond.setImageResource(R.drawable.ic_computer);
                            coolPointTextViewSecond.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                        case "expensive":
                            coolPointImageViewSecond.setImageResource(R.drawable.ic_expensive);
                            coolPointTextViewSecond.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                        case "bored":
                            coolPointImageViewSecond.setImageResource(R.drawable.ic_bored);
                            coolPointTextViewSecond.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                        default:
                            coolPointImageViewSecond.setImageResource(R.drawable.ic_free_drinks);
                            coolPointTextViewSecond.setText(coolPoint);
                            selectedCoolpoints.add(coolPoint);
                            break;
                    }
                    styleList.remove(coolPoint);
                    mAdapter.notifyDataSetChanged();
                }
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
