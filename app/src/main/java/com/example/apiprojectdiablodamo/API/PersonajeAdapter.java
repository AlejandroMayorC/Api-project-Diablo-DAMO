package com.example.apiprojectdiablodamo.API;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apiprojectdiablodamo.R;
import com.example.apiprojectdiablodamo.ui.OnFavoriteClicked;
import com.example.apiprojectdiablodamo.ui.PreferitsListManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonajeAdapter extends RecyclerView.Adapter<PersonajeAdapter.PersonajeViewHolder> {

    private final Context context;
    private List<Personaje> listaPersonajes;
    private OnPersonajeClickListener listener;
    private OnFavoriteClicked listenerFav;
    private FirebaseFirestore mFirestore;

    public PersonajeAdapter(List<Personaje> listaPersonajes, Context context) {
        this.listaPersonajes = listaPersonajes;
        this.context=context;
    }

    public static class PersonajeViewHolder extends RecyclerView.ViewHolder {
        // Aquí defines los elementos de la vista, como TextViews, ImageViews, etc.
        public TextView textViewNombre;
        public ImageView imageViewIcono;
        public ImageButton Btn_preferits_character;

        public PersonajeViewHolder(View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            imageViewIcono = itemView.findViewById(R.id.imageViewIcono);
            Btn_preferits_character = itemView.findViewById(R.id.Btn_preferits_character);
        }
    }

    // Método para actualizar la lista de personajes
    public void actualizarListaPersonajes(List<Personaje> nuevaLista) {
        listaPersonajes.clear();
        listaPersonajes.addAll(nuevaLista);
        notifyDataSetChanged();
    }

    @Override
    public PersonajeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_for_recyclers, parent, false);
        mFirestore = FirebaseFirestore.getInstance();
        return new PersonajeViewHolder(view);
    }


    public void setOnPersonajeClickListener(OnPersonajeClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(PersonajeViewHolder holder, int position) {
        Personaje personaje = listaPersonajes.get(position);
        if (personaje != null) {
            holder.textViewNombre.setText(personaje.getName());
            String imageUrl = obtenerUrlImagen(personaje.getSlug());
            if (!imageUrl.isEmpty()) {
                Glide.with(holder.imageViewIcono.getContext())
                        .load(imageUrl)
                        .override(200, 200)
                        .centerCrop()
                        .into(holder.imageViewIcono);
            }

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), DetallePersonajeActivity.class);
                intent.putExtra("personaje", new Gson().toJson(personaje));
                intent.putExtra("imagenUrl", imageUrl); // Envía la URL de la imagen
                v.getContext().startActivity(intent);
            });
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), DetallePersonajeActivity.class);
                String personajeJson = new Gson().toJson(personaje);
                intent.putExtra("personajeJson", personajeJson);
                intent.putExtra("imagenUrl", imageUrl);
                v.getContext().startActivity(intent);
            });

            // Configuració visual basada en l'estat de preferit del personatge
            /*if (PreferitsListManager.getInstance().esPreferit(personaje.getName())) {
                holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_on);
            } else {
                holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_off);
            }*/

            //Afegir com a preferit si personatge és a la DB Firebase
            mFirestore.collection("Preferits")
                    .whereEqualTo("name", personaje.getName())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                            // El personatge és a la base de dades, i s'actualitza el ImageButton.
                            holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_on);
                        } else {
                            // El personatge no és a la base de dades, no és un preferit.
                            holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_off);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Maneig de l'error en cas que la consulta no funcioni.
                    });

            holder.Btn_preferits_character.setOnClickListener(v -> {
                personaje.setPreferit(!personaje.getPreferit());
                if (personaje.getPreferit()) {
                    PreferitsListManager.getInstance().afegirPreferit(personaje);
                    putClassDB(personaje);
                } else {
                    PreferitsListManager.getInstance().eliminarPreferit(personaje);
                    //Eliminar personatge de la DB Firebase
                }

                notifyDataSetChanged();
            });
        }

    }
    public static String obtenerUrlImagen(String slug) {
        switch (slug) {
            case "barbarian": return "https://media.vandal.net/i/620x348/11-2018/20181151810494_1.jpg";
            case "wizard": return "https://media.vandal.net/i/620x294/11-2018/20181151810494_4.jpg";
            case "demon-hunter": return "https://media.vandal.net/i/620x348/11-2018/20181151810494_2.jpg";
            case "crusader": return "https://media.vandal.net/i/620x351/11-2018/20181151810494_3.jpg";
            case "monk": return "https://media.vandal.net/i/620x233/11-2018/20181151810494_5.jpg";
            case "witch-doctor": return "https://media.vandal.net/m/11-2018/20181151810494_6.jpg";
            case "necromancer": return "https://media.vandal.net/i/620x348/11-2018/20181151810494_7.jpg";
            default: return "";
        }
    }

    private void putClassDB(Personaje personaje) {
        Map<String, Object> map = new HashMap<>();
        String name = personaje.getName();
        map.put("name", name);

        // Habilitats actives
        List<Map<String, Object>> habilitatsActives = new ArrayList<>();
        for (Skill skill : personaje.getSkills().getActive()) {
            Map<String, Object> mapSkill = new HashMap<>();
            mapSkill.put("nom", skill.getName());
            mapSkill.put("nivell", skill.getLevel());
            habilitatsActives.add(mapSkill);
        }
        map.put("habilitats_actives", habilitatsActives);

        // Habilitats passives
        List<String> habilitatsPassives = new ArrayList<>();
        for (Skill skill : personaje.getSkills().getPassive()) {
            habilitatsPassives.add(skill.getName());
        }
        map.put("habilitats_passives", habilitatsPassives);

        // Ara tens tota la informació del personatge dins del mapPersonaje

        // Fer el que vulguis amb aquest map, com afegir-ho a Firestore, per exemple.
        // mFirestore.collection("Personajes").add(mapPersonaje)...
        mFirestore.collection("Preferits").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(context, "Afegit correctament", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error a l'afegir item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteClassDB(Personaje personaje) {

    }
    
    @Override
    public int getItemCount() {
        return listaPersonajes.size();
    }
}
