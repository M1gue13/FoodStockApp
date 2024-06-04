package com.example.foodstockapp;

import static android.content.ContentValues.TAG;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private int userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onRegistro(View view) {
        Intent i = new Intent(MainActivity.this , Registrarse.class);
        startActivity(i);
    }

    public void onSesion(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        EditText nombre = findViewById(R.id.idNombre);
        EditText contrasenya = findViewById(R.id.idContra);

        String nom = nombre.getText().toString();
        String conta = contrasenya.getText().toString();


        db.collection("Usuarios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean usuarioValido = false;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if (nom.equals(document.getString("nombre")) && conta.equals(document.getString("contrasenya"))){
                                    userID = document.getLong("id").intValue();
                                    usuarioValido = true;
                                    break;
                                }
                            }
                            if (usuarioValido) {
                                Intent i = new Intent(MainActivity.this, App.class);
                                i.putExtra("userId",userID);
                                startActivity(i);
                                mensajeCorto("Has iniciado sesión correctamente");
                            } else {
                                mensajeCorto("Usuario o contraseña incorrectos");
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }
    public void mensajeCorto(String mensaje){
        Context context = getApplicationContext();
        CharSequence text = mensaje;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast .makeText(context, text, duration);
        toast.show();
    }
}