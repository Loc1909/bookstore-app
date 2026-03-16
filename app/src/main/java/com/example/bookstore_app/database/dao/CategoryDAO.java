package com.example.bookstore_app.database.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bookstore_app.database.DatabaseHelper;
import com.example.bookstore_app.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    DatabaseHelper dbHelper;

    public CategoryDAO(Context context){
        dbHelper = new DatabaseHelper(context);
    }

    public List<Category> getAllCategories(){

        List<Category> list = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM categories", null);

        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));

                list.add(new Category(id, name));

            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }
}