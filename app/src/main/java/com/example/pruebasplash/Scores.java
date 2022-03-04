package com.example.pruebasplash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scores extends AppCompatActivity {
    private RecyclerView mRecyclerView2048, mRecyclerViewPeg;
    private ArrayList<PuntuacionModel> m2048Data, mPegData;
    private PuntuacionAdapter mAdapter2048, mAdapterPeg;
    private DBHandler dbHandlerPeg, dbHandler2048;
    private final String _2048 = "2048";
    private final String _PEG = "peg";

    //Sipnner
    private String campoOrdenacion = "puntuacion", ordenOrdenacion = "asc";
    private Spinner spinnerCampos, spinnerOrden;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        dbHandlerPeg = new DBHandler(Scores.this);
        dbHandler2048 = new DBHandler(Scores.this);

        TabHost host = (TabHost) findViewById(R.id.TabHost1);
        host.setup();

        TabHost.TabSpec allScoresTab = host.newTabSpec(_2048);
        allScoresTab.setIndicator(_2048, getResources().getDrawable(android.R.drawable.star_on));
        allScoresTab.setContent(R.id.ScrollView2048);
        host.addTab(allScoresTab);

        TabHost.TabSpec pegTab = host.newTabSpec(_PEG);
        pegTab.setIndicator(_PEG, getResources().getDrawable(android.R.drawable.star_on));
        pegTab.setContent(R.id.ScrollViewPeg);
        host.addTab(pegTab);

        host.setCurrentTabByTag(_2048); //el tab en el que empeiza

        setTabColor(host);
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String arg0) {
                setTabColor(host);
            }
        });

        // Initialize the RecyclerView.
        mRecyclerView2048 = findViewById(R.id.ScrollView2048);
        // Set the Layout Manager.
        mRecyclerView2048.setLayoutManager(new LinearLayoutManager(this));
        // Initialize the ArrayList that will contain the data.
        m2048Data  = dbHandler2048.getPuntuaciones(_2048, campoOrdenacion, ordenOrdenacion);

        // Initialize the adapter and set it to the RecyclerView.
        mAdapter2048 = new PuntuacionAdapter(this, m2048Data);
        mRecyclerView2048.setAdapter(mAdapter2048);

        // Get the data.
        //initializeData();
        ItemTouchHelperCustom helper2048 = new ItemTouchHelperCustom(new ItemTouchHelperCustomCalback(_2048, m2048Data, mAdapter2048,dbHandler2048) );
        // add it to your RecyclerView
        helper2048.attachToRecyclerView(mRecyclerView2048);

        //ahora con el peg
        // Initialize the RecyclerView.
        mRecyclerViewPeg = findViewById(R.id.ScrollViewPeg);
        // Set the Layout Manager.
        mRecyclerViewPeg.setLayoutManager(new LinearLayoutManager(this));
        // Initialize the ArrayList that will contain the data.
        mPegData  = dbHandlerPeg.getPuntuaciones(_PEG, campoOrdenacion, ordenOrdenacion);
        // Initialize the adapter and set it to the RecyclerView.
        mAdapterPeg = new PuntuacionAdapter(this, mPegData);
        mRecyclerViewPeg.setAdapter(mAdapterPeg);

        // Get the data.
        //initializeData();
        ItemTouchHelperCustom helperPeg = new ItemTouchHelperCustom(new
         ItemTouchHelperCustomCalback(_PEG, mPegData, mAdapterPeg, dbHandlerPeg) );
        // add it to your RecyclerView
        helperPeg.attachToRecyclerView(mRecyclerViewPeg);

        // Gestion dl spinner

        spinnerCampos = findViewById(R.id.spinnerCampo);
        spinnerOrden = findViewById(R.id.spinnerOrden);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.campos_tabla_puntuacion, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerCampos.setAdapter(adapter);

        spinnerCampos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                String[] campos = getResources().getStringArray(R.array.campos_tabla_puntuacion);
                campoOrdenacion = campos[position];
                m2048Data  = dbHandler2048.getPuntuaciones(_2048,campoOrdenacion, ordenOrdenacion);
                mAdapter2048.notifyDataSetChanged();
                mPegData  = dbHandlerPeg.getPuntuaciones(_PEG,campoOrdenacion, ordenOrdenacion);
                mAdapterPeg.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        ArrayAdapter<CharSequence> adapterOrden = ArrayAdapter.createFromResource(this,
                R.array.ordenacion_tabla_puntuacion, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerOrden.setAdapter(adapterOrden);

        spinnerOrden.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here);
                String[] orden = getResources().getStringArray(R.array.ordenacion_tabla_puntuacion);
                ordenOrdenacion = orden[position];
                m2048Data  = dbHandler2048.getPuntuaciones(_2048,campoOrdenacion, ordenOrdenacion);
                mAdapter2048.notifyDataSetChanged();
                mPegData  = dbHandlerPeg.getPuntuaciones(_PEG,campoOrdenacion, ordenOrdenacion);
                mAdapterPeg.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
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
        //String[] sportsList = getResources()
          //      .getStringArray(R.array.sports_titles);
        //String[] sportsInfo = getResources()
          //      .getStringArray(R.array.sports_info);

        // Clear the existing data (to avoid duplication).
        //mSportsData.clear();

        // Create the ArrayList of Sports objects with titles and
        // information about each sport.
        //for (int i = 0; i < sportsList.length; i++) {
          //  mSportsData.add(new PuntuacionModel(sportsList[i], sportsInfo[i]));
        //}

        // Notify the adapter of the change.

        //mAdapter2048.notifyDataSetChanged(); //esto parece qe no hace falta
    }

    //INNER CLASS
    class ItemTouchHelperCustom extends ItemTouchHelper {

        public ItemTouchHelperCustom(@NonNull Callback callback) {
            super(callback);
        }
    }

    class ItemTouchHelperCustomCalback extends ItemTouchHelper.Callback {
        private String juego;
        private ArrayList<PuntuacionModel> arrayPuntuaciones;
        private PuntuacionAdapter mAdapter;
        private DBHandler mDBHandler;

        public ItemTouchHelperCustomCalback(String juego,
                                            ArrayList<PuntuacionModel> arrayPuntuaciones ,
                                            PuntuacionAdapter mAdapter, DBHandler dbHandler) {
            this.juego = juego;
            this.arrayPuntuaciones = arrayPuntuaciones;
            this.mAdapter = mAdapter;
            this.mDBHandler = dbHandler;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int posicion = viewHolder.getAdapterPosition();
            PuntuacionModel posPuntuacion = arrayPuntuaciones.get(posicion);
            arrayPuntuaciones.remove(viewHolder.getAdapterPosition());
            mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            Toasty.Config.getInstance().setGravity(Gravity.TOP|Gravity.CENTER_VERTICAL, 0, Config.CARD_WIDTH*5).apply();
            Toasty.info(Scores.this, "SCORE REMOVED", Toast.LENGTH_SHORT, true).show();
            String  puntuacion = Integer.toString(posPuntuacion.getPuntos());
            mDBHandler.borrarPuntuacion(Config.LOGGED_USER, puntuacion, juego);
        }

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            int posicion = viewHolder.getAdapterPosition();
            PuntuacionModel posPuntuacion = arrayPuntuaciones.get(posicion);
            if(posPuntuacion.getNombre().equals(Config.LOGGED_USER)){
                return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            }else{
                Toasty.Config.getInstance().setGravity(Gravity.TOP|Gravity.CENTER_VERTICAL, 0, Config.CARD_WIDTH*5).apply();
                Toasty.error(Scores.this, "NOT YOUR SCORE", Toast.LENGTH_SHORT, true).show();
                return makeMovementFlags(0, 0);
            }
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
    }
}
