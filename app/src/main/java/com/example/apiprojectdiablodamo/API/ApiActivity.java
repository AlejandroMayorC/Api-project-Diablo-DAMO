package com.example.apiprojectdiablodamo.API;
import android.os.Bundle;
import android.util.Base64;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apiprojectdiablodamo.API.AccessTokenResponse;
import com.example.apiprojectdiablodamo.API.ApiInterface;
import com.example.apiprojectdiablodamo.API.ApiService;
import com.example.apiprojectdiablodamo.API.DataViewModel;
import com.example.apiprojectdiablodamo.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiActivity extends AppCompatActivity {
    private DataViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segona);
        viewModel = new ViewModelProvider(this).get(DataViewModel.class);
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
        navView.setOnNavigationItemSelectedListener(item -> {
            // Aquí gestionas la navegación
            return true;
        });

        obtenerTokenYDatos();
    }

    private void obtenerTokenYDatos() {
        String clientId = "tu_client_id";
        String clientSecret = "tu_client_secret";
        String authHeaderValue = "Basic " + Base64.encodeToString((clientId + ":" + clientSecret).getBytes(), Base64.NO_WRAP);

        ApiInterface oAuthApiInterface = ApiService.getOAuthApiInterface();
        Call<AccessTokenResponse> callToken = oAuthApiInterface.obtenerTokenDeAcceso("client_credentials", authHeaderValue);

        callToken.enqueue(new Callback<AccessTokenResponse>() {
            @Override
            public void onResponse(Call<AccessTokenResponse> call, Response<AccessTokenResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String accessToken = "Bearer " + response.body().getAccess_token();
                    viewModel.cargarPersonajes(ApiService.getApiInterface(), accessToken);
                    viewModel.cargarItems(ApiService.getApiInterface(), accessToken);
                }
            }

            @Override
            public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
                // Manejar el fallo en la llamada a la API
            }
        });
    }
}
