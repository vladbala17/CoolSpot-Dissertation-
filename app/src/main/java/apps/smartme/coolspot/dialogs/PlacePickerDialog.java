package apps.smartme.coolspot.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Toast;

import apps.smartme.coolspot.R;
import apps.smartme.coolspot.adapters.PlacePickerAdapter;

/**
 * Created by vlad on 08.05.2017.
 */

public class PlacePickerDialog extends DialogFragment {
    private static final String PLACE_KEY = "place";


    public static PlacePickerDialog newInstance(String[] placesList) {
        PlacePickerDialog f = new PlacePickerDialog();
        Bundle args = new Bundle();
        args.putCharSequenceArray(PLACE_KEY, placesList);
        f.setArguments(args);
        return f;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CharSequence[] places = getArguments().getCharSequenceArray(PLACE_KEY);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.custom_title, null);
        PlacePickerAdapter placePickerAdapter = new PlacePickerAdapter(getContext(), places);

        builder.setCustomTitle(view)
                .setAdapter(placePickerAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PlaceDefineDialog.newInstance().show(getActivity().getSupportFragmentManager(),"proba");
                        Toast.makeText(getActivity(), "INSFARSIT", Toast.LENGTH_SHORT).show();
                    }
                });
        return builder.create();

    }

}
