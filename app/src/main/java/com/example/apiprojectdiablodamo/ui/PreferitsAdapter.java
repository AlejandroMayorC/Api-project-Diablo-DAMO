package com.example.apiprojectdiablodamo.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apiprojectdiablodamo.API.Personaje;
import com.example.apiprojectdiablodamo.API.PersonajeAdapter;
import com.example.apiprojectdiablodamo.R;
import com.google.android.gms.common.images.ImageManager;

import java.util.List;

public class PreferitsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> listPreferits;
    private static final int TYPE_PERSONATGE = 1;
    private static final int TYPE_ITEM = 2;

    public PreferitsAdapter(List<Object> listPreferits) {
        this.listPreferits = listPreferits;
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

    /*public class ObjetoViewHolder extends RecyclerView.ViewHolder {
        // Elements de la card d'objecte...
    }*/

    /*@NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == Personaje.class.hashCode()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personaje_item, parent, false);
            return new PersonajeViewHolder(view);
        }
        /*else if (viewType == Item.class.hashCode()) {
            // Inflate the layout for the item view
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_objeto, parent, false);
            return new ItemViewHolder(view);
        }
        return null;
    }*/

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_PERSONATGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personaje_item, parent, false);
                return new PersonajeViewHolder(view);
            /*case TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_objeto, parent, false);
                return new ObjetoViewHolder(view);*/
            default:
                return null;
        }
    }


    //@Override
    /*public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object object = listPreferits.get(position);
        if (holder instanceof PersonajeViewHolder && object instanceof Personaje) {
            Personaje personaje = (Personaje) object;
            if (personaje != null) {
                PersonajeViewHolder personajeViewHolder = (PersonajeViewHolder) holder;
                personajeViewHolder.textViewNombre.setText(object.getClass().getName());
                String imageUrl = obtenerUrlImagen(personaje.getSlug());
                if (!imageUrl.isEmpty()) {
                    Glide.with(personajeViewHolder.imageViewIcono.getContext())
                            .load(imageUrl)
                            .override(200, 200)
                            .centerCrop()
                            .into(personajeViewHolder.imageViewIcono);
                }
            }
        }
    }*/

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object object = listPreferits.get(position);
        switch (holder.getItemViewType()) {
            case TYPE_PERSONATGE:
                if (object instanceof Personaje) {
                    Personaje personaje = (Personaje) object;
                    if (personaje != null) {
                        PersonajeViewHolder personajeViewHolder = (PersonajeViewHolder) holder;
                        personajeViewHolder.textViewNombre.setText(personaje.getName());
                        String imageUrl = obtenerUrlImagen(personaje.getSlug());
                        if (!imageUrl.isEmpty()) {
                            Glide.with(personajeViewHolder.imageViewIcono.getContext())
                                    .load(imageUrl)
                                    .override(200, 200)
                                    .centerCrop()
                                    .into(personajeViewHolder.imageViewIcono);
                        }
                    }
                    // Resta del teu codi de configuració per a PersonajeViewHolder
                }
                break;
            /*case TYPE_ITEM:
                if (object instanceof Item) {
                    Item item = (Item) object;
                    ObjetoViewHolder itemViewHolder = (ObjetoViewHolder) holder;
                    // Resta del teu codi de configuració per a ObjetoViewHolder
                }
                break;*/
            default:
                break;
        }
    }


    @Override
    public int getItemCount() {
        return listPreferits.size();
    }

    public String obtenerUrlImagen(String slug) {
        // Accés a la lògica de obtenerUrlImagen des d'aquí
        return PersonajeAdapter.obtenerUrlImagen(slug);
    }

    @Override
    public int getItemViewType(int position) {
        Object object = listPreferits.get(position);
        if (object instanceof Personaje) {
            return TYPE_PERSONATGE;
        } /*else if (object instanceof Item) {
            return TYPE_ITEM;
        }*/
        return -1;
    }

    /*public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }*/
}
