package com.example.bookstore_app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

    ImageButton btnBack;
    FrameLayout imgPickerArea;
    LinearLayout imgPlaceholder;

    BookDAO bookDAO;
    CategoryDAO categoryDAO;

    List<Category> categoryList;

    int bookId;
    int currentCategoryId;
    String imagePath = "";

    ActivityResultLauncher<Intent> imagePickerLauncher;

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
        imgPickerArea = findViewById(R.id.imgPickerArea);
        imgPlaceholder = findViewById(R.id.imgPlaceholder);
        btnBack = findViewById(R.id.btnBack);
        bookDAO = new BookDAO(this);
        categoryDAO = new CategoryDAO(this);

        loadCategories();

        // Image picker
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            imagePath = uri.toString();

                            Glide.with(this).load(uri).into(imgBook);

                            imgBook.setVisibility(ImageView.VISIBLE);
                            imgPlaceholder.setVisibility(LinearLayout.GONE);
                        }
                    }
                }
        );

        // Get data
        Intent intent = getIntent();

        bookId = intent.getIntExtra("id", -1);
        currentCategoryId = intent.getIntExtra("categoryId", -1);
        imagePath = intent.getStringExtra("image");

        edtTitle.setText(intent.getStringExtra("title"));
        edtAuthor.setText(intent.getStringExtra("author"));
        edtPrice.setText(String.valueOf(intent.getDoubleExtra("price", 0)));

        setSpinnerSelection();

        // Load image cũ
        if (imagePath != null && !imagePath.isEmpty()) {
            Glide.with(this).load(imagePath).into(imgBook);

            imgBook.setVisibility(ImageView.VISIBLE);
            imgPlaceholder.setVisibility(LinearLayout.GONE);
        }

        // Click chọn ảnh
        imgPickerArea.setOnClickListener(v -> {
            Intent pick = new Intent(Intent.ACTION_PICK);
            pick.setType("image/*");
            imagePickerLauncher.launch(pick);
        });

        btnUpdate.setOnClickListener(v -> updateBook());
        btnBack.setOnClickListener( v -> finish());
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

        String title = edtTitle.getText().toString().trim();
        String author = edtAuthor.getText().toString().trim();
        String priceText = edtPrice.getText().toString().trim();

        if (TextUtils.isEmpty(title) ||
                TextUtils.isEmpty(author) ||
                TextUtils.isEmpty(priceText)) {

            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (Exception e) {
            Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
            return;
        }

        int categoryId = categoryList
                .get(spinnerCategory.getSelectedItemPosition())
                .getId();

        Book book = new Book();
        book.setId(bookId);
        book.setTitle(title);
        book.setAuthor(author);
        book.setPrice(price);
        book.setCategoryId(categoryId);
        book.setImageUrl(imagePath);

        bookDAO.updateBook(book);

        Toast.makeText(this, "Book updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}