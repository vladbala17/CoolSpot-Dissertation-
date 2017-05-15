package apps.smartme.coolspot.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import apps.smartme.coolspot.R;
import apps.smartme.coolspot.adapters.FavouriteAdapter;
import apps.smartme.coolspot.adapters.RecyclerTouchListener;
import apps.smartme.coolspot.domain.Style;

public class FavouritesActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    private FavouriteAdapter mAdapter;
    private List<Style> styleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        mRecyclerView = (RecyclerView) findViewById(R.id.favourites_recycler_view);
        mAdapter = new FavouriteAdapter(styleList);
        prepareStyleData();
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Style style = styleList.get(position);
                Toast.makeText(FavouritesActivity.this, style.getStyleName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void prepareStyleData() {

        Style style = new Style("Cool", 2015);
        styleList.add(style);
        Style style1 = new Style("Cheap", 2015);
        styleList.add(style1);
        Style style2 = new Style("Expensive", 2015);
        styleList.add(style2);
        Style style3 = new Style("Cool girls", 2015);
        styleList.add(style3);
        Style style4 = new Style("Nerds", 2015);
        styleList.add(style4);
        Style style5 = new Style("Shots", 2015);
        styleList.add(style5);
        Style style6 = new Style("Concert", 2015);
        styleList.add(style6);
        Style style7 = new Style("Lemne", 2015);
        styleList.add(style7);
        Style style8 = new Style("Acadele", 2015);
        styleList.add(style8);


//       mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
