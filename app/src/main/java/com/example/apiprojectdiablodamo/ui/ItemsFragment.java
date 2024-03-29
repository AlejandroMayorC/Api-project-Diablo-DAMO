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
import com.example.apiprojectdiablodamo.API.Personaje;
import com.example.apiprojectdiablodamo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemsFragment extends Fragment {
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
        View view = inflater.inflate(R.layout.fragment_items, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItemAdapter(listaItems, getContext());
        recyclerView.setAdapter(adapter);
        spinnerOptions = view.findViewById(R.id.spinnerOptions);
        searchViewItems = view.findViewById(R.id.searchViewItems);
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
        //cargarItems(); lo llamamos en onResume()
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
        String[] itemSlugs = {"bottomless-potion-of-kulleaid-HealthPotionLegendary_06", "bottomless-potion-of-the-unfettered-HealthPotionLegendary_11", "bottomless-potion-of-amplification-HealthPotionLegendary_09", "bottomless-potion-of-mutilation-HealthPotionLegendary_05", "bottomless-potion-of-regeneration-HealthPotionLegendary_03", "bottomless-potion-of-chaos-HealthPotionLegendary_10", "bottomless-potion-of-rejuvenation-P2_HealthPotionLegendary_07", "bottomless-potion-of-fear-HealthPotionLegendary_08", "bottomless-potion-of-the-leech-HealthPotionLegendary_04", "bottomless-potion-of-the-diamond-HealthPotionLegendary_02", "bottomless-potion-of-the-tower-HealthPotionLegendary_01", "health-potion-HealthPotionBottomless", "nilfurs-boast-P61_Unique_Boots_01", "fire-walkers-Unique_Boots_007_p2", "bryners-journey-P6_Necro_Unique_Boots_22", "irontoe-mudsputters-Unique_Boots_104_x1", "boots-of-disregard-Unique_Boots_102_x1", "illusory-boots-Unique_Boots_103_x1", "rivera-dancers-P4_Unique_Boots_001", "the-crudest-boots-Unique_Boots_010_x1", "lut-socks-Unique_Boots_009_x1", "mystery-boots-PH_Boots", "cold-cathode-trousers-P69_Unique_Pants_Set_06", "unholy-plates-Unique_Pants_Set_03_p2", "marauders-encasement-Unique_Pants_Set_07_x1", "leg-guards-of-mystery-Unique_Pants_Set_02_p2", "arachyrs-legs-Unique_Pants_Set_02_p3", "sunwukos-leggings-Unique_Pants_Set_11_x1", "renewal-of-the-invoker-Unique_Pants_Set_12_x1","swamp-land-waders-P41_Unique_Pants_001", "defiler-cuisses-P7_Necro_Unique_Pants_22", "deaths-bargain-Unique_Pants_102_x1", "warlord-gauntlets-Gloves_203", "frostburn-P41_Unique_Gloves_002", "tal-rashas-grasp-P2_Unique_Gloves_02", "manifers-Gloves_202", "gladiator-gauntlets-Unique_Gloves_011_x1", "moribund-gauntlets-P6_Necro_Unique_Gloves_21", "st-archews-gage-Unique_Gloves_101_p2", "grasps-of-essence-P69_Necro_Unique_Gloves_22", "gloves-of-worship-Unique_Gloves_103_x1", "brigandine-of-valor-P67_Unique_Chest_Set_01", "typhons-thorax-P68_Unique_Chest_Set_03", "mundunugus-robe-P68_Unique_Chest_Set_04", "helltooth-tunic-Unique_Chest_Set_16_x1", "arachyrs-carapace-Unique_Chest_Set_02_p3", "tragouls-scales-P6_Necro_Set_2_Chest", "rathmas-ribcage-plate-P6_Necro_Set_1_Chest", "pestilence-robe-P6_Necro_Set_4_Chest", "inariuss-conviction-P6_Necro_Set_3_Chest", "requiem-cereplate-P6_Necro_Unique_Chest_22", "inariuss-martyrdom-P6_Necro_Set_3_Shoulders", "spaulders-of-zakara-Unique_Shoulder_102_x1", "mantle-of-channeling-P4_Unique_Shoulder_103", "lefebvres-soliloquy-P4_Unique_Shoulder_101", "corpsewhisper-pauldrons-P6_Necro_Unique_Shoulders_21", "razeths-volition-P69_Necro_Unique_Shoulders_22", "pauldrons-of-the-skeleton-king-Unique_Shoulder_103_x1", "leather-mantle-Shoulders_002", "star-pauldrons-TransmogShoulders_001", "mystery-shoulders-PH_Shoulders", "mask-of-scarlet-death-P6_Necro_Unique_Helm_21", "visage-of-gunes-P4_Unique_Helm_103", "warhelm-of-kassar-P4_Unique_Helm_102", "deathseers-cowl-Unique_Helm_102_x1", "blind-faith-Unique_Helm_007_x1", "broken-crown-P2_Unique_Helm_001", "prides-fall-Unique_Helm_103_x1", "star-helm-TransmogHelm_001", "helm-of-the-cranial-crustacean-TransmogHelm_002", "mystery-helm-PH_Helm", "valtheks-rebuke-P610_Unique_Staff_102", "ahavarion-spear-of-lycander-Unique_Staff_101_x1", "suwong-diviner-Unique_Staff_104_x1", "the-smoldering-core-Unique_Staff_103_x1", "war-staff-Staff_007", "the-broken-staff-Unique_Staff_001_x1", "yew-staff-Staff_003", "long-staff-Staff_002", "staff-of-chiroptera-P61_Unique_Staff_001", "the-reapers-kiss-TransmogStaff_241_001", "lord-greenstones-fan-P61_Unique_Dagger_102_x1", "eunjangdo-Unique_Dagger_104_x1", "karleis-point-P61_Unique_Dagger_101_x1", "wizardspike-P610_Unique_Dagger_010", "the-horadric-hamburger-Unique_Offhand_001_x1", "pig-sticker-Unique_Dagger_007_x1", "envious-blade-Unique_Dagger_103_x1", "simple-dagger-Dagger_001", "sky-splitter-Unique_Axe_1H_005_p2", "the-burning-axe-of-sankis-Unique_Axe_1H_007_x1", "mordullus-promise-P4_Unique_Axe_1H_102", "the-butchers-sickle-Unique_Axe_1H_006_x1", "hack-Unique_Axe_1H_103_x1", "flesh-tearer-Unique_Axe_1H_001_x1", "marauder-axe-Axe_1H_006", "genzaniku-Unique_Axe_1H_003_x1", "aidans-revenge-TransmogAxe_241_001", "thunderfury-blessed-blade-of-the-windseeker-Unique_Sword_1H_101_x1", "spectrum-Unique_Sword_1H_021_x1", "doombringer-Unique_Sword_1H_014_x1", "the-ancient-bonesaber-of-zumakalis-Unique_Sword_1H_003_x1", "rakanishus-blade-Unique_Sword_1H_010_104", "wildwood-Unique_Sword_1H_002_x1", "monster-hunter-Unique_Sword_1H_017_x1", "amberwing-TransmogSword_241_002", "quinquennial-sword-TransmogSword_241_004", "god-butcher-TransmogSword_241_001", "ghoul-kings-blade-TransmogSword_241_003", "royal-mace-Mace_2H_301", "schaefers-hammer-Unique_Mace_2H_009_p2", "war-maul-Mace_2H_204", "rock-breaker-Mace_2H_106", "the-furnace-Unique_Mace_2H_103_x1", "wrath-of-the-bone-king-Unique_Mace_2H_012_p1", "skywarden-Unique_Mace_2H_010_x1", "soulsmasher-Unique_Mace_2H_104_x1", "crushbane-Unique_Mace_2H_001_x1", "arthefs-spark-of-life-Unique_Mace_2H_003_x1", "ripper-axe-Axe_2H_203", "parashu-Axe_2H_104", "the-executioner-P66_Unique_Axe_2H_003", "king-maker-TransmogAxe_241_002", "decapitator-Axe_2H_205", "skorn-Unique_Axe_2H_009_x1", "messerschmidts-reaver-P66_Unique_Axe_2H_011", "burst-of-wrath-Unique_Axe_2H_103_x1", "kanais-skorn-TransmogAxe_241_003", "sungjaes-fury-TransmogAxe_241_004", "blood-brother-Unique_Sword_2H_103_x1", "cams-rebuttal-Unique_Sword_2H_102_x1", "warmonger-Unique_Sword_2H_003_x1", "maximus-Unique_Sword_2H_010_x1", "blade-of-prophecy-P61_Unique_Sword_2H_007_x1", "the-sultan-of-blinding-sand-Unique_Sword_2H_008_x1", "blackguard-Unique_Sword_2H_011_x1", "scourge-Unique_Sword_2H_004_x1", "corrupted-ashbringer-Unique_Sword_2H_104_x1", "the-zweihander-Unique_Sword_2H_002_x1"};

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

    @Override
    public void onResume() {
        super.onResume();
        cargarItems(); // Recargar los datos cuando el Fragment se reanuda

        // Restablecer la lista completa en el adapter
        if (listaItemsOriginal != null) {
            listaItems.clear();
            listaItems.addAll(listaItemsOriginal);
            adapter.actualizarListaItems(listaItemsOriginal);
        }
        // Restablecer el filtro de categoría y búsqueda
        filtrarPorCategoria(categoriaActual);
        if (searchViewItems != null) {
            CharSequence query = searchViewItems.getQuery();
            if (query != null && !query.toString().isEmpty()) {
                buscarItems(query.toString());
            }
        }
    }


}
