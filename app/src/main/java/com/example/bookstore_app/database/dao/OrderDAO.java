package com.example.bookstore_app.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bookstore_app.database.DatabaseHelper;
import com.example.bookstore_app.models.CartItem;
import com.example.bookstore_app.models.Order;
import com.example.bookstore_app.models.OrderItem;

import java.util.ArrayList;
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

    public List<Order> getOrdersByUser(int userId) {
        List<Order> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC",
                new String[]{String.valueOf(userId)}
        );

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                order.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                order.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("total_price")));
                order.setOrderDate(cursor.getLong(cursor.getColumnIndexOrThrow("order_date")));
                order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                list.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public boolean updateOrderStatus(int orderId, String newStatus) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", newStatus);
        int rows = db.update("orders", values, "id = ?", new String[]{String.valueOf(orderId)});
        db.close();
        return rows > 0;
    }

    public List<Order> getAll() {
        List<Order> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM orders ORDER BY order_date DESC",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                order.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                order.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("total_price")));
                order.setOrderDate(cursor.getLong(cursor.getColumnIndexOrThrow("order_date")));
                order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                list.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }


    public List<Order> getAllWithUserName() {
        List<Order> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT o.*, u.fullName AS fullName " +
                "FROM orders o " +
                "JOIN users u ON o.user_id = u.id " +
                "ORDER BY o.order_date DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                order.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                order.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("total_price")));
                order.setOrderDate(cursor.getLong(cursor.getColumnIndexOrThrow("order_date")));
                order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));

                list.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public boolean update(Order order) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_id", order.getUserId());
        values.put("order_date", order.getOrderDate());
        values.put("total_price", order.getTotalPrice());
        values.put("status", order.getStatus());

        int rows = db.update(
                "orders",
                values,
                "id = ?",
                new String[]{String.valueOf(order.getId())}
        );

        db.close();
        return rows > 0;
    }


    public List<Order> getOrdersByStatus(String status) {
        List<Order> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM orders WHERE status = ? ORDER BY order_date DESC",
                new String[]{status}
        );

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order();
                order.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                order.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                order.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("total_price")));
                order.setOrderDate(cursor.getLong(cursor.getColumnIndexOrThrow("order_date")));
                order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                list.add(order);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public Order getById(int orderId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM orders WHERE id = ?",
                new String[]{String.valueOf(orderId)}
        );

        Order order = null;

        if (cursor.moveToFirst()) {
            order = new Order();
            order.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            order.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
            order.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("total_price")));
            order.setOrderDate(cursor.getLong(cursor.getColumnIndexOrThrow("order_date")));
            order.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
        }

        cursor.close();
        db.close();
        return order;
    }

    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT oi.*, b.title " +
                "FROM order_items oi " +
                "JOIN books b ON oi.book_id = b.id " +
                "WHERE oi.order_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});

        if (cursor.moveToFirst()) {
            do {
                OrderItem item = new OrderItem();
                item.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                item.setOrderId(cursor.getInt(cursor.getColumnIndexOrThrow("order_id")));
                item.setBookId(cursor.getInt(cursor.getColumnIndexOrThrow("book_id")));
                item.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
                item.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                item.setBookTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                list.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
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
