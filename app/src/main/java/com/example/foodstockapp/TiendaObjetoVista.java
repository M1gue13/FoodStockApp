package com.example.foodstockapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TiendaObjetoVista extends AppCompatActivity {
    int userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda_objeto_vista);
        userID = getIntent().getIntExtra("userId", -1);


        TextView textNombre = findViewById(R.id.nombreText);
        ImageView urlIma = findViewById(R.id.imagenurl);

        Intent intent = getIntent();

        String nombre = intent.getStringExtra("nombre");
        String url = intent.getStringExtra("url");


        textNombre.setText(nombre);
        Picasso.get().load(url).into(urlIma);

    }

    public void onAnyadirLista(View view) {

        EditText edit1 = findViewById(R.id.idNumber);
        String cantidadStr = edit1.getText().toString();
        int cantidad = Integer.parseInt(cantidadStr);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        String nombre = intent.getStringExtra("nombre");
        String url = intent.getStringExtra("url");

        ItemTienda objetoTienda = new ItemTienda(id, nombre, url, cantidad);

        CarritoCompra.agregarProducto(objetoTienda);

        Intent nuevoIntent = new Intent(TiendaObjetoVista.this, TiendaVista.class);
        nuevoIntent.putExtra("userId", userID);
        startActivity(nuevoIntent);



    }

    public void onVolverGestor(View view) {
        Intent gestor = new Intent(TiendaObjetoVista.this, Tienda.class);
        gestor.putExtra("userId", userID);
        startActivity(gestor);
    }
}