package com.example.bookstore_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore_app.models.OrderItem;

import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {

    private List<OrderItem> list;

    public OrderItemAdapter(List<OrderItem> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int pos) {
        OrderItem i = list.get(pos);

        h.title.setText(i.getBookTitle());
        h.subtitle.setText(i.getQuantity() + " x " + i.getPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;

        public ViewHolder(View v) {
            super(v);

            title = v.findViewById(android.R.id.text1);
            subtitle = v.findViewById(android.R.id.text2);

            title.setTextColor(android.graphics.Color.BLACK);
            subtitle.setTextColor(android.graphics.Color.BLACK);
        }
    }
}