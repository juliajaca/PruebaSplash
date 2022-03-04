package com.example.pruebasplash;

public class PuntuacionModel {
    private String nombre;
    private int puntos;

    public PuntuacionModel(){

    }
    public PuntuacionModel(String nombre, int puntos) {
        this.nombre = nombre;
        this.puntos = puntos;
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
}

