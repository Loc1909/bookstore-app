package com.example.bookstore_app.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bookstore_app.database.DatabaseHelper;
import com.example.bookstore_app.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private DatabaseHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_USER, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USERNAME));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PASSWORD));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL));
                String fullname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FULLNAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PHONE));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ROLE));

                list.add(new User(id, username, password, email, fullname, phone, role));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    public boolean addUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_USERNAME, user.getUsername());
        values.put(DatabaseHelper.COL_PASSWORD, user.getPassword());
        values.put(DatabaseHelper.COL_EMAIL, user.getEmail());
        values.put(DatabaseHelper.COL_FULLNAME, user.getFullname());
        values.put(DatabaseHelper.COL_PHONE, user.getPhone());
        values.put(DatabaseHelper.COL_ROLE, user.getRole());

        long result = db.insert(DatabaseHelper.TABLE_USER, null, values);
        db.close();
        return result != -1;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_USERNAME, user.getUsername());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            values.put(DatabaseHelper.COL_PASSWORD, user.getPassword());
        }
        values.put(DatabaseHelper.COL_EMAIL, user.getEmail());
        values.put(DatabaseHelper.COL_FULLNAME, user.getFullname());
        values.put(DatabaseHelper.COL_PHONE, user.getPhone());
        values.put(DatabaseHelper.COL_ROLE, user.getRole());

        int result = db.update(DatabaseHelper.TABLE_USER, values, DatabaseHelper.COL_USER_ID + "=?", new String[]{String.valueOf(user.getId())});
        db.close();
        return result > 0;
    }

    public boolean deleteUser(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete(DatabaseHelper.TABLE_USER, DatabaseHelper.COL_USER_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }
}
