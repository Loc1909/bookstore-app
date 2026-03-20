package com.example.bookstore_app.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore_app.R;
import com.example.bookstore_app.adapters.CategoryAdapter;
import com.example.bookstore_app.database.dao.CategoryDAO;
import com.example.bookstore_app.models.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AdminCategoryActivity extends AppCompatActivity {

    private RecyclerView rvCategories;
    private FloatingActionButton fabAddCategory;
    private CategoryAdapter adapter;
    private List<Category> categoryList;
    private CategoryDAO categoryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        Toolbar toolbar = findViewById(R.id.toolbarCategory);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        rvCategories = findViewById(R.id.rvCategories);
        fabAddCategory = findViewById(R.id.fabAddCategory);

        categoryDAO = new CategoryDAO(this);
        categoryList = new ArrayList<>();

        adapter = new CategoryAdapter(categoryList, new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onEditClick(Category category) {
                showCategoryDialog(category);
            }

            @Override
            public void onDeleteClick(Category category) {
                confirmDeleteCategory(category);
            }
        });

        rvCategories.setLayoutManager(new LinearLayoutManager(this));
        rvCategories.setAdapter(adapter);

        loadCategories();

        fabAddCategory.setOnClickListener(v -> showCategoryDialog(null));
    }

    private void loadCategories() {
        categoryList.clear();
        categoryList.addAll(categoryDAO.getAllCategories());
        adapter.notifyDataSetChanged();
    }

    private void showCategoryDialog(Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_category, null);
        builder.setView(view);

        EditText etCategoryName = view.findViewById(R.id.etCategoryName);

        if (category != null) {
            builder.setTitle("Edit Category");
            etCategoryName.setText(category.getName());
        } else {
            builder.setTitle("Add Category");
        }

        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = etCategoryName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(AdminCategoryActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (category == null) {
                // Add
                Category newCat = new Category();
                newCat.setName(name);
                if (categoryDAO.addCategory(newCat)) {
                    Toast.makeText(AdminCategoryActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                    loadCategories();
                } else {
                    Toast.makeText(AdminCategoryActivity.this, "Failed to add", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Edit
                category.setName(name);
                if (categoryDAO.updateCategory(category)) {
                    Toast.makeText(AdminCategoryActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                    loadCategories();
                } else {
                    Toast.makeText(AdminCategoryActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void confirmDeleteCategory(Category category) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Category")
                .setMessage("Are you sure you want to delete this category?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (categoryDAO.deleteCategory(category.getId())) {
                        Toast.makeText(AdminCategoryActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                        loadCategories();
                    } else {
                        Toast.makeText(AdminCategoryActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
