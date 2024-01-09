package com.example.apiprojectdiablodamo.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apiprojectdiablodamo.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private String[] data;

    public MyAdapter(String[] data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myadapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewItem.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItem;

        MyViewHolder(View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(R.id.textViewItem);
        }
    }
}

