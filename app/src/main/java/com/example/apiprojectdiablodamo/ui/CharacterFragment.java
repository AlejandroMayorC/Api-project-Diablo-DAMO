package com.example.apiprojectdiablodamo.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apiprojectdiablodamo.API.AccessTokenResponse;
import com.example.apiprojectdiablodamo.API.ApiInterface;
import com.example.apiprojectdiablodamo.API.ApiService;
import com.example.apiprojectdiablodamo.API.DetallePersonajeActivity;
import com.example.apiprojectdiablodamo.API.Personaje;
import com.example.apiprojectdiablodamo.API.PersonajeAdapter;
import com.example.apiprojectdiablodamo.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class CharacterFragment extends Fragment {
    private RecyclerView recyclerView;
    private PersonajeAdapter adapter;
    private List<Personaje> listaPersonajes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewPersonajes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PersonajeAdapter(listaPersonajes);
        recyclerView.setAdapter(adapter);

        adapter.setOnPersonajeClickListener(personaje -> {
            Context context = getActivity();
            if (context != null) {
                Intent intent = new Intent(context, DetallePersonajeActivity.class);
                intent.putExtra("JSON_PERSONAJE", new Gson().toJson(personaje));
                startActivity(intent);
            }
        });
        cargarPersonajes();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //cargarPersonajes(); // Recargar los datos cuando el Fragment se reanuda
    }
    private void cargarPersonajes() {
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
                        // Asegúrate de actualizar la UI en el hilo principal
                        getActivity().runOnUiThread(() -> {
                            listaPersonajes.add(response.body());
                            adapter.notifyDataSetChanged();
                            Log.d("API Success", "Personaje añadido: " + response.body().toString());
                        });
                    } else {
                        Log.d("API Error", "Respuesta no exitosa");
                    }
                }

                @Override
                public void onFailure(Call<Personaje> call, Throwable t) {
                    Log.d("API Failure", "Error al llamar a la API", t);
                }
            });
        }
    }
}
