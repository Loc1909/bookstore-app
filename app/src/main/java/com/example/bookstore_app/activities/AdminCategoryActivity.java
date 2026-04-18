package com.example.bookstore_app.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import android.text.Editable;
import android.text.TextWatcher;

public class AdminCategoryActivity extends AppCompatActivity {

    private RecyclerView rvCategories;
    private FloatingActionButton fabAddCategory;
    private List<Category> categoryList;
    private CategoryAdapter adapter;
    private List<Category> fullCategoryList;
    private CategoryDAO categoryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        TextView tvCategoryCount = findViewById(R.id.tvCategoryCount);
        View btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        rvCategories = findViewById(R.id.rvCategories);
        fabAddCategory = findViewById(R.id.fabAddCategory);

        categoryDAO = new CategoryDAO(this);
        categoryList = new ArrayList<>();
        fullCategoryList = new ArrayList<>();

        EditText etSearchCategory = findViewById(R.id.etSearchCategory);
        etSearchCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCategories(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

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

        fabAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialog(null);
            }
        });
    }

    // ================= LOAD DATA (BACKGROUND THREAD) =================
    private void loadCategories() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Category> data = categoryDAO.getAllCategories();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fullCategoryList.clear();
                        fullCategoryList.addAll(data);
                        
                        // Apply current filter if any
                        EditText etSearchCategory = findViewById(R.id.etSearchCategory);
                        filterCategories(etSearchCategory.getText().toString());
                    }
                });
            }
        }).start();
    }

    private void filterCategories(String query) {
        categoryList.clear();
        if (query.isEmpty()) {
            categoryList.addAll(fullCategoryList);
        } else {
            String lowerQuery = query.toLowerCase().trim();
            for (Category cat : fullCategoryList) {
                if (cat.getName().toLowerCase().contains(lowerQuery)) {
                    categoryList.add(cat);
                }
            }
        }
        adapter.notifyDataSetChanged();
        ((TextView) findViewById(R.id.tvCategoryCount)).setText(String.valueOf(categoryList.size()));
    }

    // ================= ADD / EDIT =================
    private void showCategoryDialog(final Category category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_category, null);
        builder.setView(view);

        final EditText etCategoryName = view.findViewById(R.id.etCategoryName);

        if (category != null) {
            builder.setTitle("Edit Category");
            etCategoryName.setText(category.getName());
        } else {
            builder.setTitle("Add Category");
        }

        builder.setPositiveButton("Save", null);
        builder.setNegativeButton("Cancel", null);

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = etCategoryName.getText().toString().trim();

                if (name.isEmpty()) {
                    etCategoryName.setError("Name cannot be empty");
                    return;
                }

                if (name.length() < 2) {
                    etCategoryName.setError("Name must be at least 2 characters long");
                    return;
                }

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        final boolean success;

                        if (category == null) {
                            Category newCat = new Category();
                            newCat.setName(name);
                            success = categoryDAO.addCategory(newCat);
                        } else {
                            category.setName(name);
                            success = categoryDAO.updateCategory(category);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);

                                if (success) {
                                    Toast.makeText(AdminCategoryActivity.this,
                                            category == null ? "Added successfully" : "Updated successfully",
                                            Toast.LENGTH_SHORT).show();

                                    dialog.dismiss();
                                    loadCategories();
                                } else {
                                    Toast.makeText(AdminCategoryActivity.this,
                                            "Operation failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }

    // ================= DELETE =================
    private void confirmDeleteCategory(final Category category) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Category")
                .setMessage("Are you sure?")
                .setPositiveButton("Delete", (dialog, which) -> {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final boolean success = categoryDAO.deleteCategory(category.getId());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (success) {
                                        Toast.makeText(AdminCategoryActivity.this,
                                                "Deleted successfully",
                                                Toast.LENGTH_SHORT).show();
                                        loadCategories();
                                    } else {
                                        Toast.makeText(AdminCategoryActivity.this,
                                                "Delete failed",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).start();

                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}