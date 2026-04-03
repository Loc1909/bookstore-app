package com.example.bookstore_app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bookstore_app.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class SessionManager {

    private static final String PREF_NAME = "BookStoreSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_DATA = "userData";
    private static final String KEY_USER_ROLE = "userRole";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME = "userName";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private Gson gson;

    public SessionManager(Context context) {
        this.context = context;
        this.pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.editor = pref.edit();
        this.gson = new Gson();
    }

    /**
     * Lưu session khi đăng nhập thành công
     */
    public void saveLoginSession(User user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_NAME, user.getFullName());
        editor.putString(KEY_USER_ROLE, user.getRole());

        // Lưu toàn bộ object User dưới dạng JSON
        String userJson = gson.toJson(user);
        editor.putString(KEY_USER_DATA, userJson);

        editor.apply();
    }

    /**
     * Xóa session khi đăng xuất
     */
    public void logout() {
        editor.clear();
        editor.apply();
    }

    /**
     * Kiểm tra đã đăng nhập chưa
     */
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Lấy toàn bộ thông tin User
     */
    public User getUser() {
        String userJson = pref.getString(KEY_USER_DATA, null);
        if (userJson != null) {
            Type type = new TypeToken<User>() {}.getType(); // ← Dòng 73 đã sửa
            return gson.fromJson(userJson, type);
        }
        return null;
    }

    /**
     * Kiểm tra có phải admin không
     */
    public boolean isAdmin() {
        String role = pref.getString(KEY_USER_ROLE, "user");
        return "admin".equals(role);
    }

    /**
     * Lấy User ID
     */
    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1);
    }

    /**
     * Lấy User Email
     */
    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, null);
    }

    /**
     * Lấy User Name
     */
    public String getUserName() {
        return pref.getString(KEY_USER_NAME, null);
    }

    /**
     * Lấy User Role
     */
    public String getUserRole() {
        return pref.getString(KEY_USER_ROLE, "user");
    }

    /**
     * Cập nhật thông tin user sau khi chỉnh sửa
     */
    public void updateUserData(User user) {
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_NAME, user.getFullName());
        editor.putString(KEY_USER_ROLE, user.getRole());

        String userJson = gson.toJson(user);
        editor.putString(KEY_USER_DATA, userJson);

        editor.apply();
    }
}