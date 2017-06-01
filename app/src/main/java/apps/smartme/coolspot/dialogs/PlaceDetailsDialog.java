package apps.smartme.coolspot.dialogs;

import android.app.Dialog;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apps.smartme.coolspot.R;
import apps.smartme.coolspot.adapters.StyleDialogAdapter;
import apps.smartme.coolspot.domain.Coolpoint;

/**
 * Created by vlad on 10.05.2017.
 */

public class PlaceDetailsDialog extends DialogFragment {

    public static final String PLACE_NAME = "placeName";
    private List<Coolpoint> styleList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView placeDefineTextView;
    private StyleDialogAdapter mAdapter;

    public static PlaceDetailsDialog newInstance(String coolSpotName) {
        PlaceDetailsDialog f = new PlaceDetailsDialog();
        Bundle args = new Bundle();
        args.putString(PLACE_NAME, coolSpotName);
        f.setArguments(args);
        return f;
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
        View placeDefineDialog = inflater.inflate(R.layout.custom_place_details_dialog, null, false);
        String placeDefineTitle = getArguments().getString(PLACE_NAME);
        placeDefineTextView = (TextView) placeDefineDialog.findViewById(R.id.place_details_name);
        placeDefineTextView.setText(placeDefineTitle);
        recyclerView = (RecyclerView) placeDefineDialog.findViewById(R.id.cool_points_recycler_view);
        mAdapter = new StyleDialogAdapter(styleList);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        return placeDefineDialog;
    }

    private void prepareStyleData() {
//        Coolpoint style = new Coolpoint("Caca", 2015);
//        styleList.add(style);
//        Coolpoint style1 = new Coolpoint("Maca", 2015);
//        styleList.add(style1);
//        Coolpoint style2 = new Coolpoint("Para", 2015);
//        styleList.add(style2);


//        mAdapter.notifyDataSetChanged();
    }
}
