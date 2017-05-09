package apps.smartme.coolspot.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import apps.smartme.coolspot.R;
import apps.smartme.coolspot.domain.Style;

/**
 * Created by vlad on 09.05.2017.
 */

public class StyleDialogAdapter extends RecyclerView.Adapter<StyleDialogAdapter.MyViewHolder> {
    private List<Style> styleList;
    private ArrayList<Style> arraylist;

    public StyleDialogAdapter(List<Style> styleList) {
        this.styleList = styleList;
        this.arraylist = new ArrayList<Style>();
        this.arraylist.addAll(styleList);
    }

    @Override
    public StyleDialogAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_define__dialog_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StyleDialogAdapter.MyViewHolder holder, int position) {
        Style style = styleList.get(position);
        holder.title.setText(style.getStyleName());
        holder.image.setImageResource(R.drawable.ic_dance);
    }

    @Override
    public int getItemCount() {
        return styleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.display_image_title);
            image = (ImageView) view.findViewById(R.id.display_image);
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        styleList.clear();
        if (charText.length() == 0) {
            styleList.addAll(arraylist);
        } else {
            for (Style style : arraylist) {
                if (style.getStyleName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    styleList.add(style);
                }
            }
        }
        notifyDataSetChanged();
    }
}
