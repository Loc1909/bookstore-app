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

    /**
     * Đăng ký tài khoản mới
     * @param user Đối tượng User cần đăng ký
     * @return true nếu đăng ký thành công, false nếu email đã tồn tại
     */
    public boolean register(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // check email tồn tại trước (tránh rely vào UNIQUE exception)
        if (isEmailExists(user.getEmail())) {
            db.close();
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
        db.close();

        return result != -1;
    }

    /**
     * Đăng nhập
     * @param email Email đăng nhập
     * @param password Mật khẩu
     * @return User object nếu đăng nhập thành công, null nếu thất bại
     */
    public User login(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_USER +
                " WHERE " + DatabaseHelper.COL_EMAIL + " = ? AND " +
                DatabaseHelper.COL_PASSWORD + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        User user = null;

        if (cursor.moveToFirst()) {
            user = cursorToUser(cursor);
        }

        cursor.close();
        db.close();

        return user;
    }

    /**
     * Lấy user theo ID
     */
    public User getUserById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_USER +
                " WHERE " + DatabaseHelper.COL_USER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        User user = null;

        if (cursor.moveToFirst()) {
            user = cursorToUser(cursor);
        }

        cursor.close();
        db.close();

        return user;
    }

    /**
     * Lấy user theo email
     */
    public User getUserByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_USER +
                " WHERE " + DatabaseHelper.COL_EMAIL + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email});

        User user = null;

        if (cursor.moveToFirst()) {
            user = cursorToUser(cursor);
        }

        cursor.close();
        db.close();

        return user;
    }

    /**
     * Kiểm tra email đã tồn tại chưa
     */
    public boolean isEmailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_USER +
                " WHERE " + DatabaseHelper.COL_EMAIL + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{email});

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return count > 0;
    }

    /**
     * Lấy tất cả users (cho admin)
     */
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_USER +
                " ORDER BY " + DatabaseHelper.COL_CREATED_AT + " DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                userList.add(cursorToUser(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return userList;
    }

    /**
     * Cập nhật thông tin user
     */
    public boolean updateUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_EMAIL, user.getEmail());
        values.put(DatabaseHelper.COL_PASSWORD, user.getPassword());
        values.put(DatabaseHelper.COL_FULL_NAME, user.getFullName());
        values.put(DatabaseHelper.COL_PHONE, user.getPhone());
        values.put(DatabaseHelper.COL_ADDRESS, user.getAddress());
        values.put(DatabaseHelper.COL_ROLE, user.getRole());
        values.put(DatabaseHelper.COL_AVATAR, user.getAvatar());

        int rowsAffected = db.update(DatabaseHelper.TABLE_USER,
                values,
                DatabaseHelper.COL_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});

        db.close();
        return rowsAffected > 0;
    }

    /**
     * Xóa user (chỉ admin)
     */
    public boolean deleteUser(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rowsAffected = db.delete(DatabaseHelper.TABLE_USER,
                DatabaseHelper.COL_USER_ID + " = ?",
                new String[]{String.valueOf(id)});

        db.close();
        return rowsAffected > 0;
    }

    /**
     * Chuyển đổi Cursor thành User object
     */
    private User cursorToUser(Cursor cursor) {
        User user = new User();

        user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_USER_ID)));
        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EMAIL)));
        user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PASSWORD)));
        user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_FULL_NAME)));

        // Các field có thể null
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