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

    DatabaseHelper dbHelper;

    public BookDAO(Context context){
        dbHelper = new DatabaseHelper(context);
    }

    public void insertBook(Book book){

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", book.getTitle());
        values.put("author", book.getAuthor());
        values.put("price", book.getPrice());
        values.put("imageUrl", book.getImageUrl());

        db.insert("books", null, values);
        db.close();
    }

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

                list.add(new Book(id, title, author, "", price, "", imageUrl, 0));

            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    public void deleteBook(int id){

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete("books","id=?",new String[]{String.valueOf(id)});

        db.close();
    }

    public void updateBook(Book book){

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", book.getTitle());
        values.put("author", book.getAuthor());
        values.put("price", book.getPrice());
        values.put("imageUrl", book.getImageUrl());

        db.update("books", values, "id=?", new String[]{String.valueOf(book.getId())});

        db.close();
    }
}