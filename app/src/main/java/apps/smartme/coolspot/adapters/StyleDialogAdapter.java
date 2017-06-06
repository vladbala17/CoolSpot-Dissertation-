package apps.smartme.coolspot.adapters;

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
 * Created by vlad on 09.05.2017.
 */

public class StyleDialogAdapter extends RecyclerView.Adapter<StyleDialogAdapter.MyViewHolder> {
    private List<String> styleList;
    private ArrayList<String> arraylist;

    public StyleDialogAdapter(List<String> styleList) {
        this.styleList = styleList;
        this.arraylist = new ArrayList<String>();
        this.arraylist.addAll(styleList);
    }

    @Override
    public StyleDialogAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_define_dialog_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StyleDialogAdapter.MyViewHolder holder, int position) {
        String coolpoint = styleList.get(position);
        holder.title.setText(coolpoint);
        switch (coolpoint) {
            case "cheap":
                holder.image.setImageResource(R.drawable.ic_cheap);
                break;
            case "expensive":
                holder.image.setImageResource(R.drawable.ic_expensive);
                break;
        }
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
            for (String coolpoint : arraylist) {
                if (coolpoint.toLowerCase(Locale.getDefault()).contains(charText)) {
                    styleList.add(coolpoint);
                }
            }
        }
        notifyDataSetChanged();
    }
}
