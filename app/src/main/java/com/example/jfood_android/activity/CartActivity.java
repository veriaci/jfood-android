package com.example.jfood_android.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jfood_android.model.Food;
import com.example.jfood_android.R;
import com.example.jfood_android.database.CartDataSource;
import com.example.jfood_android.adapter.CartListAdapter;
import com.example.jfood_android.request.FoodsFetchRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity{
    CartDataSource dataSource;
    ArrayList<Food> foods = new ArrayList<>();
    ArrayList<Integer> foodIdList = new ArrayList<>();
    ArrayList<Integer> foodQuantity = new ArrayList<>();

    ProgressDialog progressDialog;
    RecyclerView rvCartList;
    TextView tvTotalPrice;
    TextView tvKosong;
    Button btnProcessCart;

    CartListAdapter cartListAdapter;
    SharedPreferences pref;

    String currentUserId = "";
    String currentUserEmail = "";

    final int deliveryFee = 10000;

    private static Integer[] totalPrice = new Integer[1];

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dataSource = new CartDataSource(this);
        progressDialog = new ProgressDialog(this);
        rvCartList = findViewById(R.id.rv_cart_list);
        btnProcessCart = findViewById(R.id.btn_process_cart);
        tvTotalPrice = findViewById(R.id.tv_total_price_cart);
        tvKosong = findViewById(R.id.tvKosong);
        tvKosong.setVisibility(View.GONE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvCartList.setLayoutManager(layoutManager);

        progressDialog.setMessage("Fetching Your food cart...");

        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        if(pref.contains("currentUserId") || pref.contains("email")){
            currentUserId = pref.getString("currentUserId", "");
            currentUserEmail = pref.getString("email", "");
        }

        cartListAdapter = new CartListAdapter(this, foods, foodQuantity, totalPrice, tvTotalPrice);
        rvCartList.setAdapter(cartListAdapter);

        btnProcessCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("my_cart", "total: " + (totalPrice[0] + deliveryFee));

                if (totalPrice[0] > 0) {
                    Intent intent = new Intent(CartActivity.this, OrderActivity.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(CartActivity.this, btnProcessCart, ViewCompat.getTransitionName(btnProcessCart));
                    startActivity(intent, options.toBundle());

                } else {
                    Toast.makeText(CartActivity.this, "You have an empty cart", Toast.LENGTH_SHORT).show();
                }
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
        getCartData();
        if(!foodIdList.isEmpty()){
            tvKosong.setVisibility(View.GONE);
            getItemCart();
        } else {
            tvKosong.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void getCartData() {
        String currentUserEmail = "";

        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        if(pref.contains("email")){
            currentUserEmail = pref.getString("email", "");
        }

        dataSource.open();
        foodIdList = removeDuplicates(dataSource.getAllItem(currentUserEmail));
        totalPrice[0] = dataSource.getTotalPrice(currentUserEmail);
        tvTotalPrice.setText("Rp. " + totalPrice[0]);
        dataSource.close();
    }

    private void getItemCart() {
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
                        cartListAdapter.setFoods(foods, foodQuantity);
                    } else {
                        Toast.makeText(CartActivity.this, "Fetch JSON empty", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e){
                    Toast.makeText(CartActivity.this, "Failed to Get the foods to cart", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CartActivity.this, MainActivity.class);
                    intent.putExtra("currentUserId", currentUserId);
                    startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(CartActivity.this).toBundle());
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
        RequestQueue queue = Volley.newRequestQueue(CartActivity.this);
        queue.add(FoodsFetchRequest);
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

}