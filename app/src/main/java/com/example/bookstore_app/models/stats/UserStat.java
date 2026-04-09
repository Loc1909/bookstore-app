package com.example.bookstore_app.models.stats;

public class UserStat {
    private String username;
    private double totalSpent;

    public UserStat(String username, double totalSpent) {
        this.username = username;
        this.totalSpent = totalSpent;
    }

    public String getUsername() { return username; }
    public double getTotalSpent() { return totalSpent; }
}