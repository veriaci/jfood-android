package com.example.jfood_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jfood_android.model.Food;
import com.example.jfood_android.R;
import com.example.jfood_android.model.Promo;
import com.example.jfood_android.model.Promo;

import java.util.ArrayList;

public class PromoListAdapter extends RecyclerView.Adapter<PromoListAdapter.PromoViewHolder> {

    Context context;
    ArrayList<Promo> promos;

    public PromoListAdapter(Context context, ArrayList<Promo> promos){
        this.context = context;
        this.promos = promos;
    }

    public void setPromo(ArrayList<Promo> promo){
        this.promos = promos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PromoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.promo_item, parent, false);
        return new PromoListAdapter.PromoViewHolder(view);
        // return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PromoViewHolder holder, int position) {
        final Promo promo = promos.get(position);


        holder.tvPromoId.setText("No. " + promo.getId());
        holder.tvPromoCode.setText(promo.getCode());
        holder.tvPromoDiscount.setText("Rp. " + promo.getDiscount());
        holder.tvPromoMinPrice.setText("Rp. " + promo.getMinPrice());

        if (promo.getActive() == true){
            holder.tvPromoActive.setText("Active");
        } else {
            holder.tvPromoActive.setText("Not Active");
        }
    }

    @Override
    public int getItemCount() {
        if (promos != null){
            return promos.size();
        }
        return 0;
    }

    public class PromoViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvPromo;
        TextView tvPromoId;
        TextView tvPromoCode;
        TextView tvPromoDiscount;
        TextView tvPromoMinPrice;
        TextView tvPromoActive;

        public PromoViewHolder(@NonNull View orderView) {
            super(orderView);
            rvPromo = orderView.findViewById(R.id.rvPromoList);
            tvPromoId = orderView.findViewById(R.id.tvPromoId);
            tvPromoCode = orderView.findViewById(R.id.tvPromoCode);
            tvPromoDiscount = orderView.findViewById(R.id.tvPromoDiscount);
            tvPromoMinPrice = orderView.findViewById(R.id.tvPromoMinPrice);
            tvPromoActive = orderView.findViewById(R.id.tvPromoActive);
        }
    }
}

