package com.example.jfood_android.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.jfood_android.request.InvoiceFetchRequest;
import com.example.jfood_android.request.InvoiceStatusRequest;
import com.example.jfood_android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class InvoiceActivity extends AppCompatActivity {

    private String currentUserId = "";
    private int currentInvoiceId = 0;

    TextView staticInvoiceId;
    TextView staticInvoiceCustomer;
    TextView staticInvoiceFood;
    TextView staticInvoiceDate;
    TextView staticInvoiceType;
    TextView staticInvoiceStatus;
    TextView staticInvoiceTotalPrice;

    TextView tvInvoiceId;
    TextView tvInvoiceCustomer;
    TextView tvInvoiceFood;
    TextView tvInvoiceDate;
    TextView tvInvoiceType;
    TextView tvInvoiceStatus;
    TextView tvInvoiceTotalPrice;

    TextView tvKosong;
    TextView tvJudul;

    Button btnInvoiceBatal;
    Button btnInvoiceSelesai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        // Find View
        staticInvoiceId = findViewById(R.id.staticInvoiceId);
        staticInvoiceCustomer = findViewById(R.id.staticInvoiceCustomer);
        staticInvoiceFood = findViewById(R.id.staticInvoiceFood);
        staticInvoiceDate = findViewById(R.id.staticInvoiceDate);
        staticInvoiceType = findViewById(R.id.staticInvoiceType);
        staticInvoiceStatus = findViewById(R.id.staticInvoiceStatus);
        staticInvoiceTotalPrice = findViewById(R.id.staticInvoiceTotalPrice);

        tvInvoiceId = findViewById(R.id.tvInvoiceId);
        tvInvoiceCustomer = findViewById(R.id.tvInvoiceCustomer);
        tvInvoiceFood = findViewById(R.id.tvInvoiceFood);
        tvInvoiceDate = findViewById(R.id.tvInvoiceDate);
        tvInvoiceType = findViewById(R.id.tvInvoiceType);
        tvInvoiceStatus = findViewById(R.id.tvInvoiceStatus);
        tvInvoiceTotalPrice = findViewById(R.id.tvInvoiceTotalPrice);

        // Set Visibility to Invisible
        staticInvoiceId.setVisibility(View.GONE);
        staticInvoiceCustomer.setVisibility(View.GONE);
        staticInvoiceFood.setVisibility(View.GONE);
        staticInvoiceDate.setVisibility(View.GONE);
        staticInvoiceType.setVisibility(View.GONE);
        staticInvoiceStatus.setVisibility(View.GONE);
        staticInvoiceTotalPrice.setVisibility(View.GONE);

        tvInvoiceId.setVisibility(View.GONE);
        tvInvoiceCustomer.setVisibility(View.GONE);
        tvInvoiceFood.setVisibility(View.GONE);
        tvInvoiceDate.setVisibility(View.GONE);
        tvInvoiceType.setVisibility(View.GONE);
        tvInvoiceStatus.setVisibility(View.GONE);
        tvInvoiceTotalPrice.setVisibility(View.GONE);

        // Button
        btnInvoiceBatal = findViewById(R.id.invoiceBatal);
        btnInvoiceSelesai = findViewById(R.id.invoiceSelesai);

        btnInvoiceBatal.setVisibility(View.VISIBLE);
        btnInvoiceSelesai.setVisibility(View.VISIBLE);

        // Bold Text
        tvJudul = findViewById(R.id.judul);
        tvKosong = findViewById(R.id.kosong);
        
        tvKosong.setVisibility(View.GONE);
        tvJudul.setVisibility(View.VISIBLE);

        // Get Intent
        if(getIntent().getExtras() != null){
            Intent intent = getIntent();
            currentUserId = intent.getStringExtra("currentUserId");
            currentInvoiceId = intent.getIntExtra("currentInvoiceId", 0);
        }

        fetchPesanan();

        btnInvoiceBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(InvoiceActivity.this, ("Test Batal"), Toast.LENGTH_LONG).show();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null){
                                Toast.makeText(InvoiceActivity.this, ("Pembatalan Invoice: " + currentInvoiceId + " Sukses"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(InvoiceActivity.this, MainActivity.class);
                                intent.putExtra("currentUserId", currentUserId);
                                startActivity(intent);
                            }
                        } catch (JSONException e){
                            Toast.makeText(InvoiceActivity.this, ("Pembatalan Invoice: " + currentInvoiceId + " GAGAL"), Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                InvoiceStatusRequest pesananStatusRequest = new InvoiceStatusRequest(currentInvoiceId, "Canceled", responseListener);
                RequestQueue queue = Volley.newRequestQueue(InvoiceActivity.this);
                queue.add(pesananStatusRequest);
            }
        });

        btnInvoiceSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject != null){
                                Toast.makeText(InvoiceActivity.this, (" Pemesanan: " + currentInvoiceId + " Sukses"), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(InvoiceActivity.this, MainActivity.class);
                                intent.putExtra("currentUserId", currentUserId);
                                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(InvoiceActivity.this).toBundle());
                            }
                        } catch (JSONException e){
                            Toast.makeText(InvoiceActivity.this, (" Pemesanan: " + currentInvoiceId + " GAGAL"), Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                InvoiceStatusRequest pesananStatusRequest = new InvoiceStatusRequest(currentInvoiceId, "Finished", responseListener);
                RequestQueue queue = Volley.newRequestQueue(InvoiceActivity.this);
                queue.add(pesananStatusRequest);
            }
        });
    }

    public void fetchPesanan(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray != null){
                        // Get Invoice
                        //Toast.makeText(InvoiceActivity.this, ("Test length: " + jsonArray.length()), Toast.LENGTH_SHORT).show();
                        if (jsonArray.length() == 0){
                            tvKosong.setVisibility(View.VISIBLE);
                            tvJudul.setVisibility(View.GONE);
                            btnInvoiceBatal.setVisibility(View.GONE);
                            btnInvoiceSelesai.setVisibility(View.GONE);
                        }
                        JSONObject invoice = jsonArray.getJSONObject(jsonArray.length()-1);
                        if(invoice.getString("invoiceStatus").equals("Ongoing")) {
                            // Set Visibility to Visible
                            staticInvoiceId.setVisibility(View.VISIBLE);
                            staticInvoiceCustomer.setVisibility(View.VISIBLE);
                            staticInvoiceFood.setVisibility(View.VISIBLE);
                            staticInvoiceDate.setVisibility(View.VISIBLE);
                            staticInvoiceType.setVisibility(View.VISIBLE);
                            staticInvoiceStatus.setVisibility(View.VISIBLE);
                            staticInvoiceTotalPrice.setVisibility(View.VISIBLE);

                            tvInvoiceId.setVisibility(View.VISIBLE);
                            tvInvoiceCustomer.setVisibility(View.VISIBLE);
                            tvInvoiceFood.setVisibility(View.VISIBLE);
                            tvInvoiceDate.setVisibility(View.VISIBLE);
                            tvInvoiceType.setVisibility(View.VISIBLE);
                            tvInvoiceStatus.setVisibility(View.VISIBLE);
                            tvInvoiceTotalPrice.setVisibility(View.VISIBLE);

                            tvKosong.setVisibility(View.GONE);
                            tvJudul.setVisibility(View.VISIBLE);
                            btnInvoiceBatal.setVisibility(View.VISIBLE);
                            btnInvoiceSelesai.setVisibility(View.VISIBLE);

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
                                tvInvoiceDate.setText(formatter.format(date));
                            } catch (Exception e){
                                Toast.makeText(InvoiceActivity.this, ("Date Parsing Error"), Toast.LENGTH_SHORT).show();
                            }

                            tvInvoiceId.setText(Integer.toString(invoiceId));
                            tvInvoiceCustomer.setText(customerName);
                            tvInvoiceFood.setText(foodName);
                            tvInvoiceType.setText(paymentType);
                            tvInvoiceStatus.setText(invoiceStatus);
                            tvInvoiceTotalPrice.setText(Integer.toString(totalPrice));

                            if (currentInvoiceId == 0) {
                                currentInvoiceId = invoiceId;
                            }
                            //Toast.makeText(InvoiceActivity.this, ("Invoice: " + currentInvoiceId), Toast.LENGTH_SHORT).show();
                        } else {
                            tvKosong.setVisibility(View.VISIBLE);
                            tvJudul.setVisibility(View.GONE);
                            btnInvoiceBatal.setVisibility(View.GONE);
                            btnInvoiceSelesai.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(InvoiceActivity.this, "Fetch JSON empty", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e){
                    Toast.makeText(InvoiceActivity.this, "Failed to Get the Invoice", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(InvoiceActivity.this, MainActivity.class);
                    intent.putExtra("currentUserId", currentUserId);
                    startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(InvoiceActivity.this).toBundle());
                }
            }
        };
        InvoiceFetchRequest pesananFetchRequest = new InvoiceFetchRequest(currentUserId, responseListener);
        RequestQueue queue = Volley.newRequestQueue(InvoiceActivity.this);
        queue.add(pesananFetchRequest);
    }
}
