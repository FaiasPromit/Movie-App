package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.movieapp.adapter.AdapterMovie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WatchList extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<movie_class> movieArray;
    user_class usernow;
    LinearLayoutManager llm1;
    RecyclerView rv;
    AdapterMovie adp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        movieArray = new ArrayList<>();
        usernow = new user_class();
        rv = findViewById(R.id.recycler_view_all_watchlist);
        loadUser();
    }
    private void toaster(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
    void loadUser() {
        final String uid = auth.getCurrentUser().getUid().toString();
        database.getReference("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    usernow = new user_class(dataSnapshot.child("name").getValue().toString(),
                            dataSnapshot.child("email").getValue().toString(),
                            dataSnapshot.child("username").getValue().toString(),
                            dataSnapshot.child("gender").getValue().toString(), uid);
                    usernow.watchlist = new ArrayList<>();
                    if (dataSnapshot.hasChild("watchList")) {
                        for (DataSnapshot ss : dataSnapshot.child("watchList").getChildren()) {
                            usernow.watchlist.add(ss.getValue(String.class));

                        }
                    }
                    downloadMovieData();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                toaster(databaseError.getMessage());
            }
        });
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
                    if(tmp!= null && usernow.watchlist.contains(tmp.uid)){
                        movieArray.add(tmp);
                    }
                }
                toaster(movieArray.size()+"");
                adp  = new AdapterMovie(getApplicationContext(),movieArray);
                llm1 = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
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
