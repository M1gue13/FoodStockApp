package com.example.foodstockapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AnalizarTicket extends AppCompatActivity {
    int userID;
    private ImageView imagen;
    private TextView texto;
    private Button escaneo;
    private Uri uri = null;
    private ProgressDialog progressDialog;
    private TextRecognizer textRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analizar_ticket);
        userID = getIntent().getIntExtra("userId", -1);
        imagen = findViewById(R.id.idFoto);
        texto = findViewById(R.id.idTextoEscan);
        escaneo = findViewById(R.id.idScan);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Espera");
        progressDialog.setCanceledOnTouchOutside(false);
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);


        escaneo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uri == null){
                    Toast.makeText(AnalizarTicket.this, "falta imagen", Toast.LENGTH_SHORT).show();
                }else{
                    reconocerTextoImagen();
                }
            }
        });
    }
    private void reconocerTextoImagen() {
        progressDialog.setMessage("Preparando Imagen");
        progressDialog.dismiss();

        try {
            InputImage inputImage = InputImage.fromFilePath(this, uri);
            progressDialog.setMessage("Reconociendo texto");
            Task<Text> textTask = textRecognizer.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            progressDialog.dismiss();
                            String textoFinal = text.getText();
                            texto.setText(textoFinal);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AnalizarTicket.this, "no se puede reconocer texto", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void onCamara(View view) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Titulo");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Des");
        uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        camara.launch(intent);
    }
    private ActivityResultLauncher<Intent> camara = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        imagen.setImageURI(uri);
                        texto.setText("");
                    }else{
                        Toast.makeText(AnalizarTicket.this, "Cancelado", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    public void onScan(View view) {

    }

    public void onMenu(View view) {
        Intent i = new Intent(AnalizarTicket.this, App.class);
        i.putExtra("userId", userID);
        startActivity(i);
    }

    public void onAnyadirInventario(View view) {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String textoEscaneado = texto.getText().toString();
        if (textoEscaneado.isEmpty()) {
            Toast.makeText(this, "No hay texto escaneado", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] lineas = textoEscaneado.split("\n");
        for (String linea : lineas) {
            String[] partes = linea.split(" ", 2);
            if (partes.length == 2) {
                String cantidad = partes[0];
                String nombre = partes[1];

                Map<String, Object> inventarioItem = new HashMap<>();
                inventarioItem.put("userId", userID);
                inventarioItem.put("nombre", nombre);
                int cantidadNumerica = Integer.parseInt(cantidad);
                inventarioItem.put("cantidad", cantidadNumerica);
                inventarioItem.put("url", "https://cdn-icons-png.flaticon.com/128/3524/3524344.png");

                db.collection("Inventario")
                        .add(inventarioItem)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                Intent ia = new Intent(AnalizarTicket.this, App.class);
                                ia.putExtra("userId", userID);
                                startActivity(ia);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);

                            }
                        });

            } else {
                Toast.makeText(this, "Formato de línea incorrecto: " + linea, Toast.LENGTH_SHORT).show();
            }
        }



    }


}