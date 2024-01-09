package com.example.apiprojectdiablodamo.ui;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.apiprojectdiablodamo.API.DetallePersonajeActivity;
import com.example.apiprojectdiablodamo.API.Item;
import com.example.apiprojectdiablodamo.API.ItemAdapter;
import com.example.apiprojectdiablodamo.API.Personaje;
import com.example.apiprojectdiablodamo.API.PersonajeAdapter;
import com.example.apiprojectdiablodamo.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PreferitsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> listPreferits;
    private static final int TYPE_PERSONATGE = 1;
    private static final int TYPE_ITEM = 2;

    public PreferitsAdapter(List<Object> listPreferits) {
        this.listPreferits = new ArrayList<>(listPreferits);
    }

    /*public static class PersonajeViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNombre;
        public ImageView imageViewIcono;
        public ImageButton Btn_preferits_character;

        public PersonajeViewHolder(View itemView, PreferitsAdapter adapter) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            imageViewIcono = itemView.findViewById(R.id.imageViewIcono);
            Btn_preferits_character = itemView.findViewById(R.id.Btn_preferits_character);

            Btn_preferits_character.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Object preferit = adapter.listPreferits.get(position);
                    if (preferit instanceof Personaje) {
                        Personaje personaje = (Personaje) preferit;
                        Intent intent = new Intent(v.getContext(), DetallePersonajeActivity.class);
                        intent.putExtra("personajeJson", new Gson().toJson(personaje));
                        String imageUrl = PreferitsAdapter.obtenerUrlImagen(personaje.getSlug(), TYPE_PERSONATGE);
                        intent.putExtra("imagenUrl", imageUrl);
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNombre;
        public ImageView imageViewIcono;
        public ImageButton Btn_preferits_character;

        public ItemViewHolder(View itemView, PreferitsAdapter adapter) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            imageViewIcono = itemView.findViewById(R.id.imageViewIcono);
            Btn_preferits_character = itemView.findViewById(R.id.Btn_preferits_character);

            Btn_preferits_character.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Object preferit = adapter.listPreferits.get(position);
                    if (preferit instanceof Item) {
                        Item item = (Item) preferit;
                        Intent intent = new Intent(v.getContext(), DetallePersonajeActivity.class);
                        intent.putExtra("personajeJson", new Gson().toJson(item));
                        String imageUrl = PreferitsAdapter.obtenerUrlImagen(item.getSlug(), TYPE_ITEM);
                        intent.putExtra("imagenUrl", imageUrl);
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }*/

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNom;
        public ImageView imageViewIcono;
        public ImageButton Btn_preferits_character;

        public ViewHolder(View itemView, PreferitsAdapter adapter) {
            super(itemView);
            textViewNom = itemView.findViewById(R.id.textViewNom);
            imageViewIcono = itemView.findViewById(R.id.imageViewIcono);
            Btn_preferits_character = itemView.findViewById(R.id.Btn_preferits_character);

            Btn_preferits_character.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Object preferit = adapter.listPreferits.get(position);
                    if (preferit instanceof Personaje) {
                        Personaje personaje = (Personaje) preferit;
                        Intent intent = new Intent(v.getContext(), DetallePersonajeActivity.class);
                        intent.putExtra("personajeJson", new Gson().toJson(personaje));
                        String imageUrl = PreferitsAdapter.obtenerUrlImagen(personaje.getSlug(), TYPE_PERSONATGE);
                        intent.putExtra("imagenUrl", imageUrl);
                        v.getContext().startActivity(intent);
                    } else if (preferit instanceof Item) {
                        Item item = (Item) preferit;
                        Intent intent = new Intent(v.getContext(), DetallePersonajeActivity.class);
                        intent.putExtra("itemJson", new Gson().toJson(item));
                        String imageUrl = PreferitsAdapter.obtenerUrlImagen(item.getSlug(), TYPE_ITEM);
                        intent.putExtra("imagenUrl", imageUrl);
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preferit_info_card, parent, false);
        return new ViewHolder(view, this);
        /*switch (viewType) {
            case TYPE_PERSONATGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personaje_preferit_item, parent, false);
                return new PersonajeViewHolder(view, this);
            case TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preferit_info_card, parent, false);
                return new ItemViewHolder(view, this);
            default:
                return null;
        }*/

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object object = listPreferits.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        // Additional configurations based on the object type
        if (object instanceof Personaje) {
            // Personaje-specific configurations here
            String name = ((Personaje) object).getName();
            Log.d("ItemAdapter", "Nom de l'ítem: " + name);
            viewHolder.textViewNom.setText(((Personaje) object).getName());

            String imageUrl = obtenerUrlImagen(((Personaje) object).getSlug(), holder.getItemViewType());
            if (!imageUrl.isEmpty()) {
                Glide.with(viewHolder.imageViewIcono.getContext())
                        .load(imageUrl)
                        .override(200, 200)
                        .centerCrop()
                        .into(viewHolder.imageViewIcono);
            }
        } else if (object instanceof Item) {
            // Item-specific configurations here
            viewHolder.textViewNom.setText(((Item) object).getName());
            String imageUrl = obtenerUrlImagen(((Item) object).getSlug(), holder.getItemViewType());
            if (!imageUrl.isEmpty()) {
                Glide.with(viewHolder.imageViewIcono.getContext())
                        .load(imageUrl)
                        .override(200, 200)
                        .centerCrop()
                        .into(viewHolder.imageViewIcono);
            }
        }
        // Glide configuration


    }
        /*switch (holder.getItemViewType()) {
            case TYPE_PERSONATGE:
                if (object instanceof Personaje) {
                    Personaje personaje = (Personaje) object;
                    if (personaje != null) {
                        PersonajeViewHolder personajeViewHolder = (PersonajeViewHolder) holder;
                        personajeViewHolder.textViewNombre.setText(personaje.getName());
                        String imageUrl = obtenerUrlImagen(personaje.getSlug(), holder.getItemViewType());
                        if (!imageUrl.isEmpty()) {
                            Glide.with(personajeViewHolder.imageViewIcono.getContext())
                                    .load(imageUrl)
                                    .override(200, 200)
                                    .centerCrop()
                                    .into(personajeViewHolder.imageViewIcono);
                        }
                    }
                }
                break;
            case TYPE_ITEM:
                if (object instanceof Item) {
                    Item item = (Item) object;
                    if (item != null) {
                        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                        itemViewHolder.textViewNombre.setText(item.getName());
                        String imageUrl = obtenerUrlImagen(item.getSlug(), holder.getItemViewType());

                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(itemViewHolder.imageViewIcono.getContext())
                                    .load(imageUrl)
                                    .override(200, 200)
                                    .centerCrop()
                                    .into(itemViewHolder.imageViewIcono);
                        }
                    }
                    break;
                }
        }*/


    @Override
    public int getItemCount() {
        return listPreferits.size();
    }

    public static String obtenerUrlImagen(String slug, int viewType) {
        switch (viewType) {
            case TYPE_PERSONATGE:
                return PersonajeAdapter.obtenerUrlImagen(slug);
            case TYPE_ITEM:
                return ItemAdapter.obtenerUrlImagen(slug);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object object = listPreferits.get(position);
        if (object instanceof Personaje) {
            return TYPE_PERSONATGE;
        } else if (object instanceof Item) {
            return TYPE_ITEM;
        }
        return -1;
    }

    public void actualizarListaPreferits(List<Object> nuevaLista) {
        listPreferits.clear();
        listPreferits.addAll(nuevaLista);
        notifyDataSetChanged();
    }

    /*public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }*/
}
