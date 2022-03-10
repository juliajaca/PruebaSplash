package com.example.pruebasplash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class _2048_Pantalla extends AppCompatActivity {
    //ATRIBUTOS
    private static _2048_Pantalla mainActivity = null;
    private int score = 0;
    private TextView tvScore, tvBestScore;
    private LinearLayout root = null;
    private ImageView btnNewGame, btnUndo;
    private _2048_Logica gameView;
    private _2048_Animation animLayer = null;
    private DBHandler dbHandler;
    private Chronometer chronometer;
    private View.OnClickListener listenerUndo = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            _2048_Card[][] cartasOld = gameView.getCardsMapOld();
            _2048_Card[][] cartas = gameView.getCardsMap();

            for (int y = 0; y < Config.LINES_2048; y++) {
                for (int x = 0; x < Config.LINES_2048; x++) {
                    cartas[x][y].setNum(cartasOld[x][y].getNum());
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
    public _2048_Pantalla() {
        mainActivity = this;
    }

    //GETTERS
    public int getScore() {
        return score;
    }

    public Chronometer getChronometer() {
        return chronometer;
    }

    public static _2048_Pantalla getMainActivity() {
        return mainActivity;
    }

    public _2048_Animation getAnimLayer() {
        return animLayer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2048_pantalla);
        chronometer = findViewById(R.id.chrono_2048);
        root = findViewById(R.id.container);
        root.setBackgroundColor(getResources().getColor(R.color.white));
        tvScore = findViewById(R.id.tvScore);
        tvBestScore = findViewById(R.id.tvBestScore);
        gameView = findViewById(R.id.gameView);
        btnNewGame = findViewById(R.id.btnNewGame);
        btnUndo = findViewById(R.id.btnUndo);
        btnNewGame.setOnClickListener(listenerStart);
        animLayer = findViewById(R.id.animLayer);
        dbHandler = new DBHandler(_2048_Pantalla.this);

        if (savedInstanceState != null) {
            restaurarPuntuacion(savedInstanceState);
            restaurarGameView(savedInstanceState);
            btnUndo.setOnClickListener(listenerUndo);
            chronometer.setBase( (Long) savedInstanceState.getLong("crono")) ;
            chronometer.start();
        }

        ///Ocultar barras en horizontal
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
            hideSystemUI();
        }
    }

    //METODOS
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

    private String getNivelDificultad(){
        String dificultad;
        switch (Config.LINES_2048){
            case 4:
                dificultad = "Hard";break;
            case 5:
                dificultad = "Medium";break;
            case 6:
                dificultad = "Easy";break;
            default:
                dificultad = "-";break;
        }
        return dificultad;
    }

    public void saveScore() {

        PuntuacionModel puntuacion= new PuntuacionModel(Config.LOGGED_USER, this.score, this.chronometer.getText().toString(),getNivelDificultad(), "2048");
        dbHandler.guardarPuntuacion(puntuacion);
    }

    public int getBestScore() {
        PuntuacionModel puntuacion = new PuntuacionModel();
        puntuacion.setNombre(Config.LOGGED_USER);
        puntuacion.setJuego("2048");
        puntuacion.setNivel(getNivelDificultad());
        int maxPunt = dbHandler.getMejorPuntuacion(puntuacion);
        return maxPunt;
    }

    public void showBestScore(int s) {
        tvBestScore.setText(s + "");
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {

        saveInstanceState.putLong("crono", chronometer.getBase());
        saveInstanceState.putSerializable("gameView", gameView);
        saveInstanceState.putSerializable("animLayer", animLayer);
        saveInstanceState.putSerializable("score", score);
        super.onSaveInstanceState(saveInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gameView = (_2048_Logica) savedInstanceState.getSerializable("gameView");
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

    private void restaurarPuntuacion(Bundle savedInstanceState) {
        score = (int) savedInstanceState.getSerializable("score");
        addScore(0);
    }

    private void restaurarGameView(Bundle savedInstanceState) {
        _2048_Logica gameViewGuardado = (_2048_Logica) savedInstanceState.getSerializable(
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