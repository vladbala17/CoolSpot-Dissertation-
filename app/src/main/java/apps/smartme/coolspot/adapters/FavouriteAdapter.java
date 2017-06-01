package apps.smartme.coolspot.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import apps.smartme.coolspot.R;
import apps.smartme.coolspot.domain.Coolpoint;

/**
 * Created by vlad on 15.05.2017.
 */

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MyViewHolder> {
    private List<Coolpoint> styleList;
    private ArrayList<Coolpoint> arraylist;

    public FavouriteAdapter(List<Coolpoint> styleList) {
        this.styleList = styleList;
        this.arraylist = new ArrayList<Coolpoint>();
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
        Coolpoint style = styleList.get(position);
        holder.image.setImageResource(R.drawable.ic_dance);
    }

    @Override
    public int getItemCount() {
        return styleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.location_iv);
        }
    }

}
