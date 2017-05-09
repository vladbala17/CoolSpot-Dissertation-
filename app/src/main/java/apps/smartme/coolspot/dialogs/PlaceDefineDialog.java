package apps.smartme.coolspot.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        prepareStyleData();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View placeDefineDialog = inflater.inflate(R.layout.custom_define_place_dialog, null);
        searchView = (SearchView) placeDefineDialog.findViewById(R.id.search_view);
        recyclerView = (RecyclerView) placeDefineDialog.findViewById(R.id.styles_galery);
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
        builder.setView(placeDefineDialog)
                .setPositiveButton("Mark place", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    private void prepareStyleData() {
        Style style = new Style("Caca", 2015);
        styleList.add(style);
        Style style1 = new Style("Maca", 2015);
        styleList.add(style1);
        Style style2 = new Style("Para", 2015);
        styleList.add(style2);
        Style style3 = new Style("Cutit", 2015);
        styleList.add(style3);
        Style style4 = new Style("Lemn", 2015);
        styleList.add(style4);
        Style style5 = new Style("Lemnarie", 2015);
        styleList.add(style5);
        Style style6 = new Style("Macanache", 2015);
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
