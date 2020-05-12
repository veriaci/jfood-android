package com.example.jfood_android.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jfood_android.Food;
import com.example.jfood_android.Location;
import com.example.jfood_android.MainListAdapter;
import com.example.jfood_android.request.MenuRequest;
import com.example.jfood_android.R;
import com.example.jfood_android.Seller;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {

    MainListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private ArrayList<Seller> listSeller = new ArrayList<>();
    private ArrayList<Food> foodIdList = new ArrayList<>();
    private HashMap<Seller, ArrayList<Food>> childMapping = new HashMap<>();

    private int currentUserId;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expListView = findViewById(R.id.lvExp);
        final Button pesanButton = findViewById(R.id.pesan);
        final FloatingActionButton fabSelesai = findViewById(R.id.fabSelesai);

        // pass currentUserId dari LoginActivity
        if(getIntent().getExtras() != null){
            Intent intent = getIntent();
            currentUserId = intent.getIntExtra("currentUserId", 0);
        }

        // preparing list data
        refreshList();

        Toast.makeText(MainActivity.this, ("Current Id = " + currentUserId), Toast.LENGTH_SHORT).show();

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Food selected = childMapping.get(listSeller.get(groupPosition)).get(childPosition);

                Intent buatPesananIntent = new Intent(MainActivity.this, BuatPesananActivity.class);
                buatPesananIntent.putExtra("currentUserId", currentUserId);
                buatPesananIntent.putExtra("foodId", selected.getId());
                buatPesananIntent.putExtra("foodName", selected.getName());
                buatPesananIntent.putExtra("foodCategory", selected.getCategory());
                buatPesananIntent.putExtra("foodPrice", selected.getPrice());

                startActivity(buatPesananIntent);
                return false;
            }
        });

        fabSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selesaiPesananIntent = new Intent(MainActivity.this, SelesaiPesananActivity.class);
                selesaiPesananIntent.putExtra("currentUserId", currentUserId);
                startActivity(selesaiPesananIntent);
            }
        });
    }


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
                            if(listSeller.isEmpty()){
                                listSeller.add(seller1);
                            } else {
                                for (Seller temp : listSeller){
                                    if (temp.getName().equals(seller1.getName())){
                                        break;
                                    } else {
                                        listSeller.add(seller1);
                                    }
                                }
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
                        }
                    }
                } catch (JSONException e){
                    Toast.makeText(MainActivity.this, "Failed to Get the Food", Toast.LENGTH_SHORT).show();
                }
                listAdapter = new MainListAdapter(MainActivity.this, listSeller, childMapping);

                // setting list adapter
                expListView.setAdapter(listAdapter);
            }
        };
        MenuRequest menuRequest = new MenuRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(menuRequest);
    }
}