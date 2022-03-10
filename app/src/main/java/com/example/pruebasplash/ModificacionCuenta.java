package com.example.pruebasplash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import es.dmoral.toasty.Toasty;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ModificacionCuenta extends FragmentActivity implements DialogoCambioContraseña.OnInputListener {

    private DBHandler dbHandler ;
    private TextView cajaNombre, cajaNivel2048, cajaNivelPeg;
    private int[] numeroJuegos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificacion_cuenta);
        dbHandler = new DBHandler(ModificacionCuenta.this);
        cajaNombre = findViewById(R.id.CajaUsuario);
        cajaNombre.setText(Config.LOGGED_USER);
        cajaNivel2048 = findViewById(R.id.CajaPuntuacion2048);
        cajaNivelPeg = findViewById(R.id.CajaPuntuacionPeg);
        cajaNivel2048.setText("nivel1");
        cajaNivelPeg.setText("nivel1");
        numeroJuegos = dbHandler.calcularJuegos(Config.LOGGED_USER);
        cajaNivel2048.setText(String.valueOf(numeroJuegos[0]));
        cajaNivelPeg.setText(String.valueOf(numeroJuegos[1]));
    }

    public void verDialogo(View view) {
        DialogFragment dialog = new DialogoCambioContraseña();
        dialog.show(getSupportFragmentManager(),DialogoCambioContraseña.TAG);
    }


    @Override
    public void sendInput(String pass1, String pass2) {
        Toasty.Config.getInstance().setGravity(Gravity.TOP|Gravity.CENTER_VERTICAL, 0, Config.CARD_WIDTH*6).apply();
        if(pass1.equals(pass2)){
            dbHandler.actualizarContraseña(new JugadorModel(Config.LOGGED_USER, pass1));
            Toasty.info(ModificacionCuenta.this, "Updated password", Toast.LENGTH_SHORT,
                    true).show();
        }else{
            Toasty.error(ModificacionCuenta.this, "Passwords do not match", Toast.LENGTH_SHORT, true).show();
        }
    }
}