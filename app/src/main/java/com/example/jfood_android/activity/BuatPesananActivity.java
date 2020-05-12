package com.example.jfood_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jfood_android.request.BuatPesananRequest;
import com.example.jfood_android.request.PromoRequest;
import com.example.jfood_android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BuatPesananActivity extends AppCompatActivity {

    private int currentUserId;
    private int foodId;
    private String foodName;
    private String foodCategory;
    private int foodPrice;
    private int deliveryFee = 10000;
    private String promoCode;
    private String selectedPayment = "";

    private  boolean promoActive;
    private int promoMinPrice;
    private int promoDiscount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_pesanan);

        // find View
        final TextView pesanan = findViewById(R.id.pesanan);
        final TextView textCode = findViewById(R.id.textCode);

        final TextView food_name = findViewById(R.id.food_name);
        final TextView food_category = findViewById(R.id.food_category);
        final TextView food_price = findViewById(R.id.food_price);
        final TextView total_price = findViewById(R.id.total_price);
        final EditText promo_code = findViewById(R.id.promo_code);

        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
        final RadioButton cash = findViewById(R.id.cash);
        final RadioButton cashless = findViewById(R.id.cashless);
        final Button hitung = findViewById(R.id.hitung);
        final Button pesan = findViewById(R.id.pesan);

        // menerima variabel dari MainActivity
        if(getIntent().getExtras() != null){
            Intent intent = getIntent();
            currentUserId = intent.getIntExtra("currentUserId", 0);
            foodId = intent.getIntExtra("foodId", 0);
            foodName = intent.getStringExtra("foodName");
            foodCategory = intent.getStringExtra("foodCategory");
            foodPrice = intent.getIntExtra("foodPrice", 0);
            Toast.makeText(BuatPesananActivity.this, ("Current Id = " + currentUserId), Toast.LENGTH_SHORT).show();
        }

        // menghilangtidak terlihat
        textCode.setVisibility(View.GONE);
        promo_code.setVisibility(View.GONE);
        pesan.setVisibility(View.GONE);

        // memberikan nilai
        food_name.setText(foodName);
        food_category.setText(foodCategory);
        food_price.setText(Integer.toString(foodPrice));
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
                    if (!promo_code.getText().toString().isEmpty()){
                        promoCode = promo_code.getText().toString();

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    if(jsonObject != null){
                                        promoActive = jsonObject.getBoolean("active");
                                        promoMinPrice = jsonObject.getInt("minPrice");
                                        promoDiscount = jsonObject.getInt("discount");
                                        if (promoActive && foodPrice > promoMinPrice){
                                            total_price.setText(Integer.toString(foodPrice - promoDiscount));
                                            hitung.setVisibility(View.GONE);
                                            pesan.setVisibility(View.VISIBLE);
                                        } else {
                                            Toast.makeText(BuatPesananActivity.this, "Invalid Promo Code", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } catch (JSONException e){
                                    Toast.makeText(BuatPesananActivity.this, "Invalid Promo Code", Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                        PromoRequest promoRequest = new PromoRequest(promoCode, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(BuatPesananActivity.this);
                        queue.add(promoRequest);
                    } else {
                        Toast.makeText(BuatPesananActivity.this, "Please Fill Promo Code", Toast.LENGTH_SHORT).show();
                    }
                } else if(selectedPayment.equals("Cash")){
                    total_price.setText(Integer.toString(foodPrice + deliveryFee));
                    hitung.setVisibility(View.GONE);
                    pesan.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(BuatPesananActivity.this, "Please Select Payment Method", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(BuatPesananActivity.this, ("PEsan foodId = " + foodId), Toast.LENGTH_SHORT).show();
                //Toast.makeText(BuatPesananActivity.this, ("PEsan Current Id = " + currentUserId), Toast.LENGTH_SHORT).show();
                //Toast.makeText(BuatPesananActivity.this, ("PEsan ongkir = " + deliveryFee), Toast.LENGTH_SHORT).show();
                //Toast.makeText(BuatPesananActivity.this, ("PEsan promoCode = " + promoCode), Toast.LENGTH_SHORT).show();

                final ArrayList<Integer> foodIdList = new ArrayList<>();
                final int customerId = currentUserId;
                final int ongkir = deliveryFee;
                final String promo = promoCode;

                foodIdList.add(foodId);

                // Response Listener
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null){
                                Toast.makeText(BuatPesananActivity.this, "Order made", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(BuatPesananActivity.this, MainActivity.class);
                                intent.putExtra("currentUserId", customerId);
                                startActivity(intent);
                            }
                        } catch (JSONException e){
                            Toast.makeText(BuatPesananActivity.this, "Order failed to be made", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                // Request Baru
                BuatPesananRequest buatRequest = null;
                if(selectedPayment.equals("Cash")){
                     buatRequest = new BuatPesananRequest(foodIdList, customerId, ongkir, responseListener);
                } else if(selectedPayment.equals("Cashless")){
                     buatRequest = new BuatPesananRequest(foodIdList, customerId, promo, responseListener);
                }

                // Queue Baru
                RequestQueue queue = Volley.newRequestQueue(BuatPesananActivity.this);
                queue.add(buatRequest);
            }
        });
    }
}
