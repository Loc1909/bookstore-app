package com.example.bookstore_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore_app.R;
import com.example.bookstore_app.models.Order;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderAdminAdapter extends RecyclerView.Adapter<OrderAdminAdapter.ViewHolder> {

    private List<Order> list;
    private Map<Integer, String> userMap;
    private OnOrderAdminActionListener listener;

    public interface OnOrderAdminActionListener {
        void onClick(Order order);
        void onConfirm(Order order);
        void onShip(Order order);
        void onComplete(Order order);
        void onCancel(Order order);
    }

    public OrderAdminAdapter(List<Order> list,
                             Map<Integer, String> userMap,
                             OnOrderAdminActionListener listener) {
        this.list = list;
        this.userMap = userMap;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_admin, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        Order o = list.get(position);

        // ===== ID =====
        h.tvOrderId.setText("#ORD" + o.getId());

        // ===== TOTAL =====
        DecimalFormat df = new DecimalFormat("#,###");
        h.tvTotalPrice.setText(df.format(o.getTotalPrice()) + "đ");

        // ===== DATE =====
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        h.tvDate.setText(sdf.format(new Date(o.getOrderDate())));

        // ===== CUSTOMER (FAST - dùng map) =====
        String name = userMap.get(o.getUserId());
        h.tvCustomerName.setText(
                name != null ? name : "User #" + o.getUserId()
        );

        // ===== STATUS =====
        bindStatus(h, o.getStatus());

        // ===== CLICK =====
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(o);
        });

        // ===== ACTION =====
        setupAction(h, o);
    }

    private void bindStatus(ViewHolder h, String status) {
        switch (status) {
            case "PENDING":
                h.tvStatus.setText("Chờ xử lý");
                h.tvStatus.setBackgroundResource(R.drawable.bg_status_pending);
                break;

            case "CONFIRMED":
                h.tvStatus.setText("Đã xác nhận");
                h.tvStatus.setBackgroundResource(R.drawable.bg_status_shipping);
                break;

            case "SHIPPING":
                h.tvStatus.setText("Đang giao");
                h.tvStatus.setBackgroundResource(R.drawable.bg_status_shipping);
                break;

            case "COMPLETED":
                h.tvStatus.setText("Hoàn thành");
                h.tvStatus.setBackgroundResource(R.drawable.bg_status_done);
                break;

            case "CANCELLED":
                h.tvStatus.setText("Đã hủy");
                h.tvStatus.setBackgroundResource(R.drawable.bg_status_cancel);
                break;
        }
    }

    private void setupAction(ViewHolder h, Order o) {

        h.btnAction.setVisibility(View.GONE);
        h.btnCancel.setVisibility(View.GONE);

        switch (o.getStatus()) {

            case "PENDING":
                h.btnAction.setText("Xác nhận");
                h.btnAction.setVisibility(View.VISIBLE);
                h.btnCancel.setVisibility(View.VISIBLE);

                h.btnAction.setOnClickListener(v -> {
                    if (listener != null) listener.onConfirm(o);
                });

                h.btnCancel.setOnClickListener(v -> {
                    if (listener != null) listener.onCancel(o);
                });
                break;

            case "CONFIRMED":
                h.btnAction.setText("Giao hàng");
                h.btnAction.setVisibility(View.VISIBLE);

                h.btnAction.setOnClickListener(v -> {
                    if (listener != null) listener.onShip(o);
                });
                break;

            case "SHIPPING":
                h.btnAction.setText("Hoàn thành");
                h.btnAction.setVisibility(View.VISIBLE);

                h.btnAction.setOnClickListener(v -> {
                    if (listener != null) listener.onComplete(o);
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    // ===== VIEW HOLDER =====
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvOrderId, tvCustomerName, tvTotalPrice, tvStatus, tvDate;
        TextView btnAction, btnCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDate = itemView.findViewById(R.id.tvDate);

            btnAction = itemView.findViewById(R.id.btnAction);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}