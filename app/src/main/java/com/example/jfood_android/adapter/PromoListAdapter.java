package com.example.jfood_android.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jfood_android.R;
import com.example.jfood_android.model.Promo;

import java.util.ArrayList;

public class PromoListAdapter extends RecyclerView.Adapter<PromoListAdapter.PromoViewHolder> {

    private Context context;
    private ArrayList<Promo> promos;

    public PromoListAdapter(Context context, ArrayList<Promo> promos){
        this.context = context;
        this.promos = promos;
    }

    public void setPromo(ArrayList<Promo> promos){
        this.promos = promos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PromoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.promo_item, parent, false);
        return new PromoViewHolder(view);
        // return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final PromoViewHolder holder, int position) {
        final Promo promo = promos.get(position);


        holder.tvPromoId.setText("No. " + promo.getId());
        holder.tvPromoCode.setText(promo.getCode());
        holder.tvPromoDiscount.setText("Rp. " + promo.getDiscount());
        holder.tvPromoMinPrice.setText("Rp. " + promo.getMinPrice());

        if (promo.getActive()){
            holder.tvPromoActive.setText("Active");
        } else {
            holder.tvPromoActive.setText("Not Active");
        }

        holder.btnCopyPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("TextView", holder.tvPromoCode.getText().toString());
                clipboard.setPrimaryClip(clipData);

                Toast.makeText(context, "Promo Code Copied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (promos != null){
            return promos.size();
        }
        return 0;
    }

    static class PromoViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rvPromo;
        TextView tvPromoId;
        TextView tvPromoCode;
        TextView tvPromoDiscount;
        TextView tvPromoMinPrice;
        TextView tvPromoActive;
        Button btnCopyPromo;

        PromoViewHolder(@NonNull View promoView) {
            super(promoView);
            rvPromo = promoView.findViewById(R.id.rvPromoList);
            tvPromoId = promoView.findViewById(R.id.tvPromoId);
            tvPromoCode = promoView.findViewById(R.id.tvPromoCode);
            tvPromoDiscount = promoView.findViewById(R.id.tvPromoDiscount);
            tvPromoMinPrice = promoView.findViewById(R.id.tvPromoMinPrice);
            tvPromoActive = promoView.findViewById(R.id.tvPromoActive);
            btnCopyPromo = promoView.findViewById(R.id.btnCopyPromo);
        }
    }
}

