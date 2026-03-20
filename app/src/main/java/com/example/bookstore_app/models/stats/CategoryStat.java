package com.example.bookstore_app.models.stats;

public class CategoryStat {
    private String categoryName;
    private int totalSold;

    public CategoryStat(String categoryName, int totalSold) {
        this.categoryName = categoryName;
        this.totalSold = totalSold;
    }

    public String getCategoryName() { return categoryName; }
    public int getTotalSold() { return totalSold; }
}