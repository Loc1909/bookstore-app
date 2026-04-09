package com.example.bookstore_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookstore_app.R;
import com.example.bookstore_app.models.CartItem;

import java.io.File;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> list;
    private OnCartActionListener listener;

    public interface OnCartActionListener {
        void onQuantityChanged(CartItem item, int newQuantity);
        void onDelete(CartItem item);
    }

    public CartAdapter(List<CartItem> list, OnCartActionListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = list.get(position);

        holder.tvTitle.setText(item.getTitle());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.tvPrice.setText(String.format("%.2f đ", item.getPrice()));

        // Hiển thị ảnh sách
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            File imageFile = new File(item.getImageUrl());
            Glide.with(holder.itemView.getContext())
                    .load(imageFile)
                    .placeholder(R.drawable.ic_image_picker)
                    .into(holder.imgBook);
        } else {
            holder.imgBook.setImageResource(R.drawable.ic_image_picker);
        }

        holder.btnPlus.setOnClickListener(v -> listener.onQuantityChanged(item, item.getQuantity() + 1));

        holder.btnMinus.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                listener.onQuantityChanged(item, item.getQuantity() - 1);
            } else {
                listener.onDelete(item);
            }
        });

        holder.btnDelete.setOnClickListener(v -> listener.onDelete(item));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuantity, tvPrice, tvTitle;
        View btnMinus, btnPlus, btnDelete;
        ImageView imgBook;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            imgBook = itemView.findViewById(R.id.imgBook);
        }
    }
}