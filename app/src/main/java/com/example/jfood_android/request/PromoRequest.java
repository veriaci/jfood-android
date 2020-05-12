package com.example.jfood_android.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

public class PromoRequest extends StringRequest {
    private static  String URL = "http://192.168.1.3:8080/promo/code/";
    public PromoRequest(String promoCode,
                        Response.Listener<String> listener) {
        super(Method.GET, URL + promoCode, listener, null);
    }

    public static String getURL() {
        return URL;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return super.getParams();
    }
}
