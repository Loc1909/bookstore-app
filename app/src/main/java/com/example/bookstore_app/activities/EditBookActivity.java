package com.example.bookstore_app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bookstore_app.R;
import com.example.bookstore_app.database.dao.BookDAO;
import com.example.bookstore_app.models.Book;

public class EditBookActivity extends AppCompatActivity {

    EditText edtTitle, edtAuthor, edtPrice;
    Button btnUpdate;
    ImageView imgBook;

    BookDAO bookDAO;

    int bookId;
    String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        edtTitle = findViewById(R.id.edtTitle);
        edtAuthor = findViewById(R.id.edtAuthor);
        edtPrice = findViewById(R.id.edtPrice);
        btnUpdate = findViewById(R.id.btnUpdate);
        imgBook = findViewById(R.id.imgBook);

        bookDAO = new BookDAO(this);

        // nhận dữ liệu từ MainActivity
        bookId = getIntent().getIntExtra("id", -1);
        String title = getIntent().getStringExtra("title");
        String author = getIntent().getStringExtra("author");
        double price = getIntent().getDoubleExtra("price", 0);
        imagePath = getIntent().getStringExtra("image");

        // set dữ liệu
        edtTitle.setText(title);
        edtAuthor.setText(author);
        edtPrice.setText(String.valueOf(price));

        // hiển thị ảnh
        Glide.with(this)
                .load(imagePath)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imgBook);

        // bấm vào ảnh để chọn ảnh mới
        imgBook.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 100);

        });

        btnUpdate.setOnClickListener(v -> {

            String newTitle = edtTitle.getText().toString().trim();
            String newAuthor = edtAuthor.getText().toString().trim();
            String priceText = edtPrice.getText().toString().trim();

            if (TextUtils.isEmpty(newTitle) || TextUtils.isEmpty(newAuthor) || TextUtils.isEmpty(priceText)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double newPrice = Double.parseDouble(priceText);

            Book book = new Book();
            book.setId(bookId);
            book.setTitle(newTitle);
            book.setAuthor(newAuthor);
            book.setPrice(newPrice);
            book.setImageUrl(imagePath);

            bookDAO.updateBook(book);

            Toast.makeText(this, "Book updated", Toast.LENGTH_SHORT).show();

            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {

            Uri uri = data.getData();

            imagePath = uri.toString();

            Glide.with(this)
                    .load(uri)
                    .into(imgBook);
        }
    }
}