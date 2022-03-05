package com.example.pruebasplash;

import static com.example.pruebasplash._Peg_Logica.Estado.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class _Peg_Pantalla extends AppCompatActivity {
    private int score = 0;
    private TextView tvScore,tvBestScore;
    private LinearLayout root = null;
    private ImageView btnNewGame, btnUndo;
    private _Peg_Logica gameView;
    private _Peg_Animation animLayer = null;
    private DBHandler dbHandler ;


    private static _Peg_Pantalla mainActivity = null;

    public static _Peg_Pantalla getMainActivity() {
        return mainActivity;
    }

    public static final String SP_KEY_BEST_SCORE = "bestScore";
    public _Peg_Pantalla() {
        mainActivity = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peg_pantalla);
        dbHandler = new DBHandler(_Peg_Pantalla.this);
        root = (LinearLayout) findViewById(R.id.container);
        root.setBackgroundColor(0xfffaf8ef);

        tvScore = (TextView) findViewById(R.id.tvScore);
        tvBestScore = (TextView) findViewById(R.id.tvBestScore);

        gameView = (_Peg_Logica) findViewById(R.id.gameView);

        btnUndo = (ImageView) findViewById(R.id.btnUndo);
        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                _Peg_Token[][] pegsOld = gameView.getTokensMapOld();
                _Peg_Token[][] pegs = gameView.getTokensMap();

                for (int y = 0; y < pegsOld.length; y++) {
                    for (int x = 0; x < pegsOld[0].length; x++) {
                        pegs[x][y].setEstadoFicha(pegsOld[x][y].getEstadoFicha());
                    }
                }
                gameView.setNumeroFichas(gameView.getNumeroFichas()+1);
                getScore();
                gameView.setEstadoJuego(SELECCION_FICHA_1);
            }});

        btnNewGame = (ImageView) findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                getScore();
                gameView.startGame();
            }});


        animLayer = (_Peg_Animation) findViewById(R.id.animLayer);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
            hideSystemUI();
        }

        //si vengo de girar la pantalla
        if (savedInstanceState != null) {

            _Peg_Logica gameViewGuardado = (_Peg_Logica) savedInstanceState.getSerializable(
                    "gameView");

            ViewGroup parent = (ViewGroup)gameView.getParent();
            final int index = parent.indexOfChild(gameView);
            parent.removeView(gameView);

            if(gameViewGuardado.getParent() != null) {
                ((ViewGroup)gameViewGuardado.getParent()).removeView(gameViewGuardado); // <- fix
            }
            parent.addView(gameViewGuardado, index);

        }
    }


    public _Peg_Animation getAnimLayer() {
        return animLayer;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.help_menu_item).setIntent(
                new Intent(this, Spaslh.class));
        menu.findItem(R.id.settings_menu_item).setIntent(
                new Intent(this, _2048_Pantalla.class));
        return true;
    }

    public void clearScore(){
        score = 0;
        showScore();
    }

    public void showScore(){
        tvScore.setText(score+"");
    }

    public void getScore(){
        score = Config.NUM_PEGS - gameView.getNumeroFichas();
        showScore();

        int maxScore = Math.max(score, getBestScore());
        showBestScore(maxScore);
    }

    public void saveScore(){
        dbHandler.guardarPuntuacion(Config.LOGGED_USER, "peg", this.score);
    }


    public int getBestScore(){
        int maxPunt = dbHandler.getMejorPuntuacion(Config.LOGGED_USER, "peg");
        return maxPunt;
    }

    public void showBestScore(int s){
        tvBestScore.setText(s+"");
    }


    // Esconder las barras en horizontal
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

    // guardar datos con el giro
    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        saveInstanceState.putSerializable("estado",gameView.getEstadoJuego() );
        saveInstanceState.putSerializable("gameView",gameView );
        saveInstanceState.putSerializable("animLayer",animLayer );
        super.onSaveInstanceState(saveInstanceState);

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gameView = (_Peg_Logica)  savedInstanceState.getSerializable("gameView");
    }
}