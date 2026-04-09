package com.example.bookstore_app.fragments;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bookstore_app.R;
import com.example.bookstore_app.utils.SessionManager;

public class AccountFragment extends Fragment {

    private SessionManager sessionManager;

    private LinearLayout guestLayout;
    private Button btnLogin, btnRegister;

    public AccountFragment() {
        super(R.layout.fragment_account);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());

        guestLayout = view.findViewById(R.id.guestLayout);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnRegister = view.findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v -> navigateToLogin());
        btnRegister.setOnClickListener(v -> navigateToRegister());

        checkLoginStatus();
    }

    private void checkLoginStatus() {
        if (sessionManager.isLoggedIn()) {
            // chuyển sang ProfileFragment
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ProfileFragment())
                    .commit();
        }
    }

    private void navigateToLogin() {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }

    private void navigateToRegister() {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new RegisterFragment())
                .addToBackStack(null)
                .commit();
    }
}