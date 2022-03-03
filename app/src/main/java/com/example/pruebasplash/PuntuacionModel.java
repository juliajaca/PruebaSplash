package com.example.pruebasplash;

public class PuntuacionModel {
    private String nombre;
    private String puntos;

    public PuntuacionModel(){

    }
    public PuntuacionModel(String nombre, String puntos) {
        this.nombre = nombre;
        this.puntos = puntos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPuntos() {
        return puntos;
    }

    public void setPuntos(String puntos) {
        this.puntos = puntos;
    }
}

