package com.example.apiprojectdiablodamo.API;

import java.util.ArrayList;
import java.util.List;

public class PersonajeManager {
    private static final PersonajeManager instance = new PersonajeManager();
    private List<Personaje> personajes = new ArrayList<>();

    public static PersonajeManager getInstance() {
        return instance;
    }

    public List<Personaje> getPersonajes() {
        return personajes;
    }

    public void setPersonajes(List<Personaje> personajes) {
        this.personajes = personajes;
    }
}

