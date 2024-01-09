package com.example.apiprojectdiablodamo.API;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.apiprojectdiablodamo.R;
import com.google.gson.Gson;

public class DetalleItemActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detalle_item);

        String nameItem = getIntent().getStringExtra("nameItem");
        String itemJson = getIntent().getStringExtra("itemJson");
        String imageUrl = getIntent().getStringExtra("imagenUrl");

        ImageView imageView = findViewById(R.id.imageViewItem);
        TextView textViewItemName = findViewById(R.id.textViewItemName);
        TextView textViewItemRequiredLevel = findViewById(R.id.textViewItemRequiredLevel);
        TextView textViewItemAccountBound = findViewById(R.id.textViewItemAccountBound);
        TextView textViewItemTypeName = findViewById(R.id.textViewItemTypeName);
        TextView textViewItemDamage = findViewById(R.id.textViewItemDamage);
        TextView textViewItemDps = findViewById(R.id.textViewItemDps);
        TextView textViewItemColor = findViewById(R.id.textViewItemColor);
        TextView textViewItemIsSeasonRequiredToDrop = findViewById(R.id.textViewItemIsSeasonRequiredToDrop);
        TextView textViewItemSlots = findViewById(R.id.textViewItemSlots);
        TextView textViewItemTwoHanded = findViewById(R.id.textViewItemTwoHanded);
        TextView textViewItemTypeId = findViewById(R.id.textViewItemTypeId);

        Glide.with(this).load(imageUrl).into(imageView);
        textViewItemName.setText(nameItem);

        try {
            Item item = new Gson().fromJson(itemJson, Item.class);
            // Establece el nombre y la descripción del item
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

        // Configurar botón de retroceso
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }
}