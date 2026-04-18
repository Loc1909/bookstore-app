package com.example.bookstore_app.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
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
import com.example.bookstore_app.database.dao.CategoryDAO;
import com.example.bookstore_app.models.Book;
import com.example.bookstore_app.utils.SessionManager;
import com.example.bookstore_app.models.Category;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class HomeFragment extends Fragment {

    private int currentUserId;
    private SessionManager sessionManager;
    private RecyclerView recyclerView;
    private RecyclerView recyclerBooks, recyclerFeatured;
    private SearchView searchView;
    private ImageView ivCart;
    private TextView txtGreeting;
    private ChipGroup chipGroupCategories;
    private BookAdapter adapterBooks, adapterFeatured;
    private BookDAO bookDAO;
    private CartDAO cartDAO;

    // ===== DATA SEPARATION =====
    private final List<Book> bookList = new ArrayList<>();         // ALL BOOKS (paging)
    private final List<Book> featuredList = new ArrayList<>();     // FILTERED BOOKS

    // ===== FILTER STATE (ONLY FEATURED) =====
    private int currentCategoryId = -1;
    private String currentKeyword = "";

    // ===== PAGINATION (ONLY BOOKS) =====
    private final int limit = 5;
    private int offset = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    private final Handler handler = new Handler();
    private Runnable searchRunnable;

    public HomeFragment() {
        super(R.layout.fragment_home);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerBooks = view.findViewById(R.id.recyclerBooks);
        recyclerFeatured = view.findViewById(R.id.recyclerFeatured);
        searchView = view.findViewById(R.id.searchView);
        ivCart = view.findViewById(R.id.ivCart);
        txtGreeting = view.findViewById(R.id.txtGreeting);
        sessionManager = new SessionManager(requireContext());
        currentUserId = sessionManager.getUserId();

        if (currentUserId == -1) {
            Toast.makeText(requireContext(), "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }
        chipGroupCategories = view.findViewById(R.id.chipGroupCategories);

        bookDAO = new BookDAO(getContext());
        cartDAO = new CartDAO(getContext());

        ivCart.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), CartActivity.class))
        );

        txtGreeting.setText(getGreeting());

        setupBooksRecycler();
        setupFeaturedRecycler();
        loadCategoryChips();
        loadFirstBooks();
        loadFeatured();
        setupSearch();
    }

    private String getGreeting() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour >= 5 && hour < 12) {
            return "Chào buổi sáng ☀️";
        } else if (hour >= 12 && hour < 18) {
            return "Chào buổi chiều 🌤️";
        } else {
            return "Chào buổi tối 🌙";
        }
    }

    // ================= BOOKS (PAGING - ALWAYS ALL BOOKS) =================

    private void setupBooksRecycler() {
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        recyclerBooks.setLayoutManager(lm);

        adapterBooks = new BookAdapter(
                bookList,
                new BookAdapter.OnBookActionListener() {

                    @Override
                    public void onBuyNow(Book book) {
                        // xử lý sau này
                    }
                    @Override
                    public void onBookClick(Book book) {
                        Intent intent = new Intent(getContext(), BookDetailActivity.class);
                        intent.putExtra("book_id", book.getId());
                        startActivity(intent);
                    }
                    @Override public void onEdit(Book book) {}
                    @Override public void onDelete(Book book) {}

                    @Override
                    public void onAddToCart(Book book) {
                        int cartId = cartDAO.getCartIdByUser(currentUserId);
                        cartDAO.addToCart(cartId, book, 1);
                        Toast.makeText(getContext(), "Đã thêm vào giỏ", Toast.LENGTH_SHORT).show();
                    }
                },
                false,
                BookAdapter.TYPE_HORIZONTAL
        );

        recyclerBooks.setAdapter(adapterBooks);

        recyclerBooks.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                int total = lm.getItemCount();
                int last = lm.findLastVisibleItemPosition();

                if (!isLoading && !isLastPage && last >= total - 2) {
                    loadMoreBooks();
                }
            }
        });
    }

    private void loadFirstBooks() {
        offset = 0;
        isLastPage = false;
        isLoading = false;

        bookList.clear();

        List<Book> data = bookDAO.getBooksPaging(limit, offset);

        if (data != null && !data.isEmpty()) {
            bookList.addAll(data);
            offset += limit;
        } else {
            isLastPage = true;
        }

        adapterBooks.notifyDataSetChanged();
    }

    private void loadMoreBooks() {
        if (isLoading || isLastPage) return;

        isLoading = true;

        bookList.add(null);
        adapterBooks.notifyItemInserted(bookList.size() - 1);

        new Handler().postDelayed(() -> {

            int pos = bookList.size() - 1;
            bookList.remove(pos);
            adapterBooks.notifyItemRemoved(pos);

            List<Book> data = bookDAO.getBooksPaging(limit, offset);

            if (data == null || data.isEmpty()) {
                isLastPage = true;
            } else {
                bookList.addAll(data);
                offset += limit;
                adapterBooks.notifyDataSetChanged();
            }

            isLoading = false;

        }, 500);
    }

    // ================= FEATURED (FILTER ONLY - NO PAGING) =================

    private void setupFeaturedRecycler() {
        recyclerFeatured.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        adapterFeatured = new BookAdapter(
                featuredList,
                new BookAdapter.OnBookActionListener() {

                    @Override
                    public void onBuyNow(Book book) {
                        // theem sau này
                    }
                    @Override
                    public void onBookClick(Book book) {
                        Intent intent = new Intent(getContext(), BookDetailActivity.class);
                        intent.putExtra("book_id", book.getId());
                        startActivity(intent);
                    }
                    @Override public void onEdit(Book book) {}
                    @Override public void onDelete(Book book) {}

                    @Override
                    public void onAddToCart(Book book) {
                        int cartId = cartDAO.getCartIdByUser(currentUserId);
                        cartDAO.addToCart(cartId, book, 1);
                        Toast.makeText(getContext(), "Đã thêm vào giỏ", Toast.LENGTH_SHORT).show();
                    }
                },
                false,
                BookAdapter.TYPE_VERTICAL
        );

        recyclerFeatured.setAdapter(adapterFeatured);
    }

    private void loadFeatured() {
        featuredList.clear();
        featuredList.addAll(getFilteredBooks());
        adapterFeatured.notifyDataSetChanged();
    }

    private List<Book> getFilteredBooks() {

        String keyword = currentKeyword == null ? "" : currentKeyword.trim();

        if (currentCategoryId == -1) {
            if (keyword.isEmpty()) {
                return bookDAO.getBooksPaging(10, 0);
            } else {
                return bookDAO.searchBooks(keyword, 10, 0);
            }
        } else {
            return bookDAO.getBooksByCategory(currentCategoryId, keyword, 10, 0);
        }
    }

    // ================= CATEGORY =================

    private void loadCategoryChips() {
        chipGroupCategories.removeAllViews();

        Chip all = new Chip(getContext());
        all.setText("Tất cả");
        all.setCheckable(true);
        all.setChecked(true);

        all.setOnClickListener(v -> {
            currentCategoryId = -1;
            currentKeyword = "";
            searchView.setQuery("", false);

            loadFeatured();
        });

        chipGroupCategories.addView(all);

        List<Category> categories = new CategoryDAO(getContext()).getAllCategories();

        for (Category c : categories) {
            Chip chip = new Chip(getContext());
            chip.setText(c.getName());
            chip.setCheckable(true);

            chip.setOnClickListener(v -> {
                currentCategoryId = c.getId();
                currentKeyword = "";
                searchView.setQuery("", false);

                loadFeatured();
            });

            chipGroupCategories.addView(chip);
        }
    }

    // ================= SEARCH (ONLY FEATURED) =================

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                currentKeyword = query.trim();
                loadFeatured();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                handler.removeCallbacks(searchRunnable);

                searchRunnable = () -> {
                    currentKeyword = newText.trim();
                    loadFeatured();
                };

                handler.postDelayed(searchRunnable, 300);

                return true;
            }
        });
    }
}