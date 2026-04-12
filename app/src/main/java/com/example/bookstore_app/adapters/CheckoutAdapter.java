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

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

    private List<CartItem> items;

    public CheckoutAdapter(List<CartItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvQuantity.setText("Số lượng: " + item.getQuantity());
        holder.tvPrice.setText(String.format("%,.0f đ", item.getPrice()));
        
        String imageUrl = item.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (imageUrl.startsWith("http")) {
                Glide.with(holder.itemView.getContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_image_picker)
                        .error(R.drawable.ic_image_picker)
                        .into(holder.ivBook);
            } else {
                Glide.with(holder.itemView.getContext())
                        .load(new File(imageUrl))
                        .placeholder(R.drawable.ic_image_picker)
                        .error(R.drawable.ic_image_picker)
                        .into(holder.ivBook);
            }
        } else {
            holder.ivBook.setImageResource(R.drawable.ic_image_picker);
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvQuantity, tvPrice;
        ImageView ivBook;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvBookTitle);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivBook = itemView.findViewById(R.id.ivBookImage);
        }
    }
}
