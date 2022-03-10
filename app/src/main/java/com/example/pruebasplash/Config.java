package com.example.pruebasplash;

public class Config {
    public static final int[] LEVELS_2048 = new int[]{6, 5, 4};
    public static final int LINES_PEG = 7;
    public static final int[][] NUM_MOVIMIENTOS_PEG = {
            {0, 2},   // Dcha
            {0, -2},   // Izda
            {2, 0},   // Abajo
            {-2, 0}    // Arriba
    };
    public static final String[][][] PEG_BOARDS = new String[][][]{
            {
                    //ENGLISH BOARD
                    {"NADA", "NADA", "FICHA", "FICHA", "FICHA", "NADA", "NADA"},
                    {"NADA", "NADA", "FICHA", "FICHA", "FICHA", "NADA", "NADA"},
                    {"FICHA", "FICHA", "FICHA", "FICHA", "FICHA", "FICHA", "FICHA"},
                    {"FICHA", "FICHA", "FICHA", "HUECO", "FICHA", "FICHA", "FICHA",},
                    {"FICHA", "FICHA", "FICHA", "FICHA", "FICHA", "FICHA", "FICHA"},
                    {"NADA", "NADA", "FICHA", "FICHA", "FICHA", "NADA", "NADA"},
                    {"NADA", "NADA", "FICHA", "FICHA", "FICHA", "NADA", "NADA"},
            },
            {
                    //FRENCH BOARD
                    {"NADA", "NADA", "FICHA", "FICHA", "FICHA", "NADA", "NADA"},
                    {"NADA", "FICHA", "FICHA", "FICHA", "FICHA", "FICHA", "NADA"},
                    {"FICHA", "FICHA", "FICHA", "FICHA", "FICHA", "FICHA", "FICHA"},
                    {"FICHA", "FICHA", "FICHA", "HUECO", "FICHA", "FICHA", "FICHA",},
                    {"FICHA", "FICHA", "FICHA", "FICHA", "FICHA", "FICHA", "FICHA"},
                    {"NADA", "FICHA", "FICHA", "FICHA", "FICHA", "FICHA", "NADA"},
                    {"NADA", "NADA", "FICHA", "FICHA", "FICHA", "NADA", "NADA"},
            },
            {
                    //GENERALIZED SMALL CROSS BOARD
                    {"NADA", "NADA", "FICHA", "FICHA", "FICHA", "NADA", "NADA"},
                    {"NADA", "NADA", "FICHA", "FICHA", "FICHA", "NADA", "NADA"},
                    {"NADA", "FICHA", "FICHA", "FICHA", "FICHA", "FICHA", "NADA"},
                    {"NADA", "FICHA", "FICHA", "HUECO", "FICHA", "FICHA", "NADA",},
                    {"NADA", "FICHA", "FICHA", "FICHA", "FICHA", "FICHA", "NADA"},
                    {"NADA", "NADA", "FICHA", "FICHA", "FICHA", "NADA", "NADA"},
                    {"NADA", "NADA", "FICHA", "FICHA", "FICHA", "NADA", "NADA"}
            }

    };
    public static int LINES_2048 = 4;
    public static int CARD_WIDTH = 50;
    public static int TOKEN_WIDTH = 20;
    public static String LOGGED_USER = "Admin";
}
