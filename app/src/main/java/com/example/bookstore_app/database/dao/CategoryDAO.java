package com.example.bookstore_app.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bookstore_app.database.DatabaseHelper;
import com.example.bookstore_app.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    private DatabaseHelper dbHelper;

    public CategoryDAO(Context context){
        dbHelper = new DatabaseHelper(context);
    }

    public List<Category> getAllCategories(){
        List<Category> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_CATEGORY, null);

        if(cursor != null && cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                list.add(new Category(id, name));
            } while(cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return list;
    }

    public String getCategoryNameById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String name = "Unknown";

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_CATEGORY,
                new String[]{"name"},
                "id=?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            cursor.close();
        }
        db.close();
        return name;
    }

    public boolean addCategory(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", category.getName());

        long result = db.insert(DatabaseHelper.TABLE_CATEGORY, null, values);

        db.close();
        return result != -1;
    }

    public boolean updateCategory(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", category.getName());

        int result = db.update(
                DatabaseHelper.TABLE_CATEGORY,
                values,
                "id=?",
                new String[]{String.valueOf(category.getId())}
        );

        db.close();
        return result > 0;
    }

    public boolean deleteCategory(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int result = db.delete(
                DatabaseHelper.TABLE_CATEGORY,
                "id=?",
                new String[]{String.valueOf(id)}
        );

        db.close();
        return result > 0;
    }
}