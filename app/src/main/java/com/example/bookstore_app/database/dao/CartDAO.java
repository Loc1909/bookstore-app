package com.example.bookstore_app.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
            db.update("cart", values, "bookId = ?", new String[]{String.valueOf(book.getId())});
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

    public void deleteItem(int cartId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("cart", "id = ?", new String[]{String.valueOf(cartId)});
    }

    // Lấy toàn bộ cart với imageUrl
    public List<CartItem> getAllCart() {
        List<CartItem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // JOIN cart với books để lấy imageUrl
        String query = "SELECT c.id, c.bookId, c.title, c.price, c.quantity, b.imageUrl " +
                "FROM cart c " +
                "JOIN books b ON c.bookId = b.id";

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            CartItem item = new CartItem();
            item.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            item.bookId = cursor.getInt(cursor.getColumnIndexOrThrow("bookId"));
            item.title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            item.price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
            item.quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
            item.imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
            list.add(item);
        }

        cursor.close();
        return list;
    }
}