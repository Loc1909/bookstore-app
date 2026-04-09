package com.example.bookstore_app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.bookstore_app.R;
import com.example.bookstore_app.activities.AdminDashboardActivity;
import com.example.bookstore_app.models.User;
import com.example.bookstore_app.utils.SessionManager;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private SessionManager sessionManager;

    private ImageView imgAvatar;
    private TextView txtUserName, txtUserEmail, txtPhone, txtAddress, txtRole, txtMemberSince;
    private MaterialCardView adminCard;
    private Button btnLogout, btnAdminDashboard;

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());

        imgAvatar = view.findViewById(R.id.imgAvatar);
        txtUserName = view.findViewById(R.id.txtUserName);
        txtUserEmail = view.findViewById(R.id.txtUserEmail);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtRole = view.findViewById(R.id.txtRole);
        txtMemberSince = view.findViewById(R.id.txtMemberSince);
        adminCard = view.findViewById(R.id.adminCard);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnAdminDashboard = view.findViewById(R.id.btnAdminDashboard);

        loadUser();

        btnLogout.setOnClickListener(v -> logout());

        btnAdminDashboard.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), AdminDashboardActivity.class))
        );
    }

    private void loadUser() {
        User user = sessionManager.getUser();
        if (user == null) return;

        txtUserName.setText(user.getFullName());
        txtUserEmail.setText(user.getEmail());

        txtPhone.setText(
                (user.getPhone() != null && !user.getPhone().isEmpty())
                        ? user.getPhone() : "Not set"
        );

        txtAddress.setText(
                (user.getAddress() != null && !user.getAddress().isEmpty())
                        ? user.getAddress() : "Not set"
        );

        String role = user.getRole();
        if (role != null && !role.isEmpty()) {
            txtRole.setText(role.substring(0, 1).toUpperCase() + role.substring(1));
        } else {
            txtRole.setText("User");
        }

        if (user.getCreatedAt() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            txtMemberSince.setText(sdf.format(new Date(user.getCreatedAt())));
        }

        // ===== Avatar =====
        String avatarUrl = user.getAvatar();
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            Glide.with(this)
                    .load(avatarUrl)
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .circleCrop()
                    .into(imgAvatar);
        } else {
            imgAvatar.setImageResource(R.drawable.ic_user);
        }

        adminCard.setVisibility("admin".equalsIgnoreCase(role)
                ? View.VISIBLE : View.GONE);
    }
    private void logout() {
        sessionManager.logout();

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new AccountFragment())
                .commit();
    }
}