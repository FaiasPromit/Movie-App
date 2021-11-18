package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieItemDetails extends AppCompatActivity {
    Button addcomment;
    movie_class mcnow;
    FirebaseAuth auth;
    user_class usernow;
    FirebaseDatabase database;
    ImageView coverpic;
    Button watchlist;
    TextView title, description;
    EditText comment;
    ListView llv;
    ArrayAdapter adapter;
    ArrayList<String> allcomment;
    boolean added;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_item_details);
        auth = FirebaseAuth.getInstance();
        allcomment = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        comment = findViewById(R.id.edit_text_comment_add);
        title = findViewById(R.id.textView_title);
        description = findViewById(R.id.textView_description);
        watchlist = findViewById(R.id.button_add_watchlist);
        coverpic = findViewById(R.id.image_view_movie_pic_details);
        addcomment = findViewById(R.id.button_add_comment);
        llv = findViewById(R.id.list_view_comments);
        mcnow = new movie_class();
        usernow = new user_class();
        getCurrentProductClass();
        getdatauser();
        addcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = comment.getText().toString();
                if (text.isEmpty() || text.length() < 5) {
                    comment.setError("Very Small Comment");
                    comment.requestFocus();
                    return;
                }
                if(mcnow==null || mcnow.uid ==null){
                    return;
                }
                String id = database.getReference("comment").push().getKey();
                comment_class cs = new comment_class(text, mcnow.uid, usernow.uid, id, true);
                database.getReference("comment").child(id).setValue(cs);
                comment.setText("");
            }
        });

        watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (added == true) {
                    added = false;
                    watchlist.setText("Add to WatchList");
                    usernow.watchlist.remove(mcnow.uid);
                    database.getReference("users").child(usernow.uid).child("watchList").setValue(usernow.watchlist);
                } else {
                    added = true;
                    watchlist.setText("Watchlisted");
                    if (!usernow.watchlist.contains(mcnow.uid)) {
                        usernow.watchlist.add(mcnow.uid);
                        database.getReference("users").child(usernow.uid).child("watchList").setValue(usernow.watchlist);
                    }
                }
            }
        });

    }

    void getComment() {
        database.getReference("comment").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    allcomment.clear();
                    for (DataSnapshot ss : dataSnapshot.getChildren()) {
                        comment_class tmp = ss.getValue(comment_class.class);
                        assert tmp != null;
                        if ( tmp.movie_id!=null && tmp.movie_id.equals(mcnow.uid)) {
                            allcomment.add(tmp.body);
                        }
                    }
                }
                if (addedToWatchList()) {
                    added = true;
                    watchlist.setText("WatchListed");
                } else {
                    added = false;
                    watchlist.setText("Add To Watchlist");
                }
                description.setText(mcnow.description);
                title.setText(mcnow.title);
                Picasso.get().load(mcnow.image_url).into(coverpic);
                llv = findViewById(R.id.list_view_comments);
                toaster(allcomment.size()+"");
                adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.comment_view,allcomment);
                llv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                toaster(databaseError.getMessage());
            }
        });
    }

    boolean addedToWatchList() {
        for (int i = 0; i < usernow.watchlist.size(); i++) {
            if (usernow.watchlist.get(i).equals(mcnow.uid)) return true;
        }
        return false;
    }

    void getCurrentProductClass() {
        Intent intent = getIntent();
        final String mcid = intent.getExtras().getString("MCID");
        database.getReference("movie").child(mcid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mcnow = new movie_class(
                            dataSnapshot.child("image_url").getValue().toString(),
                            dataSnapshot.child("userid").getValue().toString(),
                            dataSnapshot.child("description").getValue().toString(),
                            dataSnapshot.child("title").getValue().toString(),
                            mcid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                toaster(databaseError.getMessage());
            }
        });
    }

    private void toaster(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    void getdatauser() {
        final String uid = auth.getCurrentUser().getUid().toString();
        database.getReference("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    usernow = new user_class(dataSnapshot.child("name").getValue().toString(),
                            dataSnapshot.child("email").getValue().toString(),
                            dataSnapshot.child("username").getValue().toString(),
                            dataSnapshot.child("gender").getValue().toString(),
                            uid);
                    usernow.watchlist = new ArrayList<>();
                    if (dataSnapshot.hasChild("watchList")) {
                        for (DataSnapshot ss : dataSnapshot.child("watchList").getChildren()) {
                            usernow.watchlist.add(ss.getValue(String.class));
                        }
                    }
                }
                getComment();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                toaster(databaseError.getMessage());
            }
        });
    }
}
