package com.example.apiprojectdiablodamo.ui;

import com.example.apiprojectdiablodamo.API.Personaje;

import java.util.ArrayList;
import java.util.List;

public class PreferitsListManager {
    private static final PreferitsListManager ourInstance = new PreferitsListManager();
    private List<Object> llistaPreferits;

    public static PreferitsListManager getInstance() {
        return ourInstance;
    }

    private PreferitsListManager() {
        llistaPreferits = new ArrayList<>();
    }

    public List<Object> getLlistaPreferits() {
        return llistaPreferits;
    }

    public void afegirPreferit(Object preferit) {
        llistaPreferits.add(preferit);
    }

    public void eliminarPreferit(Object preferit) {
        llistaPreferits.remove(preferit);
    }

    public boolean esPreferit(String name) {
        for (Object preferit : llistaPreferits) {
            if (preferit instanceof Personaje) {
                Personaje personaje = (Personaje) preferit;
                if (personaje.getName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Altres mètodes per gestionar la llista de preferits...
}

