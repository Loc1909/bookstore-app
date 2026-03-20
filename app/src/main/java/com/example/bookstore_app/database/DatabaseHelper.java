package com.example.bookstore_app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bookstore.db";
    private static final int DATABASE_VERSION = 5;

    // TABLE NAMES
    public static final String TABLE_BOOK = "books";
    public static final String TABLE_CATEGORY = "categories";
    public static final String TABLE_USER = "users";

    // BOOK COLUMNS
    public static final String COL_BOOK_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_AUTHOR = "author";
    public static final String COL_PRICE = "price";
    public static final String COL_IMAGE = "imageUrl";
    public static final String COL_CATEGORY_ID = "category_id";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_STOCK = "stock";

    // CATEGORY COLUMNS
    public static final String COL_CATEGORY_NAME = "name";

    // USER COLUMNS
    public static final String COL_USER_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_EMAIL = "email";
    public static final String COL_FULLNAME = "fullname";
    public static final String COL_PHONE = "phone";
    public static final String COL_ROLE = "role";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private void insertDefaultCategories(SQLiteDatabase db){
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_CATEGORY + "(name) VALUES ('Programming')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_CATEGORY + "(name) VALUES ('Novel')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_CATEGORY + "(name) VALUES ('Science')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_CATEGORY + "(name) VALUES ('Business')");
    }

    private void insertDefaultAdmin(SQLiteDatabase db){
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_USER + "(username, password, email, fullname, role) " +
                "VALUES ('admin', 'admin', 'admin@bookstore.com', 'System Administrator', 'Admin')");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CATEGORY_TABLE =
                "CREATE TABLE " + TABLE_CATEGORY + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT UNIQUE)";
        db.execSQL(CREATE_CATEGORY_TABLE);

        // Insert default categories
        insertDefaultCategories(db);

        String CREATE_BOOK_TABLE =
                "CREATE TABLE " + TABLE_BOOK + " (" +
                        COL_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COL_TITLE + " TEXT," +
                        COL_AUTHOR + " TEXT," +
                        COL_PRICE + " REAL," +
                        COL_IMAGE + " TEXT," +
                        COL_CATEGORY_ID + " INTEGER," +
                        COL_DESCRIPTION + " TEXT," +
                        COL_STOCK + " INTEGER," +
                        "FOREIGN KEY(" + COL_CATEGORY_ID + ") REFERENCES "
                        + TABLE_CATEGORY + "(id)" +
                        ")";
        db.execSQL(CREATE_BOOK_TABLE);

        String CREATE_USER_TABLE =
                "CREATE TABLE " + TABLE_USER + " (" +
                        COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COL_USERNAME + " TEXT UNIQUE," +
                        COL_PASSWORD + " TEXT," +
                        COL_EMAIL + " TEXT," +
                        COL_FULLNAME + " TEXT," +
                        COL_PHONE + " TEXT," +
                        COL_ROLE + " TEXT)";
        db.execSQL(CREATE_USER_TABLE);
        
        // Insert default admin
        insertDefaultAdmin(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }
}