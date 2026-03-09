package com.example.bookstore_app.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore_app.R;
import com.example.bookstore_app.adapters.BookAdapter;
import com.example.bookstore_app.models.Book;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    BookAdapter adapter;
    List<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewBooks);

        bookList = new ArrayList<>();

        // Fake data
        bookList.add(new Book(1,"Clean Code","Robert Martin",20,"",""));
        bookList.add(new Book(2,"Design Patterns","GoF",25,"",""));
        bookList.add(new Book(3,"Android Programming","Google",30,"",""));

        adapter = new BookAdapter(bookList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}