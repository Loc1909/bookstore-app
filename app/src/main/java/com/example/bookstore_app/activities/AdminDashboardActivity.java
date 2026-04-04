package com.example.bookstore_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookstore_app.R;
import com.example.bookstore_app.utils.SessionManager;

public class AdminDashboardActivity extends AppCompatActivity {

    private CardView menuBooks;
    private CardView menuCategories;
    private CardView menuUsers;
    private CardView menuOrders;
    private CardView menuStats;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra quyền admin trước khi hiển thị
        sessionManager = new SessionManager(this);

        if (!sessionManager.isLoggedIn() || !sessionManager.isAdmin()) {
            Toast.makeText(this, "You don't have permission to access this area", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_admin_dashboard);

        menuBooks      = findViewById(R.id.menuBooks);
        menuCategories = findViewById(R.id.menuCategories);
        menuUsers      = findViewById(R.id.menuUsers);
        menuOrders     = findViewById(R.id.menuOrders);
        menuStats      = findViewById(R.id.menuStats);

        menuBooks.setOnClickListener(v ->
                startActivity(new Intent(this, AdminBookActivity.class))
        );

        menuCategories.setOnClickListener(v ->
                startActivity(new Intent(this, AdminCategoryActivity.class))
        );

        menuUsers.setOnClickListener(v ->
                startActivity(new Intent(this, AdminUserActivity.class))
        );

//        menuOrders.setOnClickListener(v ->
//                startActivity(new Intent(this, AdminOrderActivity.class))
//        );

        menuStats.setOnClickListener(v ->
                startActivity(new Intent(this, StatsActivity.class))
        );
    }
}