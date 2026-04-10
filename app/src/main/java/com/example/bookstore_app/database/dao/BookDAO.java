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

    // ================= INSERT =================
    public void insertBook(Book book){

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_TITLE, book.getTitle());
        values.put(DatabaseHelper.COL_AUTHOR, book.getAuthor());
        values.put(DatabaseHelper.COL_PRICE, book.getPrice());
        values.put(DatabaseHelper.COL_IMAGE, book.getImageUrl());
        values.put(DatabaseHelper.COL_CATEGORY_ID_BOOK, book.getCategoryId());
        values.put(DatabaseHelper.COL_DESCRIPTION, book.getDescription());
        values.put(DatabaseHelper.COL_STOCK, book.getStock());
        values.put(DatabaseHelper.COL_RATING, book.getRating());

        db.insert(DatabaseHelper.TABLE_BOOK, null, values);
        db.close();
    }

    // ================= SEARCH =================
    public List<Book> searchBooks(String keyword, int limit, int offset){

        List<Book> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DatabaseHelper.TABLE_BOOK +
                        " WHERE " + DatabaseHelper.COL_TITLE + " LIKE ? " +
                        "ORDER BY id DESC LIMIT ? OFFSET ?",
                new String[]{
                        "%" + keyword + "%",
                        String.valueOf(limit),
                        String.valueOf(offset)
                }
        );

        if(cursor.moveToFirst()){
            do{
                list.add(mapCursorToBook(cursor));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // ================= PAGING =================
    public List<Book> getBooksPaging(int limit, int offset){

        List<Book> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DatabaseHelper.TABLE_BOOK +
                        " LIMIT ? OFFSET ?",
                new String[]{
                        String.valueOf(limit),
                        String.valueOf(offset)
                }
        );

        if(cursor.moveToFirst()){
            do{
                list.add(mapCursorToBook(cursor));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // ================= FILTER CATEGORY =================
    public List<Book> getBooksByCategory(int categoryId, String keyword, int limit, int offset){

        List<Book> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        StringBuilder sql = new StringBuilder(
                "SELECT * FROM " + DatabaseHelper.TABLE_BOOK +
                        " WHERE " + DatabaseHelper.COL_CATEGORY_ID_BOOK + " = ?"
        );

        List<String> args = new ArrayList<>();
        args.add(String.valueOf(categoryId));

        if(keyword != null && !keyword.trim().isEmpty()){
            sql.append(" AND ").append(DatabaseHelper.COL_TITLE).append(" LIKE ?");
            args.add("%" + keyword.trim() + "%");
        }

        sql.append(" ORDER BY id DESC LIMIT ? OFFSET ?");
        args.add(String.valueOf(limit));
        args.add(String.valueOf(offset));

        Cursor cursor = db.rawQuery(sql.toString(), args.toArray(new String[0]));

        if(cursor.moveToFirst()){
            do{
                list.add(mapCursorToBook(cursor));
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    // ================= GET ALL =================
    public List<Book> getAllBooks(){

        List<Book> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_BOOK, null);

        if(cursor.moveToFirst()){
            do{
                list.add(mapCursorToBook(cursor));
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // ================= ADMIN SEARCH =================
    public List<Book> searchBooksAdmin(String keyword){

        List<Book> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + DatabaseHelper.TABLE_BOOK +
                        " WHERE title LIKE ? OR author LIKE ?",
                new String[]{
                        "%" + keyword + "%",
                        "%" + keyword + "%"
                }
        );

        if(cursor.moveToFirst()){
            do{
                list.add(mapCursorToBook(cursor));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    // ================= DELETE =================
    public void deleteBook(int id){

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_BOOK, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    // ================= GET BY ID =================
    public Book getBookById(int id) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Book book = null;

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_BOOK,
                null,
                "id = ?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        if(cursor != null && cursor.moveToFirst()){
            book = mapCursorToBook(cursor);
            cursor.close();
        }

        db.close();
        return book;
    }

    // ================= UPDATE =================
    public boolean updateBook(Book book){

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_TITLE, book.getTitle());
        values.put(DatabaseHelper.COL_AUTHOR, book.getAuthor());
        values.put(DatabaseHelper.COL_PRICE, book.getPrice());
        values.put(DatabaseHelper.COL_IMAGE, book.getImageUrl());
        values.put(DatabaseHelper.COL_CATEGORY_ID_BOOK, book.getCategoryId());
        values.put(DatabaseHelper.COL_DESCRIPTION, book.getDescription());
        values.put(DatabaseHelper.COL_STOCK, book.getStock());
        values.put(DatabaseHelper.COL_RATING, book.getRating()); // ✅ thêm

        int rows = db.update(
                DatabaseHelper.TABLE_BOOK,
                values,
                "id = ?",
                new String[]{String.valueOf(book.getId())}
        );

        db.close();
        return rows > 0;
    }

    // ================= MAP CURSOR =================
    private Book mapCursorToBook(Cursor cursor){

        Book book = new Book();

        book.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_BOOK_ID)));
        book.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TITLE)));
        book.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_AUTHOR)));
        book.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY_ID_BOOK)));
        book.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PRICE)));
        book.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DESCRIPTION)));
        book.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_IMAGE)));
        book.setStock(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STOCK)));


        if(cursor.getColumnIndex(DatabaseHelper.COL_RATING) != -1){
            book.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RATING)));
        }

        return book;
    }

//    public Book getBookById(int bookId) {
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        Book book = null;
//
//        Cursor cursor = db.rawQuery(
//                "SELECT * FROM books WHERE id = ?",
//                new String[]{String.valueOf(bookId)}
//        );
//
//        if (cursor.moveToFirst()) {
//
//            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
//            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
//            String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
//            double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
//            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
//            String image = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
//            int stock = cursor.getInt(cursor.getColumnIndexOrThrow("stock"));
//            int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("category_id"));
//
//            book = new Book(id, title, author, categoryId, price, description, image, stock);
//        }
//
//        cursor.close();
//        db.close();
//
//        return book;
//    }
}