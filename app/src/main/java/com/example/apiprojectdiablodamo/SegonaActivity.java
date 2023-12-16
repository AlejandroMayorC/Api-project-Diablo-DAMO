package com.example.apiprojectdiablodamo;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.apiprojectdiablodamo.databinding.ActivitySegonaBinding;

public class SegonaActivity extends AppCompatActivity {

    private ActivitySegonaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySegonaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configura la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_chronology, R.id.navigation_acts, R.id.navigation_craft,
                 R.id.navigation_character, R.id.navigation_item)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_segona);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}