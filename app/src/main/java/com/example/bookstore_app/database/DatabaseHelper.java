package com.example.bookstore_app.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bookstore.db";
    private static final int DATABASE_VERSION = 10; // tăng version để trigger onUpgrade

    // TABLES
    public static final String TABLE_BOOK       = "books";
    public static final String TABLE_CATEGORY   = "categories";
    public static final String TABLE_USER       = "users";
    public static final String TABLE_ORDER      = "orders";
    public static final String TABLE_ORDER_ITEM = "order_items";

    // USER COLUMNS
    public static final String COL_USER_ID   = "id";
    public static final String COL_EMAIL     = "email";
    public static final String COL_PASSWORD  = "password";
    public static final String COL_FULL_NAME = "fullName";
    public static final String COL_PHONE     = "phone";
    public static final String COL_ADDRESS   = "address";
    public static final String COL_ROLE      = "role";
    public static final String COL_AVATAR    = "avatar";
    public static final String COL_CREATED_AT= "createdAt";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // ── CATEGORY ──────────────────────────────────────────────
        db.execSQL("CREATE TABLE " + TABLE_CATEGORY + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT UNIQUE)");

        // ── BOOK ──────────────────────────────────────────────────
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

        // ── USER ──────────────────────────────────────────────────
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

        // ── ORDER ─────────────────────────────────────────────────
        db.execSQL("CREATE TABLE " + TABLE_ORDER + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "order_date TEXT," +
                "FOREIGN KEY(user_id) REFERENCES users(id))");

        // ── ORDER ITEM ────────────────────────────────────────────
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

        // ══════════════════════════════════════════════════════════
        // CATEGORIES (5 danh mục)
        // ══════════════════════════════════════════════════════════
        db.execSQL("INSERT OR IGNORE INTO categories(name) VALUES ('Lập trình')");       // id=1
        db.execSQL("INSERT OR IGNORE INTO categories(name) VALUES ('Tiểu thuyết')");     // id=2
        db.execSQL("INSERT OR IGNORE INTO categories(name) VALUES ('Khoa học')");        // id=3
        db.execSQL("INSERT OR IGNORE INTO categories(name) VALUES ('Kinh tế')");         // id=4
        db.execSQL("INSERT OR IGNORE INTO categories(name) VALUES ('Kỹ năng sống')");    // id=5

        // ══════════════════════════════════════════════════════════
        // BOOKS (10 sách)
        // ══════════════════════════════════════════════════════════
        // Lập trình
        db.execSQL("INSERT INTO books(title,author,price,category_id,stock) VALUES ('Java Cơ Bản','Nguyễn Văn A',120000,1,50)");         // id=1
        db.execSQL("INSERT INTO books(title,author,price,category_id,stock) VALUES ('Android Dev Pro','Trần Minh B',185000,1,30)");      // id=2
        db.execSQL("INSERT INTO books(title,author,price,category_id,stock) VALUES ('Kotlin cho người mới','Lê Thu C',150000,1,40)");    // id=3
        // Tiểu thuyết
        db.execSQL("INSERT INTO books(title,author,price,category_id,stock) VALUES ('Mắt Biếc','Nguyễn Nhật Ánh',95000,2,60)");         // id=4
        db.execSQL("INSERT INTO books(title,author,price,category_id,stock) VALUES ('Tôi Thấy Hoa Vàng','Nguyễn Nhật Ánh',88000,2,45)");// id=5
        // Khoa học
        db.execSQL("INSERT INTO books(title,author,price,category_id,stock) VALUES ('Lược Sử Thời Gian','Stephen Hawking',130000,3,25)");// id=6
        db.execSQL("INSERT INTO books(title,author,price,category_id,stock) VALUES ('Sapiens','Yuval Noah Harari',175000,3,35)");        // id=7
        // Kinh tế
        db.execSQL("INSERT INTO books(title,author,price,category_id,stock) VALUES ('Dạy Con Làm Giàu','Robert Kiyosaki',110000,4,55)"); // id=8
        // Kỹ năng sống
        db.execSQL("INSERT INTO books(title,author,price,category_id,stock) VALUES ('Đắc Nhân Tâm','Dale Carnegie',98000,5,70)");        // id=9
        db.execSQL("INSERT INTO books(title,author,price,category_id,stock) VALUES ('Nghĩ Giàu Làm Giàu','Napoleon Hill',105000,5,40)");// id=10

        // ══════════════════════════════════════════════════════════
        // USERS (1 admin + 6 khách hàng)
        // ══════════════════════════════════════════════════════════
        long now = System.currentTimeMillis();
        db.execSQL("INSERT OR IGNORE INTO users(email,password,fullName,role,createdAt) VALUES ('admin@bookstore.com','admin123','Administrator','admin'," + now + ")"); // id=1
        db.execSQL("INSERT OR IGNORE INTO users(email,password,fullName,role,phone,createdAt) VALUES ('kien@gmail.com','123','Trần Minh Kiên','user','0901234567'," + now + ")");  // id=2
        db.execSQL("INSERT OR IGNORE INTO users(email,password,fullName,role,phone,createdAt) VALUES ('an@gmail.com','123','Nguyễn Thị An','user','0912345678'," + now + ")");    // id=3
        db.execSQL("INSERT OR IGNORE INTO users(email,password,fullName,role,phone,createdAt) VALUES ('hung@gmail.com','123','Lê Văn Hùng','user','0923456789'," + now + ")");   // id=4
        db.execSQL("INSERT OR IGNORE INTO users(email,password,fullName,role,phone,createdAt) VALUES ('mai@gmail.com','123','Phạm Thị Mai','user','0934567890'," + now + ")");   // id=5
        db.execSQL("INSERT OR IGNORE INTO users(email,password,fullName,role,phone,createdAt) VALUES ('duc@gmail.com','123','Hoàng Minh Đức','user','0945678901'," + now + ")"); // id=6
        db.execSQL("INSERT OR IGNORE INTO users(email,password,fullName,role,phone,createdAt) VALUES ('linh@gmail.com','123','Vũ Thị Linh','user','0956789012'," + now + ")");  // id=7

        // ══════════════════════════════════════════════════════════
        // ORDERS + ORDER_ITEMS
        // Trải đều 12 tháng để chart doanh thu đẹp
        // ══════════════════════════════════════════════════════════

        // ── Tháng 1 ───────────────────────────────────────────────
        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (2,'2024-01-05')"); // order_id=1
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (1,9,2,98000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (1,1,1,120000)");

        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (3,'2024-01-18')"); // order_id=2
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (2,4,3,95000)");

        // ── Tháng 2 ───────────────────────────────────────────────
        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (4,'2024-02-10')"); // order_id=3
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (3,8,2,110000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (3,10,1,105000)");

        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (2,'2024-02-22')"); // order_id=4
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (4,2,1,185000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (4,9,2,98000)");

        // ── Tháng 3 ───────────────────────────────────────────────
        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (5,'2024-03-07')"); // order_id=5
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (5,7,1,175000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (5,6,1,130000)");

        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (6,'2024-03-20')"); // order_id=6
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (6,9,3,98000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (6,4,2,95000)");

        // ── Tháng 4 ───────────────────────────────────────────────
        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (3,'2024-04-03')"); // order_id=7
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (7,3,2,150000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (7,1,1,120000)");

        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (7,'2024-04-15')"); // order_id=8
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (8,5,2,88000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (8,10,2,105000)");

        // ── Tháng 5 ───────────────────────────────────────────────
        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (2,'2024-05-11')"); // order_id=9
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (9,2,2,185000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (9,7,1,175000)");

        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (4,'2024-05-28')"); // order_id=10
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (10,9,4,98000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (10,8,1,110000)");

        // ── Tháng 6 ───────────────────────────────────────────────
        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (5,'2024-06-05')"); // order_id=11
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (11,1,3,120000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (11,3,1,150000)");

        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (6,'2024-06-19')"); // order_id=12
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (12,6,2,130000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (12,7,1,175000)");

        // ── Tháng 7 ───────────────────────────────────────────────
        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (7,'2024-07-08')"); // order_id=13
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (13,9,3,98000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (13,4,2,95000)");

        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (2,'2024-07-25')"); // order_id=14
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (14,2,1,185000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (14,10,3,105000)");

        // ── Tháng 8 ───────────────────────────────────────────────
        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (3,'2024-08-14')"); // order_id=15
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (15,8,2,110000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (15,5,3,88000)");

        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (4,'2024-08-30')"); // order_id=16
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (16,1,2,120000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (16,3,2,150000)");

        // ── Tháng 9 ───────────────────────────────────────────────
        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (5,'2024-09-09')"); // order_id=17
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (17,7,2,175000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (17,9,2,98000)");

        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (6,'2024-09-22')"); // order_id=18
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (18,2,2,185000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (18,6,1,130000)");

        // ── Tháng 10 ──────────────────────────────────────────────
        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (7,'2024-10-04')"); // order_id=19
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (19,4,3,95000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (19,5,2,88000)");

        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (2,'2024-10-18')"); // order_id=20
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (20,9,5,98000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (20,8,2,110000)");

        // ── Tháng 11 ──────────────────────────────────────────────
        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (3,'2024-11-06')"); // order_id=21
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (21,1,2,120000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (21,2,1,185000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (21,3,1,150000)");

        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (4,'2024-11-20')"); // order_id=22
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (22,7,2,175000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (22,10,3,105000)");

        // ── Tháng 12 ──────────────────────────────────────────────
        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (5,'2024-12-01')"); // order_id=23
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (23,9,4,98000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (23,4,3,95000)");

        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (6,'2024-12-15')"); // order_id=24
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (24,2,2,185000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (24,7,2,175000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (24,8,1,110000)");

        db.execSQL("INSERT INTO orders(user_id,order_date) VALUES (2,'2024-12-28')"); // order_id=25
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (25,1,3,120000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (25,3,2,150000)");
        db.execSQL("INSERT INTO order_items(order_id,book_id,quantity,price) VALUES (25,10,2,105000)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }
}