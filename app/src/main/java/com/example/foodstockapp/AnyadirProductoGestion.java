package com.example.foodstockapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


public class AnyadirProductoGestion extends AppCompatActivity {

    int userID;
    EditText textN;
    EditText textU;
    ImageView imag2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anyadir_producto_gestion);
        userID = getIntent().getIntExtra("userId", -1);
        textN = findViewById(R.id.idNombreGesP);
        textU = findViewById(R.id.idUrlProductoGesP);
        imag2 = findViewById(R.id.idFotoProductoG2);


        //Metodo para cargar la imagen cuando se inserta la url
        textU.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String url = textU.getText().toString();
                    if (!url.isEmpty()) {
                        Picasso.get()
                                .load(url)
                                .into(imag2);
                    }
                }
            }
        });
    }

    public void onAnyadirGesP(View view) {
        //Hago un read de todos los productos que salen en lista cuando añades un ticket manualmente
        // Con esto obtengo la id mas alta y le añado un +1 para poder añadir nuevos productos con la id incremental
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String nombre = textN.getText().toString();
        String url = textU.getText().toString();

        Log.d(TAG, "Attempting to add product: " + nombre);

        db.collection("TiendaF")
                .orderBy("id", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int nuevaID = 1;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                long ultimaID = document.getLong("id");
                                int ultimoNumero = (int) ultimaID;
                                nuevaID = ultimoNumero + 1;
                            }

                            Map<String, Object> product = new HashMap<>();
                            product.put("id", nuevaID);
                            product.put("nombre", nombre);
                            product.put("url", url);

                            db.collection("TiendaF")
                                    .add(product)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                            Toast.makeText(AnyadirProductoGestion.this, "Producto añadido con éxito", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(AnyadirProductoGestion.this, App.class);
                                            intent.putExtra("userId", userID);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                            Toast.makeText(AnyadirProductoGestion.this, "Error al añadir producto", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(AnyadirProductoGestion.this, "Error al obtener ID", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    public void onMenuProductoAnnydir(View view) {
        //Intent para volver al menu principal
        Intent intent3 = new Intent(AnyadirProductoGestion.this, App.class);
        intent3.putExtra("userId", userID);
        startActivity(intent3);
    }
}
