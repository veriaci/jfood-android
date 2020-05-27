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
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jfood_android.model.Food;
import com.example.jfood_android.adapter.OrderListAdapter;
import com.example.jfood_android.database.CartDataSource;
import com.example.jfood_android.request.FoodsFetchRequest;
import com.example.jfood_android.request.OrderRequest;
import com.example.jfood_android.request.PromoRequest;
import com.example.jfood_android.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class OrderActivity extends AppCompatActivity {

    private String currentUserId;
    private String currentUserEmail;

    ArrayList<Food> foods = new ArrayList<>();
    ArrayList<Integer> foodIdList = new ArrayList<>();
    ArrayList<Integer> foodQuantity = new ArrayList<>();
    private int totalPrice;

    private int deliveryFee = 10000;
    private String promoCode;
    private String selectedPayment = "";

    private  boolean promoActive;
    private int promoMinPrice;
    private int promoDiscount;
    ProgressDialog progressDialog;

    CartDataSource cartDataSource;
    OrderListAdapter orderListAdapter;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        progressDialog = new ProgressDialog(this);
        cartDataSource = new CartDataSource(this);

        // find View
        final RecyclerView rvOrderList = findViewById(R.id.rvOrderList);
        final TextView textCode = findViewById(R.id.textCode);
        final TextView total_price = findViewById(R.id.total_price);
        final TextInputLayout promo_code = findViewById(R.id.promo_code);
        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
        final Button hitung = findViewById(R.id.hitung);
        final Button pesan = findViewById(R.id.pesan);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvOrderList.setLayoutManager(layoutManager);

        progressDialog.setMessage("Retrieving Order...");

        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        if(pref.contains("currentUserId") || pref.contains("email")){
            currentUserId = pref.getString("currentUserId", "");
            currentUserEmail = pref.getString("email", "");
        }

        orderListAdapter = new OrderListAdapter(this, foods, foodQuantity);
        rvOrderList.setAdapter(orderListAdapter);

        // menghilangtidak terlihat
        textCode.setVisibility(View.GONE);
        promo_code.setVisibility(View.GONE);
        pesan.setVisibility(View.GONE);

        // memberikan nilai
        total_price.setText("0");

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.cashless){
                    textCode.setVisibility(View.VISIBLE);
                    promo_code.setVisibility(View.VISIBLE);
                    hitung.setVisibility(View.VISIBLE);
                    pesan.setVisibility(View.GONE);
                    selectedPayment = "Cashless";
                } else if(checkedId == R.id.cash){
                    textCode.setVisibility(View.GONE);
                    promo_code.setVisibility(View.GONE);
                    hitung.setVisibility(View.VISIBLE);
                    pesan.setVisibility(View.GONE);
                    selectedPayment = "Cash";
                }
            }
        });

        hitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPayment.equals("Cashless")){
                    if (!promo_code.getEditText().getText().toString().isEmpty()){
                        promoCode = promo_code.getEditText().getText().toString();

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    if(jsonObject != null){
                                        promoActive = jsonObject.getBoolean("active");
                                        promoMinPrice = jsonObject.getInt("minPrice");
                                        promoDiscount = jsonObject.getInt("discount");
                                        if (promoActive && totalPrice > promoMinPrice){
                                            total_price.setText(Integer.toString(totalPrice - promoDiscount));
                                            hitung.setVisibility(View.GONE);
                                            pesan.setVisibility(View.VISIBLE);
                                        } else {
                                            promo_code.setError("Promo Code not Viable");
                                        }
                                    }
                                } catch (JSONException e){
                                    promo_code.setError("Invalid Promo Code");
                                }
                            }
                        };
                        PromoRequest promoRequest = new PromoRequest(promoCode, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(OrderActivity.this);
                        queue.add(promoRequest);
                    } else {
                        Toast.makeText(OrderActivity.this, "Please Fill Promo Code", Toast.LENGTH_SHORT).show();
                    }
                } else if(selectedPayment.equals("Cash")){
                    total_price.setText(Integer.toString(totalPrice + deliveryFee));
                    hitung.setVisibility(View.GONE);
                    pesan.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(OrderActivity.this, "Please Select Payment Method", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(OrderActivity.this, ("PEsan foodId = " + foodId), Toast.LENGTH_SHORT).show();
                //Toast.makeText(OrderActivity.this, ("PEsan Current Id = " + currentUserId), Toast.LENGTH_SHORT).show();
                //Toast.makeText(OrderActivity.this, ("PEsan ongkir = " + deliveryFee), Toast.LENGTH_SHORT).show();
                //Toast.makeText(OrderActivity.this, ("PEsan promoCode = " + promoCode), Toast.LENGTH_SHORT).show();

                cartDataSource.open();
                foodIdList = cartDataSource.getAllItem(currentUserEmail);
                cartDataSource.close();
                final String customerId = currentUserId;
                final int ongkir = deliveryFee;
                final String promo = promoCode;

                // Response Listener
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null){
                                cartDataSource.open();
                                cartDataSource.clearCart(currentUserEmail);
                                cartDataSource.close();
                                Toast.makeText(OrderActivity.this, "Order made", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(OrderActivity.this, ("foodIdList: " + foodIdList), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(OrderActivity.this, ("currentUserId: " + currentUserId), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(OrderActivity.this, ("deliveryFee: " + deliveryFee), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                                intent.putExtra("currentUserId", customerId);
                                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(OrderActivity.this).toBundle());
                            }
                        } catch (JSONException e){
                            Toast.makeText(OrderActivity.this, "Order failed to be made", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                // Request Baru
                OrderRequest buatRequest = null;
                if(selectedPayment.equals("Cash")){
                     buatRequest = new OrderRequest(foodIdList, customerId, ongkir, responseListener);
                } else if(selectedPayment.equals("Cashless")){
                     buatRequest = new OrderRequest(foodIdList, customerId, promo, responseListener);
                }

                // Queue Baru
                RequestQueue queue = Volley.newRequestQueue(OrderActivity.this);
                queue.add(buatRequest);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (foodIdList != null){
            foodIdList.clear();
        }
        if (foods != null) {
            foods.clear();
        }

        foodQuantity.clear();
        progressDialog.show();

        getOrderData();

        if(!foodIdList.isEmpty()){
            getOrderList();
        } else {
            progressDialog.dismiss();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public ArrayList<Integer> removeDuplicates(ArrayList<Integer> list) {
        ArrayList<Integer> ret = new ArrayList<>();

        for (Integer item: list) {
            if (!ret.contains(item)) {
                int qty = countQty(item, list);
                ret.add(item);
                foodQuantity.add(qty);
            }
        }
        return ret;
    }

    private int countQty(int id, ArrayList<Integer> list){
        int qty = 0;
        for (Integer idInList: list) {
            if (id == idInList) {
                qty += 1;
            }
        }
        return qty;
    }

    public void getOrderData() {
        String currentUserEmail = "";

        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        if(pref.contains("email")){
            currentUserEmail = pref.getString("email", "");
        }

        cartDataSource.open();
        foodIdList = removeDuplicates(cartDataSource.getAllItem(currentUserEmail));
        totalPrice = cartDataSource.getTotalPrice(currentUserEmail);
        cartDataSource.close();
    }

    private void getOrderList() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray != null){
                        // Get Invoice
                        //Toast.makeText(InvoiceActivity.this, ("Test length: " + jsonArray.length()), Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonFood = jsonArray.getJSONObject(i);
                            String stringFood = jsonFood.toString();

                            Gson gson = new Gson();
                            Food gFood = gson.fromJson(stringFood, Food.class);

                            //System.out.println(gFood);
                            foods.add(gFood);
                        }
                        orderListAdapter.setFoods(foods, foodQuantity);
                    } else {
                        Toast.makeText(OrderActivity.this, "Fetch JSON empty", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e){
                    Toast.makeText(OrderActivity.this, "Failed to Get the foods Order", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                    intent.putExtra("currentUserId", currentUserId);
                    startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(OrderActivity.this).toBundle());
                }
            }
        };

        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        for ( i = 0; i < foodIdList.size(); i++){
            int foodId = foodIdList.get(i);
            stringBuffer.append(foodId);
            if (i+1 != foodIdList.size()){
                stringBuffer.append(",");
            }
        }
        String foodIdString = stringBuffer.toString();

        FoodsFetchRequest FoodsFetchRequest = new FoodsFetchRequest(foodIdString, responseListener);
        RequestQueue queue = Volley.newRequestQueue(OrderActivity.this);
        queue.add(FoodsFetchRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history:
                Intent historyIntent = new Intent(OrderActivity.this, HistoryActivity.class);
                historyIntent.putExtra("currentUserId", currentUserId);
                startActivity(historyIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
                return true;

            case R.id.action_promo:
                Intent promoIntent = new Intent(OrderActivity.this, PromoActivity.class);
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
