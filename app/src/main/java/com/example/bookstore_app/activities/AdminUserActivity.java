package com.example.bookstore_app.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore_app.R;
import com.example.bookstore_app.adapters.UserAdapter;
import com.example.bookstore_app.database.dao.UserDAO;
import com.example.bookstore_app.models.User;
import com.example.bookstore_app.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AdminUserActivity extends AppCompatActivity {

    private RecyclerView rvUsers;
    private FloatingActionButton fabAddUser;
    private UserAdapter adapter;
    private List<User> userList;
    private UserDAO userDAO;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Safety check logic like AdminDashboardActivity
        sessionManager = new SessionManager(this);
        // if (!sessionManager.isLoggedIn() || !sessionManager.isAdmin()) {
        //     Toast.makeText(this, "You don't have permission to access this area", Toast.LENGTH_LONG).show();
        //     finish();
        //     return;
        // }

        setContentView(R.layout.activity_admin_user);

        Toolbar toolbar = findViewById(R.id.toolbarUser);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        rvUsers = findViewById(R.id.rvUsers);
        fabAddUser = findViewById(R.id.fabAddUser);

        userDAO = new UserDAO(this);
        userList = new ArrayList<>();

        adapter = new UserAdapter(userList, new UserAdapter.OnUserClickListener() {
            @Override
            public void onEditClick(User user) {
                showUserDialog(user);
            }

            @Override
            public void onDeleteClick(User user) {
                confirmDeleteUser(user);
            }
        });

        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(adapter);

        loadUsers();

        fabAddUser.setOnClickListener(v -> showUserDialog(null));
    }

    private void loadUsers() {
        userList.clear();
        userList.addAll(userDAO.getAllUsers());
        adapter.notifyDataSetChanged();
    }

    private void showUserDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_user, null);
        builder.setView(view);

        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etPassword = view.findViewById(R.id.etPassword);
        EditText etFullName = view.findViewById(R.id.etFullName);
        EditText etPhone = view.findViewById(R.id.etPhone);
        EditText etAddress = view.findViewById(R.id.etAddress);
        RadioGroup rgRole = view.findViewById(R.id.rgRole);
        RadioButton rbUser = view.findViewById(R.id.rbUser);
        RadioButton rbAdmin = view.findViewById(R.id.rbAdmin);

        if (user != null) {
            builder.setTitle("Edit User");
            etEmail.setText(user.getEmail());
            etEmail.setEnabled(false); // Email identifier usually cannot be changed easily
            etPassword.setHint("Leave blank to keep current");
            etFullName.setText(user.getFullName());
            etPhone.setText(user.getPhone());
            etAddress.setText(user.getAddress());
            
            if ("admin".equals(user.getRole())) {
                rbAdmin.setChecked(true);
            } else {
                rbUser.setChecked(true);
            }
        } else {
            builder.setTitle("Add User");
        }

        builder.setPositiveButton("Save", (dialog, which) -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String fullName = etFullName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String role = rbAdmin.isChecked() ? "admin" : "user";

            if (email.isEmpty() || (user == null && password.isEmpty())) {
                Toast.makeText(AdminUserActivity.this, "Email and Password are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (user == null) {
                // Add
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setPassword(password);
                newUser.setFullName(fullName);
                newUser.setPhone(phone);
                newUser.setAddress(address);
                newUser.setRole(role);

                if (userDAO.register(newUser)) {
                    Toast.makeText(AdminUserActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                    loadUsers();
                } else {
                    Toast.makeText(AdminUserActivity.this, "Failed to add (Email may already exist)", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Edit
                user.setFullName(fullName);
                user.setPhone(phone);
                user.setAddress(address);
                user.setRole(role);
                if (!password.isEmpty()) {
                    user.setPassword(password);
                }

                if (userDAO.updateUser(user)) {
                    // Update session if editing self
                    if (sessionManager.getUserId() == user.getId()) {
                        sessionManager.updateUserData(user);
                    }
                    Toast.makeText(AdminUserActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                    loadUsers();
                } else {
                    Toast.makeText(AdminUserActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void confirmDeleteUser(User user) {
        if (sessionManager.getUserId() == user.getId()) {
            Toast.makeText(this, "You cannot delete yourself!", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (userDAO.deleteUser(user.getId())) {
                        Toast.makeText(AdminUserActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                        loadUsers();
                    } else {
                        Toast.makeText(AdminUserActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
