package com.example.pruebasplash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TabHost;

public class Scores extends AppCompatActivity {

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
    }
}