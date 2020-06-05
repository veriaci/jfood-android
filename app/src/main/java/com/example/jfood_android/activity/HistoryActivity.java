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
import com.example.jfood_android.adapter.HistoryListAdapter;
import com.example.jfood_android.model.Invoice;
import com.example.jfood_android.request.InvoiceFetchRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private String currentUserId;
    private String currentUserEmail;

    ArrayList<Invoice> invoiceList = new ArrayList<>();
    ArrayList<Integer> invoiceIdList = new ArrayList<>();

    HistoryListAdapter historyListAdapter;
    SharedPreferences pref;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        progressDialog = new ProgressDialog(this);

        final RecyclerView rvHistoryList = findViewById(R.id.rvHistoryList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvHistoryList.setLayoutManager(layoutManager);
        progressDialog.setMessage("Retrieving History...");

        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        if(pref.contains("currentUserId") || pref.contains("email")){
            currentUserId = pref.getString("currentUserId", "");
            currentUserEmail = pref.getString("email", "");
        }

        historyListAdapter = new HistoryListAdapter(this, invoiceIdList);
        rvHistoryList.setAdapter(historyListAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (invoiceList != null){
            invoiceList.clear();
        }
        progressDialog.show();

        getHistoryList();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    // mengambil list seluruh invoice yang dimiliki Customer
    private void getHistoryList() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray != null){
                        // Get Invoice
                        //Toast.makeText(HistoryActivity.this, ("Test length: " + jsonArray.length()), Toast.LENGTH_SHORT).show();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int invoiceId = jsonObject.getInt("id");
                            invoiceIdList.add(invoiceId);
                        }
                        historyListAdapter.setHistory(invoiceIdList);
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(HistoryActivity.this, "Fetch JSON empty", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e){
                    Toast.makeText(HistoryActivity.this, "Failed to Get the foods Order", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                    Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                    intent.putExtra("currentUserId", currentUserId);
                    startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(HistoryActivity.this).toBundle());
                }
            }
        };

        InvoiceFetchRequest invoiceFetchRequest = new InvoiceFetchRequest(currentUserId, responseListener);
        RequestQueue queue = Volley.newRequestQueue(HistoryActivity.this);
        queue.add(invoiceFetchRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Intent intent = new Intent(HistoryActivity.this, CartActivity.class);
                intent.putExtra("currentUserId", currentUserId);
                startActivity(intent);
                return true;

            case R.id.action_promo:
                Intent promoIntent = new Intent(HistoryActivity.this, PromoActivity.class);
                promoIntent.putExtra("currentUserId", currentUserId);
                startActivity(promoIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
