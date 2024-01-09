package com.example.apiprojectdiablodamo.API;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class ItemDiffCallback extends DiffUtil.Callback {

    private final List<Item> oldList;
    private final List<Item> newList;

    public ItemDiffCallback(List<Item> oldList, List<Item> newList) {
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
        // Compara los nombres de tus ítems aquí
        return oldList.get(oldItemPosition).getName().equals(newList.get(newItemPosition).getName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        // Compara si los contenidos (nombres) de tus ítems son los mismos
        return oldList.get(oldItemPosition).getName().equals(newList.get(newItemPosition).getName());
    }
}
