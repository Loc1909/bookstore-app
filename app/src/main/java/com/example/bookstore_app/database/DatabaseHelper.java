package com.example.bookstore_app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bookstore.db";
    private static final int DATABASE_VERSION = 7; // Tăng version để cập nhật lại dữ liệu

    // TABLE NAMES
    public static final String TABLE_BOOK = "books";
    public static final String TABLE_CATEGORY = "categories";
    public static final String TABLE_USER = "users";
    public static final String TABLE_CART = "cart";

    // BOOK COLUMNS
    public static final String COL_BOOK_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_AUTHOR = "author";
    public static final String COL_PRICE = "price";
    public static final String COL_IMAGE = "imageUrl";
    public static final String COL_CATEGORY_ID = "category_id";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_STOCK = "stock";
    public static final String COL_CART_ID = "id";
    public static final String COL_CART_BOOK_ID = "bookId";
    public static final String COL_CART_TITLE = "title";
    public static final String COL_CART_PRICE = "price";
    public static final String COL_CART_QUANTITY = "quantity";

    // CATEGORY COLUMNS
    public static final String COL_CATEGORY_NAME = "name";

    // USER COLUMNS
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

    private void insertDefaultAdmin(SQLiteDatabase db) {
        String[][] admins = {
                {"admin1@gmail.com", "admin123", "Administrator 1"},
                {"admin2@gmail.com", "admin123", "Administrator 2"},
                {"admin3@gmail.com", "admin123", "Administrator 3"},
                {"admin4@gmail.com", "admin123", "Administrator 4"},
                {"admin5@gmail.com", "admin123", "Administrator 5"},
                {"admin6@gmail.com", "admin123", "Administrator 6"},
                {"admin7@gmail.com", "admin123", "Administrator 7"},
        };

        for (String[] admin : admins) {
            String insertAdmin = "INSERT OR IGNORE INTO " + TABLE_USER + "(" +
                    COL_EMAIL + ", " +
                    COL_PASSWORD + ", " +
                    COL_FULL_NAME + ", " +
                    COL_ROLE + ", " +
                    COL_CREATED_AT + ") VALUES (" +
                    "'" + admin[0] + "', " +
                    "'" + admin[1] + "', " +
                    "'" + admin[2] + "', " +
                    "'admin', " +
                    System.currentTimeMillis() + ")";
            db.execSQL(insertAdmin);
        }
    }

    private void insertDefaultUsers(SQLiteDatabase db) {
        String[][] users = {
                {"user1@example.com", "123456", "Nguyễn Văn A", "0987654321", "Hà Nội"},
                {"user2@example.com", "123456", "Trần Thị B", "0123456789", "TP. HCM"},
                {"user3@example.com", "123456", "Lê Văn C", "0909090909", "Đà Nẵng"}
        };

        for (String[] user : users) {
            String insertUser = "INSERT OR IGNORE INTO " + TABLE_USER + "(" +
                    COL_EMAIL + ", " +
                    COL_PASSWORD + ", " +
                    COL_FULL_NAME + ", " +
                    COL_PHONE + ", " +
                    COL_ADDRESS + ", " +
                    COL_ROLE + ", " +
                    COL_CREATED_AT + ") VALUES (" +
                    "'" + user[0] + "', " +
                    "'" + user[1] + "', " +
                    "'" + user[2] + "', " +
                    "'" + user[3] + "', " +
                    "'" + user[4] + "', " +
                    "'user', " +
                    System.currentTimeMillis() + ")";
            db.execSQL(insertUser);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Tạo bảng categories
        String CREATE_CATEGORY_TABLE =
                "CREATE TABLE " + TABLE_CATEGORY + " (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT UNIQUE)";
        db.execSQL(CREATE_CATEGORY_TABLE);

        // Insert default categories
        insertDefaultCategories(db);

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

        // Insert admin mặc định
        insertDefaultAdmin(db);
        // Insert user mẫu
        insertDefaultUsers(db);

        // Tạo bảng Cart
        String CREATE_CART_TABLE =
                "CREATE TABLE " + TABLE_CART + " (" +
                        COL_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_CART_BOOK_ID + " INTEGER, " +
                        COL_CART_TITLE + " TEXT, " +
                        COL_CART_PRICE + " REAL, " +
                        COL_CART_QUANTITY + " INTEGER)";

        db.execSQL(CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ và tạo lại
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER); // THÊM MỚI

        onCreate(db);
    }
}
