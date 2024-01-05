package com.example.apiprojectdiablodamo.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private List<Object> listPreferits = PreferitsListManager.getInstance().getLlistaPreferits();

    public PreferitsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preferits, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewPreferits);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PreferitsAdapter(listPreferits);
        recyclerView.setAdapter(adapter);

        return view;
    }

}