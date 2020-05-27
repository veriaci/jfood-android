package com.example.jfood_android.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class InvoiceIdRequest extends StringRequest {
    public static final String URL_GET_INVOICE = "https://jfood-hanif.herokuapp.com/invoice/";

    public InvoiceIdRequest(int id, Response.Listener<String> listener) {
        super(URL_GET_INVOICE + id, listener, null);
    }
}
