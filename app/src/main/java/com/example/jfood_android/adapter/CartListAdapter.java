package com.example.jfood_android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.jfood_android.Food;
import com.example.jfood_android.R;
import com.example.jfood_android.activity.FoodActivity;
import com.example.jfood_android.activity.OrderActivity;
import com.example.jfood_android.database.CartDataSource;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.CustomViewHolder>{

    Context context;
    ArrayList<Food> foods;
    ArrayList<Integer> foodQuantity;
    private Integer[] totalPrice;
    TextView tvTotalPrice;

    SharedPreferences pref;

    public CartListAdapter(Context context, ArrayList<Food> foods, ArrayList<Integer> foodQuantity, Integer[] totalPrice, TextView tvTotalPrice){
        this.context = context;
        this.foods = foods;
        this.foodQuantity = foodQuantity;
        this.totalPrice = totalPrice;
        this.tvTotalPrice = tvTotalPrice;
    }

    public void setFoods(ArrayList<Food> foods, ArrayList<Integer> foodQuantity){
        this.foods = foods;
        this.foodQuantity = foodQuantity;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cart_item, parent, false);
        return new CartListAdapter.CustomViewHolder(view);
        // return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {
        final Food food = foods.get(position);
        final int quantity = foodQuantity.get(position);

        String currentUserId = "";
        String currentUserEmail = "";

        pref = context.getSharedPreferences("user_details", MODE_PRIVATE);
        if(pref.contains("currentUserId") || pref.contains("email")){
            currentUserId = pref.getString("currentUserId", "");
            currentUserEmail = pref.getString("email", "");
        }

        final String tempUserId = currentUserId;
        final String tempUserEmail = currentUserEmail;

        holder.tvFoodName.setText(food.getName());
        holder.tvFoodPrice.setText("Rp. " + food.getPrice());
        holder.tvQuantityIndicator.setText(quantity + " " + (quantity > 1 ? "pcs" : "pc"));

        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, ("food id: " + food.getId()), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), FoodActivity.class);
                intent.putExtra("currentUserId", tempUserId);
                intent.putExtra("foodId", food.getId());
                intent.putExtra("foodName", food.getName());
                intent.putExtra("foodCategory", food.getCategory());
                intent.putExtra("foodPrice", food.getPrice());
                intent.putExtra("foodLocation", food.getSeller().getLocation().getCity());
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) view.getContext(), holder.imageView, ViewCompat.getTransitionName(holder.imageView));
                view.getContext().startActivity(intent, options.toBundle());
            }
        });

        holder.btnDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(food.getId());
                totalPrice[0] = totalPrice[0] - food.getPrice();
                tvTotalPrice.setText("Rp. " + totalPrice[0]);

                if (quantity > 1) {
                    foodQuantity.set(position, quantity - 1);
                } else {
                    foods.remove(position);
                    foodQuantity.remove(position);
                }
                notifyDataSetChanged();
            }
        });

        holder.btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(tempUserEmail, food.getId(), food.getPrice());
                totalPrice[0] = totalPrice[0] + food.getPrice();
                tvTotalPrice.setText("Rp. " + totalPrice[0]);

                foodQuantity.set(position, quantity + 1);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (foods != null){
            return foods.size();
        }
        return 0;
    }

    public  void deleteItem(int foodId){
        CartDataSource dataSource = new CartDataSource(context);
        String currentUserEmail = "";

        pref = context.getSharedPreferences("user_details", MODE_PRIVATE);
        if(pref.contains("email")){
            currentUserEmail = pref.getString("email", "");
        }

        if (currentUserEmail != null){
            dataSource.open();
            dataSource.deleteItemFromCart(currentUserEmail, foodId);
            dataSource.close();;
        }
    }

    public void addItem(String currentUserEmail, int foodId, int foodPrice){
        CartDataSource cartDataSource = new CartDataSource(context);
        cartDataSource.open();
        cartDataSource.addItem(currentUserEmail,foodId,foodPrice);
        cartDataSource.close();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvFoodName;
        TextView tvFoodPrice;
        CardView cvItem;
        ImageView imageView;
        TextView tvQuantityIndicator;
        ImageButton btnDeleteItem;
        ImageButton btnAddItem;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            cvItem = itemView.findViewById(R.id.cv_item_cart);
            tvFoodName = itemView.findViewById(R.id.tv_food_name_cart);
            tvFoodPrice = itemView.findViewById(R.id.tv_food_price_cart);
            imageView = itemView.findViewById(R.id.img_view_cart);
            tvQuantityIndicator = itemView.findViewById(R.id.tv_qty_indicator);
            btnDeleteItem = itemView.findViewById(R.id.btn_delete_item);
            btnAddItem = itemView.findViewById(R.id.btn_add_item);
        }
    }
}