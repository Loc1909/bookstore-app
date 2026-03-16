package com.example.bookstore_app.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore_app.R;
import com.example.bookstore_app.adapters.BookAdapter;
import com.example.bookstore_app.database.dao.BookDAO;
import com.example.bookstore_app.models.Book;

import java.util.List;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    SearchView searchView;

    BookAdapter adapter;
    BookDAO bookDAO;

    List<Book> bookList;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = view.findViewById(R.id.recyclerViewBooks);
        searchView = view.findViewById(R.id.searchView);

        bookDAO = new BookDAO(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bookList = bookDAO.getAllBooks();

        adapter = new BookAdapter(bookList, null, false);
        recyclerView.setAdapter(adapter);

        setupSearch();
    }

    private void setupSearch(){

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
}