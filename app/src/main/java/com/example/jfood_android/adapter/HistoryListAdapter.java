package com.example.jfood_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jfood_android.R;
import com.example.jfood_android.request.InvoiceIdRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.HistoryViewHolder> {

    private Context context;
    private ArrayList<Integer> invoiceIdList;
    private Integer currentInvoiceId = 0;

    public HistoryListAdapter(Context context, ArrayList<Integer> invoiceIdList){
        this.context = context;
        this.invoiceIdList = invoiceIdList;
    }

    public void setHistory(ArrayList<Integer> invoiceIdList){
        this.invoiceIdList = invoiceIdList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(view);
        // return null;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        final Integer invoiceId = invoiceIdList.get(position);
        final HistoryViewHolder finalHolder = holder;

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject invoice = new JSONObject(response);
                        // Get Invoice
                        //Toast.makeText(context, ("Test length: " + invoice.length()), Toast.LENGTH_SHORT).show();
                        int invoiceId = invoice.getInt("id");
                        String invoiceStatus = invoice.getString("invoiceStatus");
                        String paymentType = invoice.getString("paymentType");
                        int totalPrice = invoice.getInt("totalPrice");
                        JSONObject customer = invoice.getJSONObject("customer");
                        String customerName = customer.getString("name");
                        JSONArray foods = invoice.getJSONArray("foods");

                        StringBuffer stringBuffer = new StringBuffer();
                        String foodName = "";
                        for (int j = 0; j < foods.length(); j++) {
                            JSONObject food = foods.getJSONObject(j);
                            stringBuffer.append(food.getString("name"));
                            if (j+1 != foods.length()){
                                stringBuffer.append(", ");
                            }
                        }
                        foodName = stringBuffer.toString();

                        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy");
                        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(invoice.getString("date"));
                            finalHolder.tvHistoryDate.setText(formatter.format(date));
                        } catch (Exception e){
                            Toast.makeText(context, ("Date Parsing Error"), Toast.LENGTH_SHORT).show();
                        }

                        finalHolder.tvHistoryId.setText(Integer.toString(invoiceId));
                        finalHolder.tvHistoryCustomer.setText(customerName);
                        finalHolder.tvHistoryFood.setText(foodName);
                        finalHolder.tvHistoryType.setText(paymentType);
                        finalHolder.tvHistoryStatus.setText(invoiceStatus);
                        finalHolder.tvHistoryTotalPrice.setText("Rp. " + (totalPrice));

                        if (currentInvoiceId == 0) {
                            currentInvoiceId = invoiceId;
                        }
                        //Toast.makeText(context, ("Invoice: " + currentInvoiceId), Toast.LENGTH_SHORT).show();

                } catch (JSONException e){
                    Toast.makeText(context, "Failed to Get the Invoice", Toast.LENGTH_SHORT).show();
                }
            }
        };
        InvoiceIdRequest pesananFetchRequest = new InvoiceIdRequest(invoiceId, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(pesananFetchRequest);
    }

    @Override
    public int getItemCount() {
        if (invoiceIdList != null){
            return invoiceIdList.size();
        }
        return 0;
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvHistory;
        TextView tvHistoryId;
        TextView tvHistoryCustomer;
        TextView tvHistoryFood;
        TextView tvHistoryDate;
        TextView tvHistoryType;
        TextView tvHistoryStatus;
        TextView tvHistoryTotalPrice;

        HistoryViewHolder(@NonNull View orderView) {
            super(orderView);
            rvHistory = orderView.findViewById(R.id.rvHistoryList);
            tvHistoryId = orderView.findViewById(R.id.tvHistoryId);
            tvHistoryCustomer = orderView.findViewById(R.id.tvHistoryCustomer);
            tvHistoryFood = orderView.findViewById(R.id.tvHistoryFood);
            tvHistoryDate = orderView.findViewById(R.id.tvHistoryDate);
            tvHistoryType = orderView.findViewById(R.id.tvHistoryType);
            tvHistoryStatus = orderView.findViewById(R.id.tvHistoryStatus);
            tvHistoryTotalPrice = orderView.findViewById(R.id.tvHistoryTotalPrice);
        }
    }
}

