package com.example.pruebasplash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ModificacionCuenta extends AppCompatActivity implements DialogoCambioContraseña.NoticeDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificacion_cuenta);

        Button boton = findViewById(R.id.boton);


    }

    public void verDialogo(View view) {
        DialogFragment dialog = new DialogoCambioContraseña();
        dialog.show(getSupportFragmentManager(),DialogoCambioContraseña.TAG);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button

    }

}