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

        // Cargar la imagen desde la URL recibida
        Glide.with(this).load(imageUrl).into(imageView);

        // Deserializar el JSON a un objeto Personaje
        try {
            Personaje personaje = new Gson().fromJson(personajeJson, Personaje.class);
            // Mostrar detalles del personaje
            StringBuilder descripcion = new StringBuilder();
            descripcion.append("Nombre: ").append(personaje.getName()).append("\n");
            descripcion.append("Habilidades Activas:").append("\n");
            for (Skill skill : personaje.getSkills().getActive()) {
                descripcion.append("- ").append(skill.getName()).append(" (Nivel ").append(skill.getLevel()).append(")\n");
            }
            descripcion.append("Habilidades Pasivas:").append("\n");
            for (Skill skill : personaje.getSkills().getPassive()) {
                descripcion.append("- ").append(skill.getName()).append("\n");
            }
            textViewDescripcion.setText(descripcion.toString());
        } catch (Exception e) {
            textViewDescripcion.setText("Error al mostrar los detalles del personaje.");
        }

        // Configurar botón de retroceso
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }
}


