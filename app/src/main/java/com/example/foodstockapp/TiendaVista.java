package com.example.foodstockapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TiendaVista extends AppCompatActivity {
    int userID;

    private CustomAdapter carritoAdapter;
    private ArrayList<ItemTienda> listaObjetos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda_vista);

        // Aqui puedes ver los productos que tienes el ticket
        userID = getIntent().getIntExtra("userId", -1);

        RecyclerView recyclerView2 = findViewById(R.id.recyclerview2);
        listaObjetos = (ArrayList<ItemTienda>) CarritoCompra.getListaProductos();

        carritoAdapter = new CustomAdapter(getApplicationContext(), listaObjetos);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setAdapter(carritoAdapter);
    }

    public void onAñadirInventario(View view) {

    //los productos se añaden al inventario
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (ItemTienda item : listaObjetos) {
            Map<String, Object> producto = new HashMap<>();
            producto.put("userId", userID);
            producto.put("nombre", item.getNombre());
            producto.put("url", item.getUrlImage());
            producto.put("cantidad", item.getCantidad());

            db.collection("Inventario")
                    .add(producto)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }

        mensajeCorto("Se ha añadido al inventario");
        Intent pasa = new Intent(TiendaVista.this, App.class);
        pasa.putExtra("userId", userID);
        startActivity(pasa);
        listaObjetos.clear();



    }

    //Para volver a seguir añadiendo productos
    public void onSeguirComprando(View view) {

        Intent ia = new Intent(TiendaVista.this, Tienda.class);
        ia.putExtra("userId", userID);
        startActivity(ia);
    }

    //Mensajes personalizadosz
    public void mensajeCorto(String mensaje){
        Context context = getApplicationContext();
        CharSequence text = mensaje;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast .makeText(context, text, duration);
        toast.show();
    }

    //Boton para borrar todos los productos de tu ticket personalizado
    public void onBorrarCarrito(View view) {
        listaObjetos.clear();
        carritoAdapter.notifyDataSetChanged();
        mensajeCorto("El ticket ha sido vaciado");
    }
    }
