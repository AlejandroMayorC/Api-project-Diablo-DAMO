package com.example.apiprojectdiablodamo.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.apiprojectdiablodamo.API.Personaje;
import com.example.apiprojectdiablodamo.API.Skill;
import com.example.apiprojectdiablodamo.R;
import com.google.gson.Gson;


public class DetallePersonajeFragment extends Fragment {

    public static DetallePersonajeFragment newInstance(Personaje personaje) {
        DetallePersonajeFragment fragment = new DetallePersonajeFragment();
        Bundle args = new Bundle();
        args.putString("personajeJson", new Gson().toJson(personaje));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_detalle_personaje, container, false);

        // Obtener los datos de los argumentos
        String personajeJson = getArguments().getString("personajeJson");
        String imageUrl = getArguments().getString("imagenUrl");

        // Configurar la vista con la información del personaje
        ImageView imageView = view.findViewById(R.id.imageViewDetalle);
        TextView textViewHabilidadesActivas = view.findViewById(R.id.textViewHabilidadesActivas);
        TextView textViewHabilidadesPasivas = view.findViewById(R.id.textViewHabilidadesPasivas);
        TextView textViewNombre = view.findViewById(R.id.textViewNombre);

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
        ImageView backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> getFragmentManager().popBackStack());

        return view;
    }
}