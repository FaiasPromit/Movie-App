package com.example.movieapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ActivityAddMovie extends AppCompatActivity {
    ImageView movieimage;
    EditText name, despcription;
    FirebaseAuth auth;
    FirebaseDatabase database;
    Button addmovie;
    user_class usernow;
    String image_url,u_id,m_description,m_name,m_id;
    private static final int REQ_CODE = 1;
    Uri imageUri;
    int imagenow;
    Bitmap bitmap;
    boolean f1;
    boolean image_indicator;
    int imgwid;
    Display screen;
    movie_class mclass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        init();
        addmovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_movie();
            }
        });
        movieimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = null;
                imageUri = null;
                f1 = true;
                image_indicator = true;
                imagenow = R.id.image_view_movie_pic;
                showImageChooser();
            }
        });
    }

    void init() {
        f1 = false;
        screen = getWindowManager().getDefaultDisplay();
        image_indicator = false;
        imgwid = screen.getWidth();
        movieimage = findViewById(R.id.image_view_movie_pic);
        name = findViewById(R.id.edit_text_movie_name);
        despcription = findViewById(R.id.edit_text_movie_description);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        addmovie = findViewById(R.id.button_add_movie);
        usernow = new user_class();
        image_url= "";
        download_user();

    }

    void download_user() {
        final String uid = auth.getCurrentUser().getUid();
        database.getReference("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    usernow = new user_class(dataSnapshot.child("name").getValue().toString(), dataSnapshot.child("email").getValue().toString(), dataSnapshot.child("username").getValue().toString(), dataSnapshot.child("gender").getValue().toString(), uid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    void upload_movie() {
        mclass = new movie_class();
        if(usernow==null)download_user();
        u_id = usernow.uid;
        m_description = despcription.getText().toString();
        m_name = name.getText().toString();
        if(m_name.isEmpty()){
            name.setError("Name Shouldn't be Empty");
            name.requestFocus();
            return;
        }
        if(m_description.isEmpty()){
            despcription.setError("Email Shouldn't be Empty");
            despcription.requestFocus();
            return;
        }
        if(image_url.isEmpty()){
            toaster("give a image");
            movieimage.requestFocus();
            return;
        }
        try{
            mclass.description = m_description;
            mclass.title = m_name;
            mclass.image_url = image_url;
            mclass.userid=usernow.uid;
            String id = database.getReference("movie").push().getKey();
            mclass.uid = id;
            database.getReference("movie").child(id).setValue(mclass);
        }catch (Exception e){
            toaster(e.getMessage());
        }

    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void showImageChooser() {
        Intent intt = new Intent();
        intt.setType("image/*");
        intt.setAction(Intent.ACTION_GET_CONTENT);
        try {
            startActivityForResult(Intent.createChooser(intt, "Select Picture"), REQ_CODE);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void toaster(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int req_code, int res_code, Intent data) {
        super.onActivityResult(req_code, res_code, data);
        if (req_code == REQ_CODE && res_code == RESULT_OK && null != data) {
            Uri imageUrix = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUrix);
                Bitmap result = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false);
                imageUri = getImageUri(getApplicationContext(), result);
                ImageView imageView = findViewById(imagenow);
                imageView.setImageBitmap(bitmap);
                String loc = "moviePic";
                uploadImageInFirebaseStorage(loc);
            } catch (IOException e) {
                e.printStackTrace();
                toaster(e.getMessage());
            }
        }
    }

    private void uploadImageInFirebaseStorage(String loc) {
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference(loc + "/" + System.currentTimeMillis() + ".jpg");
        if (imageUri != null) {
            final StorageReference photoStorageReference = profileImageRef.child("moviePic/" + System.currentTimeMillis() + ".jpg");
            photoStorageReference.putFile(imageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return photoStorageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        image_url = downloadUri.toString();
                    } else {
                        Toast.makeText(getApplicationContext(), "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
