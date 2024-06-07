package com.example.foodstockapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Tienda extends AppCompatActivity {
    int userID;
    private CustomAdapter adapter;
    private List<ItemTienda> items = new ArrayList<>();
    private List<ItemTienda> filteredItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        EditText idBuscador = findViewById(R.id.idBuscador);

        //Cuando se carga la pantalla muestra una lista de todos los productos que hay para poder añadir un ticket de forma manual
        userID = getIntent().getIntExtra("userId", -1);

        db.collection("TiendaF")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                int id = document.getLong("id").intValue();
                                String nombre = document.getString("nombre");
                                String url = document.getString("url");

                                if (url != null) {
                                    items.add(new ItemTienda(id, nombre, url));
                                }
                            }
                            filteredItems.addAll(items);
                            adapter = new CustomAdapter(getApplicationContext(), filteredItems);
                            adapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //Cuando se clica el producto seleccionado lleva la informacion a otra pantalla
                                    Intent intent = new Intent(Tienda.this, TiendaObjetoVista.class);
                                    intent.putExtra("id", filteredItems.get(recyclerView.getChildAdapterPosition(view)).getId());
                                    intent.putExtra("nombre", filteredItems.get(recyclerView.getChildAdapterPosition(view)).getNombre());
                                    intent.putExtra("url", filteredItems.get(recyclerView.getChildAdapterPosition(view)).getUrlImage());
                                    intent.putExtra("userId", userID);
                                    startActivity(intent);
                                }
                            });

                            recyclerView.setLayoutManager(new LinearLayoutManager(Tienda.this));
                            recyclerView.setAdapter(adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        idBuscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrar(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    //Metodo para poder hacer una busqueda de un producto en especifico
    private void filtrar(String texto) {
        filteredItems.clear();
        for (ItemTienda item : items) {
            if (item.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                filteredItems.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    //Volver al menu
    public void onIrMenu(View view) {
        Intent salir = new Intent(Tienda.this, App.class);
        salir.putExtra("userId", userID);
        startActivity(salir);
    }

    //Al pulsar el boton del ticket podras ver los productos que tienes en el ticket
    public void onVistaTienda(View view) {
        Intent i = new Intent(Tienda.this, TiendaVista.class);
        i.putExtra("userId", userID);
        startActivity(i);
    }
}
