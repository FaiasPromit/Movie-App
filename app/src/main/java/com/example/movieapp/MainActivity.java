package com.example.movieapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.movieapp.adapter.AdapterMovie;
import com.example.movieapp.ui.slideshow.SlideshowFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView user_mail, user_name;
    FirebaseAuth auth;
    FirebaseDatabase database;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    NavController navController;
    user_class usernow;
    View headerView;
    private AppBarConfiguration mAppBarConfiguration;
    ArrayList<movie_class> movieArray;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        user_mail = headerView.findViewById(R.id.text_view_nav_user_mail);
        user_name = headerView.findViewById(R.id.text_view_nav_user_name);
        movieArray = new ArrayList<>();
        getdatauser();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            Fragment fragment = null;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch ((menuItem.getItemId())) {
                    case R.id.nav_home:
                        Toast.makeText(getApplicationContext(), "Home selected", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_gallery:
                        Intent i = new Intent(getApplicationContext(), ActivityAddMovie.class);
                        startActivity(i);
                        break;
                    case R.id.nav_send:
                        Toast.makeText(getApplicationContext(), "Send selected", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_slideshow:
                        Intent ix = new Intent(getApplicationContext(), WatchList.class);
                        startActivity(ix);
                        break;
                    case R.id.nav_share:
                        auth.signOut();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        finish();
                        startActivity(intent);
                        break;
                    case R.id.nav_tools:
                        Intent intentx = new Intent(getApplicationContext(), about.class);
                        finish();
                        startActivity(intentx);
                        break;

                }
                drawer.closeDrawers();
                return true;
            }
        });
    }

    void init() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        user_name = headerView.findViewById(R.id.text_view_nav_user_name);
        user_mail = headerView.findViewById(R.id.text_view_nav_user_mail);
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
                    usernow = new user_class(dataSnapshot.child("name").getValue().toString(), dataSnapshot.child("email").getValue().toString(), dataSnapshot.child("username").getValue().toString(), dataSnapshot.child("gender").getValue().toString(), uid);
                    if (user_mail != null)
                        user_mail.setText(usernow.email);
                    if (user_name != null)
                        user_name.setText(usernow.name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                toaster(databaseError.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}

