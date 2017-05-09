package apps.smartme.coolspot.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import apps.smartme.coolspot.R;

/**
 * Created by vlad on 09.05.2017.
 */

public class PlacePickerAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private CharSequence[] places;

    public PlacePickerAdapter(Context context, CharSequence[] mDataSource) {
        mContext = context;
        this.places = mDataSource;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return places.length;
    }

    @Override
    public Object getItem(int position) {
        return places[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_place_picker_row, parent, false);

            holder = new ViewHolder();
            holder.placePicker = (TextView) convertView
                    .findViewById(R.id.place_picker_title);
            Typeface type = Typeface.createFromAsset(mContext.getAssets(),"fonts/android.ttf");
            holder.placePicker.setTypeface(type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.placePicker.setText(places[position]);

        return convertView;
    }

    public class ViewHolder {
        TextView placePicker;
    }
}
