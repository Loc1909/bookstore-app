package com.example.bookstore_app.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bookstore_app.database.DatabaseHelper;
import com.example.bookstore_app.models.CartItem;

import java.util.List;

public class OrderDAO {
    private DatabaseHelper dbHelper;

    public OrderDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long createOrder(int userId, List<CartItem> cartList, double totalPrice) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long orderId = -1;

        db.beginTransaction();
        try {
            ContentValues orderValues = new ContentValues();
            orderValues.put("user_id", userId);
            orderValues.put("order_date", System.currentTimeMillis());
            orderValues.put("total_price", totalPrice);
            orderValues.put("status", "PENDING");

            orderId = db.insert("orders", null, orderValues);
            for (CartItem item : cartList) {
                ContentValues itemValues = new ContentValues();
                itemValues.put("order_id", orderId);
                itemValues.put("book_id", item.getBookId());
                itemValues.put("quantity", item.getQuantity());
                itemValues.put("price", item.getPrice());

                db.insert("order_items", null, itemValues);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        db.close();
        return orderId;
    }
    public boolean hasUserBoughtBook(int userId, int bookId) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT COUNT(*) " +
                "FROM orders o " +
                "JOIN order_items od ON o.id = od.order_id " +
                "WHERE o.user_id = ? " +
                "AND od.book_id = ? " +
                "AND o.status = ?";

        Cursor cursor = db.rawQuery(query, new String[]{
                String.valueOf(userId),
                String.valueOf(bookId),
                "COMPLETED" // chỉ tính đơn đã hoàn thành
        });

        boolean result = false;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                result = count > 0;
            }
            cursor.close();
        }

        return result;
    }
}
