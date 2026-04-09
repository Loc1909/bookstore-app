package com.example.bookstore_app.models;

public class CartItem {
    public int id;
    public int bookId;
    public String title;
    public double price;
    public int quantity;
    public String imageUrl;

    public CartItem() {
    }

    public CartItem(int id, int bookId, String title, double price, int quantity, String imageUrl) {
        this.id = id;
        this.bookId = bookId;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
