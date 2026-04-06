package com.example.bookstore_app.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore_app.R;
import com.example.bookstore_app.adapters.CartAdapter;
import com.example.bookstore_app.database.dao.CartDAO;
import com.example.bookstore_app.models.CartItem;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    CartAdapter adapter;
    CartDAO cartDAO;
    List<CartItem> cartList;
    TextView tvTotal;
    Button btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.rvCart);
        tvTotal = findViewById(R.id.tvTotalPrice);
        cartDAO = new CartDAO(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        loadCartData();

        btnCheckout = findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(v -> {
            if (cartList.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng của bạn trống", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Tính năng thanh toán đang phát triển", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCartData() {
        cartList = cartDAO.getAllCart();
        adapter = new CartAdapter(cartList, new CartAdapter.OnCartActionListener() {
            @Override
            public void onQuantityChanged(CartItem item, int newQuantity) {
                cartDAO.updateQuantity(item.getId(), newQuantity);
                loadCartData(); // Tải lại dữ liệu và cập nhật UI
            }

            @Override
            public void onDelete(CartItem item) {
                cartDAO.updateQuantity(item.getId(), 0); // 0 sẽ xóa khỏi db
                loadCartData();
            }
        });
        recyclerView.setAdapter(adapter);
        updateTotal(cartList);
    }

    private void updateTotal(List<CartItem> list) {
        double total = 0;
        for (CartItem item : list) {
            total += item.getPrice() * item.getQuantity();
        }
        tvTotal.setText(String.format("Tổng: %.2f đ", total));
    }
}
