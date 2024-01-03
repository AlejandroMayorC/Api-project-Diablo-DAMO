package com.example.apiprojectdiablodamo.API;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.apiprojectdiablodamo.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class DetallePersonajeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_personaje);
        // Obtener los datos del Intent
        String personajeJson = getIntent().getStringExtra("personajeJson");
        String imageUrl = getIntent().getStringExtra("imagenUrl");

        // Configurar la vista con la información del personaje
        ImageView imageView = findViewById(R.id.imageViewDetalle);
        TextView textViewDescripcion = findViewById(R.id.textViewDescripcion);
        TextView jsonTextView = findViewById(R.id.jsonTextView);

        // Cargar la imagen desde la URL recibida
        Glide.with(this).load(imageUrl).into(imageView);
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Parsear y mostrar el JSON
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement jsonElement = JsonParser.parseString(personajeJson);
            String prettyJson = gson.toJson(jsonElement);
            jsonTextView.setText(prettyJson);
        } catch (Exception e) {
            jsonTextView.setText("Error al mostrar el JSON.");
        }
    }
}
