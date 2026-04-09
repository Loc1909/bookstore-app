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
import com.example.bookstore_app.database.dao.OrderDAO;
import com.example.bookstore_app.models.CartItem;
import com.example.bookstore_app.utils.SessionManager;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    private int currentUserId;
    private SessionManager sessionManager;
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


        sessionManager = new SessionManager(this);
        currentUserId = sessionManager.getUserId();

        if (currentUserId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

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
                double total = calculateTotal();

                OrderDAO orderDAO = new OrderDAO(this);
                long orderId = orderDAO.createOrder(currentUserId, cartList, total);

                if (orderId != -1) {
                    Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                    cartDAO.clearCart(currentUserId);
                    loadCartData(); // reload giỏ rỗng sau khi đặt hàng
                } else {
                    Toast.makeText(this, "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadCartData() {
        cartList = cartDAO.getCartByUser(currentUserId);
        adapter = new CartAdapter(cartList, new CartAdapter.OnCartActionListener() {
            @Override
            public void onQuantityChanged(CartItem item, int newQuantity) {
                cartDAO.updateQuantity(currentUserId, item.id, newQuantity);
                loadCartData(); // Tải lại dữ liệu và cập nhật UI
            }

            @Override
            public void onDelete(CartItem item) {
                cartDAO.deleteItem(currentUserId, item.id); // 0 sẽ xóa khỏi db
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
        tvTotal.setText(String.format("Tổng: %, .0f đ", total));
    }

    private double calculateTotal() {
        double total = 0;
        for (CartItem item : cartList) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

}
