package com.example.jfood_android.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class OrderRequest extends StringRequest {
    private static final String URL_CASH = "http://192.168.1.3:8080/invoice/createCashInvoice";
    private static final String URL_CASHLESS = "http://192.168.1.3:8080/invoice/createCashlessInvoice";
    private Map<String, String> params;

    public OrderRequest(ArrayList<Integer> foodIdList, int customerId, int deliveryFee, Response.Listener<String> listener){
        super(Method.POST, URL_CASH, listener, null);

        /*
        StringBuilder sb = new StringBuilder();
        for (int i = foodIdList.size() - 1; i >= 0; i--) {
            int num = foodIdList.get(i);
            sb.append(num);
        }
        String result = sb.toString();

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

*/
        String[] str= new String[foodIdList.size()];
        Object[] objArr = foodIdList.toArray();
        int i = 0;
        for(Object obj : objArr)
        {
            str[i++] = Integer.toString((Integer)obj);
        }
        String newString = Arrays.toString(str);
        newString = newString.substring(1, newString.length()-1);

        params = new HashMap<>();
        params.put("foodIdList", newString);
        params.put("customerId", Integer.toString(customerId));
        params.put("deliveryFee", Integer.toString(deliveryFee));
    }

    public OrderRequest(ArrayList<Integer> foodIdList, int customerId, String promoCode, Response.Listener<String> listener){
        super(Method.POST, URL_CASHLESS, listener, null);

        String[] str= new String[foodIdList.size()];
        Object[] objArr = foodIdList.toArray();
        int i = 0;
        for(Object obj : objArr)
        {
            str[i++] = Integer.toString((Integer)obj);
        }
        String newString = Arrays.toString(str);
        newString = newString.substring(1, newString.length()-1);

        params = new HashMap<>();
        params.put("foodIdList", newString);
        params.put("customerId", Integer.toString(customerId));
        params.put("promoCode", promoCode);
    }

    @Override
    public Map<String, String> getParams(){
        return params;
    }
}

