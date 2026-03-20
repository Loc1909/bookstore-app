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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AdminUserActivity extends AppCompatActivity {

    private RecyclerView rvUsers;
    private FloatingActionButton fabAddUser;
    private UserAdapter adapter;
    private List<User> userList;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        EditText etUsername = view.findViewById(R.id.etUsername);
        EditText etPassword = view.findViewById(R.id.etPassword);
        EditText etFullname = view.findViewById(R.id.etFullname);
        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etPhone = view.findViewById(R.id.etPhone);
        RadioGroup rgRole = view.findViewById(R.id.rgRole);
        RadioButton rbUser = view.findViewById(R.id.rbUser);
        RadioButton rbAdmin = view.findViewById(R.id.rbAdmin);

        if (user != null) {
            builder.setTitle("Edit User");
            etUsername.setText(user.getUsername());
            etUsername.setEnabled(false); // Username usually cannot be changed easily
            etPassword.setHint("Leave blank to keep current password");
            etFullname.setText(user.getFullname());
            etEmail.setText(user.getEmail());
            etPhone.setText(user.getPhone());
            
            if ("Admin".equals(user.getRole())) {
                rbAdmin.setChecked(true);
            } else {
                rbUser.setChecked(true);
            }
        } else {
            builder.setTitle("Add User");
        }

        builder.setPositiveButton("Save", (dialog, which) -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String fullname = etFullname.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String role = rbAdmin.isChecked() ? "Admin" : "User";

            if (username.isEmpty() || (user == null && password.isEmpty())) {
                Toast.makeText(AdminUserActivity.this, "Username and Password are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (user == null) {
                // Add
                User newUser = new User();
                newUser.setUsername(username);
                newUser.setPassword(password);
                newUser.setFullname(fullname);
                newUser.setEmail(email);
                newUser.setPhone(phone);
                newUser.setRole(role);

                if (userDAO.addUser(newUser)) {
                    Toast.makeText(AdminUserActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                    loadUsers();
                } else {
                    Toast.makeText(AdminUserActivity.this, "Failed to add (Username may already exist)", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Edit
                user.setFullname(fullname);
                user.setEmail(email);
                user.setPhone(phone);
                user.setRole(role);
                if (!password.isEmpty()) {
                    user.setPassword(password);
                }

                if (userDAO.updateUser(user)) {
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
