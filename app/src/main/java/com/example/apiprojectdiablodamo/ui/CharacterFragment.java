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
import android.widget.SearchView;
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
    private List<Personaje> listaPersonajesOriginal = new ArrayList<>();
    private List<Call> activeCalls = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewPersonajes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PersonajeAdapter(listaPersonajes);
        recyclerView.setAdapter(adapter);
        SearchView searchView = view.findViewById(R.id.searchView);

        adapter.setOnPersonajeClickListener(personaje -> {
            Context context = getActivity();
            if (context != null) {
                Intent intent = new Intent(context, DetallePersonajeActivity.class);
                intent.putExtra("JSON_PERSONAJE", new Gson().toJson(personaje));
                startActivity(intent);
            }
        });
        // Configurado el SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarPersonajes(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    // Si el texto de búsqueda está vacío, muestra la lista completa
                    adapter.actualizarListaPersonajes(listaPersonajesOriginal);
                } else {
                    buscarPersonajes(newText);
                }
                return false;
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarPersonajes(); // Recargar los datos cuando el Fragment se reanuda
    }

    private void buscarPersonajes(String textoBusqueda) {
        if (textoBusqueda != null && !textoBusqueda.isEmpty()) {
            List<Personaje> listaFiltrada = new ArrayList<>();
            for (Personaje personaje : listaPersonajesOriginal) {
                if (personaje.getName().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                    listaFiltrada.add(personaje);
                }
            }
            adapter.actualizarListaPersonajes(listaFiltrada);
        } else {
            adapter.actualizarListaPersonajes(listaPersonajesOriginal);
        }
    }

    private void cargarPersonajes() {
        if (!listaPersonajesOriginal.isEmpty()) {
            // La lista ya está cargada, no es necesario volver a cargarla
            return;
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
                        // Verificar que el Fragment está agregado y el Activity no es nulo
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                // Actualizar la UI aquí
                                listaPersonajesOriginal.add(response.body()); // Añadir a lista original
                                listaPersonajes.add(response.body()); // Añadir a lista usada por el adapter
                                adapter.notifyDataSetChanged();
                            });
                        }
                    } else {
                        Log.e("API Error", "Código de error: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Personaje> call, Throwable t) {
                    Log.d("API Failure", "Error al llamar a la API", t);
                }
            });
            // Añadir la llamada a una lista para poder cancelarla después si es necesario
            activeCalls.add(callPersonaje);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // Cancelar todas las llamadas activas
        for (Call call : activeCalls) {
            call.cancel();
        }
    }


}
