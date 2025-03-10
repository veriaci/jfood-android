package com.example.jfood_android.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;

import com.example.jfood_android.model.Food;
import com.example.jfood_android.R;
import com.example.jfood_android.model.Seller;
import com.example.jfood_android.activity.FoodActivity;
import com.example.jfood_android.database.CartDataSource;

import static android.content.Context.MODE_PRIVATE;

public class MainListAdapter extends BaseExpandableListAdapter implements Filterable {
    private Context _context;
    private ArrayList<Seller> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<Seller, ArrayList<Food>> _listDataChild;
    private HashMap<Seller, ArrayList<Food>> _listDataChildFull;

    SharedPreferences pref;

    public MainListAdapter(Context context, ArrayList<Seller> listDataHeader,
                                 HashMap<Seller, ArrayList<Food>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        _listDataChildFull = new HashMap<>(listChildData);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Food food = (Food) getChild(groupPosition,childPosition);
        final String childText = food.getName();
        final int childId = food.getId();
        final int childPrice = food.getPrice();

        String currentUserEmail = "";
        String currentUserId = "";
        pref = _context.getSharedPreferences("user_details", MODE_PRIVATE);
        if(pref.contains("email") || pref.contains("currentUserId")){
            currentUserEmail = pref.getString("email", "");
            currentUserId = pref.getString("currentUserId", "");
        }
        final String tempUserEmail = currentUserEmail;
        final String tempUserId = currentUserId;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
        final View finalView = convertView;
        final ImageView lbListImage = finalView.findViewById(R.id.lblListImage);
        final ImageButton btnAddItem = finalView.findViewById(R.id.lbAddToCart);
        final TextView txtListChild = finalView.findViewById(R.id.lblListItem);
        final TextView txtListPrice = finalView.findViewById(R.id.lbListPrice);
        txtListChild.setText(childText);
        txtListPrice.setText("Rp. " + childPrice);

        // mengarahkan ke laman makanan yang dipilih
        txtListChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(_context, (locloc), Toast.LENGTH_SHORT).show();
                Intent foodIntent = new Intent(_context, FoodActivity.class);
                foodIntent.putExtra("currentUserId", tempUserId);
                foodIntent.putExtra("foodId", food.getId());
                foodIntent.putExtra("foodName", food.getName());
                foodIntent.putExtra("foodCategory", food.getCategory());
                foodIntent.putExtra("foodPrice", food.getPrice());
                foodIntent.putExtra("foodLocation", food.getSeller().getLocation().getCity());
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) _context, lbListImage, ViewCompat.getTransitionName(lbListImage));
                _context.startActivity(foodIntent, options.toBundle());
            }
        });

        // respon tombol untuk menambahkan makanan yang terpilih ke keranjang makanan
        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(tempUserEmail, childId, childPrice);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Seller seller = (Seller) getGroup(groupPosition);
        String headerTitle = seller.getName();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        Typeface typeface = ResourcesCompat.getFont(_context, R.font.lekton_bold);
        lblListHeader.setTypeface(typeface);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    // menambahkan makanan yang terpilih ke keranjang makanan
    private void addItem(String currentUserEmail, int foodId, int foodPrice){
        CartDataSource cartDataSource = new CartDataSource(_context);
        cartDataSource.open();
        cartDataSource.addItem(currentUserEmail,foodId,foodPrice);
        cartDataSource.close();
    }

    // mengembalikan filter
    @Override
    public Filter getFilter(){
        return mainFilter;
    }

    // memfilter makanan yang ada berdasarkan nama yang tertulis di searchView
    private Filter mainFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            HashMap<Seller, ArrayList<Food>> filteredList = new HashMap<>();
            ArrayList<Food> filteredFood = new ArrayList<>();
            ArrayList<Food> filteredEmpty = new ArrayList<>();
            boolean tester = false;

            if (constraint == null || constraint.length() == 0){
                filteredList.putAll(_listDataChildFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Map.Entry<Seller, ArrayList<Food>> entry : _listDataChildFull.entrySet()){
                    tester = false;
                    //System.out.println(entry.getKey() + " = " + entry.getValue());
                    for(Food food : entry.getValue()){
                        if (food.getName().toLowerCase().contains(filterPattern)){
                            filteredFood.add(food);
                            if (_listDataChild.get(entry.getKey()).contains(food)){
                                tester = true;
                            }
                        }
                    }
                    if(tester){
                        filteredList.put(entry.getKey(),filteredFood);
                    } else {
                        filteredList.put(entry.getKey(),filteredEmpty);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        // memperbaharui arraylist yang menampilkan penjual dan makanan berdasarkan filter
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            _listDataChild.clear();
            _listDataChild.putAll((HashMap)results.values);
            notifyDataSetChanged();
        }
    };
}
