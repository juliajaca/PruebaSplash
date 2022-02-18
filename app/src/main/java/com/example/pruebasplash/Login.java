package com.example.pruebasplash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private DBHandler dbHandler;
    private String nombre, contraseña;
    private EditText cajaNombre, cajaContraseña;
    private Button botonEnviarDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        cajaContraseña = findViewById(R.id.passCaja);
        cajaNombre = findViewById(R.id.nombreCaja);
        botonEnviarDatos = findViewById(R.id.botonIniciarSesion);

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
                    Toast.makeText(Login.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
                    return;
                }
                // on below line we are calling a method to add new course to sqlite data and pass all our values to it.
                int encontrados = dbHandler.buscarJugador(nombre, contraseña);

               Toast.makeText(Login.this, "Hay" +encontrados, Toast.LENGTH_SHORT).show();
            }
        });
    }
}