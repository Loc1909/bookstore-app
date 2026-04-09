package com.example.bookstore_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore_app.R;
import com.example.bookstore_app.adapters.BookAdapter;
import com.example.bookstore_app.database.dao.BookDAO;
import com.example.bookstore_app.models.Book;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AdminBookActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText searchView;
    private FloatingActionButton btnAddBook;
    private TextView tvBookCount;
    private ImageButton btnBack;

    private BookAdapter adapter;
    private BookDAO bookDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_books);

        // ===== Bind view =====
        recyclerView = findViewById(R.id.recyclerViewBooks);
        searchView = findViewById(R.id.searchView);
        btnAddBook = findViewById(R.id.btnAddBook);
        tvBookCount = findViewById(R.id.tvBookCount);
        btnBack = findViewById(R.id.btnBack);

        // ===== DAO =====
        bookDAO = new BookDAO(this);

        // ===== RecyclerView =====
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BookAdapter(
                new ArrayList<>(),
                new BookAdapter.OnBookActionListener() {

                    @Override
                    public void onBookClick(Book book) {
                        // có thể mở detail nếu cần
                    }

                    @Override
                    public void onEdit(Book book) {
                        Intent intent = new Intent(AdminBookActivity.this, EditBookActivity.class);

                        intent.putExtra("id", book.getId());
                        intent.putExtra("title", book.getTitle());
                        intent.putExtra("author", book.getAuthor());
                        intent.putExtra("price", book.getPrice());
                        intent.putExtra("categoryId", book.getCategoryId());
                        intent.putExtra("image", book.getImageUrl());

                        startActivity(intent);
                    }

                    @Override
                    public void onDelete(Book book) {
                        new AlertDialog.Builder(AdminBookActivity.this)
                                .setTitle("Delete Book")
                                .setMessage("Are you sure you want to delete this book?")
                                .setPositiveButton("Delete", (dialog, which) -> {
                                    bookDAO.deleteBook(book.getId());
                                    loadBooks();
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    }

                    @Override
                    public void onAddToCart(Book book) {
                        // admin không dùng
                    }

                },
                true, // isAdmin
                BookAdapter.TYPE_HORIZONTAL
        );

        recyclerView.setAdapter(adapter);

        // ===== Load data =====
        loadBooks();

        // ===== Back =====
        btnBack.setOnClickListener(v -> finish());

        // ===== Add book =====
        btnAddBook.setOnClickListener(v ->
                startActivity(new Intent(AdminBookActivity.this, AddBookActivity.class))
        );

        // ===== Search =====
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String keyword = s.toString().trim();

                List<Book> books;

                if (keyword.isEmpty()) {
                    books = bookDAO.getAllBooks();
                } else {
                    books = bookDAO.searchBooksAdmin(keyword);
                }

                adapter.updateData(books);
                tvBookCount.setText(String.valueOf(books.size()));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // ===== Load books =====
    private void loadBooks() {
        List<Book> books = bookDAO.getAllBooks();

        adapter.updateData(books);

        // update số lượng
        tvBookCount.setText(String.valueOf(books.size()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks();
    }
}