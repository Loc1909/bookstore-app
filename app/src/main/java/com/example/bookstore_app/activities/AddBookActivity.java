package com.example.bookstore_app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookstore_app.R;
import com.example.bookstore_app.database.dao.BookDAO;
import com.example.bookstore_app.models.Book;

public class AddBookActivity extends AppCompatActivity {

    EditText edtTitle, edtAuthor, edtPrice;
    Button btnSave;
    ImageView imgBook;

    BookDAO bookDAO;
    String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        edtTitle = findViewById(R.id.edtTitle);
        edtAuthor = findViewById(R.id.edtAuthor);
        edtPrice = findViewById(R.id.edtPrice);
        imgBook = findViewById(R.id.imgBook);
        btnSave = findViewById(R.id.btnSave);
        bookDAO = new BookDAO(this);

        // chọn ảnh từ gallery
        imgBook.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,100);
        });

        // khi bấm nút Save Book
        btnSave.setOnClickListener(v -> {

            Book book = new Book();

            book.setTitle(edtTitle.getText().toString());
            book.setAuthor(edtAuthor.getText().toString());
            book.setPrice(Double.parseDouble(edtPrice.getText().toString()));
            book.setImageUrl(imagePath);
            bookDAO.insertBook(book);

            finish(); // quay lại MainActivity
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK){

            Uri uri = data.getData();
            imagePath = uri.toString();
            imgBook.setImageURI(uri);

        }
    }
}