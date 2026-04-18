package com.example.bookstore_app.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.bookstore_app.R;
import com.example.bookstore_app.activities.AdminDashboardActivity;
import com.example.bookstore_app.database.dao.UserDAO;
import com.example.bookstore_app.models.User;
import com.example.bookstore_app.utils.SessionManager;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.net.Uri;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ProfileFragment extends Fragment {

    private SessionManager sessionManager;

    private ImageView imgAvatar, btnChangeAvatar;
    private TextView txtUserName, txtUserEmail, txtPhone, txtAddress, txtRole, txtMemberSince;
    private MaterialCardView adminCard;
    private Button btnLogout, btnAdminDashboard, btnEditProfile;
    private UserDAO userDAO;
    private int currentUserId;

    private ActivityResultLauncher<Intent> pickImageLauncher;

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        userDAO = new UserDAO(requireContext());
        currentUserId = sessionManager.getUserId();
        initActions();
        loadUser();
    }

    // ================= INIT =================

    private void initViews(View view) {
        sessionManager = new SessionManager(requireContext());

        imgAvatar = view.findViewById(R.id.imgAvatar);
        btnChangeAvatar = view.findViewById(R.id.btnChangeAvatar);

        txtUserName = view.findViewById(R.id.txtUserName);
        txtUserEmail = view.findViewById(R.id.txtUserEmail);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtRole = view.findViewById(R.id.txtRole);
        txtMemberSince = view.findViewById(R.id.txtMemberSince);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        adminCard = view.findViewById(R.id.adminCard);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnAdminDashboard = view.findViewById(R.id.btnAdminDashboard);
    }

    private void initActions() {

        btnLogout.setOnClickListener(v -> logout());

        btnAdminDashboard.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), AdminDashboardActivity.class))
        );

        // PICK IMAGE
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == requireActivity().RESULT_OK
                            && result.getData() != null) {

                        Uri imageUri = result.getData().getData();

                        if (imageUri != null) {
                            String path = saveImageToInternalStorage(imageUri);

                            if (path != null) {
                                saveAvatar(path);
                            }
                        }
                    }
                }
        );

        btnChangeAvatar.setOnClickListener(v -> openGallery());
        btnEditProfile.setOnClickListener(v -> {
            User user = sessionManager.getUser();
            if (user != null) {
                showEditDialog(user.getPhone(), user.getAddress());
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }

    // ================= AVATAR =================

    private void saveAvatar(String newPath) {
        int userId = sessionManager.getUserId();
        UserDAO userDAO = new UserDAO(requireContext());
        User user = sessionManager.getUser();

        if (user == null) return;

        //  Xóa avatar cũ
        deleteOldAvatar(user.getAvatar());

        //  Update DB
        boolean success = userDAO.updateAvatar(userId, newPath);

        if (success) {
            // update UI
            loadAvatar(newPath);

            // update session
            user.setAvatar(newPath);
            sessionManager.updateUserData(user);
        }
    }

    private void deleteOldAvatar(String oldPath) {
        if (oldPath == null || oldPath.isEmpty()) return;

        File oldFile = new File(oldPath);
        if (oldFile.exists()) {
            oldFile.delete();
        }
    }

    private void loadAvatar(String path) {
        Glide.with(requireContext())
                .load(new File(path))
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .circleCrop()
                .override(300, 300) // 🔥 tối ưu
                .into(imgAvatar);
    }

    // ================= USER =================

    private void loadUser() {
        User user = sessionManager.getUser();
        if (user == null) return;

        txtUserName.setText(user.getFullName());
        txtUserEmail.setText(user.getEmail());

        txtPhone.setText(
                isEmpty(user.getPhone()) ? "Not set" : user.getPhone()
        );

        txtAddress.setText(
                isEmpty(user.getAddress()) ? "Not set" : user.getAddress()
        );

        String role = isEmpty(user.getRole()) ? "user" : user.getRole();
        txtRole.setText(capitalize(role));

        if (user.getCreatedAt() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            txtMemberSince.setText(sdf.format(new Date(user.getCreatedAt())));
        }

        if (!isEmpty(user.getAvatar())) {
            loadAvatar(user.getAvatar());
        } else {
            imgAvatar.setImageResource(R.drawable.ic_user);
        }

        adminCard.setVisibility("admin".equalsIgnoreCase(role)
                ? View.VISIBLE : View.GONE);
    }

    // ================= UTIL =================

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    private void showEditDialog(String phone, String address) {

        View view = getLayoutInflater().inflate(R.layout.dialog_edit_profile, null);

        EditText edtPhone = view.findViewById(R.id.edtPhone);
        EditText edtAddress = view.findViewById(R.id.edtAddress);

        edtPhone.setText(phone);
        edtAddress.setText(address);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(view)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextView btnSave = view.findViewById(R.id.btnSave);
        TextView btnCancel = view.findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(v -> {
            String newPhone = edtPhone.getText().toString().trim();
            String newAddress = edtAddress.getText().toString().trim();

            updateProfile(newPhone, newAddress);
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    (int) (getResources().getDisplayMetrics().widthPixels * 0.9),
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
        }
    }

    private void updateProfile(String phone, String address) {

        User user = sessionManager.getUser();
        if (user == null) return;

        user.setId(currentUserId);
        user.setPhone(phone);
        user.setAddress(address);

        boolean success = userDAO.updateUser(user);

        if (success) {
            txtPhone.setText(phone);
            txtAddress.setText(address);

            sessionManager.updateUserData(user);

            Toast.makeText(requireContext(),
                    "Cập nhật thành công",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(),
                    "Cập nhật thất bại",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private void logout() {
        sessionManager.logout();

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new AccountFragment())
                .commit();
    }

    // ================= STORAGE =================

    private String saveImageToInternalStorage(Uri uri) {
        try {
            int userId = sessionManager.getUserId();

            InputStream inputStream = requireContext()
                    .getContentResolver()
                    .openInputStream(uri);

            if (inputStream == null) return null;

            File directory = new File(requireContext().getFilesDir(), "avatars");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = "user_" + userId + "_" + System.currentTimeMillis() + ".jpg";
            File file = new File(directory, fileName);

            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            return file.getAbsolutePath();

        } catch (Exception e) {
            Log.e("Avatar", "Save image failed", e);
            return null;
        }
    }
}