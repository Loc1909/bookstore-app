package com.example.bookstore_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookstore_app.R;
import com.example.bookstore_app.models.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HORIZONTAL = 0;
    public static final int TYPE_VERTICAL = 1;

    private static final int VIEW_TYPE_BOOK = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private int layoutType;
    private List<Book> bookList;
    private OnBookActionListener listener;
    private boolean isAdmin;

    public BookAdapter(List<Book> bookList, OnBookActionListener listener, boolean isAdmin, int layoutType) {
        this.bookList = bookList;
        this.listener = listener;
        this.isAdmin = isAdmin;
        this.layoutType = layoutType;
    }

    @Override
    public int getItemViewType(int position) {
        return bookList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_BOOK;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }

        int layoutId = (layoutType == TYPE_VERTICAL)
                ? R.layout.item_book_vertical
                : R.layout.item_book;

        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);

        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof LoadingViewHolder) return;

        Book book = bookList.get(position);
        BookViewHolder h = (BookViewHolder) holder;

        h.txtTitle.setText(book.getTitle());
        h.txtAuthor.setText(book.getAuthor());
        h.txtPrice.setText(book.getPrice() + "đ");

        if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
            Glide.with(h.itemView.getContext())
                    .load(book.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(h.imgBook);
        } else {
            h.imgBook.setImageResource(R.drawable.ic_launcher_background);
        }

        if (!isAdmin) {
            if (h.btnEditBook != null) h.btnEditBook.setVisibility(View.GONE);
            if (h.btnDeleteBook != null) h.btnDeleteBook.setVisibility(View.GONE);
        } else {
            if (h.btnAddToCart != null) h.btnAddToCart.setVisibility(View.GONE);
            if (h.btnBuyNow != null) h.btnBuyNow.setVisibility(View.GONE);
        }

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onBookClick(book);
        });

        if (h.btnAddToCart != null) {
            h.btnAddToCart.setOnClickListener(v -> {
                if (listener != null) listener.onAddToCart(book);
            });
        }

        if (h.btnEditBook != null) {
            h.btnEditBook.setOnClickListener(v -> {
                if (listener != null) listener.onEdit(book);
            });
        }

        if (h.btnDeleteBook != null) {
            h.btnDeleteBook.setOnClickListener(v -> {
                if (listener != null) listener.onDelete(book);
            });
        }
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    // ===== VIEW HOLDER =====
    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtAuthor, txtPrice;
        ImageView imgBook;
        Button btnBuyNow;
        ImageButton btnEditBook, btnDeleteBook, btnAddToCart;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            imgBook = itemView.findViewById(R.id.imgBook);

            btnEditBook = itemView.findViewById(R.id.btnEditBook);
            btnDeleteBook = itemView.findViewById(R.id.btnDeleteBook);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            btnBuyNow = itemView.findViewById(R.id.btnBuyNow);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface OnBookActionListener {
        void onBookClick(Book book);
        void onEdit(Book book);
        void onDelete(Book book);
        void onAddToCart(Book book);
    }

    public void updateData(List<Book> newList) {
        this.bookList.clear();
        this.bookList.addAll(newList);
        notifyDataSetChanged();
    }


}