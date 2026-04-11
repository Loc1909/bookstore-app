package com.example.bookstore_app.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bookstore_app.database.DatabaseHelper;
import com.example.bookstore_app.models.CartItem;

import java.util.List;

public class OrderDAO {

    private final DatabaseHelper dbHelper;

    public OrderDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // ================= CREATE ORDER =================
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

            if (orderId == -1) {
                throw new RuntimeException("Insert order failed");
            }

            for (CartItem item : cartList) {
                ContentValues itemValues = new ContentValues();
                itemValues.put("order_id", orderId);
                itemValues.put("book_id", item.getBookId());
                itemValues.put("quantity", item.getQuantity());
                itemValues.put("price", item.getPrice());

                long result = db.insert("order_items", null, itemValues);

                if (result == -1) {
                    throw new RuntimeException("Insert order item failed");
                }
            }

            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

        return orderId;
    }

    // ================= CHECK PURCHASE =================
    public boolean hasUserBoughtBook(int userId, int bookId) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql =
                "SELECT COUNT(*) " +
                        "FROM orders o " +
                        "JOIN order_items oi ON o.id = oi.order_id " +
                        "WHERE o.user_id = ? " +
                        "AND oi.book_id = ? " +
                        "AND o.status = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{
                String.valueOf(userId),
                String.valueOf(bookId),
                "COMPLETED"
        });

        boolean result = false;

        try {
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0) > 0;
            }
        } finally {
            cursor.close();
        }

        return result;
    }
}