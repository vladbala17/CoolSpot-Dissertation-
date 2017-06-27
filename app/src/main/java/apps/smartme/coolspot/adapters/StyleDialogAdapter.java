package apps.smartme.coolspot.adapters;

import android.graphics.Typeface;
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
    private Typeface typeface;

    public StyleDialogAdapter(List<String> styleList, Typeface typeface) {
        this.styleList = styleList;
        this.arraylist = new ArrayList<String>();
        this.arraylist.addAll(styleList);
        this.typeface = typeface;
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
            case "drink":
                holder.image.setImageResource(R.drawable.ic_free_drinks);
                break;
            case "fun":
                holder.image.setImageResource(R.drawable.ic_free_entrance);
                break;
            case "girls":
                holder.image.setImageResource(R.drawable.ic_girls);
                break;
            case "nerd":
                holder.image.setImageResource(R.drawable.ic_nerd);
                break;
            case "cheap":
                holder.image.setImageResource(R.drawable.ic_cheap);
                break;
            case "crowded":
                holder.image.setImageResource(R.drawable.ic_crowded);
                break;
            case "dance":
                holder.image.setImageResource(R.drawable.ic_karaoke);
                break;
            case "computer":
                holder.image.setImageResource(R.drawable.ic_computer);
                break;
            case "expensive":
                holder.image.setImageResource(R.drawable.ic_expensive);
                break;
            case "bored":
                holder.image.setImageResource(R.drawable.ic_bored);
                break;
            case "sport":
                holder.image.setImageResource(R.drawable.ic_basketball);
                break;
            default:
                holder.image.setImageResource(R.drawable.ic_free_drinks);
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
            title.setTypeface(typeface);
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
