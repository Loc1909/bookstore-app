package com.example.bookstore_app.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


import com.example.bookstore_app.database.DatabaseHelper;
import com.example.bookstore_app.models.Book;
import com.example.bookstore_app.models.CartItem;
import com.example.bookstore_app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    private DatabaseHelper dbHelper;

    public CartDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Thêm vào giỏ
    public void addToCart(int cartId, Book book, int quantity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DatabaseHelper.TABLE_CART_ITEM +
                        " WHERE " + DatabaseHelper.COL_CART_ITEM_CART_ID +
                        " = ? AND " + DatabaseHelper.COL_CART_ITEM_BOOK_ID + " = ?",
                new String[]{
                        String.valueOf(cartId),
                        String.valueOf(book.getId())
                }
        );

        if (cursor.moveToFirst()) {
            int currentQty = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));

            ContentValues values = new ContentValues();
            values.put("quantity", currentQty + quantity);

            db.update(DatabaseHelper.TABLE_CART_ITEM, values,
                    DatabaseHelper.COL_CART_ITEM_CART_ID + " = ? AND " +
                            DatabaseHelper.COL_CART_ITEM_BOOK_ID + " = ?",
                    new String[]{
                            String.valueOf(cartId),
                            String.valueOf(book.getId())
                    });

        } else {
            ContentValues values = new ContentValues();
            values.put("cart_id", cartId);
            values.put("book_id", book.getId());
            values.put("title", book.getTitle());
            values.put("price", book.getPrice());
            values.put("quantity", quantity);
            values.put("imageUrl", book.getImageUrl());

            db.insert(DatabaseHelper.TABLE_CART_ITEM, null, values);
        }

        cursor.close();
        db.close();
    }

    public void updateQuantity(int cartItemId, int newQuantity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (newQuantity > 0) {
            ContentValues values = new ContentValues();
            values.put("quantity", newQuantity);

            db.update(DatabaseHelper.TABLE_CART_ITEM,
                    values,
                    DatabaseHelper.COL_CART_ITEM_ID + " = ?",
                    new String[]{String.valueOf(cartItemId)});
        }

        db.close();
    }


    public List<CartItem> getCartItems(int cartId) {
        List<CartItem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DatabaseHelper.TABLE_CART_ITEM +
                        " WHERE " + DatabaseHelper.COL_CART_ITEM_CART_ID + " = ?",
                new String[]{String.valueOf(cartId)}
        );

        while (cursor.moveToNext()) {
            CartItem item = new CartItem();

            item.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            item.setCartId(cursor.getInt(cursor.getColumnIndexOrThrow("cart_id")));
            item.setBookId(cursor.getInt(cursor.getColumnIndexOrThrow("book_id")));
            item.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            item.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
            item.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
            item.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow("imageUrl")));

            list.add(item);
        }

        cursor.close();
        db.close();
        return list;
    }

    public void deleteItem(int cartItemId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(DatabaseHelper.TABLE_CART_ITEM,
                DatabaseHelper.COL_CART_ITEM_ID + " = ?",
                new String[]{String.valueOf(cartItemId)});

        db.close();
    }

    public int getCartIdByUser(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM " + DatabaseHelper.TABLE_CART +
                        " WHERE " + DatabaseHelper.COL_CART_USER_ID + " = ?",
                new String[]{String.valueOf(userId)}
        );

        int cartId = -1;

        if (cursor.moveToFirst()) {
            cartId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }

        cursor.close();
        db.close();
        return cartId;
    }

    public void clearCart(int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_CART,
                DatabaseHelper.COL_CART_USER_ID + " = ?"
                , new String[]{String.valueOf(userId)});
        db.close();
    }
}