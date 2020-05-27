package com.example.jfood_android.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

public class PromoAllRequest extends StringRequest {
    private static  String URL = "https://jfood-hanif.herokuapp.com/promo/";
    public PromoAllRequest(Response.Listener<String> listener) {
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
