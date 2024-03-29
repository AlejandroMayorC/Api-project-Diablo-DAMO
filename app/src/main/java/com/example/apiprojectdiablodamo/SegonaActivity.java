package com.example.apiprojectdiablodamo;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.apiprojectdiablodamo.ui.CharacterFragment;
import com.example.apiprojectdiablodamo.ui.GemsFragment;
import com.example.apiprojectdiablodamo.ui.ItemsFragment;
import com.example.apiprojectdiablodamo.ui.PreferitsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SegonaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segona);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                int itemId = item.getItemId();
                if (itemId == R.id.navigation_api1) {
                    fragment = new CharacterFragment();
                } else if (itemId == R.id.navigation_api2){
                    fragment = new ItemsFragment();
                }else if (itemId == R.id.navigation_api3) {
                    fragment = new GemsFragment();
                } else if (itemId == R.id.navigation_favoritos) {
                    fragment = new PreferitsFragment();
                }

                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                    return true;
                }

                return false;
            }
        });

        // Cargar el primer fragment por defecto
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new CharacterFragment())
                    .commit();
        }
    }
}
