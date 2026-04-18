package com.example.bookstore_app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookstore_app.R;
import com.example.bookstore_app.database.dao.BookDAO;
import com.example.bookstore_app.database.dao.CategoryDAO;
import com.example.bookstore_app.models.Book;
import com.example.bookstore_app.models.Category;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddBookActivity extends AppCompatActivity {

    EditText edtTitle, edtAuthor, edtPrice;
    Spinner spinnerCategory;

    ImageButton btnBack;
    Button btnSave;
    ImageView imgBook;
    FrameLayout imgPickerArea;
    LinearLayout imgPlaceholder;

    BookDAO bookDAO;
    CategoryDAO categoryDAO;

    List<Category> categoryList;
    String imagePath = "";

    ActivityResultLauncher<Intent> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        // Bind view
        edtTitle = findViewById(R.id.edtTitle);
        edtAuthor = findViewById(R.id.edtAuthor);
        edtPrice = findViewById(R.id.edtPrice);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        imgBook = findViewById(R.id.imgBook);
        btnSave = findViewById(R.id.btnSave);
        imgPickerArea = findViewById(R.id.imgPickerArea);
        imgPlaceholder = findViewById(R.id.imgPlaceholder);
        btnBack = findViewById(R.id.btnBack);
        bookDAO = new BookDAO(this);
        categoryDAO = new CategoryDAO(this);

        loadCategories();

        // Activity Result API
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            String path = saveImageToInternalStorage(uri);
                            if (path != null) {
                                imagePath = path;  // dùng để insert vào DB
                                imgBook.setImageURI(Uri.fromFile(new File(path))); // hiển thị trực tiếp
                                imgBook.setVisibility(ImageView.VISIBLE);
                                imgPlaceholder.setVisibility(LinearLayout.GONE);
                            }

                            imgBook.setVisibility(ImageView.VISIBLE);
                            imgPlaceholder.setVisibility(LinearLayout.GONE);
                        }
                    }
                }
        );

        // Click vùng chọn ảnh
        imgPickerArea.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnSave.setOnClickListener(v -> saveBook());
        btnBack.setOnClickListener(v-> finish());
    }

    // Hàm lưu ảnh vào internal storage và trả về đường dẫn tuyệt đối
    private String saveImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);

            File file = new File(getFilesDir(), "book_" + System.currentTimeMillis() + ".jpg");

            OutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();

            return file.getAbsolutePath(); //  trả về path chuẩn để lưu DB
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    private void saveBook() {

        String title = edtTitle.getText().toString().trim();
        String author = edtAuthor.getText().toString().trim();
        String priceText = edtPrice.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            edtTitle.setError("Title required");
            return;
        }

        if (TextUtils.isEmpty(author)) {
            edtAuthor.setError("Author required");
            return;
        }

        if (TextUtils.isEmpty(priceText)) {
            edtPrice.setError("Price required");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (Exception e) {
            edtPrice.setError("Invalid price");
            return;
        }

        int categoryId = categoryList
                .get(spinnerCategory.getSelectedItemPosition())
                .getId();

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPrice(price);
        book.setCategoryId(categoryId);
        book.setImageUrl(imagePath);

        bookDAO.insertBook(book);

        Toast.makeText(this, "Book added successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}