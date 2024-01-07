package com.example.apiprojectdiablodamo.API;

import android.os.Bundle;
import android.text.Html;
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
        TextView textViewHabilidadesActivas = findViewById(R.id.textViewHabilidadesActivas);
        TextView textViewHabilidadesPasivas = findViewById(R.id.textViewHabilidadesPasivas);
        TextView textViewNombre = findViewById(R.id.textViewNombre);

        // Cargar la imagen desde la URL recibida
        Glide.with(this).load(imageUrl).into(imageView);

        // Deserializar el JSON a un objeto Personaje
        try {
            Personaje personaje = new Gson().fromJson(personajeJson, Personaje.class);

            // Establecer el nombre del personaje
            textViewNombre.setText(personaje.getName());

            // Formatear y mostrar detalles de habilidades activas
            StringBuilder descripcionActivas = new StringBuilder();
            descripcionActivas.append("<b>Habilitats Actives:</b><br/>");
            for (Skill skill : personaje.getSkills().getActive()) {
                descripcionActivas.append("- ").append(skill.getName()).append(" (Nivell ").append(skill.getLevel()).append(")<br/>");
            }
            textViewHabilidadesActivas.setText(Html.fromHtml(descripcionActivas.toString(), Html.FROM_HTML_MODE_LEGACY));

            // Formatear y mostrar detalles de habilidades pasivas
            StringBuilder descripcionPasivas = new StringBuilder();
            descripcionPasivas.append("<b>Habilitats Passives:</b><br/>");
            for (Skill skill : personaje.getSkills().getPassive()) {
                descripcionPasivas.append("- ").append(skill.getName()).append("<br/>");
            }
            textViewHabilidadesPasivas.setText(Html.fromHtml(descripcionPasivas.toString(), Html.FROM_HTML_MODE_LEGACY));
        } catch (Exception e) {
            textViewHabilidadesActivas.setText("Error al mostrar las habilidades activas.");
            textViewHabilidadesPasivas.setText("Error al mostrar las habilidades pasivas.");
        }

        // Configurar botón de retroceso
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }
}


