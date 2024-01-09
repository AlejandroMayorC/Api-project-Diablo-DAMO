package com.example.apiprojectdiablodamo.API;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class PersonajeDiffCallback extends DiffUtil.Callback {

    private final List<Personaje> oldList;
    private final List<Personaje> newList;

    public PersonajeDiffCallback(List<Personaje> oldList, List<Personaje> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        // Considera que dos elementos son los mismos si tienen el mismo slug
        return oldList.get(oldItemPosition).getSlug().equals(newList.get(newItemPosition).getSlug());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // Comprueba si el contenido de los elementos es el mismo
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
