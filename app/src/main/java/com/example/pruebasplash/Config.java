package com.example.pruebasplash;

public class Config {
    public static final int LINES_2048 = 4;
    public static final int LINES_PEG = 7;
    public static final int[][] NUM_MOVIMIENTOS_PEG = {
            { 0,  2},   // Dcha
            { 0, -2},   // Izda
            { 2,  0},   // Abajo
            {-2,  0}    // Arriba
    };
    public static int CARD_WIDTH = 50;
    public static int TOKEN_WIDTH= 20;
    public static String LOGGED_USER = null;
}
