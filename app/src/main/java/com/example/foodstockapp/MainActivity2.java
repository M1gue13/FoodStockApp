package com.example.foodstockapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity2 extends AppCompatActivity {
    int userID;
    FirebaseFirestore db;
    String nombreProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        db = FirebaseFirestore.getInstance();

        TextView text1 = findViewById(R.id.idProductoInventario);
        EditText text2 = findViewById(R.id.idNumeroProducto);
        ImageView text3 = findViewById(R.id.idFotoInventario);

        String id = getIntent().getStringExtra("id");
        userID = getIntent().getIntExtra("userId", -1);
        nombreProducto = getIntent().getStringExtra("nombre");
        String url = getIntent().getStringExtra("url");
        int cantidad = getIntent().getIntExtra("cantidad", 0);

        text1.setText(nombreProducto);
        text2.setText(String.valueOf(cantidad));
        Glide.with(this).load(url).into(text3);
    }

    public void onAnyadirX1(View view) {
        db.collection("Inventario")
                .whereEqualTo("userId", userID)
                .whereEqualTo("nombre", nombreProducto)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        DocumentReference docRef = document.getReference();
                        int cantidadActual = document.getLong("cantidad").intValue();

                        docRef.update("cantidad", cantidadActual + 1)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(MainActivity2.this, "Cantidad actualizada", Toast.LENGTH_SHORT).show();
                                    EditText text2 = findViewById(R.id.idNumeroProducto);
                                    text2.setText(String.valueOf(cantidadActual + 1));
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(MainActivity2.this, "Error al actualizar la cantidad", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(MainActivity2.this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity2.this, "Error al obtener el producto", Toast.LENGTH_SHORT).show();
                });
    }



    public void onMenuPrincipal(View view) {
        Intent menus = new Intent(MainActivity2.this, App.class);
        menus.putExtra("userId", userID);
        startActivity(menus);
    }

    public void onAnyadirX3(View view) {
        db.collection("Inventario")
                .whereEqualTo("userId", userID)
                .whereEqualTo("nombre", nombreProducto)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        DocumentReference docRef = document.getReference();
                        int cantidadActual = document.getLong("cantidad").intValue();

                        docRef.update("cantidad", cantidadActual + 3)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(MainActivity2.this, "Cantidad actualizada", Toast.LENGTH_SHORT).show();
                                    EditText text2 = findViewById(R.id.idNumeroProducto);
                                    text2.setText(String.valueOf(cantidadActual + 3));
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(MainActivity2.this, "Error al actualizar la cantidad", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(MainActivity2.this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity2.this, "Error al obtener el producto", Toast.LENGTH_SHORT).show();
                });
    }

    public void onRetirarX1(View view) {
        db.collection("Inventario")
                .whereEqualTo("userId", userID)
                .whereEqualTo("nombre", nombreProducto)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        DocumentReference docRef = document.getReference();
                        int cantidadActual = document.getLong("cantidad").intValue();

                        if (cantidadActual > 0) {
                            int nuevaCantidad = cantidadActual - 1;
                            if (nuevaCantidad > 0) {
                                docRef.update("cantidad", nuevaCantidad)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(MainActivity2.this, "Cantidad actualizada", Toast.LENGTH_SHORT).show();
                                            // Actualizar la cantidad en la interfaz de usuario
                                            EditText text2 = findViewById(R.id.idNumeroProducto);
                                            text2.setText(String.valueOf(nuevaCantidad));
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(MainActivity2.this, "Error al actualizar la cantidad", Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                docRef.delete()
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(MainActivity2.this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                                            EditText text2 = findViewById(R.id.idNumeroProducto);
                                            text2.setText("0");
                                            Intent i = new Intent(MainActivity2.this, App.class);
                                            i.putExtra("userId", userID);
                                            startActivity(i);
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(MainActivity2.this, "Error al eliminar el producto", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        } else {
                            Toast.makeText(MainActivity2.this, "La cantidad no puede ser menor a 0", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity2.this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity2.this, "Error al obtener el producto", Toast.LENGTH_SHORT).show();
                });
    }

    public void onVolverInventario(View view) {
        Intent iasd = new Intent(MainActivity2.this , Inventario.class);
        iasd.putExtra("userId", userID);
        startActivity(iasd);
    }

    public void onFotoInventario(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar URL de la imagen");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newUrl = input.getText().toString();
                if (!newUrl.isEmpty()) {
                    updateImageAndSaveUrl(newUrl);
                } else {
                    Toast.makeText(MainActivity2.this, "URL no puede estar vacía", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    private void updateImageAndSaveUrl(String newUrl) {
        ImageView imageView = findViewById(R.id.idFotoInventario);
        Glide.with(this).load(newUrl).into(imageView);

        db.collection("Inventario")
                .whereEqualTo("userId", userID)
                .whereEqualTo("nombre", nombreProducto)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        DocumentReference docRef = document.getReference();

                        docRef.update("url", newUrl)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(MainActivity2.this, "URL actualizada y guardada", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(MainActivity2.this, "Error al guardar la nueva URL", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(MainActivity2.this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity2.this, "Error al obtener el producto", Toast.LENGTH_SHORT).show();
                });
    }

}