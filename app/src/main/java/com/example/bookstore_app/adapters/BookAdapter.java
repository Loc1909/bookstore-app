package com.example.bookstore_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookstore_app.R;
import com.example.bookstore_app.models.Book;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        this.bookList = (bookList != null) ? bookList : new ArrayList<>();
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

        if (book == null) return;

        h.txtTitle.setText(book.getTitle());
        h.txtAuthor.setText(book.getAuthor());


        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        h.txtPrice.setText(formatter.format(book.getPrice()) + " đ");

        // load ảnh an toàn
        Glide.with(h.itemView.getContext())
                .load(book.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(h.imgBook);

        // ===== ROLE =====
        if (!isAdmin) {
            if (h.btnEditBook != null) h.btnEditBook.setVisibility(View.GONE);
            if (h.btnDeleteBook != null) h.btnDeleteBook.setVisibility(View.GONE);
        } else {
            if (h.btnAddToCart != null) h.btnAddToCart.setVisibility(View.GONE);
            if (h.btnBuyNow != null) h.btnBuyNow.setVisibility(View.GONE);
        }

        // ===== CLICK =====
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onBookClick(book);
        });

        if (h.btnAddToCart != null) {
            h.btnAddToCart.setOnClickListener(v -> {
                if (listener != null) listener.onAddToCart(book);
            });
        }

        if (h.btnBuyNow != null) {
            h.btnBuyNow.setOnClickListener(v -> {
                if (listener != null) listener.onBuyNow(book);
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
        return bookList == null ? 0 : bookList.size();
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

    // ===== INTERFACE =====
    public interface OnBookActionListener {
        void onBookClick(Book book);
        void onEdit(Book book);
        void onDelete(Book book);
        void onAddToCart(Book book);


        void onBuyNow(Book book);
    }

    // ===== UPDATE DATA (DIFFUTIL) =====
    public void updateData(List<Book> newList) {
        if (newList == null) newList = new ArrayList<>();

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(this.bookList, newList));
        this.bookList.clear();
        this.bookList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    static class DiffCallback extends DiffUtil.Callback {

        private final List<Book> oldList;
        private final List<Book> newList;

        public DiffCallback(List<Book> oldList, List<Book> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getId() ==
                    newList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }
    }
}