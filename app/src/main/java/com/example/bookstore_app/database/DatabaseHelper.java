package com.example.bookstore_app.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bookstore.db";
    private static final int DATABASE_VERSION = 11;

    // TABLE NAMES
    public static final String TABLE_BOOK = "books";
    public static final String TABLE_CATEGORY = "categories";
    public static final String TABLE_USER = "users";
    public static final String TABLE_ORDER = "orders";
    public static final String TABLE_ORDER_ITEM = "order_items";

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

    @Override
    public void onCreate(SQLiteDatabase db) {

        // CATEGORY
        db.execSQL("CREATE TABLE " + TABLE_CATEGORY + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT UNIQUE)");


        // BOOK
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

        // USER
        db.execSQL("CREATE TABLE " + TABLE_USER + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "fullName TEXT NOT NULL," +
                "phone TEXT," +
                "address TEXT," +
                "role TEXT DEFAULT 'user'," +
                "avatar TEXT," +
                "createdAt INTEGER)");

        // ORDER
        db.execSQL("CREATE TABLE " + TABLE_ORDER + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "order_date TEXT," +
                "status TEXT DEFAULT 'NEW'," +
                "FOREIGN KEY(user_id) REFERENCES users(id))");

        // ORDER ITEM
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

        long now = System.currentTimeMillis();

        // CATEGORY
        db.execSQL("INSERT INTO categories(name) VALUES ('Lập trình')");
        db.execSQL("INSERT INTO categories(name) VALUES ('Tiểu thuyết')");
        db.execSQL("INSERT INTO categories(name) VALUES ('Khoa học')");

        // BOOK
        db.execSQL("INSERT INTO books(title,author,price,category_id,stock) VALUES ('Java','Nguyễn Văn A',120000,1,50)");
        db.execSQL("INSERT INTO books(title,author,price,category_id,stock) VALUES ('Android','Trần Văn B',150000,1,40)");

        // USER
        db.execSQL("INSERT INTO users(email,password,fullName,role,createdAt) VALUES ('admin@gmail.com','123','Admin','admin'," + now + ")");
        db.execSQL("INSERT INTO users(email,password,fullName,role,createdAt) VALUES ('user@gmail.com','123','User','user'," + now + ")");

        // ORDER
        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (2,'2024-01-01')");

        // ORDER ITEM
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (1,1,2,120000)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 11) {

            if (!isColumnExists(db, "orders", "status")) {
                db.execSQL("ALTER TABLE orders ADD COLUMN status TEXT DEFAULT 'NEW'");
            }

            db.execSQL("UPDATE orders SET status = 'NEW' WHERE status IS NULL");
        }
    }

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