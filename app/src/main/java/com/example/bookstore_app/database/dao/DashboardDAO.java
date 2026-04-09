package com.example.bookstore_app.database.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bookstore_app.database.DatabaseHelper;

public class DashboardDAO {

    private SQLiteDatabase db;

    public DashboardDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getReadableDatabase();
    }

    public int getTotalBooks() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM books", null);
        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        return count;
    }

    public int getTotalCategories() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM categories", null);
        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        return count;
    }

    public int getTotalUsers() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM users", null);
        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        return count;
    }

    public int getTotalOrders() {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM orders", null);
        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        return count;
    }

    // nâng cao: đơn mới
    public int getNewOrders() {
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM orders WHERE status = 'NEW'", null
        );

        int count = 0;

        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        return count;
    }
}