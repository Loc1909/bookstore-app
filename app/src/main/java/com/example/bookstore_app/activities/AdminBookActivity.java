package com.example.bookstore_app.activities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore_app.R;
import com.example.bookstore_app.adapters.BookAdapter;
import com.example.bookstore_app.database.dao.BookDAO;
import com.example.bookstore_app.models.Book;

import java.util.ArrayList;
import java.util.List;

public class AdminBookActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchView searchView;
    FloatingActionButton btnAddBook;

    BookAdapter adapter;
    BookDAO bookDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_books);

        recyclerView = findViewById(R.id.recyclerViewBooks);
        searchView = findViewById(R.id.searchView);
        btnAddBook = findViewById(R.id.btnAddBook);

        bookDAO = new BookDAO(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageButton btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            finish();
        });

        // Tạo adapter chỉ 1 lần
        adapter = new BookAdapter(new ArrayList<>(), new BookAdapter.OnBookActionListener() {

            @Override
            public void onBookClick(Book book) {
                // Có thể mở màn hình chi tiết sách sau
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

            }

        }, true);

        recyclerView.setAdapter(adapter);

        // Load dữ liệu lần đầu
        loadBooks();

        // Button thêm sách
        btnAddBook.setOnClickListener(v -> {

            startActivity(new Intent(AdminBookActivity.this, AddBookActivity.class));

        });

        // Search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                adapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    // Load danh sách sách
    private void loadBooks(){

        List<Book> books = bookDAO.getAllBooks();

        adapter.updateData(books);
    }

    @Override
    protected void onResume() {

        super.onResume();

        // Reload khi quay lại từ AddBook hoặc EditBook
        loadBooks();
    }
}