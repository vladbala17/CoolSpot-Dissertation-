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
 * Created by vlad on 10.05.2017.
 */

public class PlaceDetailsDialog extends DialogFragment {

    private List<Style> styleList = new ArrayList<>();
    private RecyclerView recyclerView;
    private StyleDialogAdapter mAdapter;

    public static PlaceDetailsDialog newInstance() {
        PlaceDetailsDialog f = new PlaceDetailsDialog();
        return f;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        prepareStyleData();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View placeDefineDialog = inflater.inflate(R.layout.custom_place_details_dialog, null);
        recyclerView = (RecyclerView) placeDefineDialog.findViewById(R.id.cool_points_recycler_view);
        mAdapter = new StyleDialogAdapter(styleList);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        builder.setView(placeDefineDialog)
                .setPositiveButton("I want there", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Keep looking", new DialogInterface.OnClickListener() {
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


//        mAdapter.notifyDataSetChanged();
    }
}
