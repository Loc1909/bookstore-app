package com.example.bookstore_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore_app.R;
import com.example.bookstore_app.activities.CartActivity;
import com.example.bookstore_app.adapters.BookAdapter;
import com.example.bookstore_app.database.dao.BookDAO;
import com.example.bookstore_app.database.dao.CartDAO;
import com.example.bookstore_app.models.Book;

import java.util.ArrayList;
import java.util.List;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.example.bookstore_app.database.dao.CategoryDAO;
import com.example.bookstore_app.models.Category;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerBooks, recyclerFeatured;
    private SearchView searchView;
    private ImageView ivCart;
    private android.widget.TextView tvCartBadge;

    private ChipGroup chipGroupCategories;
    private int currentCategoryId = -1; // -1 = tất cả

    private BookAdapter adapterBooks, adapterFeatured;
    private BookDAO bookDAO;
    private CartDAO cartDAO;

    private List<Book> bookList = new ArrayList<>();
    private List<Book> featuredList = new ArrayList<>();

    private String currentKeyword = "";

    // ===== PAGINATION =====
    private final int limit = 5;
    private int offset = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    // debounce
    private Handler handler = new Handler();
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
        tvCartBadge = view.findViewById(R.id.tvCartBadge);
        chipGroupCategories = view.findViewById(R.id.chipGroupCategories);

// Load category chips
        loadCategoryChips();

        ivCart.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), CartActivity.class))
        );

        bookDAO = new BookDAO(getContext());
        cartDAO = new CartDAO(getContext());
        updateCartBadge();

        // ===== RECYCLER BOOKS =====
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerBooks.setLayoutManager(layoutManager);

        adapterBooks = new BookAdapter(
                bookList,
                new BookAdapter.OnBookActionListener() {
                    @Override public void onBookClick(Book book) {}

                    @Override public void onEdit(Book book) {}

                    @Override public void onDelete(Book book) {}

                    @Override
                    public void onAddToCart(Book book) {
                        cartDAO.addToCart(book);
                        Toast.makeText(getContext(), "Đã thêm vào giỏ", Toast.LENGTH_SHORT).show();
                        updateCartBadge();
                    }
                },
                false,
                BookAdapter.TYPE_HORIZONTAL
        );

        recyclerBooks.setAdapter(adapterBooks);

        // ===== LOAD MORE =====
        recyclerBooks.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {

                int total = layoutManager.getItemCount();
                int last = layoutManager.findLastVisibleItemPosition();

                if (!isLoading && !isLastPage && last >= total - 2) {
                    loadMore();
                }
            }
        });

        // ===== FEATURED =====
        recyclerFeatured.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        adapterFeatured = new BookAdapter(
                featuredList,
                new BookAdapter.OnBookActionListener() {
                    @Override public void onBookClick(Book book) {}

                    @Override public void onEdit(Book book) {}

                    @Override public void onDelete(Book book) {}

                    @Override
                    public void onAddToCart(Book book) {
                        cartDAO.addToCart(book);
                        Toast.makeText(getContext(), "Đã thêm vào giỏ", Toast.LENGTH_SHORT).show();
                        updateCartBadge();
                    }
                },
                false,
                BookAdapter.TYPE_VERTICAL
        );

        recyclerFeatured.setAdapter(adapterFeatured);

        loadFirstPage();
        setupSearch();
    }

    private void loadCategoryChips() {
        chipGroupCategories.removeAllViews();

        // Chip "Tất cả"
        Chip chipAll = new Chip(getContext());
        chipAll.setText("Tất cả");
        chipAll.setCheckable(true);
        chipAll.setChecked(currentCategoryId == -1);
        chipAll.setOnClickListener(v -> {
            currentCategoryId = -1;
            loadFirstPage();
            updateChipSelection(chipAll);
        });
        chipGroupCategories.addView(chipAll);

        // Load categories từ database
        List<Category> categories = new CategoryDAO(getContext()).getAllCategories();

        for (Category cat : categories) {
            Chip chip = new Chip(getContext());
            chip.setText(cat.getName());
            chip.setCheckable(true);
            chip.setChecked(cat.getId() == currentCategoryId);

            chip.setOnClickListener(v -> {
                currentCategoryId = cat.getId();
                loadFirstPage();
                updateChipSelection(chip);
            });

            chipGroupCategories.addView(chip);
        }
    }

    private void updateChipSelection(Chip selectedChip) {
        int count = chipGroupCategories.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = chipGroupCategories.getChildAt(i);
            if (v instanceof Chip) {
                ((Chip) v).setChecked(v == selectedChip);
            }
        }
    }

    // ===== LOAD FIRST =====
    private void loadFirstPage() {
        offset = 0;
        isLastPage = false;
        bookList.clear();

        List<Book> newBooks = getData();

        if (newBooks != null && !newBooks.isEmpty()) {
            bookList.addAll(newBooks);
        } else {
            isLastPage = true;
        }

        adapterBooks.notifyDataSetChanged();
        offset += limit;

        updateFeatured();
    }

    // ===== LOAD MORE =====
    private void loadMore() {

        if (isLoading || isLastPage) return;

        isLoading = true;

        // add loading
        bookList.add(null);
        adapterBooks.notifyItemInserted(bookList.size() - 1);

        new Handler().postDelayed(() -> {

            int pos = bookList.size() - 1;
            if (pos >= 0) {
                bookList.remove(pos);
                adapterBooks.notifyItemRemoved(pos);
            }

            List<Book> newBooks = getData();

            if (newBooks == null || newBooks.isEmpty()) {
                isLastPage = true;
            } else {
                bookList.addAll(newBooks);
                adapterBooks.notifyDataSetChanged();
                offset += limit;
            }

            isLoading = false;

        }, 800);
    }

    // ===== DATA SOURCE =====
    private List<Book> getData() {

        String keyword = currentKeyword == null ? "" : currentKeyword.trim();

        if (currentCategoryId == -1) {
            // không lọc category
            if (keyword.isEmpty()) {
                return bookDAO.getBooksPaging(limit, offset);
            } else {
                return bookDAO.searchBooks(keyword, limit, offset);
            }
        } else {
            // có lọc category
            return bookDAO.getBooksByCategory(
                    currentCategoryId,
                    keyword,
                    limit,
                    offset
            );
        }
    }

    // ===== FEATURED =====
    private void updateFeatured() {
        featuredList.clear();

        for (int i = 0; i < Math.min(limit, bookList.size()); i++) {
            if (bookList.get(i) != null) {
                featuredList.add(bookList.get(i));
            }
        }

        adapterFeatured.notifyDataSetChanged();
    }

    // ===== SEARCH =====
    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                currentKeyword = query.trim();
                loadFirstPage();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                handler.removeCallbacks(searchRunnable);

                searchRunnable = () -> {
                    currentKeyword = newText.trim();
                    loadFirstPage();
                };

                handler.postDelayed(searchRunnable, 400);

                return true;
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        updateCartBadge();
    }

    private void updateCartBadge() {
        int total = cartDAO.getTotalQuantity();
        if (total > 0) {
            tvCartBadge.setVisibility(View.VISIBLE);
            tvCartBadge.setText(String.valueOf(total));
        } else {
            tvCartBadge.setVisibility(View.GONE);
        }
    }
}