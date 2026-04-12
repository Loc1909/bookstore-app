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
    public void addToCart(int userId, Book book, int quantity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM cart WHERE bookId = ? AND userId = ?",
                new String[]{String.valueOf(book.getId()),
                        String.valueOf(userId)}
        );

        if (cursor.moveToFirst()) {
            int cartQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));

            ContentValues values = new ContentValues();
            values.put("quantity", cartQuantity + quantity);

            db.update("cart", values,
                    "bookId = ? AND userId = ?",
                    new String[]{
                            String.valueOf(book.getId()),
                            String.valueOf(userId)
                    });

        } else {
            ContentValues values = new ContentValues();
            values.put("userId", userId);
            values.put("bookId", book.getId());
            values.put("title", book.getTitle());
            values.put("price", book.getPrice());
            values.put("quantity", quantity);
            values.put("imageUrl", book.getImageUrl());
            db.insert("cart", null, values);
        }

        cursor.close();
        db.close();
    }

    public void updateQuantity(int userId, int cartId, int newQuantity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (newQuantity > 0) {
            ContentValues values = new ContentValues();
            values.put("quantity", newQuantity);
            db.update("cart", values,
                    "id = ? AND userId = ?",
                    new String[]{
                            String.valueOf(cartId),
                            String.valueOf(userId)});
        }

        db.close();
    }

    public List<CartItem> getCartByUser(int userId) {
        List<CartItem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM cart WHERE userId = ?",
                new String[]{String.valueOf(userId)}
        );

        while (cursor.moveToNext()) {
            CartItem item = new CartItem();

            item.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            item.userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
            item.bookId = cursor.getInt(cursor.getColumnIndexOrThrow("bookId"));
            item.title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            item.price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
            item.quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
            item.imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));

            list.add(item);
        }

        cursor.close();
        db.close();
        return list;
    }

    public void deleteItem(int userId, int cartId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete("cart",
                "id = ? AND userId = ?",
                new String[]{
                        String.valueOf(cartId),
                        String.valueOf(userId)
                });

        db.close();
    }

    public void clearCart(int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("cart", "userId = ?", new String[]{String.valueOf(userId)});
        db.close();
    }
}