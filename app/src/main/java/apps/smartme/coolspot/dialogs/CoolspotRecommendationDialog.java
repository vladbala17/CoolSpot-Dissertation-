package apps.smartme.coolspot.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import apps.smartme.coolspot.R;
import apps.smartme.coolspot.domain.Coolpoint;

/**
 * Created by vlad on 16.06.2017.
 */

public class CoolspotRecommendationDialog extends DialogFragment implements View.OnClickListener {
    private static final String NAME_KEY = "coolpointName";
    private static final String COOLPOINT_KEY = "coolpointImage";
    private static final String COOLPOINT_POPULARITY_KEY = "coolpointPopularity";
    private static final String COOLPOINT_MUTUAL_FRIENDS_KEY = "coolpointMutualFriends";

    private List<Coolpoint> drinkCoolpointList;
    private List<Coolpoint> musicCoolpointList;
    private List<Coolpoint> nerdCoolpointList;
    private List<Coolpoint> girlCoolpointList;

    private ImageView popularCoolpointImageView;
    private TextView coolspotNameTextView;
    private TextView coolspotPopularityTextView;
    private TextView coolspotMutualFriendsTextView;

    private Button takeMeThereButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static CoolspotRecommendationDialog newInstance(String name, String coolpoint, int popularity, int mutualFriends) {
        CoolspotRecommendationDialog fragment = new CoolspotRecommendationDialog();
        Bundle bundle = new Bundle();
        bundle.putString(NAME_KEY, name);
        bundle.putString(COOLPOINT_KEY, coolpoint);
        bundle.putInt(COOLPOINT_POPULARITY_KEY, popularity);
        bundle.putInt(COOLPOINT_MUTUAL_FRIENDS_KEY, mutualFriends);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_coolspot_recommendation_dialog, null, false);

        String name = getArguments().getString(NAME_KEY);
        int popularity = getArguments().getInt(COOLPOINT_POPULARITY_KEY);
        int mutualFriends = getArguments().getInt(COOLPOINT_MUTUAL_FRIENDS_KEY);
        popularCoolpointImageView = (ImageView) view.findViewById(R.id.iv_coolspot_popular_coolpoint);
        coolspotNameTextView = (TextView) view.findViewById(R.id.tv_coolspot_name_value);
        coolspotPopularityTextView = (TextView) view.findViewById(R.id.tv_coolspot_popularity_value);
        coolspotMutualFriendsTextView = (TextView) view.findViewById(R.id.tv_coolspot_mutual_friends_value);
        takeMeThereButton = (Button) view.findViewById(R.id.take_me_there_btn);
//        coolspotNameTextView.setText(name);
//        coolspotPopularityTextView.setText(String.valueOf(popularity));
//        coolspotMutualFriendsTextView.setText(String.valueOf(mutualFriends));
        takeMeThereButton.setOnClickListener(this);
        return view;


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_me_there_btn:
                String url = "https://www.google.com/maps/dir/?api=1&origin=The+Office+Cluj-Napoca&destination=Cluj+Arena";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                this.dismiss();
                break;
        }
    }
}
