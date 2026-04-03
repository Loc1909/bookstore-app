package com.example.bookstore_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookstore_app.R;
import com.example.bookstore_app.utils.SessionManager;

public class AdminDashboardActivity extends AppCompatActivity {

    LinearLayout menuBooks;
    private SessionManager sessionManager;
    LinearLayout menuStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra quyền admin trước khi hiển thị
        sessionManager = new SessionManager(this);

        if (!sessionManager.isLoggedIn() || !sessionManager.isAdmin()) {
            // Không phải admin, quay về MainActivity
            Toast.makeText(this, "You don't have permission to access this area", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_admin_dashboard);

        menuBooks = findViewById(R.id.menuBooks);
        menuStats = findViewById(R.id.menuStats);

        menuBooks.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminBookActivity.class));
        });

        menuStats.setOnClickListener(v ->
                startActivity(new Intent(this, StatsActivity.class))
        );
    }
}