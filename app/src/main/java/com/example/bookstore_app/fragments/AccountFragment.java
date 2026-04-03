package com.example.bookstore_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.bookstore_app.R;
import com.example.bookstore_app.activities.AdminDashboardActivity;
import com.example.bookstore_app.models.User;
import com.example.bookstore_app.utils.SessionManager;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AccountFragment extends Fragment {

    private SessionManager sessionManager;

    // Views cho profile (khi đã login)
    private ScrollView profileLayout;
    private ImageView imgAvatar;
    private TextView txtUserName, txtUserEmail, txtPhone, txtAddress, txtRole, txtMemberSince;
    private Button btnLogout;
    private MaterialCardView adminCard;
    private Button btnAdminDashboard;

    // Views cho login/register (khi chưa login)
    private LinearLayout guestLayout;
    private Button btnLogin, btnRegister;

    public AccountFragment() {
        super(R.layout.fragment_account);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Khởi tạo SessionManager
        sessionManager = new SessionManager(requireContext());

        // Khởi tạo views
        initViews(view);

        // Kiểm tra trạng thái đăng nhập và hiển thị UI tương ứng
        checkLoginStatus();
    }

    private void initViews(View view) {
        // Profile layout (đã login)
        profileLayout = view.findViewById(R.id.profileLayout);
        imgAvatar = view.findViewById(R.id.imgAvatar);
        txtUserName = view.findViewById(R.id.txtUserName);
        txtUserEmail = view.findViewById(R.id.txtUserEmail);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtRole = view.findViewById(R.id.txtRole);
        txtMemberSince = view.findViewById(R.id.txtMemberSince);
        btnLogout = view.findViewById(R.id.btnLogout);
        adminCard = view.findViewById(R.id.adminCard);
        btnAdminDashboard = view.findViewById(R.id.btnAdminDashboard);

        // Guest layout (chưa login)
        guestLayout = view.findViewById(R.id.guestLayout);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnRegister = view.findViewById(R.id.btnRegister);

        // Set click listeners
        btnLogout.setOnClickListener(v -> logout());
        btnLogin.setOnClickListener(v -> navigateToLogin());
        btnRegister.setOnClickListener(v -> navigateToRegister());
        btnAdminDashboard.setOnClickListener(v -> navigateToAdminDashboard());
    }

    private void checkLoginStatus() {
        if (sessionManager.isLoggedIn()) {
            // Đã đăng nhập - hiển thị profile
            showProfile();
        } else {
            // Chưa đăng nhập - hiển thị guest layout
            showGuestMode();
        }
    }

    private void showProfile() {
        // Ẩn guest layout, hiện profile layout
        guestLayout.setVisibility(View.GONE);
        profileLayout.setVisibility(View.VISIBLE);

        // Lấy thông tin user
        User user = sessionManager.getUser();

        if (user != null) {
            // Hiển thị thông tin cơ bản
            txtUserName.setText(user.getFullName());
            txtUserEmail.setText(user.getEmail());

            // Hiển thị phone (nếu có)
            if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                txtPhone.setText(user.getPhone());
            } else {
                txtPhone.setText("Not set");
            }

            // Hiển thị address (nếu có)
            if (user.getAddress() != null && !user.getAddress().isEmpty()) {
                txtAddress.setText(user.getAddress());
            } else {
                txtAddress.setText("Not set");
            }

            // Hiển thị role
            String role = user.getRole();
            txtRole.setText(role.substring(0, 1).toUpperCase() + role.substring(1));

            // Hiển thị ngày tham gia
            if (user.getCreatedAt() > 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String date = sdf.format(new Date(user.getCreatedAt()));
                txtMemberSince.setText(date);
            }

            // Hiển thị avatar (nếu có)
            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                Glide.with(this)
                        .load(user.getAvatar())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .circleCrop()
                        .into(imgAvatar);
            }

            // Kiểm tra role để hiển thị admin card
            if ("admin".equals(user.getRole())) {
                adminCard.setVisibility(View.VISIBLE);
            } else {
                adminCard.setVisibility(View.GONE);
            }
        }
    }

    private void showGuestMode() {
        // Ẩn profile layout, hiện guest layout
        profileLayout.setVisibility(View.GONE);
        guestLayout.setVisibility(View.VISIBLE);
    }

    private void logout() {
        // Xóa session
        sessionManager.logout();

        // Hiển thị thông báo
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Chuyển về guest mode
        showGuestMode();
    }

    private void navigateToLogin() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new LoginFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToRegister() {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new RegisterFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void navigateToAdminDashboard() {
        Intent intent = new Intent(requireContext(), AdminDashboardActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Khi quay lại fragment, kiểm tra lại trạng thái login
        checkLoginStatus();
    }
}