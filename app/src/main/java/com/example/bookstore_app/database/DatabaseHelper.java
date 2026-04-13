package com.example.bookstore_app.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bookstore.db";
    private static final int DATABASE_VERSION = 21;

    // ---------------- TABLE NAMES ----------------
    public static final String TABLE_BOOK = "books";
    public static final String TABLE_CATEGORY = "categories";
    public static final String TABLE_USER = "users";
    public static final String TABLE_ORDER = "orders";
    public static final String TABLE_ORDER_ITEM = "order_items";
    public static final String TABLE_CART = "carts";
    public static final String TABLE_CART_ITEM = "cart_items";
    public static final String TABLE_REVIEW = "reviews";

    // ---------------- CATEGORY ----------------
    public static final String COL_CATEGORY_ID = "id";
    public static final String COL_CATEGORY_NAME = "name";

    // ---------------- BOOK ----------------
    public static final String COL_BOOK_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_AUTHOR = "author";
    public static final String COL_PRICE = "price";
    public static final String COL_IMAGE = "imageUrl";
    public static final String COL_CATEGORY_ID_BOOK = "category_id";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_STOCK = "stock";
    public static final String COL_RATING = "rating";

    // ---------------- USER ----------------
    public static final String COL_USER_ID = "id";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_FULL_NAME = "fullName";
    public static final String COL_PHONE = "phone";
    public static final String COL_ADDRESS = "address";
    public static final String COL_ROLE = "role";
    public static final String COL_AVATAR = "avatar";
    public static final String COL_CREATED_AT = "createdAt";

    // ---------------- CART ----------------
    public static final String COL_CART_ID = "id";
    public static final String COL_CART_USER_ID = "user_id";

    // ---------------- CART ITEM ----------------
    public static final String COL_CART_ITEM_ID = "id";
    public static final String COL_CART_ITEM_CART_ID = "cart_id";
    public static final String COL_CART_ITEM_BOOK_ID = "book_id";
    public static final String COL_CART_ITEM_TITLE = "title";
    public static final String COL_CART_ITEM_PRICE = "price";
    public static final String COL_CART_ITEM_QUANTITY = "quantity";
    public static final String COL_CART_ITEM_IMAGE = "imageUrl";

    // ---------------- ORDER ----------------
    public static final String COL_ORDER_ID = "id";
    public static final String COL_ORDER_USER_ID = "user_id";
    public static final String COL_ORDER_TOTAL_PRICE = "total_price";
    public static final String COL_ORDER_DATE = "order_date";
    public static final String COL_ORDER_STATUS = "status";

    public static final String COL_ORDER_PAYMENT_METHOD = "payment_method";

    // ---------------- ORDER ITEM ----------------
    public static final String COL_ORDER_ITEM_ID = "id";
    public static final String COL_ORDER_ITEM_ORDER_ID = "order_id";
    public static final String COL_ORDER_ITEM_BOOK_ID = "book_id";
    public static final String COL_ORDER_ITEM_QUANTITY = "quantity";
    public static final String COL_ORDER_ITEM_PRICE = "price";

    // ---------------- REVIEW ----------------
    public static final String COL_REVIEW_ID = "id";
    public static final String COL_REVIEW_USER_ID = "user_id";
    public static final String COL_REVIEW_BOOK_ID = "book_id";
    public static final String COL_REVIEW_RATING = "rating";
    public static final String COL_REVIEW_COMMENT = "comment";
    public static final String COL_REVIEW_CREATED_AT = "created_at";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_CATEGORY + " (" +
                COL_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CATEGORY_NAME + " TEXT UNIQUE)");

        db.execSQL("CREATE TABLE " + TABLE_BOOK + " (" +
                COL_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_AUTHOR + " TEXT, " +
                COL_PRICE + " REAL, " +
                COL_IMAGE + " TEXT, " +
                COL_CATEGORY_ID_BOOK + " INTEGER, " +
                COL_DESCRIPTION + " TEXT, " +
                COL_STOCK + " INTEGER, " +
                COL_RATING + " REAL DEFAULT 0, " +
                "FOREIGN KEY(" + COL_CATEGORY_ID_BOOK + ") REFERENCES " + TABLE_CATEGORY + "(" + COL_CATEGORY_ID + "))");

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

        db.execSQL("CREATE TABLE " + TABLE_CART + " (" +
                COL_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CART_USER_ID + " INTEGER UNIQUE)");

        db.execSQL("CREATE TABLE " + TABLE_CART_ITEM + " (" +
                COL_CART_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CART_ITEM_CART_ID + " INTEGER, " +
                COL_CART_ITEM_BOOK_ID + " INTEGER, " +
                COL_CART_ITEM_TITLE + " TEXT, " +
                COL_CART_ITEM_PRICE + " REAL, " +
                COL_CART_ITEM_QUANTITY + " INTEGER, " +
                COL_CART_ITEM_IMAGE + " TEXT, " +
                "UNIQUE(" + COL_CART_ITEM_CART_ID + "," + COL_CART_ITEM_BOOK_ID + "), " +
                "FOREIGN KEY(" + COL_CART_ITEM_CART_ID + ") REFERENCES " + TABLE_CART + "(" + COL_CART_ID + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + COL_CART_ITEM_BOOK_ID + ") REFERENCES " + TABLE_BOOK + "(" + COL_BOOK_ID + ") ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE " + TABLE_ORDER + " (" +
                COL_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ORDER_USER_ID + " INTEGER, " +
                COL_ORDER_TOTAL_PRICE + " REAL NOT NULL, " +
                COL_ORDER_DATE + " INTEGER NOT NULL, " +
                COL_ORDER_STATUS + " TEXT DEFAULT 'NEW', " +
                COL_ORDER_PAYMENT_METHOD + " TEXT DEFAULT 'COD', " +
                "FOREIGN KEY(" + COL_ORDER_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COL_USER_ID + "))");

        db.execSQL("CREATE TABLE " + TABLE_ORDER_ITEM + " (" +
                COL_ORDER_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ORDER_ITEM_ORDER_ID + " INTEGER, " +
                COL_ORDER_ITEM_BOOK_ID + " INTEGER, " +
                COL_ORDER_ITEM_QUANTITY + " INTEGER, " +
                COL_ORDER_ITEM_PRICE + " REAL, " +
                "FOREIGN KEY(" + COL_ORDER_ITEM_ORDER_ID + ") REFERENCES " + TABLE_ORDER + "(" + COL_ORDER_ID + "), " +
                "FOREIGN KEY(" + COL_ORDER_ITEM_BOOK_ID + ") REFERENCES " + TABLE_BOOK + "(" + COL_BOOK_ID + "))");

        db.execSQL("CREATE TABLE " + TABLE_REVIEW + " (" +
                COL_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_REVIEW_USER_ID + " INTEGER NOT NULL, " +
                COL_REVIEW_BOOK_ID + " INTEGER NOT NULL, " +
                COL_REVIEW_RATING + " REAL CHECK(" + COL_REVIEW_RATING + " >= 1 AND " + COL_REVIEW_RATING + " <= 5), " +
                COL_REVIEW_COMMENT + " TEXT, " +
                COL_REVIEW_CREATED_AT + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
                "UNIQUE(" + COL_REVIEW_USER_ID + "," + COL_REVIEW_BOOK_ID + "), " +
                "FOREIGN KEY(" + COL_REVIEW_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COL_USER_ID + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + COL_REVIEW_BOOK_ID + ") REFERENCES " + TABLE_BOOK + "(" + COL_BOOK_ID + ") ON DELETE CASCADE)");

        insertSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 21) {
            migrateCart(db);
        }
    }

    private void migrateCart(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);

        db.execSQL("CREATE TABLE " + TABLE_CART + " (" +
                COL_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CART_USER_ID + " INTEGER UNIQUE)");

        db.execSQL("CREATE TABLE " + TABLE_CART_ITEM + " (" +
                COL_CART_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CART_ITEM_CART_ID + " INTEGER, " +
                COL_CART_ITEM_BOOK_ID + " INTEGER, " +
                COL_CART_ITEM_TITLE + " TEXT, " +
                COL_CART_ITEM_PRICE + " REAL, " +
                COL_CART_ITEM_QUANTITY + " INTEGER, " +
                COL_CART_ITEM_IMAGE + " TEXT, " +
                "UNIQUE(" + COL_CART_ITEM_CART_ID + "," + COL_CART_ITEM_BOOK_ID + "), " +
                "FOREIGN KEY(" + COL_CART_ITEM_CART_ID + ") REFERENCES " + TABLE_CART + "(" + COL_CART_ID + ") ON DELETE CASCADE, " +
                "FOREIGN KEY(" + COL_CART_ITEM_BOOK_ID + ") REFERENCES " + TABLE_BOOK + "(" + COL_BOOK_ID + ") ON DELETE CASCADE)");
    }


    private void insertSampleData(SQLiteDatabase db) {

        long now = System.currentTimeMillis();

        db.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COL_CATEGORY_NAME + ") VALUES ('Lập trình')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COL_CATEGORY_NAME + ") VALUES ('Tiểu thuyết')");
        db.execSQL("INSERT INTO " + TABLE_CATEGORY + "(" + COL_CATEGORY_NAME + ") VALUES ('Khoa học')");

        db.execSQL("INSERT INTO " + TABLE_BOOK + "(" +
                COL_TITLE + "," + COL_AUTHOR + "," + COL_PRICE + "," + COL_CATEGORY_ID_BOOK + "," + COL_STOCK +
                ") VALUES ('Java','Nguyễn Văn A',120000,1,50)");

        db.execSQL("INSERT INTO " + TABLE_BOOK + "(" +
                COL_TITLE + "," + COL_AUTHOR + "," + COL_PRICE + "," + COL_CATEGORY_ID_BOOK + "," + COL_STOCK +
                ") VALUES ('Android','Trần Văn B',150000,1,40)");

        db.execSQL("INSERT INTO " + TABLE_USER + "(" +
                COL_EMAIL + "," + COL_PASSWORD + "," + COL_FULL_NAME + "," + COL_ROLE + "," + COL_CREATED_AT +
                ") VALUES ('admin@gmail.com','123','Admin','admin'," + now + ")");

        db.execSQL("INSERT INTO " + TABLE_USER + "(" +
                COL_EMAIL + "," + COL_PASSWORD + "," + COL_FULL_NAME + "," + COL_ROLE + "," + COL_CREATED_AT +
                ") VALUES ('user@gmail.com','123','User','user'," + now + ")");

        db.execSQL("INSERT OR IGNORE INTO " + TABLE_CART + " (" +
                COL_CART_USER_ID + ") VALUES (1)");

        db.execSQL("INSERT OR IGNORE INTO " + TABLE_CART + " (" +
                COL_CART_USER_ID + ") VALUES (2)");
    }

    private boolean isColumnExists(SQLiteDatabase db, String tableName, String columnName) {
        Cursor cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
        while (cursor.moveToNext()) {
            if (cursor.getString(1).equals(columnName)) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }
}