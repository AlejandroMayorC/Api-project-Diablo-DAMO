package com.example.apiprojectdiablodamo.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apiprojectdiablodamo.API.Item;
import com.example.apiprojectdiablodamo.R;
import com.google.gson.Gson;

public class DetalleItemFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_item, container, false);

        // Obtener los datos de los argumentos
        String itemJson = getArguments().getString("itemJson");
        String imageUrl = getArguments().getString("imagenUrl");

        // Configurar la vista con la información del item
        ImageView imageView = view.findViewById(R.id.imageViewItem);
        TextView textViewItemName = view.findViewById(R.id.textViewItemName);
        TextView textViewItemRequiredLevel = view.findViewById(R.id.textViewItemRequiredLevel);
        TextView textViewItemAccountBound = view.findViewById(R.id.textViewItemAccountBound);
        TextView textViewItemTypeName = view.findViewById(R.id.textViewItemTypeName);
        TextView textViewItemDamage = view.findViewById(R.id.textViewItemDamage);
        TextView textViewItemDps = view.findViewById(R.id.textViewItemDps);
        TextView textViewItemColor = view.findViewById(R.id.textViewItemColor);
        TextView textViewItemIsSeasonRequiredToDrop = view.findViewById(R.id.textViewItemIsSeasonRequiredToDrop);
        TextView textViewItemSlots = view.findViewById(R.id.textViewItemSlots);
        TextView textViewItemTwoHanded = view.findViewById(R.id.textViewItemTwoHanded);
        TextView textViewItemTypeId = view.findViewById(R.id.textViewItemTypeId);

        ImageView btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> getFragmentManager().popBackStack());

        // Cargar la imagen desde la URL recibida
        Glide.with(this).load(imageUrl).into(imageView);

        // Deserializar el JSON a un objeto Item
        try {
            Item item = new Gson().fromJson(itemJson, Item.class);

            // Establece el nombre y la descripción del item
            textViewItemName.setText(item.getName());
            textViewItemRequiredLevel.setText("Nivell mínim: " + String.valueOf(item.getRequiredLevel()));            textViewItemAccountBound.setText(item.isAccountBound() ? "Es necessita compte vinculada? Sí" : "Es necessita compte vinculada? No");
            textViewItemTypeName.setText(item.getTypeName());
            textViewItemDamage.setText("Damage: " + item.getDamage());
            textViewItemDps.setText("DPS: " + item.getDps());
            textViewItemColor.setText("Color: " + item.getColor());
            textViewItemIsSeasonRequiredToDrop.setText(item.getIsSeasonRequiredToDrop() ? "Requereix una temporada? Sí" : "Requereix una temporada? No");
            textViewItemSlots.setText("Slots: " + TextUtils.join(", ", item.getSlots()));
            textViewItemTwoHanded.setText(item.getType().isTwoHanded() ? "És una arma a dos mans" : "Només ocupa un slot / mà");
            textViewItemTypeId.setText("Id: " + item.getType().getId());

        } catch (Exception e) {
            textViewItemName.setText("Error al mostrar la información del item.");
        }

        return view;
    }

    public static DetalleItemFragment newInstance(Item item, String imageUrl) {
        DetalleItemFragment fragment = new DetalleItemFragment();
        Bundle args = new Bundle();
        args.putString("itemJson", new Gson().toJson(item));
        args.putString("imagenUrl", imageUrl);
        fragment.setArguments(args);
        return fragment;
    }
}

