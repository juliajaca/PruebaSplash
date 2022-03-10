package com.example.pruebasplash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class _2048_Seleccion_Nivel extends AppCompatActivity {
    private Button btnFacil, btnMedio, btnDificil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2048_seleccion_nivel);

        btnFacil = findViewById(R.id.btnFacil);
        btnMedio = findViewById(R.id.btnMedio);
        btnDificil = findViewById(R.id.btnDificil);
    }


    public void seleccionarNivel(View v){
        switch (v.getId()) {
            case R.id.btnFacil:
                Config.LINES_2048 = Config.LEVELS_2048[0];
                break;
            case R.id.btnMedio:
                Config.LINES_2048 = Config.LEVELS_2048[1];
                break;
            case R.id.btnDificil:
                Config.LINES_2048 = Config.LEVELS_2048[2];
                break;
        }
        Intent intent = new Intent(this, _2048_Pantalla.class);
        startActivity(intent);
    }
}