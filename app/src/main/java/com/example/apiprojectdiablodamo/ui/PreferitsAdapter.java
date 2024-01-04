package com.example.apiprojectdiablodamo.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apiprojectdiablodamo.API.Personaje;
import com.example.apiprojectdiablodamo.R;

import java.util.List;

public class PreferitsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> listPreferits;
    private static final int TYPE_PERSONATGE = 1;
    private static final int TYPE_ITEM = 2;

    public PreferitsAdapter(List<Object> listPreferits) {
        this.listPreferits = listPreferits;
    }

    @NonNull
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
        }*/
        return null;
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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listPreferits.size();
    }

    /*public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }*/
}
