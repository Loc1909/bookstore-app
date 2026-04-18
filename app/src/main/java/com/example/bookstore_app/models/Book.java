package com.example.bookstore_app.models;

public class Book {

    private int id;
    private String title;
    private String author;
    private int categoryId;
    private double price;
    private String description;
    private String imageUrl;
    private int stock;

    // rating system
    private float rating;        // điểm trung bình
    private int reviewCount;     // số lượt đánh giá

    // ===== Constructor rỗng =====
    public Book() {
    }

    // ===== Constructor đầy đủ =====
    public Book(int id, String title, String author, int categoryId,
                double price, String description, String imageUrl,
                int stock, float rating, int reviewCount) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.categoryId = categoryId;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.stock = stock;
        this.rating = rating;
        this.reviewCount = reviewCount;
    }

    // ===== Constructor đơn giản =====
    public Book(int id, String title, String author,
                double price, String description, String imageUrl) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.rating = 0;
        this.reviewCount = 0;
    }

    // ================= GETTER =================

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getStock() {
        return stock;
    }

    public float getRating() {
        return rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    // ================= SETTER =================

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setPrice(double price) {
        if (price < 0) price = 0;
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setStock(int stock) {
        if (stock < 0) stock = 0;
        this.stock = stock;
    }

    public void setRating(float rating) {
        // giới hạn 0 → 5
        if (rating < 0) rating = 0;
        if (rating > 5) rating = 5;
        this.rating = rating;
    }

    public void setReviewCount(int reviewCount) {
        if (reviewCount < 0) reviewCount = 0;
        this.reviewCount = reviewCount;
    }

    // ================= toString =================

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", categoryId=" + categoryId +
                ", price=" + price +
                ", stock=" + stock +
                ", rating=" + rating +
                ", reviewCount=" + reviewCount +
                '}';
    }
}