package apps.smartme.coolspot.adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import apps.smartme.coolspot.R;
import apps.smartme.coolspot.domain.UserCoolspot;

/**
 * Created by vlad on 15.05.2017.
 */

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MyViewHolder> {

    private List<UserCoolspot> styleList;
    private ArrayList<UserCoolspot> arraylist;
    private Typeface typeface;


    public FavouriteAdapter(List<UserCoolspot> styleList, Typeface typeface) {
        this.styleList = styleList;
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(styleList);
        this.typeface = typeface;
    }

    @Override
    public FavouriteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_favourites_row_item, parent, false);

        return new FavouriteAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavouriteAdapter.MyViewHolder holder, int position) {

        holder.locationImageView.setImageResource(R.drawable.ic_dance);
        holder.locationHitsValueTextView.setText(String.valueOf(styleList.get(position).getHits()));
        holder.locationNameValueTextView.setText(styleList.get(position).getName());
        holder.lastActivityValueTextView.setText(convertTime(styleList.get(position).getTimestamp()));
        if (styleList.get(position).getCoolpointFirst() != null) {
            holder.firstCoolpointImageView.setImageResource(selectCoolpointImage(styleList.get(position).getCoolpointFirst()));
        }
        if (styleList.get(position).getCoolpointSecond() != null) {
            holder.secondCoolpointImageView.setImageResource(selectCoolpointImage(styleList.get(position).getCoolpointSecond()));
        }
    }

    @Override
    public int getItemCount() {
        return styleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView locationImageView;
        private TextView locationNameValueTextView;
        private TextView locationNameTextView;
        private TextView lastActivityValueTextView;
        private TextView lastActivityTextView;
        private TextView locationHitsValueTextView;
        private TextView locationHitsTextView;
        private TextView usedCoolpointsTextView;
        private ImageView firstCoolpointImageView;
        private ImageView secondCoolpointImageView;

        public MyViewHolder(View view) {
            super(view);
            locationImageView = (ImageView) view.findViewById(R.id.location_iv);
            locationNameValueTextView = (TextView) view.findViewById(R.id.location_value_tv);
            locationNameValueTextView.setTypeface(typeface);
            lastActivityValueTextView = (TextView) view.findViewById(R.id.location_date_value_tv);
            lastActivityValueTextView.setTypeface(typeface);
            locationHitsValueTextView = (TextView) view.findViewById(R.id.location_visits_value_tv);
            locationHitsValueTextView.setTypeface(typeface);
            locationNameTextView = (TextView) view.findViewById(R.id.location_name_tv);
            locationNameTextView.setTypeface(typeface);
            lastActivityTextView = (TextView) view.findViewById(R.id.location_date_tv);
            lastActivityTextView.setTypeface(typeface);
            locationHitsTextView = (TextView) view.findViewById(R.id.location_visits_tv);
            locationHitsTextView.setTypeface(typeface);
            usedCoolpointsTextView = (TextView) view.findViewById(R.id.used_filters_tv);
            usedCoolpointsTextView.setTypeface(typeface);
            firstCoolpointImageView = (ImageView) view.findViewById(R.id.first_coolpoint_iv);
            secondCoolpointImageView = (ImageView) view.findViewById(R.id.second_coolpoint_iv);
        }
    }

    public String convertTime(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date);
    }

    private int selectCoolpointImage(String coolPoint) {
        switch (coolPoint) {
            case "drink":
                return R.drawable.ic_free_drinks;
            case "fun":
                return R.drawable.ic_free_entrance;
            case "girls":
                return R.drawable.ic_girls;
            case "nerd":
                return R.drawable.ic_nerd;
            case "cheap":
                return R.drawable.ic_cheap;
            case "crowded":
                return R.drawable.ic_crowded;
            case "dance":
                return R.drawable.ic_karaoke;
            case "computer":
                return R.drawable.ic_computer;
            case "expensive":
                return R.drawable.ic_expensive;
            case "bored":
                return R.drawable.ic_bored;
            default:
                return R.drawable.ic_free_drinks;
        }
    }
}
