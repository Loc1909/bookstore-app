package com.example.bookstore_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookstore_app.R;

public class AdminDashboardActivity extends AppCompatActivity {

    LinearLayout menuBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        menuBooks = findViewById(R.id.menuBooks);

        menuBooks.setOnClickListener(v -> {

            startActivity(new Intent(this, AdminBookActivity.class));

        });
    }
}