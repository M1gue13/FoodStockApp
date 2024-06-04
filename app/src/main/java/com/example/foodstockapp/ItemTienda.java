package com.example.foodstockapp;

import java.io.Serializable;

public class ItemTienda implements Serializable {
    private int id;  // Cambiado a tipo int
    private String nombre;
    private String urlImage;
    private int cantidad;

    public ItemTienda(int id, String nombre, String urlImage, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.urlImage = urlImage;
        this.cantidad = cantidad;
    }

    public ItemTienda(int id, String nombre, String urlImage) {
        this.id = id;
        this.nombre = nombre;
        this.urlImage = urlImage;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public int getId() {  // Cambiado a tipo int
        return id;
    }

    public void setId(int id) {  // Cambiado a tipo int
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ItemTienda(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}