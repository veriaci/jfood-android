package com.example.jfood_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jfood_android.R;
import com.example.jfood_android.adapter.HistoryListAdapter;
import com.example.jfood_android.adapter.OrderListAdapter;
import com.example.jfood_android.model.CashInvoice;
import com.example.jfood_android.model.CashlessInvoice;
import com.example.jfood_android.model.Customer;
import com.example.jfood_android.model.Food;
import com.example.jfood_android.model.Invoice;
import com.example.jfood_android.model.Promo;
import com.example.jfood_android.request.FoodsFetchRequest;
import com.example.jfood_android.request.InvoiceFetchRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    private void getHistoryList() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray != null){
                        // Get Invoice
                        Toast.makeText(HistoryActivity.this, ("Test length: " + jsonArray.length()), Toast.LENGTH_SHORT).show();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int invoiceId = jsonObject.getInt("id");
                            /*
                            String stringInvoice = jsonObject.toString();
                            System.out.println(stringInvoice);
                            //JsonParser jsonParser = new JsonParser().parse();
                            //JsonElement jsonElement = jsonParser.parse(stringInvoice);
                            //System.out.println(jsonElement);
                            int invoiceId = jsonObject.getInt("id");
                            System.out.println(invoiceId);

                            JSONArray invoiceFoods = jsonObject.getJSONArray("foods");
                            Gson gsonFoods = new Gson();
                            Type invoiceType = new TypeToken<List<Food>>(){}.getType();
                            ArrayList<Food> gFood = gsonFoods.fromJson(invoiceFoods.toString(), invoiceType);
                            System.out.println(gFood.toString());

                            JSONObject invoiceCustomer = jsonObject.getJSONObject("customer");
                            Gson gsonCustomer = new GsonBuilder().//setDateFormat("yyyy-MM-dd'T'HH:mm:ss*SSSZZZZ").create();
                                    registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                                @Override
                                public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
                                    throws JsonParseException{
                                    return new Date(jsonElement.getAsLong());
                                }
                            });
                            Customer gCustomer = gsonCustomer.fromJson(invoiceCustomer.toString(), Customer.class);
                            System.out.println(gCustomer.toString());

                            JSONObject invoicePromo = jsonObject.getJSONObject("promo");
                            Gson gsonPromo = new Gson();
                            Promo gPromo = gsonCustomer.fromJson(invoicePromo.toString(), Promo.class);
                            System.out.println(gPromo.toString());

                            int invoiceDeliv = jsonObject.getInt("deliveryFee");

                            Invoice gInvoice = null;
                            if (jsonObject.getString("paymentType").equals("Cashless")){
                                 //gInvoice = gson.fromJson(jsonObject.toString(), CashlessInvoice.class);
                                 gInvoice = new CashlessInvoice(invoiceId, gFood, gCustomer, gPromo);
                            } else {
                                //gInvoice = gson.fromJson(jsonObject.toString(), CashInvoice.class);
                                gInvoice = new CashInvoice(invoiceId, gFood, gCustomer, invoiceDeliv);
                            }
                            */
                            //System.out.println(gInvoice);
                            invoiceIdList.add(invoiceId);
                        }
                        //String stringInvoice = jsonArray.toString();
                        //Gson gson = new Gson();
                        //Type invoiceType = new TypeToken<List<Invoice>>(){}.getType();
                        //ArrayList<Invoice> gInvoice = new Gson().fromJson(jsonArray.toString(),invoiceType);
                        //invoiceList = gInvoice;
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
}
