package com.example.jfood_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jfood_android.request.LoginRequest;
import com.example.jfood_android.R;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private String id;
    SharedPreferences pref;
    Intent mainIntent;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Toolbar myToolbar = findViewById(R.id.my_toolbar);
        //setSupportActionBar(myToolbar);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging In...");

        final TextInputLayout etEmail = findViewById(R.id.etEmail);
        final TextInputLayout etPassword = findViewById(R.id.etPassword);
        final Button btnLogin = findViewById(R.id.btnLogin);
        final TextView tvRegister = findViewById(R.id.tvRegister);

        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        if(pref.contains("email") && pref.contains("password")){
            etEmail.getEditText().setText(pref.getString("email", ""));
            etPassword.getEditText().setText(pref.getString("password", ""));
        }

        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
                public void onClick(View view){
            final String email = etEmail.getEditText().getText().toString();
            final String password = etPassword.getEditText().getText().toString();
            progressDialog.show();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null){
                                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                id = jsonObject.getString("id");
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("email", email);
                                editor.putString("password", password);
                                editor.putString("currentUserId", id);
                                editor.commit();
                                //mainIntent.putExtra("currentUserId", id);
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(mainIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                            }
                        } catch (JSONException e){
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(email, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);

                /*
                if(email.equals("test@test.com") && password.equals("test")){
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
                 */
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
            }
        });
    }
}


