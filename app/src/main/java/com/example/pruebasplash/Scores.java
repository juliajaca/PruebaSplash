package com.example.pruebasplash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;

public class Scores extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<PuntuacionModel> mSportsData;
    private PuntuacionAdapter mAdapter;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        dbHandler = new DBHandler(Scores.this);

        TabHost host = (TabHost) findViewById(R.id.TabHost1);
        host.setup();

        TabHost.TabSpec allScoresTab = host.newTabSpec("2048");

        allScoresTab.setIndicator("2048", getResources().getDrawable(android.R.drawable.star_on));
        allScoresTab.setContent(R.id.ScrollViewAllScores);
        host.addTab(allScoresTab);

        TabHost.TabSpec pegTab = host.newTabSpec("Peg");
        pegTab.setIndicator("Peg", getResources().getDrawable(android.R.drawable.star_on));
        pegTab.setContent(R.id.ScrollViewFriendScores);
        host.addTab(pegTab);

        host.setCurrentTabByTag("2048"); //el tab en el que empeiza

        setTabColor(host);
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String arg0) {
                setTabColor(host);
            }
        });

        // Initialize the RecyclerView.
        mRecyclerView = findViewById(R.id.ScrollViewAllScores);
        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Initialize the ArrayList that will contain the data.
        mSportsData  = dbHandler.getPuntuaciones("2048");
        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new PuntuacionAdapter(this, mSportsData);
        mRecyclerView.setAdapter(mAdapter);

        // Get the data.
        initializeData();
    }

    public void setTabColor(TabHost tabhost) {

        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
            tabhost.getTabWidget().getChildAt(i)
            .setBackgroundColor(getResources().getColor(R.color.rojo_corazon_claro)); //
            View v = tabhost.getTabWidget().getChildAt(i);
            TextView tv = (TextView) tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(getResources().getColor(R.color.white));
        }
        tabhost.getTabWidget().setCurrentTab(0);
        tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab())
                .setBackgroundColor(getResources().getColor(R.color.rojo_corazon_oscuro)); //
    }

    /**
     * Initialize the sports data from resources.
     */
    private void initializeData() {
        // Get the resources from the XML file.
        String[] sportsList = getResources()
                .getStringArray(R.array.sports_titles);
        String[] sportsInfo = getResources()
                .getStringArray(R.array.sports_info);

        // Clear the existing data (to avoid duplication).
        //mSportsData.clear();

        // Create the ArrayList of Sports objects with titles and
        // information about each sport.
        //for (int i = 0; i < sportsList.length; i++) {
          //  mSportsData.add(new PuntuacionModel(sportsList[i], sportsInfo[i]));
        //}

        // Notify the adapter of the change.

        mAdapter.notifyDataSetChanged();
    }
}
