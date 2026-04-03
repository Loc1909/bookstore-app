package com.example.bookstore_app.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
        edtFullName = view.findViewById(R.id.edtFullName);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtAddress = view.findViewById(R.id.edtAddress);
        edtPassword = view.findViewById(R.id.edtPassword);
        edtConfirmPassword = view.findViewById(R.id.edtConfirmPassword);
        btnRegister = view.findViewById(R.id.btnRegister);
        progressBar = view.findViewById(R.id.progressBar);
        txtLogin = view.findViewById(R.id.txtLogin);

        userDAO = new UserDAO(requireContext());

        btnRegister.setOnClickListener(v -> attemptRegister());
        txtLogin.setOnClickListener(v -> navigateToLogin());
    }

    private void attemptRegister() {
        String fullName = edtFullName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            edtFullName.setError("Full name is required");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Email is required");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Invalid email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Password is required");
            return;
        }

        if (password.length() < 6) {
            edtPassword.setError("Min 6 chars");
            return;
        }

        if (!password.equals(confirmPassword)) {
            edtConfirmPassword.setError("Not match");
            return;
        }

        showProgress(true);
        registerUser(fullName, email, phone, address, password);
    }

    private void registerUser(String fullName, String email, String phone, String address, String password) {

        User newUser = new User();
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setPhone(phone);
        newUser.setAddress(address);
        newUser.setRole("user");

        new Thread(() -> {
            boolean success = false;

            try {
                success = userDAO.register(newUser);
            } catch (Exception e) {
                e.printStackTrace();
            }

            boolean finalSuccess = success;

            // FIX crash lifecycle
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    showProgress(false);

                    if (finalSuccess) {
                        Toast.makeText(requireContext(),
                                "Registration successful! Please login.",
                                Toast.LENGTH_LONG).show();
                        navigateToLogin();
                    } else {
                        Toast.makeText(requireContext(),
                                "Registration failed!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
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