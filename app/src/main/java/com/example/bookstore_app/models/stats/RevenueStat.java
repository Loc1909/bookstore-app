package com.example.bookstore_app.models.stats;

public class RevenueStat {
    private String month;
    private double revenue;

    public RevenueStat(String month, double revenue) {
        this.month = month;
        this.revenue = revenue;
    }

    public String getMonth() {
        return month;
    }

    public double getRevenue() {
        return revenue;
    }
}