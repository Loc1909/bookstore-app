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

import com.example.bookstore_app.R;
import com.example.bookstore_app.database.dao.BookDAO;
import com.example.bookstore_app.database.dao.CategoryDAO;
import com.example.bookstore_app.models.Book;
import com.example.bookstore_app.models.Category;

import java.util.ArrayList;
import java.util.List;

public class AddBookActivity extends AppCompatActivity {

    EditText edtTitle, edtAuthor, edtPrice;
    Spinner spinnerCategory;
    Button btnSave;
    ImageView imgBook;

    BookDAO bookDAO;
    CategoryDAO categoryDAO;

    List<Category> categoryList;

    String imagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        edtTitle = findViewById(R.id.edtTitle);
        edtAuthor = findViewById(R.id.edtAuthor);
        edtPrice = findViewById(R.id.edtPrice);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        imgBook = findViewById(R.id.imgBook);
        btnSave = findViewById(R.id.btnSave);

        bookDAO = new BookDAO(this);
        categoryDAO = new CategoryDAO(this);

        loadCategories();

        imgBook.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");

            startActivityForResult(intent, 100);
        });

        btnSave.setOnClickListener(v -> saveBook());
    }

    private void loadCategories(){

        categoryList = categoryDAO.getAllCategories();

        List<String> names = new ArrayList<>();

        for(Category c : categoryList){
            names.add(c.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                names
        );

        spinnerCategory.setAdapter(adapter);
    }

    private void saveBook(){

        String title = edtTitle.getText().toString().trim();
        String author = edtAuthor.getText().toString().trim();
        String priceText = edtPrice.getText().toString().trim();

        if(TextUtils.isEmpty(title)){
            edtTitle.setError("Title required");
            return;
        }

        if(TextUtils.isEmpty(author)){
            edtAuthor.setError("Author required");
            return;
        }

        if(TextUtils.isEmpty(priceText)){
            edtPrice.setError("Price required");
            return;
        }

        double price;

        try{
            price = Double.parseDouble(priceText);
        }catch (Exception e){
            edtPrice.setError("Invalid price");
            return;
        }

        int categoryId =
                categoryList.get(spinnerCategory.getSelectedItemPosition()).getId();

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPrice(price);
        book.setCategoryId(categoryId);
        book.setImageUrl(imagePath);

        bookDAO.insertBook(book);

        Toast.makeText(this,"Book added successfully",Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == RESULT_OK && data != null){

            Uri uri = data.getData();

            if(uri != null){
                imagePath = uri.toString();
                imgBook.setImageURI(uri);
            }
        }
    }
}