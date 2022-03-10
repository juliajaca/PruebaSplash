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
import android.widget.Adapter;
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
    private final String _2048 = "2048";
    private final String _PEG = "peg";
    private RecyclerView mRecyclerView2048, mRecyclerViewPeg;
    private ArrayList<PuntuacionModel> m2048Data, mPegData;
    private PuntuacionAdapter mAdapter2048, mAdapterPeg;
    private DBHandler dbHandlerPeg, dbHandler2048;
    private String campoOrdenacion = "puntuacion", ordenOrdenacion = "desc";
    //Spinner
    AdapterView.OnItemSelectedListener listenerCampos = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            // your code here
            String[] campos = getResources().getStringArray(R.array.campos_tabla_puntuacion);
            campoOrdenacion = campos[position];
            m2048Data = dbHandler2048.getPuntuaciones(_2048, campoOrdenacion, ordenOrdenacion);
            mAdapter2048.notifyDataSetChanged();
            mPegData = dbHandlerPeg.getPuntuaciones(_PEG, campoOrdenacion, ordenOrdenacion);
            mAdapterPeg.notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            //nada
        }
    };
    AdapterView.OnItemSelectedListener listenerOrden = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            String[] orden = getResources().getStringArray(R.array.ordenacion_tabla_puntuacion);
            ordenOrdenacion = orden[position];
            m2048Data = dbHandler2048.getPuntuaciones(_2048, campoOrdenacion, ordenOrdenacion);
            mAdapter2048.notifyDataSetChanged();
            mPegData = dbHandlerPeg.getPuntuaciones(_PEG, campoOrdenacion, ordenOrdenacion);
            mAdapterPeg.notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    private Spinner spinnerCampos, spinnerOrden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        dbHandlerPeg = new DBHandler(Scores.this);
        dbHandler2048 = new DBHandler(Scores.this);

        activarTabs();
        activarRecyclerViews();
        activarSpinners();
    }

    private void activarTabs() {
        TabHost host = (TabHost) findViewById(R.id.TabHost1);
        host.setup();

        TabHost.TabSpec allScoresTab = host.newTabSpec(_2048);
        allScoresTab.setIndicator(_2048);
        allScoresTab.setContent(R.id.ScrollView2048);
        host.addTab(allScoresTab);

        TabHost.TabSpec pegTab = host.newTabSpec(_PEG);
        pegTab.setIndicator(_PEG);
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
    }

    private void activarRecyclerViews() {
        activarRecyclerView2048();
        activarRecyclerViewPeg();
    }

    private void activarSpinners() {
        activarSpinnerCampos();
        activarSpinnerOrden();
    }

    private void activarRecyclerView2048() {
        mRecyclerView2048 = findViewById(R.id.ScrollView2048);
        mRecyclerView2048.setLayoutManager(new LinearLayoutManager(this));
        m2048Data = dbHandler2048.getPuntuaciones(_2048, campoOrdenacion, ordenOrdenacion);
        mAdapter2048 = new PuntuacionAdapter(this, m2048Data);
        mRecyclerView2048.setAdapter(mAdapter2048);
        ItemTouchHelperCustom helper2048 = new ItemTouchHelperCustom(new ItemTouchHelperCustomCalback(_2048, m2048Data, mAdapter2048, dbHandler2048));
        helper2048.attachToRecyclerView(mRecyclerView2048);
    }

    private void activarRecyclerViewPeg() {
        mRecyclerViewPeg = findViewById(R.id.ScrollViewPeg);
        mRecyclerViewPeg.setLayoutManager(new LinearLayoutManager(this));
        mPegData = dbHandlerPeg.getPuntuaciones(_PEG, campoOrdenacion, ordenOrdenacion);
        mAdapterPeg = new PuntuacionAdapter(this, mPegData);
        mRecyclerViewPeg.setAdapter(mAdapterPeg);
        ItemTouchHelperCustom helperPeg = new ItemTouchHelperCustom(new ItemTouchHelperCustomCalback(_PEG, mPegData, mAdapterPeg, dbHandlerPeg));
        helperPeg.attachToRecyclerView(mRecyclerViewPeg);
    }

    private void activarSpinnerOrden() {
        spinnerOrden = findViewById(R.id.spinnerOrden);
        ArrayAdapter<CharSequence> adapterOrden = ArrayAdapter.createFromResource(this,
                R.array.ordenacion_tabla_puntuacion, android.R.layout.simple_spinner_item);
        adapterOrden.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrden.setAdapter(adapterOrden);
        spinnerOrden.setOnItemSelectedListener(listenerOrden);
    }

    private void activarSpinnerCampos() {
        spinnerCampos = findViewById(R.id.spinnerCampo);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.campos_tabla_puntuacion, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCampos.setAdapter(adapter);
        spinnerCampos.setOnItemSelectedListener(listenerCampos);
    }

    public void setTabColor(TabHost tabhost) {
        for (int i = 0; i < tabhost.getTabWidget().getChildCount(); i++) {
            tabhost.getTabWidget().getChildAt(i)
                    .setBackgroundColor(getResources().getColor(R.color.rojo_corazon_claro)); //
            TextView tv = (TextView) tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(getResources().getColor(R.color.white));
        }
        tabhost.getTabWidget().setCurrentTab(0);
        tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab())
                .setBackgroundColor(getResources().getColor(R.color.rojo_corazon_oscuro)); //
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
                                            ArrayList<PuntuacionModel> arrayPuntuaciones,
                                            PuntuacionAdapter mAdapter, DBHandler dbHandler) {
            this.juego = juego;
            this.arrayPuntuaciones = arrayPuntuaciones;
            this.mAdapter = mAdapter;
            this.mDBHandler = dbHandler;
            Toasty.Config.getInstance().setGravity(Gravity.TOP | Gravity.CENTER_VERTICAL, 0, Config.CARD_WIDTH * 2).apply();
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int posicion = viewHolder.getAdapterPosition();
            PuntuacionModel posPuntuacion = arrayPuntuaciones.get(posicion - 1);
            arrayPuntuaciones.remove(viewHolder.getAdapterPosition()-1);
            mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

            Toasty.info(Scores.this, "SCORE REMOVED", Toast.LENGTH_SHORT, true).show();
            String puntuacion = Integer.toString(posPuntuacion.getPuntos());
            mDBHandler.borrarPuntuacion(Config.LOGGED_USER, puntuacion, juego);
        }

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            //evitar swipear el header
            if (viewHolder.getAdapterPosition() == 0) {
                return makeMovementFlags(0, 0);}
            // si no es el header ya vemos si es el dueño de la puntuacion o no
            int posicion = viewHolder.getAdapterPosition();
            PuntuacionModel posPuntuacion = arrayPuntuaciones.get(posicion - 1);
            if (posPuntuacion.getNombre().equals(Config.LOGGED_USER)) {
                return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            } else {
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
