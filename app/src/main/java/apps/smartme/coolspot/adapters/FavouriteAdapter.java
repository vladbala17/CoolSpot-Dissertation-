package apps.smartme.coolspot.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import apps.smartme.coolspot.R;
import apps.smartme.coolspot.domain.Style;

/**
 * Created by vlad on 15.05.2017.
 */

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.MyViewHolder> {
    private List<Style> styleList;
    private ArrayList<Style> arraylist;

    public FavouriteAdapter(List<Style> styleList) {
        this.styleList = styleList;
        this.arraylist = new ArrayList<Style>();
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
        Style style = styleList.get(position);
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
