package com.example.bookstore_app.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore_app.R;
import com.example.bookstore_app.adapters.OrderAdapter;
import com.example.bookstore_app.database.dao.OrderDAO;
import com.example.bookstore_app.models.Order;
import com.example.bookstore_app.utils.SessionManager;

import java.util.List;

public class OrdersFragment extends Fragment {

    private RecyclerView rvOrders;
    private TextView tvNoOrders;
    private OrderAdapter adapter;
    private OrderDAO orderDAO;
    private int userId;

    public OrdersFragment() {
        super(R.layout.fragment_orders);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        
        SessionManager sessionManager = new SessionManager(requireContext());
        userId = sessionManager.getUserId();

        if (userId == -1) {
            Toast.makeText(getContext(), "Vui lòng đăng nhập để xem đơn hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        orderDAO = new OrderDAO(requireContext());
        loadOrders();
    }

    private void initViews(View view) {
        rvOrders = view.findViewById(R.id.rvOrders);
        tvNoOrders = view.findViewById(R.id.tvNoOrders);
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void loadOrders() {
        List<Order> orders = orderDAO.getOrdersByUser(userId);
        if (orders.isEmpty()) {
            tvNoOrders.setVisibility(View.VISIBLE);
            rvOrders.setVisibility(View.GONE);
        } else {
            tvNoOrders.setVisibility(View.GONE);
            rvOrders.setVisibility(View.VISIBLE);
            adapter = new OrderAdapter(orders, new OrderAdapter.OnOrderActionListener() {
                @Override
                public void onCancelOrder(Order order) {
                    handleCancelOrder(order);
                }
            });
            rvOrders.setAdapter(adapter);
        }
    }

    private void handleCancelOrder(Order order) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận hủy đơn")
                .setMessage("Bạn có chắc chắn muốn hủy đơn hàng này không?")
                .setPositiveButton("Đồng ý", (dialog, which) -> {
                    boolean success = orderDAO.updateOrderStatus(order.getId(), "CANCELLED");
                    if (success) {
                        Toast.makeText(getContext(), "Đã hủy đơn hàng thành công", Toast.LENGTH_SHORT).show();
                        loadOrders();
                    } else {
                        Toast.makeText(getContext(), "Lỗi khi hủy đơn hàng", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy bỏ", null)
                .show();
    }
}
