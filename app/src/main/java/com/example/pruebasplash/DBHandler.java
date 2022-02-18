package com.example.pruebasplash;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DB_NAME = "juegosdb";
    private static final int DB_VERSION = 1;
    private static final String PLAYERS_TABLE_NAME = "tabla_jugadores";
    private static final String PLAYERS_ID_COL = "id";
    private static final String PLAYERS_NAME_COL = "nombre";
    private static final String PLAYERS_PASS_COL = "contraseña";

    public DBHandler(Context c) {
        super(c, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + PLAYERS_TABLE_NAME + "("
                + PLAYERS_ID_COL + "INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PLAYERS_NAME_COL + " TEXT,"
                + PLAYERS_PASS_COL + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ PLAYERS_TABLE_NAME);
        onCreate(db);
    }

    public void añadirJugador(String nombre, String contraseña){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PLAYERS_NAME_COL, nombre);
        values.put(PLAYERS_PASS_COL, contraseña);
        db.insert(PLAYERS_TABLE_NAME, null, values);
        db.close();
    }


}
