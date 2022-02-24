package com.example.pruebasplash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class _2048_Pantalla extends AppCompatActivity {

    private int score = 0;
    private TextView tvScore,tvBestScore;
    private LinearLayout root = null;
    private ImageView btnNewGame;
    private _2048_Logica gameView;
    private _2048_Animation animLayer = null;

    private static _2048_Pantalla mainActivity = null;

    public static _2048_Pantalla getMainActivity() {
        return mainActivity;
    }

    public static final String SP_KEY_BEST_SCORE = "bestScore";
    public _2048_Pantalla() {
        mainActivity = this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
        }

        Toast.makeText(_2048_Pantalla.this,"creae", Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_2048_pantalla);

        root = (LinearLayout) findViewById(R.id.container);
        root.setBackgroundColor(0xfffaf8ef);

        tvScore = (TextView) findViewById(R.id.tvScore);
        tvBestScore = (TextView) findViewById(R.id.tvBestScore);

        gameView = (_2048_Logica) findViewById(R.id.gameView);

        btnNewGame = (ImageView) findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
            gameView.startGame();
        }});

        animLayer = (_2048_Animation) findViewById(R.id.animLayer);

        if (savedInstanceState != null) {
            _2048_Logica gameViewGuardado = (_2048_Logica) savedInstanceState.getSerializable(
                    "gameView");
            ViewGroup parent = (ViewGroup)gameView.getParent();
            final int index = parent.indexOfChild(gameView);
            parent.removeView(gameView);

            if(gameViewGuardado.getParent() != null) {
                ((ViewGroup)gameViewGuardado.getParent()).removeView(gameViewGuardado); // <- fix
            }
            parent.addView(gameViewGuardado, index);
            //añado la animLayer
            _2048_Animation animGuardado = (_2048_Animation) savedInstanceState.getSerializable(
                    "animLayer");
            //Toast.makeText(_2048_Pantalla.this, "eñ toast  numero "+animGuardado.cards.get(0).getNum(), Toast.LENGTH_SHORT).show();
            //animGuardado.cards.get(0).getNum();
            ViewGroup parent2 = (ViewGroup)animLayer.getParent();
            final int index2 = parent2.indexOfChild(animLayer);
            parent2.removeView(animLayer);

            if(animGuardado.getParent() != null) {
                ((ViewGroup)animGuardado.getParent()).removeView(animGuardado); // <- fix
            }
            parent2.addView(animGuardado, index2);

            ///Cambiar tamaño

                /*
                for (int y = 0; y < Config.LINES_2048; y++) {
                    for (int x = 0; x < Config.LINES_2048; x++) {
                        _2048_Card c = gameViewGuardado.getCardsMap()[x][y];
                        c.getLayoutParams().height = Config.CARD_WIDTH-10;
                        c.getLayoutParams().width = Config.CARD_WIDTH-10;
                    }
                }
            } else{
                for (int y = 0; y < Config.LINES_2048; y++) {
                    for (int x = 0; x < Config.LINES_2048; x++) {
                        _2048_Card c = gameViewGuardado.getCardsMap()[x][y];
                        c.getLayoutParams().height = Config.CARD_WIDTH;
                        c.getLayoutParams().width = Config.CARD_WIDTH;
                    }
                }
                */


        }

        ///Cambiar tamaño
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
            hideSystemUI();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.help_menu_item).setIntent(
                new Intent(this, Spaslh.class));
        menu.findItem(R.id.settings_menu_item).setIntent(
                new Intent(this, _Peg_Pantalla.class));
        return true;
    }

    public void clearScore(){
        score = 0;
        showScore();
    }

    public void showScore(){
        tvScore.setText(score+"");
    }

    public void addScore(int s){
        score+=s;
        showScore();

        int maxScore = Math.max(score, getBestScore());
        saveBestScore(maxScore);
        showBestScore(maxScore);
    }

    public void saveBestScore(int s){
        SharedPreferences.Editor e = getPreferences(MODE_PRIVATE).edit();
        e.putInt(SP_KEY_BEST_SCORE, s);
        e.commit();
    }

    public int getBestScore(){
        return getPreferences(MODE_PRIVATE).getInt(SP_KEY_BEST_SCORE, 0);
    }

    public void showBestScore(int s){
        tvBestScore.setText(s+"");
    }

    public _2048_Animation getAnimLayer() {
        return animLayer;
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState) {
        saveInstanceState.putSerializable("gameView",gameView );
        saveInstanceState.putSerializable("animLayer",animLayer );
        super.onSaveInstanceState(saveInstanceState);

    }
        /*
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
            super.onRestoreInstanceState(savedInstanceState);
            gameView = (_2048_Logica) savedInstanceState.getSerializable("gameView");
            animLayer = (_2048_Animation)  savedInstanceState.getSerializable("animLayer");


    }*/

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            //hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
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

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

}