package com.example.apiprojectdiablodamo.API;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonajeAdapter extends RecyclerView.Adapter<PersonajeAdapter.PersonajeViewHolder> {

    private final Context context;
    private List<Personaje> listaPersonajes;
    private OnPersonajeClickListener listener;
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
        //comprovacioEsPreferitDB();
        PersonajeViewHolder holder = new PersonajeViewHolder(view);
        return holder;
    }


    public void setOnPersonajeClickListener(OnPersonajeClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(PersonajeViewHolder holder, int position) {
        Personaje personaje = listaPersonajes.get(position);
        comprovacioEsPreferitDB(personaje, holder);
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
            if (personaje.getPreferit()) {
                holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_on);
            } else {
                holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_off);
            }

            holder.Btn_preferits_character.setOnClickListener(v -> {
                personaje.setPreferit(!personaje.getPreferit());

                if (personaje.getPreferit()) {
                    PreferitsListManager.getInstance().afegirPreferit(personaje);
                    putClassDB(personaje, holder);
                    holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_on);
                } else {
                    PreferitsListManager.getInstance().eliminarPreferit(personaje);
                    deleteClassDB(personaje, holder);
                    holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_off);
                }
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

    private void putClassDB(Personaje personaje, PersonajeViewHolder holder) {
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

        mFirestore.collection("Preferits").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(context, "Afegit correctament", Toast.LENGTH_SHORT).show();
                holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_on);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error a l'afegir item", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteClassDB(Personaje personaje, PersonajeViewHolder holder) {

        mFirestore.collection("Preferits")
                .whereEqualTo("name", personaje.getName())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        // Si es troba el document amb el nom del personatge, eliminar-lo
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            mFirestore.collection("Preferits")
                                    .document(document.getId()) // Obté l'ID del document
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        // Eliminat amb èxit
                                        Toast.makeText(context, "Personatge eliminat!", Toast.LENGTH_SHORT).show();
                                        holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_off);
                                    })
                                    .addOnFailureListener(e -> {
                                        // Error en eliminar el document
                                        Toast.makeText(context, "Error en eliminar el personatge", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        // Si no es troba cap document amb aquest nom
                        Toast.makeText(context, "No s'ha trobat el personatge", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Error en la consulta
                    Toast.makeText(context, "Error en la consulta", Toast.LENGTH_SHORT).show();
                });
    }

    private void comprovacioEsPreferitDB(Personaje personaje, PersonajeViewHolder holder) {
        PreferitsListManager.getInstance().buidarObjecteLlistaPreferits(Personaje.class);
        mFirestore.collection("Preferits")
                .whereEqualTo("name", personaje.getName())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        personaje.setPreferit(true);
                        holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_on);
                        PreferitsListManager.getInstance().afegirPreferit(personaje);
                    } else {
                        personaje.setPreferit(false);
                        holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_off);
                    }
                })
                .addOnFailureListener(e -> {
                    // Maneig de l'error en cas que la consulta no funcioni.
                });
    }
    
    @Override
    public int getItemCount() {
        return listaPersonajes.size();
    }
}
