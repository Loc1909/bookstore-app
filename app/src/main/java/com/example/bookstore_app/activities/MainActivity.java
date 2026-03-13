package com.example.bookstore_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    BookAdapter adapter;

    List<Book> bookList = new ArrayList<>();
    List<Book> filteredList = new ArrayList<>();

    BookDAO bookDAO;

    Button btnAdd;
    Button btnSortName;
    Button btnSortPrice;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewBooks);
        btnAdd = findViewById(R.id.btnAdd);
        btnSortName = findViewById(R.id.btnSortName);
        btnSortPrice = findViewById(R.id.btnSortPrice);
        searchView = findViewById(R.id.searchView);

        bookDAO = new BookDAO(this);
        bookDAO.getAllBooks();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BookAdapter(filteredList, listener);
        recyclerView.setAdapter(adapter);

        loadBooks();

        // Add Book
        btnAdd.setOnClickListener(v -> {

            Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
            startActivity(intent);

        });

        setupSearch();

        setupSortButtons();
    }

    // Listener cho Adapter
    BookAdapter.OnBookActionListener listener = new BookAdapter.OnBookActionListener() {

        @Override
        public void onEdit(Book book) {

            Intent intent = new Intent(MainActivity.this, EditBookActivity.class);

            intent.putExtra("id", book.getId());
            intent.putExtra("title", book.getTitle());
            intent.putExtra("author", book.getAuthor());
            intent.putExtra("price", book.getPrice());
            intent.putExtra("image", book.getImageUrl());

            startActivity(intent);
        }

        @Override
        public void onDelete(Book book) {

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Delete Book")
                    .setMessage("Are you sure you want to delete this book?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        bookDAO.deleteBook(book.getId());
                        loadBooks();

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    };

    // Load dữ liệu từ database
    private void loadBooks() {

        bookList.clear();
        bookList.addAll(bookDAO.getAllBooks());

        filteredList.clear();
        filteredList.addAll(bookList);

        adapter.notifyDataSetChanged();
    }

    // Search
    private void setupSearch() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filteredList.clear();

                for (Book book : bookList) {

                    if (book.getTitle().toLowerCase().contains(newText.toLowerCase())
                            || book.getAuthor().toLowerCase().contains(newText.toLowerCase())) {

                        filteredList.add(book);
                    }
                }

                adapter.notifyDataSetChanged();

                return true;
            }
        });
    }

    // Sort buttons
    private void setupSortButtons() {

        btnSortName.setOnClickListener(v -> {

            Collections.sort(filteredList, (b1, b2) ->
                    b1.getTitle().compareToIgnoreCase(b2.getTitle()));

            adapter.notifyDataSetChanged();
        });

        btnSortPrice.setOnClickListener(v -> {

            Collections.sort(filteredList, (b1, b2) ->
                    Double.compare(b1.getPrice(), b2.getPrice()));

            adapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void onResume() {

        super.onResume();
        loadBooks();

    }
}