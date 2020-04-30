package com.example.jfood_android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the listview
        expListView = findViewById(R.id.lvExp);

        // preparing list data
        refreshList();
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
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
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