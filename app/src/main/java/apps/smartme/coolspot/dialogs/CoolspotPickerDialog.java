package apps.smartme.coolspot.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import apps.smartme.coolspot.R;
import apps.smartme.coolspot.adapters.PlacePickerAdapter;

/**
 * Created by vlad on 08.05.2017.
 */

public class CoolspotPickerDialog extends DialogFragment implements AdapterView.OnItemClickListener {

    private static final String PLACE_KEY = "place";

    public static final String SELECTED_ITEM_POSITION = "selected_item_position";
    public static final int PLACE_DEFINE_DIALOG = 2;
    public static final int PLACE_DEFINE_DIALOG_MARKER = 3;
    public static final String SELECTED_ITEM_NAME = "selected_item_name";

    private ListView placePickerListView;


    public static CoolspotPickerDialog newInstance(String[] placesList) {
        CoolspotPickerDialog f = new CoolspotPickerDialog();
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
        placePickerListView = (ListView) v.findViewById(R.id.places_lv);
        placePickerListView.setOnItemClickListener(this);
        placePickerListView.setAdapter(placePickerAdapter);
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra(SELECTED_ITEM_POSITION, position);
        intent.putExtra(SELECTED_ITEM_NAME, (String) placePickerListView.getItemAtPosition(position));
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

        getDialog().dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setTargetFragment(this, PLACE_DEFINE_DIALOG_MARKER);
    }
}
