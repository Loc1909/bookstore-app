package com.example.bookstore_app.database.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bookstore_app.database.DatabaseHelper;

public class OrderDAO {

    private SQLiteDatabase db;

    public OrderDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getReadableDatabase();
    }

    /**
     * Kiểm tra user đã mua sách chưa
     */
    public boolean hasUserBoughtBook(int userId, int bookId) {

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