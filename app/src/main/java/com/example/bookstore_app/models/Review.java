package com.example.bookstore_app.models;

public class Review {

    private int id;
    private int bookId;
    private int userId;

    private float rating;      // 1.0 - 5.0
    private String comment;
    private String createdAt;

    //  thêm để hiển thị UI (không bắt buộc lưu DB)
    private String userName;
    private String userAvatar;

    // ===== Constructor rỗng =====
    public Review() {
    }

    // ===== Constructor đầy đủ (DB + JOIN) =====
    public Review(int id, int bookId, int userId,
                  float rating, String comment, String createdAt,
                  String userName, String userAvatar) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.userName = userName;
        this.userAvatar = userAvatar;
    }

    // ===== Constructor insert =====
    public Review(int bookId, int userId,
                  float rating, String comment, String createdAt) {
        this.bookId = bookId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // ================= GETTER / SETTER =================

    public int getId() {
        return id;
    }

    public int getBookId() {
        return bookId;
    }

    public int getUserId() {
        return userId;
    }

    public float getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setRating(float rating) {
        //  validate tránh lỗi data
        if (rating < 1) rating = 1;
        if (rating > 5) rating = 5;
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", userId=" + userId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}