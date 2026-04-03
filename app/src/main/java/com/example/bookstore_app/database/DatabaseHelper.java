package com.example.bookstore_app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bookstore.db";
    private static final int DATABASE_VERSION = 6;

    // TABLES
    public static final String TABLE_BOOK = "books";
    public static final String TABLE_CATEGORY = "categories";
    public static final String TABLE_USER = "users"; // THÊM MỚI

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

    public static final String TABLE_ORDER = "orders";
    public static final String TABLE_ORDER_ITEM = "order_items";

    // USER COLUMNS - THÊM MỚI
    public static final String COL_USER_ID = "id";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_FULL_NAME = "fullName";
    public static final String COL_PHONE = "phone";
    public static final String COL_ADDRESS = "address";
    public static final String COL_ROLE = "role";
    public static final String COL_AVATAR = "avatar";
    public static final String COL_CREATED_AT = "createdAt";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private void insertDefaultCategories(SQLiteDatabase db){
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_CATEGORY + "(name) VALUES ('Programming')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_CATEGORY + "(name) VALUES ('Novel')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_CATEGORY + "(name) VALUES ('Science')");
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_CATEGORY + "(name) VALUES ('Business')");
    }

    private void insertDefaultAdmin(SQLiteDatabase db) { // THÊM MỚI
        // Tạo tài khoản admin mặc định
        String insertAdmin = "INSERT OR IGNORE INTO " + TABLE_USER + "(" +
                COL_EMAIL + ", " +
                COL_PASSWORD + ", " +
                COL_FULL_NAME + ", " +
                COL_ROLE + ", " +
                COL_CREATED_AT + ") VALUES (" +
                "'admin@bookstore.com', " + // email
                "'admin123', " + // password
                "'Administrator', " + // fullName
                "'admin', " + // role
                + System.currentTimeMillis() + ")"; // createdAt

        db.execSQL(insertAdmin);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Tạo bảng categories
        String CREATE_CATEGORY_TABLE =
                "CREATE TABLE " + TABLE_CATEGORY + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT UNIQUE)";
        db.execSQL(CREATE_CATEGORY_TABLE);
        // ================= CATEGORY =================
        db.execSQL("CREATE TABLE " + TABLE_CATEGORY + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT UNIQUE)");

        // ================= BOOK =================
        db.execSQL("CREATE TABLE " + TABLE_BOOK + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "author TEXT," +
                "price REAL," +
                "imageUrl TEXT," +
                "category_id INTEGER," +
                "description TEXT," +
                "stock INTEGER," +
                "FOREIGN KEY(category_id) REFERENCES categories(id))");

        // ================= USER =================
        db.execSQL("CREATE TABLE " + TABLE_USER + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "email TEXT)");

        // ================= ORDER =================
        db.execSQL("CREATE TABLE " + TABLE_ORDER + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "order_date TEXT," +
                "FOREIGN KEY(user_id) REFERENCES users(id))");

        // ================= ORDER ITEM =================
        db.execSQL("CREATE TABLE " + TABLE_ORDER_ITEM + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "order_id INTEGER," +
                "book_id INTEGER," +
                "quantity INTEGER," +
                "price REAL," +
                "FOREIGN KEY(order_id) REFERENCES orders(id)," +
                "FOREIGN KEY(book_id) REFERENCES books(id))");

        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {

        // Categories
        db.execSQL("INSERT INTO categories(name) VALUES ('Programming')");
        db.execSQL("INSERT INTO categories(name) VALUES ('Novel')");
        db.execSQL("INSERT INTO categories(name) VALUES ('Science')");

        // Books
        db.execSQL("INSERT INTO books(title,author,price,category_id,stock) VALUES ('Java Basics','John',100,1,10)");
        db.execSQL("INSERT INTO books(title,author,price,category_id,stock) VALUES ('Android Dev','Mike',150,1,8)");
        db.execSQL("INSERT INTO books(title,author,price,category_id,stock) VALUES ('Love Story','Anna',80,2,5)");

        // Tạo bảng books
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

        // Tạo bảng users - THÊM MỚI
        String CREATE_USER_TABLE =
                "CREATE TABLE " + TABLE_USER + " (" +
                        COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COL_EMAIL + " TEXT UNIQUE NOT NULL," +
                        COL_PASSWORD + " TEXT NOT NULL," +
                        COL_FULL_NAME + " TEXT NOT NULL," +
                        COL_PHONE + " TEXT," +
                        COL_ADDRESS + " TEXT," +
                        COL_ROLE + " TEXT DEFAULT 'user'," +
                        COL_AVATAR + " TEXT," +
                        COL_CREATED_AT + " INTEGER" +
                        ")";
        db.execSQL(CREATE_USER_TABLE);

        // Insert admin mặc định - THÊM MỚI
        insertDefaultAdmin(db);
        // Users
        db.execSQL("INSERT INTO users(username,email) VALUES ('kien','kien@gmail.com')");
        db.execSQL("INSERT INTO users(username,email) VALUES ('an','an@gmail.com')");

        // Orders
        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (1,'2026-03-01')");
        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (2,'2026-03-02')");

        // Order Items
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (1,1,2,100)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (1,2,1,150)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (2,3,3,80)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ và tạo lại
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER); // THÊM MỚI

        db.execSQL("DROP TABLE IF EXISTS order_items");
        db.execSQL("DROP TABLE IF EXISTS orders");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS books");
        db.execSQL("DROP TABLE IF EXISTS categories");

        onCreate(db);
    }
}