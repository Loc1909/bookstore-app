package com.example.bookstore_app.models.stats;

public class TopProduct {
    private String bookName;
    private int totalSold;

    public TopProduct(String bookName, int totalSold) {
        this.bookName = bookName;
        this.totalSold = totalSold;
    }

    public String getBookName() { return bookName; }
    public int getTotalSold() { return totalSold; }
}