package apps.smartme.coolspot.dialogs;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

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
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CharSequence[] places = getArguments().getCharSequenceArray(PLACE_KEY);
        View v = inflater.inflate(R.layout.custom_place_picker_dialog, null, false);
        TextView titleTextView = (TextView) v.findViewById(R.id.place_picker_txt);
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/android.ttf");
        titleTextView.setTypeface(tf);
        PlacePickerAdapter placePickerAdapter = new PlacePickerAdapter(getContext(), places);
        ListView placePickerListView = (ListView) v.findViewById(R.id.places_lv);
        placePickerListView.setAdapter(placePickerAdapter);
        return v;
    }
}
