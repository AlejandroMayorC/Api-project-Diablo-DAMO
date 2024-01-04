package com.example.apiprojectdiablodamo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.apiprojectdiablodamo.API.ApiActivity;

public class PortadaActivity extends AppCompatActivity {

    private ImageView logoImage;
    private Button IniciActivitat_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portada);

        logoImage=(ImageView) findViewById(R.id.logoImageView);
        IniciActivitat_btn=(Button) findViewById(R.id.IniciActivitat_btn);

        IniciActivitat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PortadaActivity.this, SegonaActivity.class);
                startActivity(intent);
            }
        });
    }
}