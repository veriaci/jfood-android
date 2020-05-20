package com.example.jfood_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jfood_android.Food;
import com.example.jfood_android.R;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderViewHolder> {

    Context context;
    ArrayList<Food> foods;
    ArrayList<Integer> foodQuantity;

    public OrderListAdapter(Context context, ArrayList<Food> foods, ArrayList<Integer> foodQuantity ){
        this.context = context;
        this.foods = foods;
        this.foodQuantity = foodQuantity;
    }

    public void setFoods(ArrayList<Food> foods, ArrayList<Integer> foodQuantity){
        this.foods = foods;
        this.foodQuantity = foodQuantity;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.order_item, parent, false);
        return new OrderListAdapter.OrderViewHolder(view);
        // return null;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        final Food food = foods.get(position);
        final int quantity = foodQuantity.get(position);

        holder.tvOrderName.setText(food.getName());
        holder.tvOrderPrice.setText("Rp. " + (food.getPrice()));
        holder.tvOrderCategory.setText(food.getCategory());
        holder.tvOrderQuantity.setText(quantity + " " + (quantity > 1 ? "pcs" : "pc"));
        holder.tvOrderSubTotal.setText("Rp. " + (food.getPrice() * quantity));
    }

    @Override
    public int getItemCount() {
        if (foods != null){
            return foods.size();
        }
        return 0;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvOrder;
        TextView tvOrderName;
        TextView tvOrderPrice;
        TextView tvOrderCategory;
        TextView tvOrderQuantity;
        TextView tvOrderSubTotal;

        public OrderViewHolder(@NonNull View orderView) {
            super(orderView);
            rvOrder = orderView.findViewById(R.id.rvOrderList);
            tvOrderName = orderView.findViewById(R.id.tvOrderName);
            tvOrderPrice = orderView.findViewById(R.id.tvOrderPrice);
            tvOrderCategory = orderView.findViewById(R.id.tvOrderCategory);
            tvOrderQuantity = orderView.findViewById(R.id.tvOrderQuantity);
            tvOrderSubTotal = orderView.findViewById(R.id.tvOrderSubTotal);
        }
    }
}
