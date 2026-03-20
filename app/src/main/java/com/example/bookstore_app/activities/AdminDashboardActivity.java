package com.example.bookstore_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookstore_app.R;

public class AdminDashboardActivity extends AppCompatActivity {

    LinearLayout menuBooks;
    LinearLayout menuStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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