package com.example.jfood_android.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MenuRequest extends StringRequest {
    private static final String URL = "http://192.168.1.3:8080/food";
    private Map<String, String> params;

    public MenuRequest(//int id, String name, Seller seller, int price, String category,
                       Response.Listener<String> listener){
        super(Method.GET, URL, listener, null);
        params = new HashMap<>();
        //params.put("id", Integer.toString(id));
        //params.put("name", name);
        //params.put("seller", java.lang.String.valueOf(seller));
        //params.put("price", Integer.toString(price));
        //params.put("category", category);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
