package com.example.jfood_android.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class InvoiceFetchRequest extends StringRequest {
    public static final String URL_GET_INVOICE = "http://192.168.1.3:8080/invoice/customer/";

    public InvoiceFetchRequest(int id, Response.Listener<String> listener) {
        super(URL_GET_INVOICE + id, listener, null);
    }
}
