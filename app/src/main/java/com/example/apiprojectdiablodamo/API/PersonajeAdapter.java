package com.example.apiprojectdiablodamo.API;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apiprojectdiablodamo.R;
import com.example.apiprojectdiablodamo.ui.OnFavoriteClicked;
import com.google.gson.Gson;

import java.util.List;

public class PersonajeAdapter extends RecyclerView.Adapter<PersonajeAdapter.PersonajeViewHolder> {

    private List<Personaje> listaPersonajes;
    private OnPersonajeClickListener listener;
    private OnFavoriteClicked listenerFav;

    public PersonajeAdapter(List<Personaje> listaPersonajes) {
        this.listaPersonajes = listaPersonajes;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personaje_item, parent, false);
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
            if (personaje.getPreferit()) {
                holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_on);
            } else {
                holder.Btn_preferits_character.setImageResource(R.drawable.btn_star_big_off);
            }

            holder.Btn_preferits_character.setOnClickListener(v -> {
                // Inverteix l'estat de preferit del personatge
                personaje.setPreferit(!personaje.getPreferit());

                // Notifica els canvis al RecyclerView
                notifyDataSetChanged();

                // Aquí pots cridar una funció del listener si necessites informar a altres components de l'acció
                /*if (listener != null) {
                    listenerFav.onFavoriteClicked(personaje);
                }*/
            });
        }

    }
    private String obtenerUrlImagen(String slug) {
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


    @Override
    public int getItemCount() {
        return listaPersonajes.size();
    }
}
