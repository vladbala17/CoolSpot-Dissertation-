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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import apps.smartme.coolspot.R;
import apps.smartme.coolspot.adapters.RecyclerTouchListener;
import apps.smartme.coolspot.adapters.StyleDialogAdapter;
import apps.smartme.coolspot.domain.Coolpoint;

/**
 * Created by vlad on 09.05.2017.
 */

public class PlaceDefineDialog extends DialogFragment implements SearchView.OnQueryTextListener {
    public static final String PLACE_NAME = "place_name";
    public static final String SELECTED_ITEM_POSITION = "selected_item_position";
    private List<Coolpoint> styleList = new ArrayList<>();
    private RecyclerView recyclerView;
    SearchView searchView;
    private StyleDialogAdapter mAdapter;
    private TextView placeDefineTextView;
    private Button markPlaceButton;

    public static PlaceDefineDialog newInstance(String placeName, int position) {
        PlaceDefineDialog t = new PlaceDefineDialog();
        Bundle args = new Bundle();
        args.putString(PLACE_NAME, placeName);
        args.putInt(SELECTED_ITEM_POSITION, position);
        t.setArguments(args);
        return t;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_place_define_dialog, null, false);
        String placeDefineTitle = getArguments().getString(PLACE_NAME);
        final int position = getArguments().getInt(SELECTED_ITEM_POSITION);
        prepareStyleData();
        placeDefineTextView = (TextView) v.findViewById(R.id.place_define_name);
        placeDefineTextView.setText(placeDefineTitle);
        markPlaceButton = (Button) v.findViewById(R.id.mark_place_there_btn);
        markPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getActivity().getIntent();
                intent.putExtra(SELECTED_ITEM_POSITION, position);
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
                Coolpoint style = styleList.get(position);
//                Toast.makeText(getContext(), style.getPointName() + " is selected!", Toast.LENGTH_SHORT).show();
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


//        mAdapter.notifyDataSetChanged();
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
