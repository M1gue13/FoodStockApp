package com.example.foodstockapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class Registrarse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
    }

    public void onRegistrarse(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        EditText nombre = findViewById(R.id.idNombreRe);
        EditText gmail = findViewById(R.id.idGmail);
        EditText contraRe = findViewById(R.id.idContraRe);
        EditText contraCoRe = findViewById(R.id.idContraCoRe);
        CheckBox checkBox = findViewById(R.id.checkBox);

        String nombreF = nombre.getText().toString();
        String gmailF = gmail.getText().toString();
        String contraF = contraRe.getText().toString();
        String contraF2 = contraCoRe.getText().toString();

        if (nombreF.length() >= 5) {
            if (gmailF.contains("@gmail.com") || gmailF.contains("@hotmail.com") || gmailF.contains("@xuqueralumnat.es")) {
                if (contraF.equals(contraF2)) {
                    if (checkBox.isChecked()) {
                        db.collection("Usuarios")
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

                                            Map<String, Object> user = new HashMap<>();
                                            user.put("id", nuevaID); // Usa nuevaID como número
                                            user.put("nombre", nombreF);
                                            user.put("gmail", gmailF);
                                            user.put("contrasenya", contraF);

                                            db.collection("Usuarios")
                                                    .add(user)
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

                                            Intent intent = new Intent(Registrarse.this, MainActivity.class);
                                            startActivity(intent);
                                            mensajeCorto("Bienvenido a FoodStock");
                                        } else {
                                            Log.d(TAG, "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                    } else {
                        mensajeCorto("Selecciona los términos y condiciones");
                    }
                } else {
                    mensajeCorto("Las contraseñas no son iguales");
                }
            } else {
                mensajeCorto("Añade un correo electrónico válido");
            }
        } else {
            mensajeCorto("El usuario tiene que tener como mínimo 5 caracteres");
        }
    }

    public void mensajeCorto(String mensaje){
        Context context = getApplicationContext();
        CharSequence text = mensaje;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast .makeText(context, text, duration);
        toast.show();
    }

    public void onPrinciApp(View view) {
        Intent ia = new Intent(Registrarse.this, MainActivity.class);
        startActivity(ia);
    }
}