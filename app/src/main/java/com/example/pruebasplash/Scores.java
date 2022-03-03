package com.example.pruebasplash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

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

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper
                .SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT |
                ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                Collections.swap(mSportsData, from, to);
                mAdapter.notifyItemMoved(from, to);
                return false;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int posicion = viewHolder.getAdapterPosition();
                PuntuacionModel puntuacion = mSportsData.get(posicion);
                if(puntuacion.getNombre().equals(Config.LOGGED_USER)){
                    Toast.makeText(Scores.this, "IGUAL", Toast.LENGTH_LONG).show();
                    mSportsData.remove(viewHolder.getAdapterPosition());
                    mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                }else{
                    Toast.makeText(Scores.this, "NO IGUAL", Toast.LENGTH_LONG).show();
                }

            }
        });
        // add it to your RecyclerView
        helper.attachToRecyclerView(mRecyclerView);
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
