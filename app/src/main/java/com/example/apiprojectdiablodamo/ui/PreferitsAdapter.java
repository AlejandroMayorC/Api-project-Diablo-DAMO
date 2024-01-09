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
import com.example.apiprojectdiablodamo.API.DetalleItemActivity;
import com.example.apiprojectdiablodamo.API.DetallePersonajeActivity;
import com.example.apiprojectdiablodamo.API.Item;
import com.example.apiprojectdiablodamo.API.ItemAdapter;
import com.example.apiprojectdiablodamo.API.Personaje;
import com.example.apiprojectdiablodamo.API.PersonajeAdapter;
import com.example.apiprojectdiablodamo.API.PersonajeManager;
import com.example.apiprojectdiablodamo.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PreferitsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> listPreferits;
    private FirebaseFirestore mFirestore;
    private static final int TYPE_PERSONATGE = 1;
    private static final int TYPE_ITEM = 2;
    private Set<String> nombresUnicos = new HashSet<>();

    public PreferitsAdapter(List<Object> listPreferits) {
        this.listPreferits = new ArrayList<>(listPreferits);
        mFirestore = FirebaseFirestore.getInstance();
        afegirObjectesALlistaPreferits();
        actualizarListaPreferits(listPreferits);
        //actualizarNombresUnicos();
    }

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
                        Intent intent = new Intent(v.getContext(), DetalleItemActivity.class);
                        intent.putExtra("nameItem", item.getName());
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
        Log.d("Personajes", "listpreferits: " + listPreferits);
        //afegirObjectesALlistaPreferits();
        Log.d("Personajes", "Personajes disponibles despres: " + PreferitsListManager.getInstance().getLlistaPreferits());
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preferit_info_card, parent, false);
        return new ViewHolder(view, this);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object object = listPreferits.get(position);
        Log.d("list", "Llista onbindviewholder: " + listPreferits);
        //Object object = PreferitsListManager.getInstance().getLlistaPreferits().get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        Log.d("Noms", "Nom preferits: " + nombresUnicos);
        // Additional configurations based on the object type
        if (object instanceof Personaje) {
            // Personaje-specific configurations here
            String name = ((Personaje) object).getName();
            Log.d("ItemAdapter", "Nom de l'ítem: " + name);
            viewHolder.textViewNom.setText(((Personaje) object).getName());

            String slug = ((Personaje) object).getSlug();
            if (slug != null) {
                String imageUrl = obtenerUrlImagen(slug, TYPE_PERSONATGE);
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Glide.with(viewHolder.imageViewIcono.getContext())
                            .load(imageUrl)
                            .override(200, 200)
                            .centerCrop()
                            .into(viewHolder.imageViewIcono);
                }
            }
        } else if (object instanceof Item) {
            // Item-specific configurations here
            viewHolder.textViewNom.setText(((Item) object).getName());
            String slug = ((Item) object).getSlug();
            if (slug != null) {
                String imageUrl = obtenerUrlImagen(slug, TYPE_ITEM);
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Glide.with(viewHolder.imageViewIcono.getContext())
                            .load(imageUrl)
                            .override(200, 200)
                            .centerCrop()
                            .into(viewHolder.imageViewIcono);
                }
            }
        }
        // Glide configuration


    }

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
        actualizarNombresUnicos();
        notifyDataSetChanged();
    }

    private void actualizarNombresUnicos() {
        nombresUnicos.clear(); // Limpiar el conjunto antes de actualizarlo
        for (Object object : listPreferits) {
            if (object instanceof Personaje) {
                nombresUnicos.add(((Personaje) object).getName());
            } else if (object instanceof Item) {
                nombresUnicos.add(((Item) object).getName());
            }
        }
    }

    private void afegirObjectesALlistaPreferits() {
        Log.d("Preferits", "Personajes disponibles abans: " + PreferitsListManager.getInstance().getLlistaPreferits());
        //PreferitsListManager.getInstance().buidarLlistaPreferits();
        listPreferits.clear();
        mFirestore.collection("Preferits")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String objectClassName = (String) document.get("tipusClasse");
                            Object object = null;
                            //Object object = document.toObject(Object.class);
                            //String nomTipus = String.valueOf(object.getClass());
                            Log.d("Preferits", "Nom objecte DB: " + objectClassName);
                            //Log.d("Preferits", "Objecte afegit a la llista de preferits: " + object.toString());
                            if ("Item".equals(objectClassName)) {
                                object = document.toObject(Item.class);
                                Item item = (Item) object;
                                item.setPreferit(true);
                                //PreferitsListManager.getInstance().afegirPreferit(item);
                                String nombre = obtenerNombreUnico(object);
                                Log.d("Preferits", "Nom actual: " + nombre);
                                if (!nombresUnicos.contains(nombre)) {
                                    Log.d("Preferits", "Nombres unics abans: " + nombresUnicos);
                                    nombresUnicos.add(nombre);
                                    Log.d("Preferits", "Nombres unics despres: " + nombresUnicos);
                                    Log.d("Preferits", "Objectes llista preferits abans" + listPreferits.toString());
                                    listPreferits.add(object);
                                    Log.d("Preferits", "Objectes llista preferits despres" + listPreferits.toString());
                                    PreferitsListManager.getInstance().afegirPreferit(object);
                                }
                            } else if ("Personaje".equals(objectClassName)) {
                                object = document.toObject(Personaje.class);
                                Personaje personaje = (Personaje) object;
                                personaje.setPreferit(true);
                                //PreferitsListManager.getInstance().afegirPreferit(personaje);
                                String nombre = obtenerNombreUnico(object);
                                Log.d("actual", "Nom actual: " + nombre);
                                if (!nombresUnicos.contains(nombre)) {
                                    Log.d("Preferits", "Nombres unics abans: " + nombresUnicos);
                                    nombresUnicos.add(nombre);
                                    Log.d("Preferits", "Nombres unics despres: " + nombresUnicos);
                                    Log.d("Preferits", "Objectes llista preferits abans" + listPreferits.toString());
                                    listPreferits.add(object);
                                    Log.d("Preferits", "Objectes llista preferits despres" + listPreferits.toString());
                                    PreferitsListManager.getInstance().afegirPreferit(object);
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Maneig de l'error en cas que la consulta no funcioni.
                });
        actualizarListaPreferits(listPreferits);
        notifyDataSetChanged();
        //Log.d("Preferits", "Objectes llista preferits" + listPreferits.toString());
        //Log.d("Preferits", "Objectes llista_original" + PreferitsListManager.getInstance().getLlistaPreferits().toString());
    }

    private String obtenerNombreUnico(Object object) {
        if (object instanceof Personaje) {
            return ((Personaje) object).getName();
        } else if (object instanceof Item) {
            return ((Item) object).getName();
        }
        return null;
    }

}
