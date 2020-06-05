package com.example.jfood_android.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jfood_android.model.Food;
import com.example.jfood_android.model.Location;
import com.example.jfood_android.adapter.MainListAdapter;
import com.example.jfood_android.request.MenuRequest;
import com.example.jfood_android.R;
import com.example.jfood_android.model.Seller;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    // mempersiapkan variabel
    MainListAdapter listAdapter;
    ExpandableListView expListView;

    private ArrayList<Seller> listSeller = new ArrayList<>();
    private LinkedHashSet<Seller> setSeller = new LinkedHashSet<>();
    private ArrayList<Food> foodIdList = new ArrayList<>();
    private HashMap<Seller, ArrayList<Food>> childMapping = new HashMap<>();

    MenuItem searchItem = null;
    SearchView searchView = null;

    private String currentUserId;
    SharedPreferences pref;
    ProgressDialog progressDialog;

    // mempersiapkan activity saat pertama kali dimulai
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expListView = findViewById(R.id.lvExp);
        final FloatingActionButton fabSelesai = findViewById(R.id.fabSelesai);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching foods...");

        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        if(pref.contains("currentUserId")){
            currentUserId = pref.getString("currentUserId", "");
        }

        // mengarahkan ke laman penyelesaian pesanan
        fabSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selesaiPesananIntent = new Intent(MainActivity.this, InvoiceActivity.class);
                selesaiPesananIntent.putExtra("currentUserId", currentUserId);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, fabSelesai, ViewCompat.getTransitionName(fabSelesai));
                startActivity(selesaiPesananIntent, options.toBundle());
            }
        });
    }

    // merapihkan arraylist yang ada saat pengguna kembali ke MainActivity, untuk menghindari duplikasi data
    @Override
    protected void onResume(){
        super.onResume();
        if (foodIdList != null){
            foodIdList.clear();
        }

        if (listSeller != null){
            listSeller.clear();
        }

        if (setSeller != null){
            setSeller.clear();
        }
        clearSearch();
        progressDialog.show();
        // preparing list data
        refreshList();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    // memperbaharui list penjual dan makanan yang dijual
    protected void refreshList(){
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
            try{
                JSONArray jsonResponse = new JSONArray(response);
                if (jsonResponse != null){
                    for (int i = 0; i < jsonResponse.length(); i++){
                        JSONObject food = jsonResponse.getJSONObject(i);
                        JSONObject seller = food.getJSONObject("seller");
                        JSONObject location = seller.getJSONObject("location");

                        // location
                        String province = location.getString("province");
                        String description = location.getString("description");
                        String city = location.getString("city");

                        // Seller
                        int idSeller = seller.getInt("id");
                        String nameSeller = seller.getString("name");
                        String email = seller.getString("email");
                        String phoneNumber = seller.getString("phoneNumber");

                        // Food
                        int idFood = food.getInt("id");
                        String nameFood = food.getString("name");
                        int price = food.getInt("price");
                        String category = food.getString("category");

                        // Create Object
                        Location location1 = new Location(province, description, city);
                        Seller seller1 = new Seller(idSeller, nameSeller, email, phoneNumber, location1);
                        Food food1 = new Food(idFood, nameFood, seller1, price, category);

                        // Add to List
                        boolean tester = true;
                        for (Seller tempSeller : listSeller){
                            if (tempSeller.getId() == seller1.getId()){
                                tester = false;
                            }
                        }
                        if (tester){
                            listSeller.add(seller1);
                        }

                        foodIdList.add(food1);
                        for (Seller sel : listSeller){
                            ArrayList<Food> temp = new ArrayList<>();
                            for (Food foo : foodIdList){
                                if(foo.getSeller().getName().equals(sel.getName()) || foo.getSeller().getEmail().equals(sel.getEmail()) || foo.getSeller().getPhoneNumber().equals(sel.getPhoneNumber())){
                                    temp.add(foo);
                                }
                            }
                            childMapping.put(sel,temp);
                        }

                        listAdapter = new MainListAdapter(MainActivity.this, listSeller, childMapping);
                        progressDialog.dismiss();
                        expListView.setAdapter(listAdapter);
                    }
                }
            } catch (JSONException e){
                Toast.makeText(MainActivity.this, "Failed to Get the Food", Toast.LENGTH_SHORT).show();
            }
            }
        };
        MenuRequest menuRequest = new MenuRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(menuRequest);
    }

    // mengubah OptionsMenu default dan mengisi tombol serta fitur tambahan
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    // memberikan reaksi saat tombol di OptionsMenu ditekan
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                Intent cartIntent = new Intent(MainActivity.this, CartActivity.class);
                cartIntent.putExtra("currentUserId", currentUserId);
                startActivity(cartIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
                return true;

            case R.id.action_history:
                Intent historyIntent = new Intent(MainActivity.this, HistoryActivity.class);
                historyIntent.putExtra("currentUserId", currentUserId);
                startActivity(historyIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
                return true;

            case R.id.action_promo:
                Intent promoIntent = new Intent(MainActivity.this, PromoActivity.class);
                promoIntent.putExtra("currentUserId", currentUserId);
                startActivity(promoIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    // mengosongkan kembali searchbar
    public void clearSearch(){
        searchView.onActionViewCollapsed();
        searchView.setQuery("", false);
        searchView.clearFocus();
    }
}