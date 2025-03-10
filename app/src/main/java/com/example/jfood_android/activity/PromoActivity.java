package com.example.jfood_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jfood_android.R;
import com.example.jfood_android.adapter.PromoListAdapter;
import com.example.jfood_android.model.Promo;
import com.example.jfood_android.request.PromoAllRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PromoActivity extends AppCompatActivity {
    private String currentUserId;
    private String currentUserEmail;

    ArrayList<Promo> promoList = new ArrayList<>();

    PromoListAdapter promoListAdapter;
    SharedPreferences pref;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);
        progressDialog = new ProgressDialog(this);

        final RecyclerView rvPromoList = findViewById(R.id.rvPromoList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvPromoList.setLayoutManager(layoutManager);
        progressDialog.setMessage("Retrieving Promo...");

        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        if(pref.contains("currentUserId") || pref.contains("email")){
            currentUserId = pref.getString("currentUserId", "");
            currentUserEmail = pref.getString("email", "");
        }

        promoListAdapter = new PromoListAdapter(this, promoList);
        rvPromoList.setAdapter(promoListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (promoList != null){
            promoList.clear();
        }
        progressDialog.show();

        getPromoList();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    // mengambil promo yang ada dari server
    private void getPromoList() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray != null){
                        // Get Promo
                        //Toast.makeText(PromoActivity.this, ("Test length: " + jsonArray.length()), Toast.LENGTH_SHORT).show();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Gson gsonPromo = new Gson();
                            Promo gPromo = gsonPromo.fromJson(jsonObject.toString(), Promo.class);

                            System.out.println(gPromo);
                            promoList.add(gPromo);
                        }
                        promoListAdapter.setPromo(promoList);
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(PromoActivity.this, "Fetch JSON empty", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e){
                    Toast.makeText(PromoActivity.this, "Failed to Get the foods Order", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                    Intent intent = new Intent(PromoActivity.this, MainActivity.class);
                    intent.putExtra("currentUserId", currentUserId);
                    startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(PromoActivity.this).toBundle());
                }
            }
        };

        PromoAllRequest promoFetchRequest = new PromoAllRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(PromoActivity.this);
        queue.add(promoFetchRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_promo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Intent intent = new Intent(PromoActivity.this, CartActivity.class);
                intent.putExtra("currentUserId", currentUserId);
                startActivity(intent);
                return true;

            case R.id.action_history:
                Intent historyIntent = new Intent(PromoActivity.this, HistoryActivity.class);
                historyIntent.putExtra("currentUserId", currentUserId);
                startActivity(historyIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
