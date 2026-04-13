package com.example.bookstore_app.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bookstore_app.database.DatabaseHelper;
import com.example.bookstore_app.models.User;
import com.example.bookstore_app.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private DatabaseHelper dbHelper;

    public UserDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // ================= REGISTER =================
    public boolean register(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();

        try {
            if (isEmailExists(db, user.getEmail())) {
                return false;
            }

            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COL_EMAIL, user.getEmail());
            values.put(DatabaseHelper.COL_PASSWORD, user.getPassword());
            values.put(DatabaseHelper.COL_FULL_NAME, user.getFullName());
            values.put(DatabaseHelper.COL_PHONE, user.getPhone());
            values.put(DatabaseHelper.COL_ADDRESS, user.getAddress());
            values.put(DatabaseHelper.COL_ROLE, user.getRole() != null ? user.getRole() : "user");
            values.put(DatabaseHelper.COL_AVATAR, user.getAvatar());
            values.put(DatabaseHelper.COL_CREATED_AT, System.currentTimeMillis());

            long result = db.insert(DatabaseHelper.TABLE_USER, null, values);
            if (result == -1) {
                return false;
            }
            ContentValues v2 = new ContentValues();
            v2.put(DatabaseHelper.COL_CART_USER_ID, result);
            long result2 = db.insert(DatabaseHelper.TABLE_CART, null, v2);
            if (result2 == -1) {
                return false;
            }
            db.setTransactionSuccessful();
            return true;
        }finally {
            db.endTransaction();
            db.close();
        }
    }

    // ================= LOGIN =================
    public User login(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DatabaseHelper.TABLE_USER +
                        " WHERE " + DatabaseHelper.COL_EMAIL + "=? AND " +
                        DatabaseHelper.COL_PASSWORD + "=?",
                new String[]{email, password}
        );

        User user = null;

        if (cursor != null && cursor.moveToFirst()) {
            user = cursorToUser(cursor);
            cursor.close();
        }

        db.close();
        return user;
    }

    // ================= GET BY ID =================
    public User getUserById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DatabaseHelper.TABLE_USER +
                        " WHERE " + DatabaseHelper.COL_USER_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        User user = null;

        if (cursor != null && cursor.moveToFirst()) {
            user = cursorToUser(cursor);
            cursor.close();
        }

        db.close();
        return user;
    }


    // ================= GET BY EMAIL =================
    public User getUserByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DatabaseHelper.TABLE_USER +
                        " WHERE " + DatabaseHelper.COL_EMAIL + "=?",
                new String[]{email}
        );

        User user = null;

        if (cursor != null && cursor.moveToFirst()) {
            user = cursorToUser(cursor);
            cursor.close();
        }

        db.close();
        return user;
    }

    // ================= CHECK EMAIL =================
    private boolean isEmailExists(SQLiteDatabase db, String email) {
        Cursor cursor = db.rawQuery(
                "SELECT 1 FROM " + DatabaseHelper.TABLE_USER +
                        " WHERE " + DatabaseHelper.COL_EMAIL + "=? LIMIT 1",
                new String[]{email}
        );

        boolean exists = cursor.moveToFirst();
        cursor.close();

        return exists;
    }

    // ================= GET ALL =================
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DatabaseHelper.TABLE_USER +
                        " ORDER BY " + DatabaseHelper.COL_CREATED_AT + " DESC",
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(cursorToUser(cursor));
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return list;
    }



    // ================= UPDATE =================
    public boolean updateUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_FULL_NAME, user.getFullName());
        values.put(DatabaseHelper.COL_PHONE, user.getPhone());
        values.put(DatabaseHelper.COL_ADDRESS, user.getAddress());
        values.put(DatabaseHelper.COL_ROLE, user.getRole());
        values.put(DatabaseHelper.COL_AVATAR, user.getAvatar());

        // chỉ update password nếu có
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            values.put(DatabaseHelper.COL_PASSWORD, user.getPassword());
        }

        int result = db.update(
                DatabaseHelper.TABLE_USER,
                values,
                DatabaseHelper.COL_USER_ID + "=?",
                new String[]{String.valueOf(user.getId())}
        );

        db.close();
        return result > 0;
    }


    public boolean updateAvatar(int userId, String avatarPath) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_AVATAR, avatarPath);

        int result = db.update(
                DatabaseHelper.TABLE_USER,
                values,
                DatabaseHelper.COL_USER_ID + "=?",
                new String[]{String.valueOf(userId)}
        );

        db.close();
        return result > 0;
    }





    // ================= DELETE =================
    public boolean deleteUser(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int result = db.delete(
                DatabaseHelper.TABLE_USER,
                DatabaseHelper.COL_USER_ID + "=?",
                new String[]{String.valueOf(id)}
        );

        db.close();
        return result > 0;
    }

    // ================= CURSOR → USER =================
    private User cursorToUser(Cursor cursor) {
        User user = new User();

        user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID)));
        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL)));
        user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PASSWORD)));
        user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FULL_NAME)));

        int phoneIndex = cursor.getColumnIndex(DatabaseHelper.COL_PHONE);
        if (phoneIndex != -1) {
            user.setPhone(cursor.getString(phoneIndex));
        }

        int addressIndex = cursor.getColumnIndex(DatabaseHelper.COL_ADDRESS);
        if (addressIndex != -1) {
            user.setAddress(cursor.getString(addressIndex));
        }

        user.setRole(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ROLE)));

        int avatarIndex = cursor.getColumnIndex(DatabaseHelper.COL_AVATAR);
        if (avatarIndex != -1) {
            user.setAvatar(cursor.getString(avatarIndex));
        }

        user.setCreatedAt(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CREATED_AT)));

        return user;
    }

}