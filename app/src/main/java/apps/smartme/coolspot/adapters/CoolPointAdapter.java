package apps.smartme.coolspot.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;

import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import apps.smartme.coolspot.R;
import apps.smartme.coolspot.domain.Coolpoint;

/**
 * Created by vlad on 03.06.2017.
 */

public class CoolPointAdapter extends CursorAdapter {

    private List<String> items;

    private TextView text;
    private ImageView image;

    public CoolPointAdapter(Context context, Cursor cursor, List<String> items) {

        super(context, cursor, false);
        this.items = items;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        text.setText(items.get(cursor.getPosition()));
        switch (items.get(cursor.getPosition())) {
            case "drink":
                image.setImageResource(R.drawable.ic_free_drinks);
                break;
            case "fun":
                image.setImageResource(R.drawable.ic_free_entrance);
                break;
            case "girls":
                image.setImageResource(R.drawable.ic_girls);
                break;
            case "nerd":
                image.setImageResource(R.drawable.ic_nerd);
                break;
            case "cheap":
                image.setImageResource(R.drawable.ic_cheap);
                break;
            case "crowded":
                image.setImageResource(R.drawable.ic_crowded);
                break;
            case "dance":
                image.setImageResource(R.drawable.ic_karaoke);
                break;
            case "computer":
                image.setImageResource(R.drawable.ic_computer);
                break;
            case "expensive":
                image.setImageResource(R.drawable.ic_expensive);
                break;
            case "bored":
                image.setImageResource(R.drawable.ic_bored);
                break;
            default:
                image.setImageResource(R.drawable.ic_free_drinks);
                break;
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_coolpoint_search, parent, false);

        text = (TextView) view.findViewById(R.id.tv_coolpoint_name);
        image = (ImageView) view.findViewById(R.id.iv_coolpoint_image);
        return view;
    }

}
