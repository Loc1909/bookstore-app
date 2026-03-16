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

    // Constructor rỗng (bắt buộc cho Firebase / JSON)
    public Book() {
    }

    // Constructor đầy đủ
    public Book(int id, String title, String author, int categoryId,
                double price, String description, String imageUrl, int stock) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.categoryId = categoryId;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.stock = stock;
    }

    // Constructor dùng cho fake data
    public Book(int id, String title, String author,
                double price, String description, String imageUrl) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }

// Getter và Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", category='" + categoryId + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }

}
