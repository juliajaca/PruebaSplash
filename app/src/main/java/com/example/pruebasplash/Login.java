package com.example.pruebasplash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class Login extends AppCompatActivity {
    private DBHandler dbHandler;
    private String nombre, contraseña;
    private EditText cajaNombre, cajaContraseña;
    private Button botonEnviarDatos, botonCrearCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        cajaContraseña = findViewById(R.id.passCaja);
        cajaNombre = findViewById(R.id.nombreCaja);
        botonEnviarDatos = findViewById(R.id.botonIniciarSesion);
        botonCrearCuenta = findViewById(R.id.buttonCrearcuenta);

        // creating a new dbhandler class and passing our context to it.
        dbHandler = new DBHandler(Login.this);

        // //------------------------------BOTON AÑADIR COMENTARIO
        botonEnviarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // below line is to get data from all edit text fields.
                nombre = cajaNombre.getText().toString();
                contraseña = cajaContraseña.getText().toString();

                // validating if the text fields are empty or not.
                if ((nombre.isEmpty()|| nombre.length() == 0 || nombre.equals("") || nombre == null ) ||
                        (  contraseña.isEmpty() || contraseña.length() == 0 || contraseña.equals("") || contraseña == null)) {
                    Toasty.error(Login.this, "Please enter alll the data...", Toasty.LENGTH_SHORT, true).show();

                    return;
                }
                // on below line we are calling a method to add new course to sqlite data and pass all our values to it.
                JugadorModel jugadormodel = new JugadorModel(nombre, contraseña);
                String user = dbHandler.buscarJugador(jugadormodel);

                if (user != null){
                    Intent i = new Intent(Login.this, Menu.class);
                    i.putExtra("usuario", user);
                    startActivity(i);
                    Config.LOGGED_USER = user;
                }else{
                    Toasty.error(Login.this, "Username or password incorrect", Toasty.LENGTH_SHORT, true).show();
                }

            }
        });


        botonCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, CreacionCuenta.class));
            }
        });
    }
}