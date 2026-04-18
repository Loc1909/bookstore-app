package com.example.bookstore_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookstore_app.R;
import com.example.bookstore_app.database.dao.DashboardDAO;
import com.example.bookstore_app.utils.SessionManager;

public class AdminDashboardActivity extends AppCompatActivity {

    private CardView menuBooks;
    private CardView menuCategories;
    private CardView menuUsers;
    private CardView menuOrders;
    private CardView menuStats;

    private SessionManager sessionManager;


    private TextView txtTotalBooks,
            txtTotalUsers,
            txtTotalOrders,
            txtTotalOrdersv2,
            txtTotalBooksv2,
            txtTotalUsersv2,
            txtTotalCategory,
            txtTotalCategoryv2;;
    private DashboardDAO dashboardDAO;

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

        dashboardDAO = new DashboardDAO(this);

        txtTotalBooks = findViewById(R.id.txtTotalBooks);
        txtTotalUsers = findViewById(R.id.txtTotalUsers);
        txtTotalOrders = findViewById(R.id.txtTotalOrders);
        txtTotalCategory = findViewById(R.id.txtTotalCategory);
        txtTotalBooksv2 = findViewById(R.id.txtTotalBooksv2);
        txtTotalUsersv2 = findViewById(R.id.txtTotalUsersv2);
        txtTotalOrdersv2 = findViewById(R.id.txtTotalOrdersv2);
        loadDashboardData();

        menuBooks      = findViewById(R.id.menuBooks);
        menuCategories = findViewById(R.id.menuCategories);
        menuUsers      = findViewById(R.id.menuUsers);
        menuOrders     = findViewById(R.id.menuOrders);
        menuStats      = findViewById(R.id.menuStats);
        menuOrders     = findViewById(R.id.menuOrders);

        menuBooks.setOnClickListener(v ->
                startActivity(new Intent(this, AdminBookActivity.class))
        );

        menuCategories.setOnClickListener(v ->
                startActivity(new Intent(this, AdminCategoryActivity.class))
        );

        menuUsers.setOnClickListener(v ->
                startActivity(new Intent(this, AdminUserActivity.class))
        );

        menuOrders.setOnClickListener(v ->
                startActivity(new Intent(this, AdminOrderActivity.class))
        );

        menuStats.setOnClickListener(v ->
                startActivity(new Intent(this, StatsActivity.class))
        );
    }

    private void loadDashboardData() {
        int totalBooks = dashboardDAO.getTotalBooks();
        int totalUsers = dashboardDAO.getTotalUsers();
        int totalOrders = dashboardDAO.getTotalOrders();
        int newOrders = dashboardDAO.getNewOrders();
        int totalCategory = dashboardDAO.getTotalCategories();


        txtTotalBooks.setText(String.valueOf(totalBooks));
        txtTotalUsers.setText(String.valueOf(totalUsers));
        txtTotalOrders.setText(String.valueOf(newOrders));


        txtTotalCategory.setText(totalCategory + " danh mục");
        txtTotalBooksv2.setText(totalBooks + " đầu sách");
        txtTotalUsersv2.setText(totalUsers + " tài khoản");
        txtTotalOrdersv2.setText(totalOrders + " đơn hàng");

    }
}