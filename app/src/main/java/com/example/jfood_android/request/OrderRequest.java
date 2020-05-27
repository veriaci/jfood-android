package com.example.jfood_android.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class OrderRequest extends StringRequest {
    private static final String URL_CASH = "https://jfood-hanif.herokuapp.com/invoice/createCashInvoice";
    private static final String URL_CASHLESS = "https://jfood-hanif.herokuapp.com//invoice/createCashlessInvoice";
    private Map<String, String> params;

    public OrderRequest(ArrayList<Integer> foodIdList, String customerId, int deliveryFee, Response.Listener<String> listener){
        super(Method.POST, URL_CASH, listener, null);

        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        for ( i = 0; i < foodIdList.size(); i++){
            int foodId = foodIdList.get(i);
            stringBuffer.append(foodId);
            if (i+1 != foodIdList.size()){
                stringBuffer.append(",");
            }
        }
        String foodIdString = stringBuffer.toString();

        params = new HashMap<>();
        params.put("foodIdList", foodIdString);
        params.put("customerId", customerId);
        params.put("deliveryFee", Integer.toString(deliveryFee));
    }

    public OrderRequest(ArrayList<Integer> foodIdList, String customerId, String promoCode, Response.Listener<String> listener){
        super(Method.POST, URL_CASHLESS, listener, null);

        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        for ( i = 0; i < foodIdList.size(); i++){
            int foodId = foodIdList.get(i);
            stringBuffer.append(foodId);
            if (i+1 != foodIdList.size()){
                stringBuffer.append(",");
            }
        }
        String foodIdString = stringBuffer.toString();

        params = new HashMap<>();
        params.put("foodIdList", foodIdString);
        params.put("customerId", customerId);
        params.put("promoCode", promoCode);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}

