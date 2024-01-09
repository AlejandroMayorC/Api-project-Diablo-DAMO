package com.example.apiprojectdiablodamo.ui;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apiprojectdiablodamo.API.AccessTokenResponse;
import com.example.apiprojectdiablodamo.API.ApiInterface;
import com.example.apiprojectdiablodamo.API.ApiService;
import com.example.apiprojectdiablodamo.API.Item;
import com.example.apiprojectdiablodamo.API.ItemAdapter;
import com.example.apiprojectdiablodamo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GemsFragment extends Fragment {
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<Item> listaItems = new ArrayList<>();
    private List<Item> listaItemsOriginal = new ArrayList<>();
    private Spinner spinnerOptions;
    private SearchView searchViewItems;
    private String categoriaActual = "Tots"; // Por defecto
    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gems, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewGems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItemAdapter(listaItems, getContext());
        recyclerView.setAdapter(adapter);
        spinnerOptions = view.findViewById(R.id.spinnerOptionsGems);
        searchViewItems = view.findViewById(R.id.searchViewGems);
        // Configurado el Spinner con las opciones
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOptions.setAdapter(spinnerAdapter);
        spinnerOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!listaItemsOriginal.isEmpty()) {
                    categoriaActual = parent.getItemAtPosition(position).toString();
                    filtrarPorCategoria(categoriaActual);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Opcionalmente, manejar el caso en que no se selecciona nada
            }
        });
        // Configurado el SearchView
        searchViewItems.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarItems(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    // Si el texto de búsqueda está vacío, muestra la lista completa de la categoría
                    filtrarPorCategoria(categoriaActual);
                } else {
                    buscarItems(newText);
                }
                return false;
            }
        });
        cargarItems();
        return view;
    }

    private void filtrarPorCategoria(String categoria) {
        List<Item> listaFiltrada = new ArrayList<>();
        if (categoria.equals("Tots")) {
            listaFiltrada.addAll(listaItemsOriginal);
        } else {
            for (Item item : listaItemsOriginal) {
                if (categoriaCorresponde(item, categoria)) {
                    listaFiltrada.add(item);
                }
            }
        }
        adapter.actualizarListaItems(listaFiltrada);
    }

    private boolean categoriaCorresponde(Item item, String categoria) {
        //cómo determinar si un ítem corresponde a una categoría
        switch (categoria) {
            case "Consumibles":
                return item.getSlug().contains("potion");
            case "Botes":
                return Arrays.asList("mystery-boots", "lut-socks", "the-crudest-boots", "rivera-dancers", "illusory-boots", "boots-of-disregard", "irontoe-mudsputters", "bryners-journey", "fire-walkers", "nilfurs-boast-P61_Unique_Boots_01").contains(item.getSlug());
            case "Pantalons":
                return Arrays.asList("cold-cathode-trousers", "unholy-plates", "marauders-encasement", "leg-guards-of-mystery", "arachyrs-legs", "sunwukos-leggings", "renewal-of-the-invoker", "swamp-land-waders", "defiler-cuisses", "deaths-bargain").contains(item.getSlug());
            case "Guants":
                return Arrays.asList("warlord-gauntlets", "frostburn", "tal-rashas-grasp", "manifers", "gladiator-gauntlets", "moribund-gauntlets", "st-archews-gage", "grasps-of-essence", "gloves-of-worship").contains(item.getSlug());
            case "Pitxeres":
                return Arrays.asList("brigandine-of-valor", "typhons-thorax", "mundunugus-robe", "helltooth-tunic", "arachyrs-carapace", "tragouls-scales", "rathmas-ribcage-plate", "pestilence-robe", "inariuss-conviction", "requiem-cereplate").contains(item.getSlug());
            case "Hombreres":
                return Arrays.asList("inariuss-martyrdom", "spaulders-of-zakara", "mantle-of-channeling", "lefebvres-soliloquy", "corpsewhisper-pauldrons", "razeths-volition", "pauldrons-of-the-skeleton-king", "leather-mantle", "star-pauldrons", "mystery-shoulders").contains(item.getSlug());
            case "Cascos":
                return Arrays.asList("mystery-helm", "helm-of-the-cranial-crustacean", "star-helm", "prides-fall", "broken-crown", "blind-faith", "deathseers-cowl", "warhelm-of-kassar", "visage-of-gunes", "mask-of-scarlet-death").contains(item.getSlug());
            case "Vares":
                return Arrays.asList("the-reapers-kiss", "staff-of-chiroptera", "long-staff", "yew-staff", "the-broken-staff", "war-staff", "the-smoldering-core", "suwong-diviner", "ahavarion-spear-of-lycander", "valtheks-rebuke").contains(item.getSlug());
            case "Dagues":
                return Arrays.asList("simple-dagger", "envious-blade", "pig-sticker", "the-horadric-hamburger", "wizardspike", "karleis-point", "eunjangdo", "lord-greenstones-fan").contains(item.getSlug());
            case "Destrals (1 mà)":
                return Arrays.asList("aidans-revenge", "genzaniku", "marauder-axe", "flesh-tearer", "hack", "the-butchers-sickle", "mordullus-promise", "the-burning-axe-of-sankis", "sky-splitter").contains(item.getSlug());
            case "Espases (1 mà)":
                return Arrays.asList("ghoul-kings-blade", "god-butcher", "quinquennial-sword", "amberwing", "monster-hunter", "wildwood", "rakanishus-blade", "the-ancient-bonesaber-of-zumakalis", "doombringer", "spectrum", "thunderfury-blessed-blade-of-the-windseeker").contains(item.getSlug());
            case "Espases (2 mans)":
                return item.getSlug().equals("the-zweihander") ||
                        item.getSlug().equals("corrupted-ashbringer") ||
                        item.getSlug().equals("scourge") ||
                        item.getSlug().equals("blackguard") ||
                        item.getSlug().equals("the-sultan-of-blinding-sand") ||
                        item.getSlug().equals("blade-of-prophecy") ||
                        item.getSlug().equals("maximus") ||
                        item.getSlug().equals("warmonger") ||
                        item.getSlug().equals("cams-rebuttal") ||
                        item.getSlug().equals("blood-brother");
            case "Destrals (2 mans)":
                return item.getSlug().equals("sungjaes-fury") ||
                        item.getSlug().equals("kanais-skorn") ||
                        item.getSlug().equals("burst-of-wrath") ||
                        item.getSlug().equals("messerschmidts-reaver") ||
                        item.getSlug().equals("skorn") ||
                        item.getSlug().equals("decapitator") ||
                        item.getSlug().equals("king-maker") ||
                        item.getSlug().equals("the-executioner") ||
                        item.getSlug().equals("parashu") ||
                        item.getSlug().equals("ripper-axe");
            case "Maces (2 mans)":
                return item.getSlug().equals("arthefs-spark-of-life") ||
                        item.getSlug().equals("crushbane") ||
                        item.getSlug().equals("soulsmasher") ||
                        item.getSlug().equals("skywarden") ||
                        item.getSlug().equals("wrath-of-the-bone-king") ||
                        item.getSlug().equals("the-furnace") ||
                        item.getSlug().equals("rock-breaker") ||
                        item.getSlug().equals("war-maul") ||
                        item.getSlug().equals("schaefers-hammer") ||
                        item.getSlug().equals("royal-mace");
        }
        return false;
    }


    private void buscarItems(String textoBusqueda) {
        List<Item> listaFiltrada = new ArrayList<>();
        List<Item> listaBusqueda = categoriaActual.equals("Tots") ? listaItemsOriginal : adapter.getListaItems();

        for (Item item : listaBusqueda) {
            if (item.getName().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                listaFiltrada.add(item);
            }
        }
        adapter.actualizarListaItems(listaFiltrada);
    }


    private void cargarItems() {
        if (!listaItemsOriginal.isEmpty()) {
            // La lista ya está cargada, no es necesario volver a cargarla
            return;
        }
        if (!listaItems.isEmpty()) {
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
                    obtenerItems(ApiService.getApiInterface(), accessToken);
                } else {
                    Log.e("API Error", "Error al obtener token: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
                Log.e("API Failure", "Error al obtener token", t);
            }
        });
    }

    private void obtenerItems(ApiInterface apiInterface, String accessToken) {
        String[] itemSlugs = {"iceblink-Unique_Gem_021_x1_PTR", "mirinae-teardrop-of-the-starweaver-Unique_Gem_007_x1_PTR", "simplicitys-strength-Unique_Gem_013_x1", "esoteric-alteration-Unique_Gem_016_x1_PTR", "red-soul-shard-Unique_Gem_022_x1_PTR", "molten-wildebeests-gizzard-Unique_Gem_017_x1_PTR", "bane-of-the-stricken-Unique_Gem_018_x1", "boon-of-the-hoarder-Unique_Gem_014_x1_PTR", "invigorating-gemstone-Unique_Gem_009_x1_PTR", "boyarskys-chip-Unique_Gem_020_x1", "bane-of-the-trapped-Unique_Gem_002_x1_PTR", "mutilation-guard-Unique_Gem_019_x1_PTR", "gem-of-efficacious-toxin-Unique_Gem_005_x1", "taeguk-Unique_Gem_015_x1_PTR", "wreath-of-lightning-Unique_Gem_004_x1", "whisper-of-atonement-P73_Unique_Gem_128", "legacy-of-dreams-Unique_Gem_023_x1_PTR", "bane-of-the-powerful-Unique_Gem_001_x1_PTR"};
        for (String slug : itemSlugs) {
            Runnable task = () -> {
                Call<Item> callItem = apiInterface.obtenerItem(slug, accessToken);
                callItem.enqueue(new Callback<Item>() {
                    @Override
                    public void onResponse(Call<Item> call, Response<Item> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (isAdded() && getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    listaItemsOriginal.add(response.body());
                                    listaItems.add(response.body());
                                    adapter.notifyDataSetChanged();
                                });
                            }
                        } else {
                            Log.e("API Error", "Código de error: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<Item> call, Throwable t) {
                        Log.d("API Failure", "Error al llamar a la API", t);
                    }
                });
            };
            executorService.submit(task);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        executorService.shutdownNow(); // Asegurarse de apagar el servicio al detener el fragmento
    }

}
