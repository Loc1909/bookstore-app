package com.example.bookstore_app.models;

public class User {

    private int id;
    private String username;
    private String email;
    private String password; // Trong thực tế nên hash, nhưng demo thì để plain
    private String fullName;
    private String phone;
    private String address;
    private String role; // "admin" hoặc "user"
    private String avatar;
    private long createdAt;

    // Constructor rỗng (bắt buộc cho SQLite)
    public User() {
    }
    public User() {}

    // Constructor cho đăng ký
    public User(String email, String password, String fullName, String role) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.createdAt = System.currentTimeMillis();
    }

    // Constructor đầy đủ
    public User(int id, String email, String password, String fullName,
                String phone, String address, String role, String avatar, long createdAt) {
    public User(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.avatar = avatar;
        this.createdAt = createdAt;
    }

    // GETTERS và SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() {
        return fullName;
    }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}