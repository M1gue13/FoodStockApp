package com.example.foodstockapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;

public class App extends AppCompatActivity {
    int userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        userID = getIntent().getIntExtra("userId", -1);


    }



    public void onTienda(View view) {
        Intent i = new Intent(App.this, Tienda.class);
        i.putExtra("userId", userID);
        startActivity(i);
    }

    public void onCerrarSesion(View view) {
        Intent cerarSesion = new Intent(App.this, MainActivity.class);
        startActivity(cerarSesion);
    }

    public void onAnalizarTicket(View view) {
        Intent i2 = new Intent(App.this, AnalizarTicket.class);
        i2.putExtra("userId", userID);
        startActivity(i2);
    }

    public void onInventario(View view) {
        Intent inven = new Intent(App.this, Inventario.class);
        inven.putExtra("userId", userID);
        startActivity(inven);
    }

    public void onAnyadirProductogestion(View view) {
        Intent ges = new Intent(App.this, AnyadirProductoGestion.class);
        ges.putExtra("userId", userID);
        startActivity(ges);
    }
}
