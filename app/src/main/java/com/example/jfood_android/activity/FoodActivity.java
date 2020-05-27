package com.example.jfood_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jfood_android.R;
import com.example.jfood_android.database.CartDataSource;

public class FoodActivity extends AppCompatActivity {

    private String currentUserId;
    private String currentUserEmail;

    private TextView tvMakanName;
    private ImageView imgMakan;
    private TextView tvMakanLocation;
    private TextView tvMakanPrice;
    private TextView tvMakanCategory;

    private Button btnBuyNow;
    private Button btnAddToCart;

    private int makanId;
    private String makanName;
    // Image??
    private String makanLocation;
    private int makanPrice;
    private String makanCategory;

    SharedPreferences pref;
    CartDataSource cartDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        tvMakanName = findViewById(R.id.tvMakanName);
        imgMakan = findViewById(R.id.imgMakan);
        tvMakanLocation = findViewById(R.id.tvMakanLocation);
        tvMakanPrice = findViewById(R.id.tvMakanPrice);
        tvMakanCategory = findViewById(R.id.tvMakanCategory);
        btnBuyNow = findViewById(R.id.btnBuyNow);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        if(pref.contains("currentUserId") || pref.contains("email")){
            currentUserId = pref.getString("currentUserId", "");
            currentUserEmail = pref.getString("email", "");
        }

        if(getIntent().getExtras() != null){
            Intent intent = getIntent();
            makanId = intent.getIntExtra("foodId", 0);
            makanName = intent.getStringExtra("foodName");
            makanPrice = intent.getIntExtra("foodPrice", 0);
            makanCategory = intent.getStringExtra("foodCategory");
            makanLocation = intent.getStringExtra("foodLocation");
        }

        tvMakanName.setText(makanName);
        tvMakanCategory.setText(makanCategory);
        tvMakanPrice.setText("Rp. " + makanPrice);
        tvMakanLocation.setText(makanLocation);

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(currentUserEmail, makanId, makanPrice);
            }
        });

        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(currentUserEmail, makanId, makanPrice);
                Intent intent = new Intent(FoodActivity.this,OrderActivity.class);
                startActivity(intent);
            }
        });
    }

    public void addItem(String currentUserEmail, int foodId, int foodPrice){
        CartDataSource cartDataSource = new CartDataSource(FoodActivity.this);
        cartDataSource.open();
        cartDataSource.addItem(currentUserEmail,foodId,foodPrice);
        cartDataSource.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_food, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Intent intent = new Intent(FoodActivity.this, CartActivity.class);
                intent.putExtra("currentUserId", currentUserId);
                startActivity(intent);
                return true;

            case R.id.action_history:
                Intent historyIntent = new Intent(FoodActivity.this, HistoryActivity.class);
                historyIntent.putExtra("currentUserId", currentUserId);
                startActivity(historyIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
                return true;

            case R.id.action_promo:
                Intent promoIntent = new Intent(FoodActivity.this, PromoActivity.class);
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
