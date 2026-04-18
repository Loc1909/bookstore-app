package com.example.bookstore_app.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bookstore_app.database.DatabaseHelper;
import com.example.bookstore_app.models.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    private DatabaseHelper dbHelper;

    public ReviewDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public boolean hasReviewed(int userId, int bookId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT COUNT(*) FROM reviews WHERE user_id=? AND book_id=?",
                new String[]{String.valueOf(userId), String.valueOf(bookId)}
        );

        boolean result = false;
        if (c.moveToFirst()) {
            result = c.getInt(0) > 0;
        }

        c.close();
        return result;
    }

    public boolean insertOrUpdate(Review r) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_id", r.getUserId());
        values.put("book_id", r.getBookId());
        values.put("rating", r.getRating());
        values.put("comment", r.getComment());
        values.put("created_at", System.currentTimeMillis());

        boolean success;

        if (hasReviewed(r.getUserId(), r.getBookId())) {
            int rows = db.update("reviews", values,
                    "user_id=? AND book_id=?",
                    new String[]{
                            String.valueOf(r.getUserId()),
                            String.valueOf(r.getBookId())
                    });
            success = rows > 0;
        } else {
            long id = db.insert("reviews", null, values);
            success = id != -1;
        }

        updateBookRating(r.getBookId());
        return success;
    }

    public Review getReviewByUserAndBook(int userId, int bookId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM reviews WHERE user_id=? AND book_id=?",
                new String[]{String.valueOf(userId), String.valueOf(bookId)}
        );

        Review r = null;

        if (c.moveToFirst()) {
            r = new Review();
            r.setId(c.getInt(0));
            r.setUserId(c.getInt(1));
            r.setBookId(c.getInt(2));
            r.setRating(c.getFloat(3));
            r.setComment(c.getString(4));
            r.setCreatedAt(String.valueOf(c.getLong(5)));
        }

        c.close();
        return r;
    }

    private void updateBookRating(int bookId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL(
                "UPDATE books SET rating = (" +
                        "SELECT AVG(rating) FROM reviews WHERE book_id=?" +
                        ") WHERE id=?",
                new Object[]{bookId, bookId}
        );
    }

    public List<Review> getReviewsByBook(int bookId) {
        List<Review> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql = "SELECT r.*, u.fullName, u.avatar " +
                "FROM reviews r " +
                "JOIN users u ON r.user_id = u.id " +
                "WHERE r.book_id=? " +
                "ORDER BY r.created_at DESC";

        Cursor c = db.rawQuery(sql, new String[]{String.valueOf(bookId)});

        while (c.moveToNext()) {
            Review r = new Review();

            r.setId(c.getInt(0));
            r.setUserId(c.getInt(1));
            r.setBookId(c.getInt(2));
            r.setRating(c.getFloat(3));
            r.setComment(c.getString(4));

            long time = c.getLong(5);
            r.setCreatedAt(String.valueOf(time));

            r.setUserName(c.getString(6));
            r.setUserAvatar(c.getString(7));

            list.add(r);
        }

        c.close();
        return list;
    }

    public int getReviewCount(int bookId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT COUNT(*) FROM reviews WHERE book_id=?",
                new String[]{String.valueOf(bookId)}
        );

        int count = 0;
        if (c.moveToFirst()) {
            count = c.getInt(0);
        }

        c.close();
        return count;
    }

    public float getAverageRating(int bookId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT AVG(rating) FROM reviews WHERE book_id=?",
                new String[]{String.valueOf(bookId)}
        );

        float avg = 0;
        if (c.moveToFirst()) {
            avg = c.getFloat(0);
        }

        c.close();
        return avg;
    }
}