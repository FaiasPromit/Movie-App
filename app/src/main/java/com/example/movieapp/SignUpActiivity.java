package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActiivity extends AppCompatActivity {
    EditText name, password, confirmpass, username, mail;
    RadioButton fgender, mgender;
    Button register_button;
    FirebaseAuth mAuth;
    DatabaseReference databaseuser;

    String u_name, u_password, u_username, u_conpassword, u_mail, u_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init_view();
        mAuth = FirebaseAuth.getInstance();
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean res = validate();
                if (res) {
                    signupuser();
                }
            }
        });
    }

    void init_view() {
        name = findViewById(R.id.edit_text_name);
        password = findViewById(R.id.edit_text_password_signup);
        confirmpass = findViewById(R.id.edit_text_confirm_password_signup);
        username = findViewById(R.id.edit_text_user_name);
        fgender = findViewById(R.id.radio_gender_female);
        mgender = findViewById(R.id.radio_gender_male);
        mail = findViewById(R.id.edit_text_email_signup);
        register_button = findViewById(R.id.button_register_signup);
    }

    boolean validate() {
        u_name = name.getText().toString();
        u_password = password.getText().toString();
        u_conpassword = confirmpass.getText().toString();
        u_username = username.getText().toString();
        u_mail = mail.getText().toString();
        u_gender = fgender.isSelected() ? "Female" : "Male";
        if (u_name.isEmpty()) {
            name.setError("Name Cannot be empty");
            name.requestFocus();
            return false;
        }
        if (u_password.isEmpty()) {
            password.setError("Name Cannot be empty");
            password.requestFocus();
            return false;
        }
        if (u_conpassword.isEmpty()) {
            confirmpass.setError("Name Cannot be empty");
            confirmpass.requestFocus();
            return false;
        }
        if (u_username.isEmpty()) {
            username.setError("Name Cannot be empty");
            username.requestFocus();
            return false;
        }
        if (u_mail.isEmpty()) {
            mail.setError("Name Cannot be empty");
            mail.requestFocus();
            return false;
        }
        if (!u_password.equals(u_conpassword)) {
            confirmpass.setError("Password didn't matched");
            confirmpass.requestFocus();
            return false;
        }
        return true;
    }

    void signupuser() {
        mAuth.createUserWithEmailAndPassword(u_mail, u_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        String uid = user.getUid();
                        databaseuser = FirebaseDatabase.getInstance().getReference("users");
                        user_class x = new user_class(u_name, u_mail, u_username, u_gender, uid);
                        try {
                            databaseuser.child(uid).setValue(x);
                            finish();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification();
                            Toast.makeText(SignUpActiivity.this, "Verification Mail is sent. Please Verify Your Mail", Toast.LENGTH_SHORT).show();
                            Log.d("CAME", "uploaded_data_user");
                        } catch (Exception e) {
                            Log.e("OKNOT", e.getMessage());
                            toaster(e.getMessage());
                        }
                    } else {
                        toaster("User is empty\n");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void toaster(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}
