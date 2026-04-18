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
    private static final String KEY_USER_AVATAR = "userAvatar";

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

    // ================= LOGIN =================
    public void saveLoginSession(User user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_NAME, user.getFullName());
        editor.putString(KEY_USER_ROLE, user.getRole());

        // SAFE NULL
        editor.putString(KEY_USER_AVATAR,
                user.getAvatar() != null ? user.getAvatar() : "");

        String userJson = gson.toJson(user);
        editor.putString(KEY_USER_DATA, userJson);

        editor.apply();
    }

    // ================= LOGOUT =================
    public void logout() {
        editor.clear();
        editor.apply();
    }

    // ================= CHECK LOGIN =================
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // ================= GET USER (FULL SAFE) =================
    public User getUser() {
        String userJson = pref.getString(KEY_USER_DATA, null);

        if (userJson != null) {
            Type type = new TypeToken<User>() {}.getType();
            User user = gson.fromJson(userJson, type);

            if (user == null) return null;

            if (user.getAvatar() == null || user.getAvatar().isEmpty()) {
                user.setAvatar(pref.getString(KEY_USER_AVATAR, ""));
            }

            if (user.getPhone() == null) {
                user.setPhone("");
            }

            if (user.getAddress() == null) {
                user.setAddress("");
            }

            if (user.getRole() == null) {
                user.setRole("user");
            }

            if (user.getFullName() == null) {
                user.setFullName("");
            }

            if (user.getEmail() == null) {
                user.setEmail("");
            }

            return user;
        }
        return null;
    }

    // ================= ADMIN CHECK =================
    public boolean isAdmin() {
        String role = pref.getString(KEY_USER_ROLE, "user");
        return "admin".equalsIgnoreCase(role);
    }

    // ================= GETTERS =================
    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1);
    }

    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, "");
    }

    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "");
    }

    public String getUserRole() {
        return pref.getString(KEY_USER_ROLE, "user");
    }

    // ================= UPDATE USER =================
    public void updateUserData(User user) {
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_NAME, user.getFullName());
        editor.putString(KEY_USER_ROLE, user.getRole());

        editor.putString(KEY_USER_AVATAR,
                user.getAvatar() != null ? user.getAvatar() : "");

        String userJson = gson.toJson(user);
        editor.putString(KEY_USER_DATA, userJson);

        editor.apply();
    }
}