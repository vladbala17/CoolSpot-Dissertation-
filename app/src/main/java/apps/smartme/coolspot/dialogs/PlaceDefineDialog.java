package apps.smartme.coolspot.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import apps.smartme.coolspot.R;
import apps.smartme.coolspot.adapters.RecyclerTouchListener;
import apps.smartme.coolspot.adapters.StyleDialogAdapter;
import apps.smartme.coolspot.domain.Style;

/**
 * Created by vlad on 09.05.2017.
 */

public class PlaceDefineDialog extends DialogFragment implements SearchView.OnQueryTextListener {
    private List<Style> styleList = new ArrayList<>();
    private RecyclerView recyclerView;
    SearchView searchView;
    private StyleDialogAdapter mAdapter;

    public static PlaceDefineDialog newInstance() {
        PlaceDefineDialog t = new PlaceDefineDialog();

        return t;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_place_define_dialog, null, false);
        prepareStyleData();
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
                Style style = styleList.get(position);
                Toast.makeText(getContext(), style.getStyleName() + " is selected!", Toast.LENGTH_SHORT).show();
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
        Style style = new Style("Cool", 2015);
        styleList.add(style);
        Style style1 = new Style("Cheap", 2015);
        styleList.add(style1);
        Style style2 = new Style("Expensive", 2015);
        styleList.add(style2);
        Style style3 = new Style("Cool girls", 2015);
        styleList.add(style3);
        Style style4 = new Style("Nerds", 2015);
        styleList.add(style4);
        Style style5 = new Style("Shots", 2015);
        styleList.add(style5);
        Style style6 = new Style("Concert", 2015);
        styleList.add(style6);


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
