package com.example.bookstore_app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bookstore_app.R;
import com.example.bookstore_app.database.dao.BookDAO;
import com.example.bookstore_app.database.dao.CategoryDAO;
import com.example.bookstore_app.models.Book;
import com.example.bookstore_app.models.Category;

import java.util.ArrayList;
import java.util.List;

public class EditBookActivity extends AppCompatActivity {

    EditText edtTitle, edtAuthor, edtPrice;
    Spinner spinnerCategory;
    Button btnUpdate;
    ImageView imgBook;

    BookDAO bookDAO;
    CategoryDAO categoryDAO;

    List<Category> categoryList;

    int bookId;
    int currentCategoryId;
    String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        edtTitle = findViewById(R.id.edtTitle);
        edtAuthor = findViewById(R.id.edtAuthor);
        edtPrice = findViewById(R.id.edtPrice);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnUpdate = findViewById(R.id.btnUpdate);
        imgBook = findViewById(R.id.imgBook);

        bookDAO = new BookDAO(this);
        categoryDAO = new CategoryDAO(this);

        loadCategories();

        Intent intent = getIntent();

        bookId = intent.getIntExtra("id", -1);
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        double price = intent.getDoubleExtra("price", 0);
        currentCategoryId = intent.getIntExtra("categoryId", -1);
        imagePath = intent.getStringExtra("image");

        if (bookId == -1) {
            Toast.makeText(this, "Invalid book", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        edtTitle.setText(title);
        edtAuthor.setText(author);
        edtPrice.setText(String.valueOf(price));

        setSpinnerSelection();

        if (imagePath != null && !imagePath.isEmpty()) {

            Glide.with(this)
                    .load(imagePath)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imgBook);
        }

        imgBook.setOnClickListener(v -> {

            Intent pickImage = new Intent(Intent.ACTION_PICK);
            pickImage.setType("image/*");

            startActivityForResult(pickImage, 100);

        });

        btnUpdate.setOnClickListener(v -> updateBook());
    }

    private void loadCategories() {

        categoryList = categoryDAO.getAllCategories();

        List<String> names = new ArrayList<>();

        for (Category c : categoryList) {
            names.add(c.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                names
        );

        spinnerCategory.setAdapter(adapter);
    }

    private void setSpinnerSelection() {

        for (int i = 0; i < categoryList.size(); i++) {

            if (categoryList.get(i).getId() == currentCategoryId) {

                spinnerCategory.setSelection(i);
                break;

            }
        }
    }

    private void updateBook() {

        String newTitle = edtTitle.getText().toString().trim();
        String newAuthor = edtAuthor.getText().toString().trim();
        String priceText = edtPrice.getText().toString().trim();

        if (TextUtils.isEmpty(newTitle) ||
                TextUtils.isEmpty(newAuthor) ||
                TextUtils.isEmpty(priceText)) {

            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double newPrice;

        try {
            newPrice = Double.parseDouble(priceText);
        } catch (Exception e) {

            Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
            return;
        }

        int categoryId =
                categoryList.get(spinnerCategory.getSelectedItemPosition()).getId();

        Book book = new Book();
        book.setId(bookId);
        book.setTitle(newTitle);
        book.setAuthor(newAuthor);
        book.setPrice(newPrice);
        book.setCategoryId(categoryId);
        book.setImageUrl(imagePath);

        bookDAO.updateBook(book);

        Toast.makeText(this, "Book updated successfully", Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            Uri uri = data.getData();

            if (uri != null) {

                imagePath = uri.toString();

                Glide.with(this)
                        .load(uri)
                        .into(imgBook);
            }
        }
    }
}