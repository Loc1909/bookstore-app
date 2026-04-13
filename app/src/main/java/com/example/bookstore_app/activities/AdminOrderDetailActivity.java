package com.example.bookstore_app.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore_app.R;
import com.example.bookstore_app.adapters.OrderItemAdapter;
import com.example.bookstore_app.database.dao.OrderDAO;
import com.example.bookstore_app.models.Order;
import com.example.bookstore_app.models.OrderItem;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminOrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderId, tvStatus, tvDate, tvTotal;
    private Button btnAction, btnCancel;
    private RecyclerView rvItems;

    private OrderDAO orderDAO;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_detail);

        initViews();
        loadData();
        setupButtons();
    }

    private void initViews() {
        tvOrderId = findViewById(R.id.tvOrderId);
        tvStatus = findViewById(R.id.tvStatus);
        tvDate = findViewById(R.id.tvDate);
        tvTotal = findViewById(R.id.tvTotal);
        btnAction = findViewById(R.id.btnAction);
        btnCancel = findViewById(R.id.btnCancel);
        rvItems = findViewById(R.id.rvItems);

        orderDAO = new OrderDAO(this);
    }

    private void loadData() {
        int orderId = getIntent().getIntExtra("orderId", -1);
        order = orderDAO.getById(orderId);

        if (order == null) return;

        tvOrderId.setText("#ORD" + order.getId());
        tvStatus.setText(order.getStatus());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        tvDate.setText(sdf.format(new Date(order.getOrderDate())));

        DecimalFormat df = new DecimalFormat("#,###");
        tvTotal.setText("Tổng: " + df.format(order.getTotalPrice()) + "đ");

        // load items
        List<OrderItem> items = orderDAO.getOrderItemsByOrderId(order.getId());

        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(new OrderItemAdapter(items));
    }

    private void setupButtons() {
        btnAction.setVisibility(Button.GONE);
        btnCancel.setVisibility(Button.GONE);

        if (order == null) return;

        switch (order.getStatus()) {

            case "PENDING":
                btnAction.setText("Xác nhận");
                btnAction.setVisibility(Button.VISIBLE);
                btnCancel.setVisibility(Button.VISIBLE);

                btnAction.setOnClickListener(v -> updateStatus("CONFIRMED"));
                btnCancel.setOnClickListener(v -> updateStatus("CANCELLED"));
                break;

            case "CONFIRMED":
                btnAction.setText("Giao hàng");
                btnAction.setVisibility(Button.VISIBLE);

                btnAction.setOnClickListener(v -> updateStatus("SHIPPING"));
                break;

            case "SHIPPING":
                btnAction.setText("Hoàn thành");
                btnAction.setVisibility(Button.VISIBLE);

                btnAction.setOnClickListener(v -> updateStatus("DELIVERED"));
                break;
        }
    }

    private void updateStatus(String status) {
        orderDAO.updateOrderStatus(order.getId(), status);
        order.setStatus(status);

        tvStatus.setText(status);
        setupButtons();
    }
}