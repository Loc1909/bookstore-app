package com.example.bookstore_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore_app.R;
import com.example.bookstore_app.adapters.CheckoutAdapter;
import com.example.bookstore_app.database.dao.BookDAO;
import com.example.bookstore_app.database.dao.CartDAO;
import com.example.bookstore_app.database.dao.OrderDAO;
import com.example.bookstore_app.database.dao.UserDAO;
import com.example.bookstore_app.models.Book;
import com.example.bookstore_app.models.CartItem;
import com.example.bookstore_app.models.User;
import com.example.bookstore_app.utils.SessionManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private int currentUserId;
    private SessionManager sessionManager;
    RecyclerView rvCheckoutItems;
    TextView tvSubtotal, tvCheckoutTotal;
    EditText edtPhone, edtAddress;
    Button btnConfirmOrder;
    CheckoutAdapter adapter;
    List<CartItem> checkoutList = new ArrayList<>();

    double subtotal = 0;
    final double SHIPPING_FEE = 30000;
    private boolean isFromCart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        sessionManager = new SessionManager(this);
        currentUserId = sessionManager.getUserId();

        initViews();
        prepareData();

        adapter = new CheckoutAdapter(checkoutList);
        rvCheckoutItems.setLayoutManager(new LinearLayoutManager(this));
        rvCheckoutItems.setAdapter(adapter);

        calculateTotal();

        btnConfirmOrder.setOnClickListener(v -> handlePlaceOrder());
    }

    private void initViews() {
        rvCheckoutItems = findViewById(R.id.rvCheckoutItems);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvCheckoutTotal = findViewById(R.id.tvCheckoutTotal);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);

        UserDAO userDAO = new UserDAO(this);

        User currentUser = userDAO.getUserById(currentUserId);
        if (currentUser != null) {
            if (currentUser.getPhone() != null) {
                edtPhone.setText(currentUser.getPhone());
            }
            if (currentUser.getAddress() != null) {
                edtAddress.setText(currentUser.getAddress());
            }
        }
    }


    private void prepareData() {
        String jsonItems = getIntent().getStringExtra("selected_items");
        if (jsonItems != null && !jsonItems.isEmpty()) {
            isFromCart = true;
            Gson gson = new Gson();
            Type type = new TypeToken<List<CartItem>>(){}.getType();
            List<CartItem> items = gson.fromJson(jsonItems, type);
            if (items != null) {
                checkoutList.addAll(items);
            }
        } else {
            int bookId = getIntent().getIntExtra("book_id", -1);
            int quantity = getIntent().getIntExtra("quantity", 1);
            String imageUrl = getIntent().getStringExtra("imageUrl");

            if (bookId != -1) {
                BookDAO bookDAO = new BookDAO(this);
                Book book = bookDAO.getBookById(bookId);

                if (book != null) {
                    CartItem item = new CartItem();
                    item.setBookId(book.getId());
                    item.setTitle(book.getTitle());
                    item.setPrice(book.getPrice());
                    item.setQuantity(quantity);
                    item.setImageUrl(imageUrl);
                    checkoutList.add(item);
                }
            }
        }
    }

    private void calculateTotal() {
        subtotal = 0;
        for (CartItem item : checkoutList) {
            subtotal += item.getPrice() * item.getQuantity();
        }
        tvSubtotal.setText(String.format("%,.0f đ", subtotal));
        tvCheckoutTotal.setText(String.format("%,.0f đ", subtotal + SHIPPING_FEE));
    }

    private void handlePlaceOrder() {
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        if (phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin giao hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        OrderDAO orderDAO = new OrderDAO(this);
        long result = orderDAO.createOrder(currentUserId, checkoutList, subtotal + SHIPPING_FEE);
        if (result != -1) {
            Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_LONG).show();

            if (isFromCart) {
                CartDAO cartDAO = new CartDAO(this);
                for (CartItem item : checkoutList) {
                    cartDAO.deleteItem(item.getId());
                }
            }

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Có lỗi xảy ra khi đặt hàng", Toast.LENGTH_SHORT).show();
        }
    }
}
