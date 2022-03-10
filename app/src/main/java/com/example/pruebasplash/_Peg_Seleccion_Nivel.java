package com.example.pruebasplash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class _Peg_Seleccion_Nivel extends AppCompatActivity {
    private Button btnEnglish, btnFrench, btnOtherCross;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peg_seleccion_nivel);

        btnEnglish = findViewById(R.id.btnEnglishBoard);
        btnFrench = findViewById(R.id.btnFrenchBoard);
        btnOtherCross = findViewById(R.id.btnOtherCrossBoard);
    }

    public void seleccionarNivel(View v){
        Intent intent = new Intent(this, _Peg_Pantalla.class);

        switch (v.getId()) {
            case R.id.btnEnglishBoard:
                intent.putExtra("pos_value", 0);
                break;
            case R.id.btnFrenchBoard:
                intent.putExtra("pos_value", 1);
                break;
            case R.id.btnOtherCrossBoard:
                intent.putExtra("pos_value", 2);
                break;
        }

        startActivity(intent);

    }
}