package com.example.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.MovieItemDetails;
import com.example.movieapp.R;
import com.example.movieapp.movie_class;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterMovie extends RecyclerView.Adapter<movie_cell_holder> {
    Context ctx;
    static movie_class mc;
    ArrayList<movie_class>datanow;
    public AdapterMovie(Context cntxt,ArrayList<movie_class>tmp){
        ctx = cntxt;
        datanow = tmp;
    }

    @NonNull
    @Override
    public movie_cell_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflat = LayoutInflater.from(ctx).inflate(R.layout.moviecell,parent,false);
        return  new movie_cell_holder(inflat);
    }

    @Override
    public void onBindViewHolder(@NonNull movie_cell_holder holder, final int position) {
        Picasso.get().load(datanow.get(position).image_url).into(holder.img);
        holder.title.setText(datanow.get(position).title);
        holder.mcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ctx, MovieItemDetails.class);
                mc = datanow.get(position);
                i.putExtra("MCID",mc.uid);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datanow.size();
    }
}
