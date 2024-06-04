package com.example.foodstockapp;

import java.util.ArrayList;
import java.util.List;

public class CarritoCompra {
    private static List<ItemTienda> listaProductos = new ArrayList<>();

    public static List<ItemTienda> getListaProductos() {
        return listaProductos;
    }

    public static void agregarProducto(ItemTienda producto) {
        listaProductos.add(producto);
    }
}
