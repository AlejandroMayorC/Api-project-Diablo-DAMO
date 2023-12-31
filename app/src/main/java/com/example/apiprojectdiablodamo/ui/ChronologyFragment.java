package com.example.apiprojectdiablodamo.ui;

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

public class ChronologyFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textViewTitle;

    public ChronologyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_chronology, container, false);

        textViewTitle = root.findViewById(R.id.textViewTitle);
        recyclerView = root.findViewById(R.id.recyclerView);

        // Aquí se configuraría el RecyclerView con un LayoutManager y un Adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Datos de ejemplo, reemplar con los propios datos de la API
        String[] myData = {"Dato 1", "Dato 2", "Dato 3"};
        MyAdapter adapter = new MyAdapter(myData);
        recyclerView.setAdapter(adapter);

        // Establecer el título según la opción del menú seleccionada
        // textViewTitle.setText("Chronology");

        return root;
    }

    // Aquí podrías incluir más métodos, por ejemplo, para actualizar la lista cuando se seleccionan los botones
}
