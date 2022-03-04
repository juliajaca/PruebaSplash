package com.example.pruebasplash;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "juegosdb";
    private static final int DB_VERSION = 1;
    private static final String PLAYERS_TABLE_NAME = "tabla_jugadores";
    private static final String PLAYERS_ID_COL = "id";
    private static final String PLAYERS_NAME_COL = "nombre";
    private static final String PLAYERS_PASS_COL = "contraseña";

    private static final String SCORES_TABLE_NAME = "tabla_puntuaciones";
    private static final String SCORES_ID_COL = "id";
    private static final String SCORES_NAME_COL = "nombre";
    private static final String SCORES_SCORE_COL = "puntuacion";
    private static final String SCORES_GAME_COL = "juego";
    private ArrayList<PuntuacionModel> puntuacionList = new ArrayList<PuntuacionModel>();

    public DBHandler(Context c) {
        super(c, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + PLAYERS_TABLE_NAME + "("
                + PLAYERS_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PLAYERS_NAME_COL + " TEXT,"
                + PLAYERS_PASS_COL + " TEXT);";
        db.execSQL(query);

        //Creo la tabla de puntuaciones
        String query2 = "CREATE TABLE " + SCORES_TABLE_NAME + "("
                + SCORES_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SCORES_NAME_COL + " TEXT,"
                + SCORES_SCORE_COL + " INT,"
                + SCORES_GAME_COL + " TEXT);";
        db.execSQL(query2);
       addDemoData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ PLAYERS_TABLE_NAME);
        onCreate(db);
    }

    public String buscarJugador(String nombre, String pass ) {
        String buscado = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = { nombre, pass };
        Cursor cursor = db.rawQuery("select * from 'tabla_jugadores' where nombre=? and " +
                "contraseña=?", args, null);
        if (cursor.getCount() != 0) {
            buscado = nombre;
        }
        cursor.close();

        db.close();
        return buscado;
    }

    public void añadirJugador(String nombre, String contraseña){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PLAYERS_NAME_COL, nombre);
        values.put(PLAYERS_PASS_COL, contraseña);
        db.insert(PLAYERS_TABLE_NAME, null, values);
        db.close();
    }

    public boolean isNombreUnico(String nombre){
        boolean isUnico = true;
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = { nombre};
        Cursor cursor = db.rawQuery("select * from 'tabla_jugadores' where nombre=?", args, null);
        if (cursor.getCount() != 0) {
            isUnico = false;
        }
        cursor.close();

        db.close();
        return isUnico;

    }

    public void actualizarContraseña(String usuario, String contraseña){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(PLAYERS_NAME_COL,usuario); //These Fields should be your String values of actual
        // column names
        cv.put(PLAYERS_PASS_COL,contraseña);
        try {
            String[] args = { usuario };
            db.update(PLAYERS_TABLE_NAME, cv, "nombre=?", args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();

    }

    public int[] calcularJuegos(String usuario){
        int[] numeroJuegos = new int[2];
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = { usuario, "2048"};
        Cursor cursor = db.rawQuery("select * from 'tabla_puntuaciones' where nombre=? and " +
                "juego=?", args, null);
        numeroJuegos[0] = cursor.getCount();
        cursor.close();
        // juegos peg
        String[] args2 = { usuario, "peg"};
        Cursor cursor2 = db.rawQuery("select * from 'tabla_puntuaciones' where nombre=? and " +
                "juego=?", args2, null);
        numeroJuegos[1] = cursor2.getCount();
        cursor.close();
        db.close();
        return numeroJuegos;
    }

    public ArrayList<PuntuacionModel> getPuntuaciones(String juegoSeleccionado,
                                                      String campoOrdenacion, String orden) {
        puntuacionList.clear();
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = { juegoSeleccionado };
        Cursor cursor = db.rawQuery("select * from 'tabla_puntuaciones' where juego=?  order by " +
                        campoOrdenacion +" " + orden,
                args, null);
        if (cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    PuntuacionModel item = new PuntuacionModel();
                    int posicionNombre = cursor.getColumnIndex(SCORES_NAME_COL);
                    item.setNombre(cursor.getString(posicionNombre));
                    int posicionPuntuacion = cursor.getColumnIndex(SCORES_SCORE_COL);
                    item.setPuntos(cursor.getInt(posicionPuntuacion));
                    puntuacionList.add(item);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return puntuacionList;
    }

    public void borrarPuntuacion(String nombreJ, String puntuacion, String juego){
        try {
            String[] args = { nombreJ , puntuacion, juego};
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("delete from "+  SCORES_TABLE_NAME +" where  nombre =? and puntuacion =? and  juego = ?", args);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addDemoData(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(PLAYERS_NAME_COL, "Admin");
        values.put(PLAYERS_PASS_COL, "Admin");
        db.insert(PLAYERS_TABLE_NAME, null, values);

        ContentValues values2 = new ContentValues();
        values2.put(PLAYERS_NAME_COL, "Admin");
        values2.put(SCORES_SCORE_COL, 200);
        values2.put(SCORES_GAME_COL, "2048");
        db.insert(SCORES_TABLE_NAME, null, values2);

        ContentValues values3 = new ContentValues();
        values3.put(PLAYERS_NAME_COL, "Julia");
        values3.put(PLAYERS_PASS_COL, "Julia");
        db.insert(PLAYERS_TABLE_NAME, null, values3);

        ContentValues values4 = new ContentValues();
        values4.put(PLAYERS_NAME_COL, "Julia");
        values4.put(SCORES_SCORE_COL, 800);
        values4.put(SCORES_GAME_COL, "2048");
        db.insert(SCORES_TABLE_NAME, null, values4);

        ContentValues values5 = new ContentValues();
        values5.put(PLAYERS_NAME_COL, "Julia");
        values5.put(SCORES_SCORE_COL, 80);
        values5.put(SCORES_GAME_COL, "peg");
        db.insert(SCORES_TABLE_NAME, null, values5);

        ContentValues values6 = new ContentValues();
        values6.put(PLAYERS_NAME_COL, "Admin");
        values6.put(SCORES_SCORE_COL, 40);
        values6.put(SCORES_GAME_COL, "peg");
        db.insert(SCORES_TABLE_NAME, null, values6);

        ContentValues values7 = new ContentValues();
        values7.put(PLAYERS_NAME_COL, "Sofi");
        values7.put(PLAYERS_PASS_COL, "Sofi");
        db.insert(PLAYERS_TABLE_NAME, null, values7);

        ContentValues values8 = new ContentValues();
        values8.put(PLAYERS_NAME_COL, "Sofi");
        values8.put(SCORES_SCORE_COL, 10);
        values8.put(SCORES_GAME_COL, "peg");
        db.insert(SCORES_TABLE_NAME, null, values8);

        ContentValues values9 = new ContentValues();
        values9.put(PLAYERS_NAME_COL, "Sofi");
        values9.put(SCORES_SCORE_COL, 1000);
        values9.put(SCORES_GAME_COL, "2048");
        db.insert(SCORES_TABLE_NAME, null, values9);
    }

}
