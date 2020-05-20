package com.example.jfood_android.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

public class FoodsFetchRequest extends StringRequest {
    public static final String URL = "http://192.168.1.3:8080/food/multi/";

    public FoodsFetchRequest(String foodIdList, Response.Listener<String> listener) {
        super(URL + foodIdList, listener, null);
    }
}
