package com.example.bookstore_app.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bookstore_app.database.DatabaseHelper;
import com.example.bookstore_app.models.Book;

import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    private DatabaseHelper dbHelper;

    public BookDAO(Context context){
        dbHelper = new DatabaseHelper(context);
    }

    // INSERT BOOK
    public void insertBook(Book book){

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", book.getTitle());
        values.put("author", book.getAuthor());
        values.put("price", book.getPrice());
        values.put("imageUrl", book.getImageUrl());
        values.put("category_id", book.getCategoryId());
        values.put("description", book.getDescription());
        values.put("stock", book.getStock());

        db.insert("books", null, values);
        db.close();
    }

    // GET ALL BOOKS
    public List<Book> getAllBooks(){

        List<Book> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM books", null);

        if(cursor.moveToFirst()){
            do{

                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
                int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("category_id"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                int stock = cursor.getInt(cursor.getColumnIndexOrThrow("stock"));

                Book book = new Book(id, title, author, categoryId, price, description, imageUrl, stock);

                list.add(book);

            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    // DELETE BOOK
    public void deleteBook(int id){

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete("books","id=?",new String[]{String.valueOf(id)});

        db.close();
    }

    // UPDATE BOOK
    public void updateBook(Book book){

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", book.getTitle());
        values.put("author", book.getAuthor());
        values.put("price", book.getPrice());
        values.put("imageUrl", book.getImageUrl());
        values.put("category_id", book.getCategoryId());
        values.put("description", book.getDescription());
        values.put("stock", book.getStock());

        db.update("books", values, "id=?", new String[]{String.valueOf(book.getId())});

        db.close();
    }

    public Book getBookById(int bookId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Book book = null;

        Cursor cursor = db.rawQuery(
                "SELECT * FROM books WHERE id = ?",
                new String[]{String.valueOf(bookId)}
        );

        if (cursor.moveToFirst()) {

            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String image = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
            int stock = cursor.getInt(cursor.getColumnIndexOrThrow("stock"));
            int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("category_id"));

            book = new Book(id, title, author, categoryId, price, description, image, stock);
        }

        cursor.close();
        db.close();

        return book;
    }
}