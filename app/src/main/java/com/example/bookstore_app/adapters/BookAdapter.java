package com.example.bookstore_app.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookstore_app.R;
import com.example.bookstore_app.models.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> bookList;
    private OnBookActionListener listener;

    public BookAdapter(List<Book> bookList, OnBookActionListener listener) {
        this.bookList = bookList;
        this.listener = listener;
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
        holder.txtPrice.setText("Price: $" + book.getPrice());

        // load ảnh nếu có
        if(book.getImageUrl() != null && !book.getImageUrl().isEmpty()){
            Glide.with(holder.itemView.getContext())
                    .load(book.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.imgBook);        } else {
            holder.imgBook.setImageResource(R.drawable.ic_launcher_background);
        }

        // nút edit
        holder.btnEdit.setOnClickListener(v -> {
            if(listener != null){
                listener.onEdit(book);
            }
        });

        // nút delete
        holder.btnDelete.setOnClickListener(v -> {
            if(listener != null){
                listener.onDelete(book);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle, txtAuthor, txtPrice;
        Button btnEdit, btnDelete;
        ImageView imgBook;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            imgBook = itemView.findViewById(R.id.imgBook);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public interface OnBookActionListener{
        void onEdit(Book book);
        void onDelete(Book book);
    }
}