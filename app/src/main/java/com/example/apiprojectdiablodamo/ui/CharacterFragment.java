package com.example.apiprojectdiablodamo.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apiprojectdiablodamo.API.AccessTokenResponse;
import com.example.apiprojectdiablodamo.API.ApiInterface;
import com.example.apiprojectdiablodamo.API.ApiService;
import com.example.apiprojectdiablodamo.API.Personaje;
import com.example.apiprojectdiablodamo.API.PersonajeAdapter;
import com.example.apiprojectdiablodamo.API.PersonajeManager;
import com.example.apiprojectdiablodamo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class CharacterFragment extends Fragment {
    private RecyclerView recyclerView;
    private PersonajeAdapter adapter;
    private List<Personaje> listaPersonajes = new ArrayList<>();
    private List<Personaje> listaPersonajesOriginal = new ArrayList<>();
    private List<Call> activeCalls = new ArrayList<>();
    private SearchView searchView;
    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character, container, false);

        listaPersonajes = PersonajeManager.getInstance().getPersonajes();
        listaPersonajesOriginal = new ArrayList<>(PersonajeManager.getInstance().getPersonajes());
        recyclerView = view.findViewById(R.id.recyclerViewPersonajes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PersonajeAdapter(listaPersonajes, getContext());
        recyclerView.setAdapter(adapter);
        searchView = view.findViewById(R.id.searchView);

        adapter.setOnPersonajeClickListener(personaje -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            DetallePersonajeFragment detallePersonajeFragment = DetallePersonajeFragment.newInstance(personaje);

            transaction.replace(R.id.fragment_container, detallePersonajeFragment);
            transaction.addToBackStack(null); // Permite que el botón de atrás revierta la transacción
            transaction.commit();
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
        // Restablece la lista completa en el adapter
        if (listaPersonajesOriginal != null) {
            listaPersonajes.clear();
            listaPersonajes.addAll(listaPersonajesOriginal);
            adapter.actualizarListaPersonajes(listaPersonajesOriginal);
        }
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
        if (!listaPersonajes.isEmpty()) {
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
            Runnable task = () -> {
                Call<Personaje> callPersonaje = apiInterface.obtenerPersonaje(slug, accessToken);
                callPersonaje.enqueue(new Callback<Personaje>() {
                    @Override
                    public void onResponse(Call<Personaje> call, Response<Personaje> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (isAdded() && getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    listaPersonajesOriginal.add(response.body());
                                    listaPersonajes.add(response.body());
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
                activeCalls.add(callPersonaje);
            };
            executorService.submit(task);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //executorService.shutdownNow(); // Asegurarse de apagar el servicio al detener el fragmento
        for (Call call : activeCalls) {
            call.cancel();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // Restablece el texto del SearchView a vacío
        if (searchView != null) {
            searchView.setQuery("", false);
            searchView.clearFocus();
        }

        // Restablecer la lista a su estado original
        adapter.actualizarListaPersonajes(listaPersonajesOriginal);
    }
}
