package com.example.bookstore_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore_app.R;
import com.example.bookstore_app.adapters.OrderAdminAdapter;
import com.example.bookstore_app.database.dao.OrderDAO;
import com.example.bookstore_app.database.dao.UserDAO;
import com.example.bookstore_app.models.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminOrderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchOrder;
    private ImageButton btnBack;

    private OrderAdminAdapter adapter;
    private List<Order> list = new ArrayList<>();
    private List<Order> originalList = new ArrayList<>();

    private OrderDAO orderDAO;
    private UserDAO userDAO;
    private Map<Integer, String> userMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);

        initViews();
        initData();
        setupRecyclerView();
        setupSearch();
        setupBack();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewOrders);
        searchOrder = findViewById(R.id.searchOrder);
        btnBack = findViewById(R.id.btnBack);

        orderDAO = new OrderDAO(this);
        userDAO = new UserDAO(this);
    }

    private void initData() {
        // load orders
        list.clear();
        list.addAll(orderDAO.getAll());

        originalList.clear();
        originalList.addAll(list);

        //load user map
        userMap = userDAO.getUserMap();
    }

    private void setupRecyclerView() {

        adapter = new OrderAdminAdapter(
                list,
                userMap,
                new OrderAdminAdapter.OnOrderAdminActionListener() {

                    @Override
                    public void onClick(Order order) {
                        Intent i = new Intent(AdminOrderActivity.this, AdminOrderDetailActivity.class);
                        i.putExtra("orderId", order.getId());
                        startActivity(i);
                    }

                    @Override
                    public void onConfirm(Order order) {
                        order.setStatus("CONFIRMED");
                        orderDAO.update(order);
                        reload();
                    }

                    @Override
                    public void onShip(Order order) {
                        order.setStatus("SHIPPING");
                        orderDAO.update(order);
                        reload();
                    }

                    @Override
                    public void onComplete(Order order) {
                        order.setStatus("COMPLETED");
                        orderDAO.update(order);
                        reload();
                    }

                    @Override
                    public void onCancel(Order order) {
                        order.setStatus("CANCELLED");
                        orderDAO.update(order);
                        reload();
                    }
                }
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void reload() {
        list.clear();
        list.addAll(orderDAO.getAll());

        originalList.clear();
        originalList.addAll(list);

        //  reload lại userMap (phòng khi có user mới)
        userMap = userDAO.getUserMap();

        adapter.notifyDataSetChanged();
    }

    private void setupSearch() {
        searchOrder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filter(String keyword) {
        List<Order> filtered = new ArrayList<>();

        for (Order o : originalList) {
            if (String.valueOf(o.getId()).contains(keyword)) {
                filtered.add(o);
            }
        }

        list.clear();
        list.addAll(filtered);
        adapter.notifyDataSetChanged();
    }

    private void setupBack() {
        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        reload();
    }
}