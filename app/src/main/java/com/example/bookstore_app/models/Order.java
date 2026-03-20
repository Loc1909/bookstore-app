package com.example.bookstore_app.models;

public class Order {
    private int id;
    private int userId;
    private String orderDate; // yyyy-MM-dd

    public Order() {}

    public Order(int id, int userId, String orderDate) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
}