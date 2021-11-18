package com.example.movieapp.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;

public class movie_cell_holder extends RecyclerView.ViewHolder {
    ImageView img;
    TextView title;
    CardView mcv;
    public movie_cell_holder(@NonNull View itemView) {
        super(itemView);
        mcv = itemView.findViewById(R.id.item_card);
        img = itemView.findViewById(R.id.item_image);
        title = itemView.findViewById(R.id.item_name);
    }
}
