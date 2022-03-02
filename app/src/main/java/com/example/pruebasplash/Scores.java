package com.example.pruebasplash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TabHost;

import java.util.ArrayList;

public class Scores extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<Puntuacion> mSportsData;
    private PuntuacionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

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

        host.setCurrentTabByTag("2048");

        // Initialize the RecyclerView.
        mRecyclerView = findViewById(R.id.ScrollViewAllScores);

        // Set the Layout Manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the ArrayList that will contain the data.
        mSportsData = new ArrayList<>();

        // Initialize the adapter and set it to the RecyclerView.
        mAdapter = new PuntuacionAdapter(this, mSportsData);
        mRecyclerView.setAdapter(mAdapter);

        // Get the data.
        initializeData();
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
        mSportsData.clear();

        // Create the ArrayList of Sports objects with titles and
        // information about each sport.
        for(int i=0;i<sportsList.length;i++){
            mSportsData.add(new Puntuacion(sportsList[i],sportsInfo[i]));
        }

        // Notify the adapter of the change.
        mAdapter.notifyDataSetChanged();
    }
    }
}