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

    public List<Book> searchBooks(String keyword, int limit, int offset){

        List<Book> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM books WHERE title LIKE ? ORDER BY id DESC LIMIT ? OFFSET ?",
                new String[]{
                        "%" + keyword + "%",
                        String.valueOf(limit),
                        String.valueOf(offset)
                }
        );

        if(cursor.moveToFirst()){
            do{
                Book book = new Book(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("author")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("category_id")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getString(cursor.getColumnIndexOrThrow("imageUrl")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("stock"))
                );
                list.add(book);
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }



    public List<Book> getBooksPaging(int limit, int offset){

        List<Book> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM books LIMIT ? OFFSET ?",
                new String[]{
                        String.valueOf(limit),
                        String.valueOf(offset)
                }
        );

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

            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    public List<Book> getBooksByCategory(int categoryId, String keyword, int limit, int offset){

        List<Book> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        StringBuilder sql = new StringBuilder("SELECT * FROM books WHERE category_id = ?");
        List<String> argsList = new ArrayList<>();

        argsList.add(String.valueOf(categoryId));

        // nếu có keyword thì thêm điều kiện
        if(keyword != null && !keyword.trim().isEmpty()){
            sql.append(" AND title LIKE ?");
            argsList.add("%" + keyword.trim() + "%");
        }

        sql.append(" ORDER BY id DESC LIMIT ? OFFSET ?");
        argsList.add(String.valueOf(limit));
        argsList.add(String.valueOf(offset));

        Cursor cursor = db.rawQuery(sql.toString(), argsList.toArray(new String[0]));

        if(cursor.moveToFirst()){
            do{
                Book book = new Book(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("author")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("category_id")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getString(cursor.getColumnIndexOrThrow("imageUrl")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("stock"))
                );
                list.add(book);

            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
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

    public List<Book> searchBooksAdmin(String keyword){

        List<Book> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM books WHERE title LIKE ? OR author LIKE ?",
                new String[]{
                        "%" + keyword + "%",
                        "%" + keyword + "%"
                }
        );

        if(cursor.moveToFirst()){
            do{
                Book book = new Book();

                book.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                book.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                book.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow("author")));
                book.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow("category_id")));
                book.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                book.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                book.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow("imageUrl")));
                book.setStock(cursor.getInt(cursor.getColumnIndexOrThrow("stock")));

                list.add(book);

            } while(cursor.moveToNext());
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

    public Book getBookById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Book book = null;

        Cursor cursor = db.query(
                "books",                  // tên bảng
                null,                     // tất cả cột
                "id = ?",                 // điều kiện WHERE
                new String[]{String.valueOf(id)}, // giá trị tham số
                null, null, null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                book = new Book();
                book.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                book.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                book.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow("author")));
                book.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                book.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow("imageUrl")));
                book.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow("category_id")));
                book.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                book.setStock(cursor.getInt(cursor.getColumnIndexOrThrow("stock")));
            }
            cursor.close();
        }

        db.close();
        return book;
    }

    // UPDATE BOOK
    public boolean updateBook(Book book){

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", book.getTitle());
        values.put("author", book.getAuthor());
        values.put("price", book.getPrice());
        values.put("imageUrl", book.getImageUrl());
        values.put("category_id", book.getCategoryId());
        values.put("description", book.getDescription());
        values.put("stock", book.getStock());

        int rows = db.update("books", values, "id = ?", new String[]{String.valueOf(book.getId())});
        return rows > 0;
    }
}