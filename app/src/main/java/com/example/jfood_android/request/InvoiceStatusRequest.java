package com.example.jfood_android.request;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class InvoiceStatusRequest extends StringRequest {
    public static final String URL_CANCELED_INVOICE = "https://jfood-hanif.herokuapp.com//invoice/invoiceStatus/";
    private Map<String, String> params;

    public InvoiceStatusRequest(int id, String invoiceStatus, Response.Listener<String> listener) {
        super(Method.PUT,URL_CANCELED_INVOICE + id, listener, null);

        params = new HashMap<>();
        params.put("invoiceStatus", invoiceStatus);
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
