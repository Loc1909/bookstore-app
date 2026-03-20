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
import com.example.bookstore_app.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginFragment extends Fragment {

    private TextInputEditText edtEmail, edtPassword;
    private MaterialButton btnLogin;
    private ProgressBar progressBar;
    private TextView txtRegister;

    private UserDAO userDAO;
    private SessionManager sessionManager;

    public LoginFragment() {
        super(R.layout.fragment_login);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo views
        edtEmail = view.findViewById(R.id.edtEmail);
        edtPassword = view.findViewById(R.id.edtPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        progressBar = view.findViewById(R.id.progressBar);
        txtRegister = view.findViewById(R.id.txtRegister);

        // Khởi tạo DAO và Session
        userDAO = new UserDAO(requireContext());
        sessionManager = new SessionManager(requireContext());

        // Xử lý sự kiện click
        btnLogin.setOnClickListener(v -> attemptLogin());
        txtRegister.setOnClickListener(v -> navigateToRegister());
    }

    private void attemptLogin() {
        // Lấy dữ liệu từ form
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Validate
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Email is required");
            edtEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Password is required");
            edtPassword.requestFocus();
            return;
        }

        // Show progress
        showProgress(true);

        // Thực hiện đăng nhập (trong thực tế nên dùng AsyncTask hoặc Thread)
        new Thread(() -> {
            User user = userDAO.login(email, password);

            requireActivity().runOnUiThread(() -> {
                showProgress(false);

                if (user != null) {
                    // Đăng nhập thành công
                    sessionManager.saveLoginSession(user);
                    Toast.makeText(requireContext(), "Login successful! Welcome " + user.getFullName(), Toast.LENGTH_SHORT).show();

                    // Chuyển về AccountFragment để hiển thị profile
                    navigateToAccount();
                } else {
                    // Đăng nhập thất bại
                    Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                }
            });
        }).start();
    }

    private void navigateToRegister() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new RegisterFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToAccount() {
        // Quay về AccountFragment (sẽ tự động load profile vì onResume sẽ check session)
        getParentFragmentManager().popBackStack();
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
        edtEmail.setEnabled(!show);
        edtPassword.setEnabled(!show);
    }
}