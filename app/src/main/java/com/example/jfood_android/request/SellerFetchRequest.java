package com.example.jfood_android.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

public class SellerFetchRequest extends StringRequest {
    private static  String URL = "https://jfood-hanif.herokuapp.com/seller/";
    public SellerFetchRequest(Response.Listener<String> listener) {
        super(Method.GET, URL, listener, null);
    }

    public static String getURL() {
        return URL;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return super.getParams();
    }
}
