package com.example.jfood_android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(@Nullable Context context) {
        super(context, "jfood", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_TBL_CART = "CREATE TABLE cart (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "customer_email TEXT NOT NULL, food_id INTEGER NOT NULL, price INTEGER NOT NULL)";

        sqLiteDatabase.execSQL(SQL_TBL_CART);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
