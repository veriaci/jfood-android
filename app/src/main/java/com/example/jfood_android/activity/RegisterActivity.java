package com.example.jfood_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jfood_android.R;
import com.example.jfood_android.request.RegisterRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout etName;
    private TextInputLayout etEmail;
    private TextInputLayout etPassword;
    private Button btnRegister;
    private TextView tvLogin;
    ProgressDialog progressDialog;

    String name = "";
    String email = "";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering Account...");

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            progressDialog.show();
            name = etName.getEditText().getText().toString();
            if(checkEmail() | checkPassword()){
                email = etEmail.getEditText().getText().toString();
                password = etPassword.getEditText().getText().toString();
            }

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject != null){
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e){
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            RegisterRequest registerRequest = new RegisterRequest(name, email, password, responseListener);
            RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
            queue.add(registerRequest);
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(RegisterActivity.this).toBundle());
            }
        });
    }

    private boolean checkEmail(){
        String emailInput = etEmail.getEditText().getText().toString().trim();
        Pattern p = Pattern.compile("^[\\w+&*~-]+(?:\\.[\\w+&*~-]+)*@(?!-)(?:[\\w-]+\\.)[a-zA-Z]{2,7}$");
        Matcher m = p.matcher(emailInput);

        if (emailInput.isEmpty()){
            etEmail.setError("Field can't be empty!");
            return false;
        } else if (!m.matches()){
            etEmail.setError("Please use an appropriate email!");
            return false;
        } else {
            etEmail.setError(null);
            return true;
        }
    }

    private boolean checkPassword() {
        String passwordInput = etPassword.getEditText().getText().toString().trim();
        Pattern p = Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}");
        Matcher m = p.matcher(passwordInput);

        if (passwordInput.isEmpty()){
            etPassword.setError("Field can't be empty!");
            return false;
        } else if (!m.matches()){
            etPassword.setError("Password not strong enough, use Upper Case, Lower Case, and a number!");
            return false;
        } else {
            etPassword.setError(null);
            return true;
        }
    }

    public void confirmInput(View view){
        Toast.makeText(RegisterActivity.this, "Validation success!?", Toast.LENGTH_SHORT).show();
    }
}
