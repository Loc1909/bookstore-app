package com.example.bookstore_app.activities;

import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.bookstore_app.R;
import com.example.bookstore_app.fragments.AccountFragment;
import com.example.bookstore_app.fragments.HomeFragment;
import com.example.bookstore_app.fragments.OrdersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        com.example.bookstore_app.database.DatabaseHelper dbHelper =
                new com.example.bookstore_app.database.DatabaseHelper(this);
        dbHelper.getWritableDatabase();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(
                    ContextCompat.getColor(this, R.color.dark_espresso)
            );
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // icon trắng
            getWindow().getDecorView().setSystemUiVisibility(0);
        }

        bottomNavigation = findViewById(R.id.bottomNavigation);

        loadFragment(new HomeFragment());

        bottomNavigation.setOnItemSelectedListener(item -> {

            Fragment fragment = null;

            if(item.getItemId() == R.id.nav_home){
                fragment = new HomeFragment();
            }
            else if(item.getItemId() == R.id.nav_orders){
                fragment = new OrdersFragment();
            }
            else if(item.getItemId() == R.id.nav_account){
                fragment = new AccountFragment();
            }

            if(fragment != null){
                loadFragment(fragment);
            }

            return true;
        });
    }

    private void loadFragment(Fragment fragment){

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}