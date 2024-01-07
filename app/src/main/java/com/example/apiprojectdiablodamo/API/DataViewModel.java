package com.example.apiprojectdiablodamo.API;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataViewModel extends ViewModel {
    private final MutableLiveData<List<Personaje>> personajes = new MutableLiveData<>();
    private final MutableLiveData<List<Item>> items = new MutableLiveData<>();

    public LiveData<List<Personaje>> getPersonajes() {
        return personajes;
    }

    public LiveData<List<Item>> getItems() {
        return items;
    }

    public void cargarPersonajes(ApiInterface apiInterface, String accessToken) {
        String[] classSlugs = {"barbarian", "wizard", "demon-hunter", "crusader", "monk", "witch-doctor", "necromancer"};
        List<Personaje> personajesTemp = new ArrayList<>();

        for (String slug : classSlugs) {
            Call<Personaje> callPersonaje = apiInterface.obtenerPersonaje(slug, accessToken);
            callPersonaje.enqueue(new Callback<Personaje>() {
                @Override
                public void onResponse(Call<Personaje> call, Response<Personaje> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        personajesTemp.add(response.body());
                        personajes.setValue(personajesTemp);
                    }
                }

                @Override
                public void onFailure(Call<Personaje> call, Throwable t) {
                    // Manejar el fallo en la llamada a la API
                }
            });
        }
    }

    public void cargarItems(ApiInterface apiInterface, String accessToken) {
        String[] itemSlugs = {"corrupted-ashbringer-Unique_Sword_2H_104_x1", "cams-rebuttal-Unique_Sword_2H_102_x1", "blood-brother-Unique_Sword_2H_103_x1"};
        List<Item> itemsTemp = new ArrayList<>();

        for (String slug : itemSlugs) {
            Call<Item> callItem = apiInterface.obtenerItem(slug, accessToken);
            callItem.enqueue(new Callback<Item>() {
                @Override
                public void onResponse(Call<Item> call, Response<Item> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        itemsTemp.add(response.body());
                        items.setValue(itemsTemp);
                    }
                }

                @Override
                public void onFailure(Call<Item> call, Throwable t) {
                    // Manejar el fallo en la llamada a la API
                }
            });
        }
    }
}
