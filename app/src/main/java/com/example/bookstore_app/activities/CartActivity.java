package com.example.bookstore_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore_app.R;
import com.example.bookstore_app.adapters.CartAdapter;
import com.example.bookstore_app.database.dao.CartDAO;
import com.example.bookstore_app.models.CartItem;
import com.example.bookstore_app.models.Order;
import com.example.bookstore_app.utils.SessionManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private int currentUserId;
    private SessionManager sessionManager;
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private CartDAO cartDAO;
    private List<CartItem> cartList;
    private TextView tvTotal;
    private Button btnCheckout;
    private LinearLayout layoutEmpty;

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
        btnCheckout = findViewById(R.id.btnCheckout);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        cartDAO = new CartDAO(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadCartData();

        btnCheckout.setOnClickListener(v -> {
            List<CartItem> selectedItems = getSelectedItems();
            if (selectedItems.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn ít nhất một sản phẩm để thanh toán", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, CheckoutActivity.class);
                String jsonItems = new Gson().toJson(selectedItems);
                intent.putExtra("selected_items", jsonItems);
                startActivity(intent);
            }
        });
    }

    private void loadCartData() {
        cartList = cartDAO.getCartItems(currentUserId);
        if (cartList == null || cartList.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            updateTotal();
        } else {
            layoutEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new CartAdapter(cartList, new CartAdapter.OnCartActionListener() {
                @Override
                public void onQuantityChanged(CartItem item, int newQuantity) {
                    cartDAO.updateQuantity(item.getId(), newQuantity);
                    item.setQuantity(newQuantity);
                    adapter.notifyDataSetChanged();
                    updateTotal();
                }

                @Override
                public void onDelete(CartItem item) {
                    new AlertDialog.Builder(CartActivity.this)
                            .setTitle("Xóa sản phẩm")
                            .setMessage("Bạn có muốn xóa \"" + item.getTitle() + "\" khỏi giỏ hàng?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Xóa", (dialog, which) -> {
                                cartDAO.deleteItem(item.getId());
                                cartList.remove(item);
                                adapter.notifyDataSetChanged();
                                if (cartList.isEmpty()) {
                                    layoutEmpty.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                                updateTotal();
                            })
                            .setNegativeButton("Hủy", null)
                            .show();
                }

                @Override
                public void onSelectionChanged() {
                    updateTotal();
                }
            });
            recyclerView.setAdapter(adapter);
            updateTotal();
        }
    }

    private List<CartItem> getSelectedItems() {
        List<CartItem> selected = new ArrayList<>();
        if (cartList != null) {
            for (CartItem item : cartList) {
                if (item.isSelected()) {
                    selected.add(item);
                }
            }
        }
        return selected;
    }

    private void updateTotal() {
        double total = 0;
        int count = 0;
        if (cartList != null) {
            for (CartItem item : cartList) {
                if (item.isSelected()) {
                    total += item.getPrice() * item.getQuantity();
                    count++;
                }
            }
        }
        tvTotal.setText(String.format("Tổng (%d sản phẩm): %,.0f đ", count, total));
        btnCheckout.setEnabled(count > 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCartData();
    }
}
