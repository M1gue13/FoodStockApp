package com.example.foodstockapp;

import static android.content.ContentValues.TAG;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Inventario extends AppCompatActivity {
    FirebaseFirestore db2 = FirebaseFirestore.getInstance();
    private static final String CHANNEL_ID = "InventoryChannel";
    int userID;
    private CustomAdapter inventarioAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);



        userID = getIntent().getIntExtra("userId", -1);

        TextView textpreuba = findViewById(R.id.textView8);


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView3 = findViewById(R.id.recyclerview3);

        db.collection("Inventario")
                .whereEqualTo("userId", userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<ItemTienda> items = new ArrayList<ItemTienda>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                int id = document.getLong("userId").intValue();
                                String nombre = document.getString("nombre");
                                String url = document.getString("url");
                                Long cantidad = document.getLong("cantidad");

                                if (url != null) {
                                    items.add(new ItemTienda(id, nombre, url, cantidad.intValue()));
                                }



                            }
                            inventarioAdapter = new CustomAdapter(getApplicationContext(), items);
                            inventarioAdapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent pro = new Intent(Inventario.this, MainActivity2.class);
                                    pro.putExtra("id", items.get(recyclerView3.getChildAdapterPosition(view)).getId());
                                    pro.putExtra("nombre", items.get(recyclerView3.getChildAdapterPosition(view)).getNombre());
                                    pro.putExtra("url", items.get(recyclerView3.getChildAdapterPosition(view)).getUrlImage());
                                    pro.putExtra("cantidad", items.get(recyclerView3.getChildAdapterPosition(view)).getCantidad());
                                    pro.putExtra("userId",userID);
                                    startActivity(pro);
                                }
                            });
                            recyclerView3.setLayoutManager(new LinearLayoutManager(Inventario.this));
                            recyclerView3.setAdapter(inventarioAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        createNotificationChannel();

        if (userID != -1) {
            checkInventory(userID);
        }

    }


    private void checkInventory(int userId) {
        db2.collection("Inventario")
                .whereEqualTo("userId", userId)
                .whereIn("cantidad", Arrays.asList(1, 2, 3))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String nombre = document.getString("nombre");
                                int cantidad = document.getLong("cantidad").intValue();
                                showNotification(nombre, cantidad);
                            }
                        } else {

                        }
                    } else {

                    }
                });
    }




    private void showNotification(String nombre, int cantidad) {
        String contentText = "El producto " + nombre + " tiene " + cantidad + " unidades restantes.";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_1)
                .setContentTitle("Stock bajo")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            return;
        }
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Inventory Channel";
            String description = "Channel for inventory notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    public void onMenuPrin(View view) {

        Intent aos = new Intent(Inventario.this, App.class);
        aos.putExtra("userId", userID);
        startActivity(aos);
    }



}