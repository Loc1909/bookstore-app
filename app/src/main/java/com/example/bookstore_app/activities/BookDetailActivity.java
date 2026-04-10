package com.example.bookstore_app.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookstore_app.R;
import com.example.bookstore_app.adapters.ReviewAdapter;
import com.example.bookstore_app.database.dao.BookDAO;
import com.example.bookstore_app.database.dao.CartDAO;
import com.example.bookstore_app.database.dao.CategoryDAO;
import com.example.bookstore_app.database.dao.OrderDAO;
import com.example.bookstore_app.database.dao.ReviewDAO;
import com.example.bookstore_app.models.Book;
import com.example.bookstore_app.models.Review;
import com.example.bookstore_app.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookDetailActivity extends AppCompatActivity {
    private ImageView ivBookImage, btnMinus, btnPlus, imgBook;
    private TextView tvBookName,tvTitle, tvPrice, tvDescription, tvQuantity, tvStock, tvAuthor, tvCategory;
    private Button btnAddToCart, btnBuyNow;

    private RatingBar ratingAvg;

    private TextView tvRatingAvg, tvReviewCount;
    private RecyclerView rvReview;
    private Button btnReview;
    private ReviewAdapter adapter;
    private ReviewDAO reviewDAO;
    private CategoryDAO categoryDAO;
    private OrderDAO orderDAO;
    private BookDAO bookDAO;
    private CartDAO cartDAO;
    private SessionManager sessionManager;
    private int bookId, userId, quantity = 1;
    private Book currentBook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(
                    ContextCompat.getColor(this, R.color.dark_espresso)
            );
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(0);
        }

        initView();
        initData();
    }

    private void initView() {
        imgBook = findViewById(R.id.ivBookImage);
        tvTitle = findViewById(R.id.tvBookName);
        tvPrice = findViewById(R.id.tvPrice);
        tvCategory = findViewById(R.id.tvCategory);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvStock = findViewById(R.id.tvStock);
        ratingAvg = findViewById(R.id.ratingAvg);
        tvRatingAvg = findViewById(R.id.tvRatingAvg);
        tvReviewCount = findViewById(R.id.tvReviewCount);

        rvReview = findViewById(R.id.rvReview);
        btnReview = findViewById(R.id.btnReview);

        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        tvQuantity = findViewById(R.id.tvQuantity);

        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);
    }

    private void initData() {

        categoryDAO = new CategoryDAO(this);
        reviewDAO = new ReviewDAO(this);
        orderDAO = new OrderDAO(this);
        bookDAO = new BookDAO(this);
        cartDAO = new CartDAO(this);
        sessionManager = new SessionManager(this);

        if (!sessionManager.isLoggedIn()) {
            finish();
            return;
        }

        userId = sessionManager.getUserId();

        bookId = getIntent().getIntExtra("book_id", -1);
        if (bookId == -1) {
            finish();
            return;
        }

        currentBook = bookDAO.getBookById(bookId);

        bindBookData();
        setupRecyclerView();
        loadReviewData();
        setupReviewButton();
        setupQuantity();
    }

    private void bindBookData() {
        if (currentBook == null) return;

        tvTitle.setText(currentBook.getTitle());
        tvPrice.setText(String.format("%,.0f đ", currentBook.getPrice()));
        String categoryName = categoryDAO.getNameById(currentBook.getCategoryId());
        tvCategory.setText(categoryName);
        tvAuthor.setText(currentBook.getAuthor());
        tvStock.setText("Còn lại: " + currentBook.getStock() + " cuốn");
        Glide.with(this)
                .load(currentBook.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(imgBook);
    }

    private void setupRecyclerView() {
        rvReview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReviewAdapter(this, new java.util.ArrayList<>());
        rvReview.setAdapter(adapter);
    }

    private void loadReviewData() {
        List<Review> reviews = reviewDAO.getReviewsByBook(bookId);
        adapter.setData(reviews);

        float avg = reviewDAO.getAverageRating(bookId);
        int count = reviewDAO.getReviewCount(bookId);

        ratingAvg.setRating(avg);
        tvRatingAvg.setText(String.format(Locale.getDefault(), "%.1f", avg));
        tvReviewCount.setText("(" + count + " đánh giá)");
    }

    private void setupReviewButton() {

        boolean hasBought = orderDAO.hasUserBoughtBook(userId, bookId);

        if (!hasBought) {
            btnReview.setEnabled(false);
            btnReview.setText("Chưa mua");
            return;
        }

        btnReview.setOnClickListener(v -> showReviewDialog());
    }

    private void showReviewDialog() {

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_review, null);

        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        EditText edtComment = view.findViewById(R.id.edtComment);

        Review old = reviewDAO.getReviewByUserAndBook(userId, bookId);

        if (old != null) {
            ratingBar.setRating(old.getRating());
            edtComment.setText(old.getComment());
        }

        AlertDialog dialog = new AlertDialog.Builder(
                this,
                R.style.CustomDialogTheme
        )
                .setView(view)
                .setPositiveButton("Gửi", null)
                .setNegativeButton("Hủy", null)
                .create();

        dialog.show();

        if (dialog.getWindow() != null) {

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);

            dialog.getWindow().setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
        }


        Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        btnPositive.setTextColor(ContextCompat.getColor(this, R.color.bg_cream));
        btnNegative.setTextColor(ContextCompat.getColor(this, R.color.bg_cream));

        btnPositive.setOnClickListener(v -> {

            float rating = ratingBar.getRating();
            String comment = edtComment.getText().toString().trim();

            if (rating == 0) {
                Toast.makeText(this, "Chọn số sao", Toast.LENGTH_SHORT).show();
                return;
            }

            Review review = new Review(
                    bookId,
                    userId,
                    rating,
                    comment,
                    getCurrentTime()
            );

            reviewDAO.insertOrUpdate(review);
            loadReviewData();
            dialog.dismiss();
        });
    }

    private String getCurrentTime() {
        return new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
        ).format(new Date());
    }

    private void setupQuantity() {
        tvQuantity.setText(String.valueOf(quantity));

        btnPlus.setOnClickListener(v -> {
            if (quantity < currentBook.getStock()) {
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
            cartDAO.addToCart(userId, currentBook, quantity);

            Toast.makeText(this, "Đã thêm vào giỏ", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnBuyNow.setOnClickListener(v -> {
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra("book_id", currentBook.getId());
            intent.putExtra("quantity", quantity);
            startActivity(intent);
        });
    }
}