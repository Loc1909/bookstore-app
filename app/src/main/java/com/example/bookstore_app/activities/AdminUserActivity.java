package com.example.bookstore_app.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import android.text.Editable;
import android.text.TextWatcher;

public class AdminUserActivity extends AppCompatActivity {

    private RecyclerView rvUsers;
    private FloatingActionButton fabAddUser;
    private UserAdapter adapter;
    private List<User> userList;
    private List<User> fullUserList;
    private UserDAO userDAO;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);

        setContentView(R.layout.activity_admin_user);

        TextView tvUserCount = findViewById(R.id.tvUserCount);
        View btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        rvUsers = findViewById(R.id.rvUsers);
        fabAddUser = findViewById(R.id.fabAddUser);

        userDAO = new UserDAO(this);
        userList = new ArrayList<>();
        fullUserList = new ArrayList<>();

        EditText etSearchUser = findViewById(R.id.etSearchUser);
        etSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

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
        fullUserList.clear();
        fullUserList.addAll(userDAO.getAllUsers());

        // Apply current filter
        EditText etSearchUser = findViewById(R.id.etSearchUser);
        filterUsers(etSearchUser.getText().toString());
    }

    private void filterUsers(String query) {
        userList.clear();
        if (query.isEmpty()) {
            userList.addAll(fullUserList);
        } else {
            String lowerQuery = query.toLowerCase().trim();
            for (User user : fullUserList) {
                if (user.getFullName().toLowerCase().contains(lowerQuery) ||
                        user.getEmail().toLowerCase().contains(lowerQuery)) {
                    userList.add(user);
                }
            }
        }
        adapter.notifyDataSetChanged();
        ((TextView) findViewById(R.id.tvUserCount)).setText(String.valueOf(userList.size()));
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

        builder.setPositiveButton("Save", null);
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String fullName = etFullName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String role = rbAdmin.isChecked() ? "admin" : "user";

            // Email validation
            if (email.isEmpty()) {
                etEmail.setError("Email is required");
                return;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Invalid email format");
                return;
            }

            // Password validation
            if (user == null && password.isEmpty()) {
                etPassword.setError("Password is required for new users");
                return;
            }
            if (!password.isEmpty() && password.length() < 6) {
                etPassword.setError("Password must be at least 6 characters");
                return;
            }

            // Full Name validation
            if (fullName.isEmpty()) {
                etFullName.setError("Full name is required");
                return;
            }

            // Phone validation - simple check for digits and length
            if (!phone.isEmpty() && !phone.matches("\\d{10,11}")) {
                etPhone.setError("Invalid phone number (10-11 digits needed)");
                return;
            }

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

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
                    dialog.dismiss();
                } else {
                    Toast.makeText(AdminUserActivity.this, "Failed to add (Email may already exist)",
                            Toast.LENGTH_SHORT).show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
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
                    if (sessionManager.getUserId() == user.getId()) {
                        sessionManager.updateUserData(user);
                    }
                    Toast.makeText(AdminUserActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                    loadUsers();
                    dialog.dismiss();
                } else {
                    Toast.makeText(AdminUserActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });
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
