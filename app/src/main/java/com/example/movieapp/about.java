package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class about extends AppCompatActivity {
    TextView name,email,phone;
    String  json;
    String u_name,u_email,u_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        name = findViewById(R.id.text_view_name_about);
        email = findViewById(R.id.text_view_email_about);
        phone = findViewById(R.id.text_view_phone_about);
        GETDATA getdata = new GETDATA();
        getdata.execute();

    }
    void set_view(){
        process_JSON();
        name.setText(u_name);
        email.setText(u_email);
        phone.setText(u_phone);
    }
    void process_JSON(){
        String maindata = json;
        try{
            JSONObject jsonObject = new JSONObject(maindata);
            u_email = jsonObject.getString("mail");
            u_phone = jsonObject.getString("phone");
            u_name = jsonObject.getString("name");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private class GETDATA extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String url = "https://api.myjson.com/bins/pst5c";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(about.this);
            progressDialog.setMessage("Updating..");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            String jsnStr = sh.makeServiceCall(url);
            try {
                Log.e("JSON", jsnStr);
            } catch (Exception e) {
                Log.e("JSONERROR", e.getMessage());
            }
            if (jsnStr != null) {
                json = jsnStr;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            set_view();
        }
    }
}

