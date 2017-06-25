package apps.smartme.coolspot.adapters;

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
import apps.smartme.coolspot.domain.Coolpoint;
import apps.smartme.coolspot.domain.UserCoolspot;

/**
 * Created by vlad on 15.05.2017.
 */

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MyViewHolder> {

    private List<UserCoolspot> styleList;
    private ArrayList<UserCoolspot> arraylist;


    public FavouriteAdapter(List<UserCoolspot> styleList) {
        this.styleList = styleList;
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(styleList);
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
        holder.locationHitsTextView.setText(String.valueOf(styleList.get(position).getHits()));
        holder.locationNameTextView.setText(styleList.get(position).getName());
        holder.lastActivityTextView.setText(convertTime(styleList.get(position).getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return styleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView locationImageView;
        private TextView locationNameTextView;
        private TextView lastActivityTextView;
        private TextView locationHitsTextView;

        public MyViewHolder(View view) {
            super(view);
            locationImageView = (ImageView) view.findViewById(R.id.location_iv);
            locationNameTextView = (TextView) view.findViewById(R.id.location_value_tv);
            lastActivityTextView = (TextView) view.findViewById(R.id.location_date_value_tv);
            locationHitsTextView = (TextView) view.findViewById(R.id.location_visits_value_tv);
        }
    }

    public String convertTime(long time) {
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date);
    }
}
