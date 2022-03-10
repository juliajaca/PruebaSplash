package com.example.pruebasplash;

public class PuntuacionModel {
    private String nombre;
    private int puntos;
    private String tiempo;
    private String nivel;
    private String juego;

    public PuntuacionModel(){

    }
    public PuntuacionModel(String nombre, int puntos, String tiempo, String nivel, String juego) {
        this.nombre = nombre;
        this.puntos = puntos;
        this.tiempo = tiempo;
        this.nivel = nivel;
        this.juego = juego;
    }

    public String getJuego() {
        return juego;
    }

    public void setJuego(String juego) {
        this.juego = juego;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }
}

