package com.example.bookstore_app.database.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bookstore_app.database.DatabaseHelper;
import com.example.bookstore_app.models.stats.CategoryStat;
import com.example.bookstore_app.models.stats.RevenueStat;
import com.example.bookstore_app.models.stats.TopProduct;
import com.example.bookstore_app.models.stats.UserStat;

import java.util.ArrayList;
import java.util.List;

public class StatisticDAO {
    private SQLiteDatabase db;

    public StatisticDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getReadableDatabase();
    }

    // 1. Doanh thu theo tháng
    public List<RevenueStat> getRevenueByMonth() {
        List<RevenueStat> list = new ArrayList<>();

        String sql = "SELECT substr(o.order_date,1,7) as month, " +
                "SUM(oi.quantity * oi.price) as revenue " +
                "FROM orders o " +
                "JOIN order_items oi ON o.id = oi.order_id " +
                "GROUP BY month ORDER BY month DESC";

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            list.add(new RevenueStat(
                    cursor.getString(0),
                    cursor.getDouble(1)
            ));
        }

        cursor.close();
        return list;
    }

    // 2. Top sản phẩm
    public List<TopProduct> getTopProducts() {
        List<TopProduct> list = new ArrayList<>();

        String sql = "SELECT b.title, SUM(oi.quantity) as total " +   //
                "FROM order_items oi " +
                "JOIN books b ON oi.book_id = b.id " +
                "GROUP BY b.id ORDER BY total DESC LIMIT 5";

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            list.add(new TopProduct(
                    cursor.getString(0),
                    cursor.getInt(1)
            ));
        }

        cursor.close();
        return list;
    }

    // 3. Thống kê user
    public List<UserStat> getUserStats() {
        List<UserStat> list = new ArrayList<>();

        String sql = "SELECT u.username, SUM(oi.quantity * oi.price) " +
                "FROM orders o " +
                "JOIN users u ON o.user_id = u.id " +
                "JOIN order_items oi ON oi.order_id = o.id " +
                "GROUP BY u.id ORDER BY 2 DESC";

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            list.add(new UserStat(
                    cursor.getString(0),
                    cursor.getDouble(1)
            ));
        }

        cursor.close();
        return list;
    }

    // 4. Theo category
    public List<CategoryStat> getCategoryStats() {
        List<CategoryStat> list = new ArrayList<>();

        String sql = "SELECT c.name, SUM(oi.quantity) " +
                "FROM order_items oi " +
                "JOIN books b ON oi.book_id = b.id " +
                "JOIN categories c ON b.category_id = c.id " +
                "GROUP BY c.id ORDER BY 2 DESC";

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            list.add(new CategoryStat(
                    cursor.getString(0),
                    cursor.getInt(1)
            ));
        }

        cursor.close();
        return list;
    }


    public double getTotalRevenue() {
        double total = 0;

        String sql = "SELECT SUM(quantity * price) FROM order_items";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }

        cursor.close();
        return total;
    }
}