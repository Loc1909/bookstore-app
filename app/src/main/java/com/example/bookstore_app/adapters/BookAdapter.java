package com.example.bookstore_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookstore_app.R;
import com.example.bookstore_app.models.Book;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> implements Filterable {

    private List<Book> bookList;
    private List<Book> bookListFull;
    private OnBookActionListener listener;
    private boolean isAdmin;

    public BookAdapter(List<Book> bookList, OnBookActionListener listener, boolean isAdmin) {

        this.bookList = new ArrayList<>(bookList);
        this.bookListFull = new ArrayList<>(bookList);
        this.listener = listener;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);

        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

        Book book = bookList.get(position);

        holder.txtTitle.setText(book.getTitle());
        holder.txtAuthor.setText(book.getAuthor());
        holder.txtPrice.setText(String.format("%,.0fđ", book.getPrice()));

        if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {

            Glide.with(holder.itemView.getContext())
                    .load(book.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.imgBook);

        } else {

            holder.imgBook.setImageResource(R.drawable.ic_launcher_background);
        }

        if (!isAdmin) {
            holder.btnEditBook.setVisibility(View.GONE);
            holder.btnDeleteBook.setVisibility(View.GONE);
        } else {
            holder.btnAddToCart.setVisibility(View.GONE);
        }

        // Click item
        holder.itemView.setOnClickListener(v -> {

            int pos = holder.getAdapterPosition();

            if (listener != null && pos != RecyclerView.NO_POSITION) {
                listener.onBookClick(bookList.get(pos));
            }
        });

        // Edit book
        holder.btnEditBook.setOnClickListener(v -> {

            int pos = holder.getAdapterPosition();

            if (listener != null && pos != RecyclerView.NO_POSITION) {
                listener.onEdit(bookList.get(pos));
            }
        });

        // Delete book
        holder.btnDeleteBook.setOnClickListener(v -> {

            int pos = holder.getAdapterPosition();

            if (listener != null && pos != RecyclerView.NO_POSITION) {
                listener.onDelete(bookList.get(pos));
            }
        });

        //Add book to cart
        holder.btnAddToCart.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddToCart(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtAuthor, txtPrice;
        ImageView imgBook;
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
        }
    }

    // Update RecyclerView data
    public void updateData(List<Book> newList) {

        bookList.clear();
        bookList.addAll(newList);

        bookListFull.clear();
        bookListFull.addAll(newList);

        notifyDataSetChanged();
    }

    public interface OnBookActionListener {

        void onBookClick(Book book);

        void onEdit(Book book);

        void onDelete(Book book);

        void onAddToCart(Book book);
    }

    // SEARCH FILTER

    @Override
    public Filter getFilter() {
        return bookFilter;
    }

    private Filter bookFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Book> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {

                filteredList.addAll(bookListFull);

            } else {

                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Book book : bookListFull) {

                    if (book.getTitle().toLowerCase().contains(filterPattern)
                            || book.getAuthor().toLowerCase().contains(filterPattern)) {

                        filteredList.add(book);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            bookList.clear();
            bookList.addAll((List<Book>) results.values);

            notifyDataSetChanged();
        }
    };
}