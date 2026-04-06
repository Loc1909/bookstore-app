package com.example.bookstore_app.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


import com.example.bookstore_app.database.DatabaseHelper;
import com.example.bookstore_app.models.Book;
import com.example.bookstore_app.models.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    private DatabaseHelper dbHelper;

    public CartDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Thêm vào giỏ
    public void addToCart(Book book) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM cart WHERE bookId = ?",
                new String[]{String.valueOf(book.getId())}
        );

        if (cursor.moveToFirst()) {
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));

            ContentValues values = new ContentValues();
            values.put("quantity", quantity + 1);

            db.update("cart", values, "bookId = ?",
                    new String[]{String.valueOf(book.getId())});

        } else {
            ContentValues values = new ContentValues();
            values.put("bookId", book.getId());
            values.put("title", book.getTitle());
            values.put("price", book.getPrice());
            values.put("quantity", 1);

            db.insert("cart", null, values);
        }

        cursor.close();
    }

    public void updateQuantity(int cartId, int newQuantity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (newQuantity > 0) {
            ContentValues values = new ContentValues();
            values.put("quantity", newQuantity);
            db.update("cart", values, "id = ?", new String[]{String.valueOf(cartId)});
        }
    }

    public List<CartItem> getAllCart() {
        List<CartItem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM cart", null);

        while (cursor.moveToNext()) {
            CartItem item = new CartItem();
            item.id = cursor.getInt(0);
            item.bookId = cursor.getInt(1);
            item.title = cursor.getString(2);
            item.price = cursor.getDouble(3);
            item.quantity = cursor.getInt(4);

            list.add(item);
        }

        cursor.close();
        return list;
    }
}