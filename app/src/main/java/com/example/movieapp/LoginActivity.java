package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText mail, password;
    Button signinbutton, registerbutton;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    FirebaseAuth auth;

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            finish();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                , MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        auth = FirebaseAuth.getInstance();
        mail = findViewById(R.id.edit_text_signin_mail);
        password = findViewById(R.id.edit_text_signin_pass);
        signinbutton = findViewById(R.id.button_signin_action);
        registerbutton = findViewById(R.id.button_signup_go);
        signinbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userlogin();
            }
        });
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActiivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(LoginActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    private void userlogin() {
        String email, pass;
        email = mail.getText().toString();
        pass = password.getText().toString();
        if (email.isEmpty()) {
            mail.setError("Email Shouldn't be Empty");
            mail.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            password.setError("Email Shouldn't be Empty");
            password.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mail.setError("Invalid Email Address");
            mail.requestFocus();
            return;
        }
        if (password.length() < 6) {
            password.setError("Invalid Password");
            password.requestFocus();
            return;
        }
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    finish();
                    Toast.makeText(getApplicationContext(), "User Login Successfull", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });

    }
}
