package com.example.bookstore_app.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bookstore.db";
    private static final int DATABASE_VERSION = 13;

    // TABLE NAMES
    public static final String TABLE_BOOK = "books";
    public static final String TABLE_CATEGORY = "categories";
    public static final String TABLE_USER = "users";
    public static final String TABLE_ORDER = "orders";
    public static final String TABLE_ORDER_ITEM = "order_items";
    public static final String TABLE_CART = "cart";

    // CART COLUMNS
    public static final String COL_CART_ID = "id";
    public static final String COL_CART_USER_ID = "userId";
    public static final String COL_CART_BOOK_ID = "bookId";
    public static final String COL_CART_TITLE = "title";
    public static final String COL_CART_PRICE = "price";
    public static final String COL_CART_QUANTITY = "quantity";
    public static final String COL_CART_IMAGE = "imageUrl";

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
                "user_id INTEGER NOT NULL," +
                "order_date INTEGER NOT NULL," +   // timestamp (long)
                "total_price REAL NOT NULL," +
                "status TEXT DEFAULT 'PENDING'," +
                "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ")");

        // ORDER ITEM
        db.execSQL("CREATE TABLE " + TABLE_ORDER_ITEM + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "order_id INTEGER NOT NULL," +
                "book_id INTEGER NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "price REAL NOT NULL," +
                "FOREIGN KEY(order_id) REFERENCES orders(id) ON DELETE CASCADE," +
                "FOREIGN KEY(book_id) REFERENCES books(id)" +
                ")");

        // CREATE CART TABLE
        String CREATE_CART_TABLE =
                "CREATE TABLE " + TABLE_CART + " (" +
                        COL_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_CART_USER_ID + " INTEGER, " +
                        COL_CART_BOOK_ID + " INTEGER, " +
                        COL_CART_TITLE + " TEXT, " +
                        COL_CART_PRICE + " REAL, " +
                        COL_CART_QUANTITY + " INTEGER, " +
                        COL_CART_IMAGE + " TEXT, "+
                        "UNIQUE(" + COL_CART_USER_ID + ", " + COL_CART_BOOK_ID + ")" +
                        ")";

        db.execSQL(CREATE_CART_TABLE);

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
        db.execSQL("INSERT INTO orders (user_id, order_date, total_price, status) VALUES (1, " + now + ", 150000, 'PENDING')");

        // ORDER ITEM
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (1,1,2,120000)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//        if (oldVersion < 11) {
//
//            if (!isColumnExists(db, "orders", "status")) {
//                db.execSQL("ALTER TABLE orders ADD COLUMN status TEXT DEFAULT 'NEW'");
//            }
//
//            db.execSQL("UPDATE orders SET status = 'NEW' WHERE status IS NULL");
//        }
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEM);
        onCreate(db);
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