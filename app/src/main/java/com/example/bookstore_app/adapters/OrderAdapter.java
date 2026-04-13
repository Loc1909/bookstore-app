package com.example.bookstore_app.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore_app.R;
import com.example.bookstore_app.database.dao.OrderDAO;
import com.example.bookstore_app.models.Order;
import com.example.bookstore_app.models.OrderItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<Order> list;
    private OnOrderActionListener listener;
    private OrderDAO orderDAO;

    public interface OnOrderActionListener {
        void onCancelOrder(Order order);
    }

    public OrderAdapter(List<Order> list, OnOrderActionListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        if (orderDAO == null) {
            orderDAO = new OrderDAO(parent.getContext());
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = list.get(position);
        holder.tvStatus.setText(order.getStatus());

        // Dynamic status styling
        if ("PENDING".equals(order.getStatus())) {
            holder.tvStatus.setBackgroundResource(R.drawable.badge_blue);
            holder.tvStatus.setTextColor(Color.parseColor("#1976D2"));
            holder.btnCancel.setVisibility(View.VISIBLE);
        } else if ("CANCELLED".equals(order.getStatus())) {
            holder.tvStatus.setBackgroundResource(R.drawable.badge_red);
            holder.tvStatus.setTextColor(Color.parseColor("#D32F2F"));
            holder.btnCancel.setVisibility(View.GONE);
        } else {
            holder.tvStatus.setBackgroundResource(R.drawable.badge_green);
            holder.tvStatus.setTextColor(Color.parseColor("#388E3C"));
            holder.btnCancel.setVisibility(View.GONE);
        }

        holder.tvTotalPrice.setText(String.format("%,.0f đ", order.getTotalPrice()));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.tvDate.setText("Ngày đặt: " + sdf.format(new Date(order.getOrderDate())));

        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.tvEstimateDate.setText("Dự kiến giao hàng: " +
                sdf2.format(new Date(order.getOrderDate() + 4 * 24 * 60 * 60 * 1000)));

        String paymentMethod = order.getPaymentMethod();
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            paymentMethod = "COD";
        }
        holder.tvPaymentMethod.setText("Thanh toán: " + paymentMethod);

        List<OrderItem> items = orderDAO.getOrderItemsByOrderId(order.getId());
        StringBuilder details = new StringBuilder();
        for (OrderItem item : items) {
            details.append("- ")
                    .append(item.getBookTitle()).append("\n  ")
                    .append(item.getQuantity())
                    .append(" x ")
                    .append(String.format("%,.0f đ", item.getPrice()))
                    .append("\n");
        }
        if (details.length() > 0) {
            details.setLength(details.length() - 1);
        }
        holder.tvOrderDetails.setText(details.toString());

        if (holder.btnCancel.getVisibility() == View.VISIBLE) {
            holder.btnCancel.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onCancelOrder(order);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus, tvTotalPrice, tvDate, tvEstimateDate, tvOrderDetails, tvPaymentMethod;
        Button btnCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvTotalPrice = itemView.findViewById(R.id.tvOrderTotalPrice);
            tvDate = itemView.findViewById(R.id.tvOrderDate);
            tvEstimateDate = itemView.findViewById(R.id.tvEstimateDate);
            tvOrderDetails = itemView.findViewById(R.id.tvOrderDetails);
            tvPaymentMethod = itemView.findViewById(R.id.tvOrderPaymentMethod);
            btnCancel = itemView.findViewById(R.id.btnCancelOrder);
        }
    }
}
