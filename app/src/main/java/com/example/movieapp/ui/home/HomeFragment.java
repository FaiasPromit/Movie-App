package com.example.movieapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieapp.R;
import com.example.movieapp.adapter.AdapterMovie;
import com.example.movieapp.movie_class;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ArrayList<movie_class> movieArray;
    AdapterMovie adp;
    LinearLayoutManager llm1;
    RecyclerView rv;
    FirebaseDatabase database;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        rv = root.findViewById(R.id.recycler_view_all_movie);
        database = FirebaseDatabase.getInstance();
        movieArray = new ArrayList<>();
        downloadMovieData();
        return root;
    }
    private void toaster(String s) {
        Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
    void downloadMovieData()
    {
        DatabaseReference mdatabase = database.getReference("movie");
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                movieArray.clear();
                for(DataSnapshot x: dataSnapshot.getChildren()){
                    movie_class tmp = x.getValue(movie_class.class);
                    if(tmp!= null){
                        movieArray.add(tmp);
                    }
                }
                toaster(movieArray.size()+"");
                adp  = new AdapterMovie(getActivity().getApplicationContext(),movieArray);
                llm1 = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
                rv.setAdapter(adp);
                rv.setLayoutManager(llm1);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                toaster(databaseError.getMessage());
            }
        });
    }
}