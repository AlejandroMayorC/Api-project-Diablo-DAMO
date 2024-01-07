package com.example.apiprojectdiablodamo.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apiprojectdiablodamo.API.Personaje;
import com.example.apiprojectdiablodamo.API.PersonajeAdapter;
import com.example.apiprojectdiablodamo.R;

import java.util.ArrayList;
import java.util.List;

public class PreferitsFragment extends Fragment {

    private RecyclerView recyclerView;
    private PreferitsAdapter adapter;
    private List<Object> listPreferitsOriginal; // Lista original
    private List<Object> listPreferits; // Lista para mostrar datos

    public PreferitsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preferits, container, false);

        listPreferitsOriginal = PreferitsListManager.getInstance().getLlistaPreferits();
        listPreferits = new ArrayList<>(listPreferitsOriginal); // Crear una copia para mostrar
        recyclerView = view.findViewById(R.id.recyclerViewPreferits);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PreferitsAdapter(listPreferits);
        recyclerView.setAdapter(adapter);
        SearchView searchViewPreferits = view.findViewById(R.id.searchViewFav);
        searchViewPreferits.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrarPreferits(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarPreferits(newText);
                return true;
            }
        });


        return view;
    }

    private void filtrarPreferits(String texto) {
        List<Object> listaFiltradaTemp = new ArrayList<>();
        for (Object obj : listPreferitsOriginal) {
            if (obj instanceof Personaje) {
                Personaje personaje = (Personaje) obj;
                if (personaje.getName().toLowerCase().contains(texto.toLowerCase())) {
                    listaFiltradaTemp.add(personaje);
                }
            }
        }
        adapter.actualizarListaPreferits(listaFiltradaTemp); // Actualiza la lista del adaptador
    }


}