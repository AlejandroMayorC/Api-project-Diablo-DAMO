package com.example.apiprojectdiablodamo.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apiprojectdiablodamo.R;

public class CharacterFragment extends Fragment {

    private RecyclerView recyclerView;
    private Button buttonAll;
    private Button buttonFavorites;
    private TextView textViewTitle;

    public CharacterFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_character, container, false);

        textViewTitle = root.findViewById(R.id.textViewTitle);
        buttonAll = root.findViewById(R.id.buttonAll);
        buttonFavorites = root.findViewById(R.id.buttonFavorites);
        recyclerView = root.findViewById(R.id.recyclerView);

        // Aquí se configuraría el RecyclerView con un LayoutManager y un Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Suponiendo que tienes un adaptador llamado MyAdapter
        // recyclerView.setAdapter(new MyAdapter());

        // Configurar los oyentes de los botones
        buttonAll.setOnClickListener(view -> {
            // Aquí manejarías el clic en el botón "Todos"
        });

        buttonFavorites.setOnClickListener(view -> {
            // Aquí manejarías el clic en el botón "Favoritos"
        });

        // Establecer el título según la opción del menú seleccionada
        // textViewTitle.setText("Character");

        return root;
    }

    // Aquí podrías incluir más métodos, por ejemplo, para actualizar la lista cuando se seleccionan los botones
}