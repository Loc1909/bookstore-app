package com.example.bookstore_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bookstore_app.R;
import com.example.bookstore_app.database.dao.BookDAO;
import com.example.bookstore_app.database.dao.CartDAO;
import com.example.bookstore_app.database.dao.CategoryDAO;
import com.example.bookstore_app.models.Book;
import com.example.bookstore_app.utils.SessionManager;

public class BookDetailActivity extends AppCompatActivity {
    ImageView ivBookImage, btnMinus, btnPlus;
    TextView tvBookName, tvPrice, tvDescription, tvQuantity, tvStock, tvAuthor, tvCategory;
    Button btnAddToCart, btnBuyNow;
    int quantity = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_detail);

        tvAuthor = findViewById(R.id.tvAuthor);
        tvCategory = findViewById(R.id.tvCategory);
        tvStock = findViewById(R.id.tvStock);
        ivBookImage = findViewById(R.id.ivBookImage);
        tvBookName = findViewById(R.id.tvBookName);
        tvPrice = findViewById(R.id.tvPrice);
        tvDescription = findViewById(R.id.tvDescription);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);


        int bookId = getIntent().getIntExtra("book_id", -1);

        BookDAO bookDAO = new BookDAO(this);
        Book book = bookDAO.getBookById(bookId);

        if (book == null) {
            Toast.makeText(this, "Không tìm thấy sách", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        CategoryDAO categoryDAO = new CategoryDAO(this);

        tvAuthor.setText("Tác giả: " + book.getAuthor());
        tvCategory.setText("Thể loại: " + categoryDAO.getCategoryNameById(book.getCategoryId()));
        tvBookName.setText(book.getTitle());
        tvPrice.setText(String.format("Tổng: %, .0f đ", book.getPrice()));
        tvDescription.setText(book.getDescription());
        tvQuantity.setText(String.valueOf(quantity));
        tvStock.setText(String.format("Kho: %d", book.getStock()));

        if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(book.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(ivBookImage);
        } else {
            ivBookImage.setImageResource(R.drawable.ic_launcher_background);
        }

        btnPlus.setOnClickListener(v -> {
            if (quantity < book.getStock()) {
                quantity++;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        btnMinus.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        btnAddToCart.setOnClickListener(v -> {
            SessionManager sessionManager = new SessionManager(this);
            int userId = sessionManager.getUserId();

            if (userId == -1) {
                Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
                return;
            }

            CartDAO cartDAO = new CartDAO(this);
            cartDAO.addToCart(userId, book, quantity);

            Toast.makeText(this, "Đã thêm vào giỏ", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnBuyNow.setOnClickListener(v -> {
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra("book_id", book.getId());
            intent.putExtra("quantity", quantity);
            startActivity(intent);
        });
    }
}