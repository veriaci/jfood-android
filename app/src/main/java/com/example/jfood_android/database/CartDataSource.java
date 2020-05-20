package com.example.jfood_android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class CartDataSource {
    private DbHelper dbHelper;

    private SQLiteDatabase database;

    public CartDataSource(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public boolean addItem(String customer_email, int food_id, int food_price){
        ContentValues contentValues = new ContentValues();
        contentValues.put("customer_email", customer_email);
        contentValues.put("food_id", food_id);
        contentValues.put("price", food_price);

        long id = database.insert("cart", null, contentValues);

        return id > 0;
    }

    public ArrayList<Integer> getAllItem(String customerEmail) {
        final String SQL = "SELECT food_id FROM cart WHERE customer_email=?";
        ArrayList<Integer> foodIdList = new ArrayList<>();

        Cursor cursor = database.rawQuery(SQL, new String[]{customerEmail});
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            foodIdList.add(cursor.getInt(0));
            cursor.moveToNext();
        }

        return foodIdList;
    }

    public int getTotalPrice(String customerEmail) {
        int totalPrice;

        final String SQL = "SELECT SUM(price) from cart WHERE customer_email=?";
        Cursor cursor = database.rawQuery(SQL, new String[]{customerEmail});
        cursor.moveToFirst();
        totalPrice = cursor.getInt(0);
        return totalPrice;
    }

    public void clearCart(String customerEmail){

        database.delete("cart", "customer_email=?", new String[]{customerEmail});

    }

    public void deleteItemFromCart(String customerEmail, int food_id){
        final String SQL_SELECT = "(SELECT id FROM cart WHERE customer_email='" + customerEmail + "'"
                + " AND food_id=" + food_id + " LIMIT 1)";
        final String SQL_DELETE = "DELETE FROM cart WHERE id" + " IN " + SQL_SELECT;
        database.execSQL(SQL_DELETE);
    }
}
