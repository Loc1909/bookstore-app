package com.example.bookstore_app.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.bookstore_app.R;
import com.example.bookstore_app.database.dao.UserDAO;
import com.example.bookstore_app.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterFragment extends Fragment {

    private TextInputEditText edtFullName, edtEmail, edtPhone, edtAddress, edtPassword, edtConfirmPassword;
    private MaterialButton btnRegister;
    private ProgressBar progressBar;
    private TextView txtLogin;

    private UserDAO userDAO;

    public RegisterFragment() {
        super(R.layout.fragment_register);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo views
        edtFullName = view.findViewById(R.id.edtFullName);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtAddress = view.findViewById(R.id.edtAddress);
        edtPassword = view.findViewById(R.id.edtPassword);
        edtConfirmPassword = view.findViewById(R.id.edtConfirmPassword);
        btnRegister = view.findViewById(R.id.btnRegister);
        progressBar = view.findViewById(R.id.progressBar);
        txtLogin = view.findViewById(R.id.txtLogin);

        // Khởi tạo DAO
        userDAO = new UserDAO(requireContext());

        // Xử lý sự kiện
        btnRegister.setOnClickListener(v -> attemptRegister());
        txtLogin.setOnClickListener(v -> navigateToLogin());
    }

    private void attemptRegister() {
        // Lấy dữ liệu
        String fullName = edtFullName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        // Validate
        if (TextUtils.isEmpty(fullName)) {
            edtFullName.setError("Full name is required");
            edtFullName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Email is required");
            edtEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Invalid email format");
            edtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Password is required");
            edtPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            edtPassword.setError("Password must be at least 6 characters");
            edtPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            edtConfirmPassword.setError("Passwords do not match");
            edtConfirmPassword.requestFocus();
            return;
        }

        // Show progress
        showProgress(true);

        // Kiểm tra email đã tồn tại chưa
        new Thread(() -> {
            boolean emailExists = userDAO.isEmailExists(email);

            requireActivity().runOnUiThread(() -> {
                if (emailExists) {
                    showProgress(false);
                    edtEmail.setError("Email already exists");
                    edtEmail.requestFocus();
                } else {
                    // Tiến hành đăng ký
                    registerUser(fullName, email, phone, address, password);
                }
            });
        }).start();
    }

    private void registerUser(String fullName, String email, String phone, String address, String password) {
        // Tạo user mới
        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPassword(password); // Trong thực tế nên hash
        newUser.setPhone(phone);
        newUser.setAddress(address);
        newUser.setRole("user"); // Mặc định là user thường

        // Lưu vào database
        new Thread(() -> {
            boolean success = userDAO.register(newUser);

            requireActivity().runOnUiThread(() -> {
                showProgress(false);

                if (success) {
                    Toast.makeText(requireContext(), "Registration successful! Please login.", Toast.LENGTH_LONG).show();
                    // Quay lại màn hình login
                    navigateToLogin();
                } else {
                    Toast.makeText(requireContext(), "Registration failed. Please try again.", Toast.LENGTH_LONG).show();
                }
            });
        }).start();
    }

    private void navigateToLogin() {
        getParentFragmentManager().popBackStack();
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!show);
        edtFullName.setEnabled(!show);
        edtEmail.setEnabled(!show);
        edtPhone.setEnabled(!show);
        edtAddress.setEnabled(!show);
        edtPassword.setEnabled(!show);
        edtConfirmPassword.setEnabled(!show);
    }
}