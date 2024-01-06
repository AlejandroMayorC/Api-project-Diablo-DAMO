package com.example.apiprojectdiablodamo.API;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.apiprojectdiablodamo.R;
import com.example.apiprojectdiablodamo.ui.ActsFragment;
import com.example.apiprojectdiablodamo.ui.CharacterFragment;
import com.example.apiprojectdiablodamo.ui.CraftFragment;
import com.example.apiprojectdiablodamo.ui.PreferitsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class ApiActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PersonajeAdapter adapter;
    private List<Personaje> listaPersonajes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segona);

        recyclerView = findViewById(R.id.recyclerViewPersonajes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PersonajeAdapter(listaPersonajes, getApplicationContext());
        recyclerView.setAdapter(adapter);

        adapter.setOnPersonajeClickListener(personaje -> {
            Intent intent = new Intent(this, DetallePersonajeActivity.class);
            intent.putExtra("JSON_PERSONAJE", new Gson().toJson(personaje));
            startActivity(intent);
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                int itemId = item.getItemId();
                if (itemId == R.id.navigation_api1) {
                    fragment = new CharacterFragment();
                } else if (itemId == R.id.navigation_api2) {
                    fragment = new ActsFragment();
                } else if (itemId == R.id.navigation_api3) {
                    fragment = new CraftFragment();
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

        String clientId = "0cd1b84e2eb34dcf89f6731e1282f74e";
        String clientSecret = "6MKHGsFhv8dU9lE3jvL9z2aUhpGsmbwW";
        String credentials = clientId + ":" + clientSecret;
        String authHeaderValue = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        ApiInterface oAuthApiInterface = ApiService.getOAuthApiInterface();
        Call<AccessTokenResponse> callToken = oAuthApiInterface.obtenerTokenDeAcceso("client_credentials", authHeaderValue);

        callToken.enqueue(new Callback<AccessTokenResponse>() {
            @Override
            public void onResponse(Call<AccessTokenResponse> call, Response<AccessTokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String accessToken = "Bearer " + response.body().getAccess_token();
                    obtenerPersonajes(ApiService.getApiInterface(), accessToken);
                }
            }

            @Override
            public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
                // Manejar el fallo en la llamada a la API
            }
        });
    }

    private void obtenerPersonajes(ApiInterface apiInterface, String accessToken) {
        String[] classSlugs = {"barbarian", "wizard", "demon-hunter", "crusader", "monk", "witch-doctor", "necromancer"};

        for (String slug : classSlugs) {
            Call<Personaje> callPersonaje = apiInterface.obtenerPersonaje(slug, accessToken);
            callPersonaje.enqueue(new Callback<Personaje>() {
                @Override
                public void onResponse(Call<Personaje> call, Response<Personaje> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        listaPersonajes.add(response.body());
                        adapter.notifyDataSetChanged();
                    } else {
                        // Manejar respuesta no exitosa
                    }
                }

                @Override
                public void onFailure(Call<Personaje> call, Throwable t) {
                    // Manejar el fallo en la llamada a la API
                }
            });
        }
    }
}

