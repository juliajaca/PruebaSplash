package com.example.pruebasplash;

import static com.example.pruebasplash._Peg_Logica.Estado.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class _Peg_Pantalla extends AppCompatActivity {
    private int pos_value;
    private String[][] TABLERO_JUEGO;
    private int score = 0;
    private TextView tvScore, tvBestScore;
    private LinearLayout root = null;
    private ImageView btnNewGame, btnUndo;
    private _Peg_Logica gameView;
    private _Peg_Animation animLayer = null;
    private DBHandler dbHandler;
    private Chronometer chronometer;
    private static _Peg_Pantalla mainActivity = null;
    private View.OnClickListener listenerUndo = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            _Peg_Token[][] pegsOld = gameView.getTokensMapOld();
            _Peg_Token[][] pegs = gameView.getTokensMap();

            for (int y = 0; y < pegsOld.length; y++) {
                for (int x = 0; x < pegsOld[0].length; x++) {
                    pegs[x][y].setEstadoFicha(pegsOld[x][y].getEstadoFicha());
                }
            }
            score = new Integer(gameView.getScoreOld());
            showScore();
        }
    };

    private View.OnClickListener listenerStart = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            getScore();
            gameView.startGame();
            btnUndo.setOnClickListener(listenerUndo);
        }
    };

    //CONSTRUCTOR
    public _Peg_Pantalla() {
        mainActivity = this;
    }

    //GETTERS

    public String[][] getTABLERO_JUEGO() {
        return TABLERO_JUEGO;
    }

    public _Peg_Logica getGameView() {
        return gameView;
    }

    public _Peg_Animation getAnimLayer() {
        return animLayer;
    }

    public static _Peg_Pantalla getMainActivity() {
        return mainActivity;
    }

    public Chronometer getChronometer() {
        return chronometer;
    }

    public int getBestScore() {
        PuntuacionModel puntuacion = new PuntuacionModel();
        puntuacion.setNombre(Config.LOGGED_USER);
        puntuacion.setJuego("peg");
        puntuacion.setNivel(getNivelDificultad());
        int maxPunt = dbHandler.getMejorPuntuacion(puntuacion);
        return maxPunt;
    }

    public int getScore() {
        return score;
    }

    //METODOS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peg_pantalla);

        Intent intent = getIntent();
        pos_value = intent.getIntExtra("pos_value", 0);
        TABLERO_JUEGO = Config.PEG_BOARDS[pos_value];

        dbHandler = new DBHandler(_Peg_Pantalla.this);
        chronometer = findViewById(R.id.chrono_peg);
        root = findViewById(R.id.container);
        root.setBackgroundColor(getResources().getColor(R.color.white));
        tvScore = findViewById(R.id.tvScore);
        tvBestScore = findViewById(R.id.tvBestScore);
        gameView = findViewById(R.id.gameView);
        btnUndo = findViewById(R.id.btnUndo);
        btnNewGame = findViewById(R.id.btnNewGame);
        animLayer = findViewById(R.id.animLayer);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
            hideSystemUI();
        }

        //si vengo de girar la pantalla
        if (savedInstanceState != null) {
            btnUndo.setOnClickListener(listenerUndo);
            restaurarPuntuacion(savedInstanceState);
            restaurarGameView(savedInstanceState);
            chronometer.setBase( (Long) savedInstanceState.getLong("crono")) ;
            chronometer.start();
        }

        btnNewGame.setOnClickListener(listenerStart);
    }

    public void clearScore() {
        score = 0;
        showScore();
    }

    public void showScore() {
        tvScore.setText(score + "");
    }

    public void addScore(int s) {
        score += s;
        showScore();
        int maxScore = Math.max(score, getBestScore());
        showBestScore(maxScore);
    }

    public String getNivelDificultad(){
        String dificultad;
        switch (pos_value){
            case 0:
                dificultad = "British";
                break;
            case 1:
                dificultad = "French";
                break;
            case 2:
                dificultad = "General";
                break;
            default:
                dificultad = "-";
                break;
        }
        return dificultad;
    }

    public void saveScore() {
        PuntuacionModel puntuacion = new PuntuacionModel(Config.LOGGED_USER, this.score, this.chronometer.getText().toString(),getNivelDificultad(), "peg");
        dbHandler.guardarPuntuacion(puntuacion);
    }

    public void showBestScore(int s) {
        tvBestScore.setText(s + "");
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        saveInstanceState.putSerializable("gameView", gameView);
        saveInstanceState.putSerializable("animLayer", animLayer);
        saveInstanceState.putSerializable("score", score);
        saveInstanceState.putLong("crono", chronometer.getBase());
        super.onSaveInstanceState(saveInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gameView = (_Peg_Logica) savedInstanceState.getSerializable("gameView");
    }

    private void restaurarPuntuacion(Bundle savedInstanceState){
        score = (int) savedInstanceState.getSerializable("score");
        addScore(0);
    }

    private void restaurarGameView(Bundle savedInstanceState){
        _Peg_Logica gameViewGuardado = (_Peg_Logica) savedInstanceState.getSerializable(
                "gameView");

        ViewGroup parent = (ViewGroup) gameView.getParent();
        final int index = parent.indexOfChild(gameView);
        parent.removeView(gameView);

        if (gameViewGuardado.getParent() != null) {
            ((ViewGroup) gameViewGuardado.getParent()).removeView(gameViewGuardado); // <- fix
        }
        parent.addView(gameViewGuardado, index);
    }
}