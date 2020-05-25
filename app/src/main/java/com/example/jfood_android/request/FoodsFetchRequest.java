package com.example.jfood_android.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

public class FoodsFetchRequest extends StringRequest {
    public static final String URL = "https://jfood-hanif.herokuapp.com//food/multi/";

    public FoodsFetchRequest(String foodIdList, Response.Listener<String> listener) {
        super(URL + foodIdList, listener, null);
    }
}
