package apps.smartme.coolspot.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
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
import apps.smartme.coolspot.domain.Recommendation;

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
    private TextView placeDetailsTitleTextView;

    private Typeface typeface;

    private Button takeMeThereButton;
    private Button recommendAnotherButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static CoolspotRecommendationDialog newInstance(Recommendation recommendation) {
        CoolspotRecommendationDialog fragment = new CoolspotRecommendationDialog();
        Bundle bundle = new Bundle();
        bundle.putString(NAME_KEY, recommendation.getCoolspotName());
        bundle.putString(COOLPOINT_KEY, recommendation.getCoolpoint());
        bundle.putInt(COOLPOINT_POPULARITY_KEY, recommendation.getPopularity());
        bundle.putInt(COOLPOINT_MUTUAL_FRIENDS_KEY, recommendation.getMutualFriendsNo());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_coolspot_recommendation_dialog, null, false);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/android.ttf");
        String name = getArguments().getString(NAME_KEY);
        int popularity = getArguments().getInt(COOLPOINT_POPULARITY_KEY);
        int mutualFriends = getArguments().getInt(COOLPOINT_MUTUAL_FRIENDS_KEY);
        placeDetailsTitleTextView = (TextView) view.findViewById(R.id.place_details_name);
        placeDetailsTitleTextView.setTypeface(typeface);
        popularCoolpointImageView = (ImageView) view.findViewById(R.id.iv_coolspot_popular_coolpoint);
        coolspotNameTextView = (TextView) view.findViewById(R.id.tv_coolspot_name_value);
        coolspotNameTextView.setTypeface(typeface);
        coolspotPopularityTextView = (TextView) view.findViewById(R.id.tv_coolspot_popularity_value);
        coolspotPopularityTextView.setTypeface(typeface);
        coolspotMutualFriendsTextView = (TextView) view.findViewById(R.id.tv_coolspot_mutual_friends_value);
        coolspotMutualFriendsTextView.setTypeface(typeface);
        takeMeThereButton = (Button) view.findViewById(R.id.take_me_there_btn);
        takeMeThereButton.setTypeface(typeface);
//        coolspotNameTextView.setText(name);
//        coolspotPopularityTextView.setText(String.valueOf(popularity));
//        coolspotMutualFriendsTextView.setText(String.valueOf(mutualFriends));
        takeMeThereButton.setOnClickListener(this);
        recommendAnotherButton = (Button) view.findViewById(R.id.keep_looking_btn);
        recommendAnotherButton.setTypeface(typeface);
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
