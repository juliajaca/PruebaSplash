package com.example.pruebasplash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class _Peg_Pantalla extends AppCompatActivity {
    private int score = 0;
    private TextView tvScore,tvBestScore;
    private LinearLayout root = null;
    private Button btnNewGame;
    private _Peg_Logica gameView;
    private _Peg_Animation animLayer = null;


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

        root = (LinearLayout) findViewById(R.id.container);
        root.setBackgroundColor(0xfffaf8ef);

        tvScore = (TextView) findViewById(R.id.tvScore);
        tvBestScore = (TextView) findViewById(R.id.tvBestScore);

        gameView = (_Peg_Logica) findViewById(R.id.gameView);

        btnNewGame = (Button) findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                gameView.startGame();
            }});

        animLayer = (_Peg_Animation) findViewById(R.id.animLayer);
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

    public void showScore(){
        tvScore.setText(gameView.getNumeroFichas()+"");
    }

    public void addScore(){

        showScore();

    }
}