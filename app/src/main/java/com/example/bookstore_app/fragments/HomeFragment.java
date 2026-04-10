package com.example.bookstore_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore_app.activities.BookDetailActivity;
import com.example.bookstore_app.R;
import com.example.bookstore_app.activities.CartActivity;
import com.example.bookstore_app.adapters.BookAdapter;
import com.example.bookstore_app.database.dao.BookDAO;
import com.example.bookstore_app.database.dao.CartDAO;
import com.example.bookstore_app.models.Book;
import com.example.bookstore_app.utils.SessionManager;

import java.util.List;

public class HomeFragment extends Fragment {

    private int currentUserId;
    private SessionManager sessionManager;

    RecyclerView recyclerView;
    SearchView searchView;
    ImageView ivCart;
    BookAdapter adapter;
    BookDAO bookDAO;
    CartDAO cartDAO;

    List<Book> bookList;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView = view.findViewById(R.id.recyclerViewBooks);
        searchView = view.findViewById(R.id.searchView);
        ivCart = view.findViewById(R.id.ivCart);
        ivCart.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CartActivity.class);
            startActivity(intent);
        });
        sessionManager = new SessionManager(requireContext());
        currentUserId = sessionManager.getUserId();

        if (currentUserId == -1) {
            Toast.makeText(requireContext(), "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }

        bookDAO = new BookDAO(getContext());

        cartDAO = new CartDAO(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bookList = bookDAO.getAllBooks();

        adapter = new BookAdapter(bookList, new BookAdapter.OnBookActionListener() {
            @Override
            public void onBookClick(Book book) {
                Intent intent = new Intent(getContext(), BookDetailActivity.class);
                intent.putExtra("book_id", book.getId());
                startActivity(intent);
            }

            @Override
            public void onEdit(Book book) {
            }

            @Override
            public void onDelete(Book book) {
            }

            @Override
            public void onAddToCart(Book book) {
                cartDAO.addToCart(currentUserId, book, 1);
                Toast.makeText(getContext(), "Đã thêm vào giỏ", Toast.LENGTH_SHORT).show();
            }
        }, false);

        recyclerView.setAdapter(adapter);

        setupSearch();
    }

    private void setupSearch() {

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