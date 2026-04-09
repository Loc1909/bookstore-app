package com.example.bookstore_app.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bookstore.db";
    private static final int DATABASE_VERSION = 13;

    // ------------------- TABLE NAMES -------------------
    public static final String TABLE_BOOK = "books";
    public static final String TABLE_CATEGORY = "categories";
    public static final String TABLE_USER = "users";
    public static final String TABLE_ORDER = "orders";
    public static final String TABLE_ORDER_ITEM = "order_items";
    public static final String TABLE_CART = "cart";

    // ------------------- CATEGORY COLUMNS -------------------
    public static final String COL_CATEGORY_ID = "id";        // id của bảng categories
    public static final String COL_CATEGORY_NAME = "name";

    // ------------------- BOOK COLUMNS -------------------
    public static final String COL_BOOK_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_AUTHOR = "author";
    public static final String COL_PRICE = "price";
    public static final String COL_IMAGE = "imageUrl";
    public static final String COL_CATEGORY_ID_BOOK = "category_id"; // foreign key
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_STOCK = "stock";

    // ------------------- USER COLUMNS -------------------
    public static final String COL_USER_ID = "id";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_FULL_NAME = "fullName";
    public static final String COL_PHONE = "phone";
    public static final String COL_ADDRESS = "address";
    public static final String COL_ROLE = "role";
    public static final String COL_AVATAR = "avatar";
    public static final String COL_CREATED_AT = "createdAt";

    // ------------------- ORDER COLUMNS -------------------
    public static final String COL_ORDER_ID = "id";
    public static final String COL_ORDER_USER_ID = "user_id";
    public static final String COL_ORDER_DATE = "order_date";
    public static final String COL_ORDER_STATUS = "status";

    // ------------------- ORDER ITEM COLUMNS -------------------
    public static final String COL_ORDER_ITEM_ID = "id";
    public static final String COL_ORDER_ITEM_ORDER_ID = "order_id";
    public static final String COL_ORDER_ITEM_BOOK_ID = "book_id";
    public static final String COL_ORDER_ITEM_QUANTITY = "quantity";
    public static final String COL_ORDER_ITEM_PRICE = "price";

    // ------------------- CART COLUMNS -------------------
    public static final String COL_CART_ID = "id";
    public static final String COL_CART_BOOK_ID = "bookId";
    public static final String COL_CART_TITLE = "title";
    public static final String COL_CART_PRICE = "price";
    public static final String COL_CART_QUANTITY = "quantity";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // ------------------- CREATE CATEGORY -------------------
        db.execSQL("CREATE TABLE " + TABLE_CATEGORY + " (" +
                COL_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CATEGORY_NAME + " TEXT UNIQUE)");

        // ------------------- CREATE BOOK -------------------
        db.execSQL("CREATE TABLE " + TABLE_BOOK + " (" +
                COL_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_AUTHOR + " TEXT, " +
                COL_PRICE + " REAL, " +
                COL_IMAGE + " TEXT, " +
                COL_CATEGORY_ID_BOOK + " INTEGER, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_STOCK + " INTEGER, " +
                "FOREIGN KEY(" + COL_CATEGORY_ID_BOOK + ") REFERENCES " + TABLE_CATEGORY + "(" + COL_CATEGORY_ID + ")" +
                ")");

        // ------------------- CREATE USER -------------------
        db.execSQL("CREATE TABLE " + TABLE_USER + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COL_PASSWORD + " TEXT NOT NULL, " +
                COL_FULL_NAME + " TEXT NOT NULL, " +
                COL_PHONE + " TEXT, " +
                COL_ADDRESS + " TEXT, " +
                COL_ROLE + " TEXT DEFAULT 'user', " +
                COL_AVATAR + " TEXT, " +
                COL_CREATED_AT + " INTEGER)");

        // ------------------- CREATE ORDER -------------------
        db.execSQL("CREATE TABLE " + TABLE_ORDER + " (" +
                COL_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ORDER_USER_ID + " INTEGER, " +
                COL_ORDER_DATE + " TEXT, " +
                COL_ORDER_STATUS + " TEXT DEFAULT 'NEW', " +
                "FOREIGN KEY(" + COL_ORDER_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COL_USER_ID + "))");

        // ------------------- CREATE ORDER ITEM -------------------
        db.execSQL("CREATE TABLE " + TABLE_ORDER_ITEM + " (" +
                COL_ORDER_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ORDER_ITEM_ORDER_ID + " INTEGER, " +
                COL_ORDER_ITEM_BOOK_ID + " INTEGER, " +
                COL_ORDER_ITEM_QUANTITY + " INTEGER, " +
                COL_ORDER_ITEM_PRICE + " REAL, " +
                "FOREIGN KEY(" + COL_ORDER_ITEM_ORDER_ID + ") REFERENCES " + TABLE_ORDER + "(" + COL_ORDER_ID + "), " +
                "FOREIGN KEY(" + COL_ORDER_ITEM_BOOK_ID + ") REFERENCES " + TABLE_BOOK + "(" + COL_BOOK_ID + "))");

        // ------------------- CREATE CART -------------------
        db.execSQL("CREATE TABLE " + TABLE_CART + " (" +
                COL_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CART_BOOK_ID + " INTEGER, " +
                COL_CART_TITLE + " TEXT, " +
                COL_CART_PRICE + " REAL, " +
                COL_CART_QUANTITY + " INTEGER)");

        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        long now = System.currentTimeMillis();

        // CATEGORY
        db.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COL_CATEGORY_NAME + ") VALUES ('Lập trình')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COL_CATEGORY_NAME + ") VALUES ('Tiểu thuyết')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COL_CATEGORY_NAME + ") VALUES ('Khoa học')");

        // BOOK
        db.execSQL("INSERT INTO " + TABLE_BOOK + "(" +
                COL_TITLE + "," + COL_AUTHOR + "," + COL_PRICE + "," + COL_CATEGORY_ID_BOOK + "," + COL_STOCK +
                ") VALUES ('Java','Nguyễn Văn A',120000,1,50)");
        db.execSQL("INSERT INTO " + TABLE_BOOK + "(" +
                COL_TITLE + "," + COL_AUTHOR + "," + COL_PRICE + "," + COL_CATEGORY_ID_BOOK + "," + COL_STOCK +
                ") VALUES ('Android','Trần Văn B',150000,1,40)");

        // USER
        db.execSQL("INSERT INTO " + TABLE_USER + "(" +
                COL_EMAIL + "," + COL_PASSWORD + "," + COL_FULL_NAME + "," + COL_ROLE + "," + COL_CREATED_AT +
                ") VALUES ('admin@gmail.com','123','Admin','admin'," + now + ")");
        db.execSQL("INSERT INTO " + TABLE_USER + "(" +
                COL_EMAIL + "," + COL_PASSWORD + "," + COL_FULL_NAME + "," + COL_ROLE + "," + COL_CREATED_AT +
                ") VALUES ('user@gmail.com','123','User','user'," + now + ")");

        // ORDER
        db.execSQL("INSERT INTO " + TABLE_ORDER + "(" +
                COL_ORDER_USER_ID + "," + COL_ORDER_DATE +
                ") VALUES (2,'2024-01-01')");

        // ORDER ITEM
        db.execSQL("INSERT INTO " + TABLE_ORDER_ITEM + "(" +
                COL_ORDER_ITEM_ORDER_ID + "," + COL_ORDER_ITEM_BOOK_ID + "," + COL_ORDER_ITEM_QUANTITY + "," + COL_ORDER_ITEM_PRICE +
                ") VALUES (1,1,2,120000)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 13) {
            // 1. Rename old cart table
            db.execSQL("ALTER TABLE cart RENAME TO cart_old");

            // 2. Create new cart table
            db.execSQL("CREATE TABLE cart (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "bookId INTEGER, " +
                    "title TEXT, " +
                    "price REAL, " +
                    "quantity INTEGER)");

            // 3. Copy data
            db.execSQL("INSERT INTO cart (id, bookId, title, price, quantity) " +
                    "SELECT id, bookId, title, price, quantity FROM cart_old");

            // 4. Drop old table
            db.execSQL("DROP TABLE cart_old");
        }
    }

    // Check if column exists
    private boolean isColumnExists(SQLiteDatabase db, String tableName, String columnName) {
        Cursor cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
        boolean exists = false;
        while (cursor.moveToNext()) {
            String colName = cursor.getString(1);
            if (colName.equals(columnName)) {
                exists = true;
                break;
            }
        }
        cursor.close();
        return exists;
    }
}