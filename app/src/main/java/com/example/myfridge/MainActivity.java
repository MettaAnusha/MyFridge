package com.example.myfridge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.myfridge.Common.KeywordsUtils;
import com.example.myfridge.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the DBHelper instance


        // Check if user is already logged in
        SharedPreferences sharedPreferences = getSharedPreferences(KeywordsUtils.SP_userSession, Context.MODE_PRIVATE);
        boolean containsKey = sharedPreferences.contains(KeywordsUtils.SP_userName);

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);

        // If user is logged in, navigate to the home activity; otherwise, navigate to the login activity
        if (containsKey) {
            intent = new Intent(MainActivity.this, HomeActivity.class);
        }

        // Start the appropriate activity and finish the current activity
        startActivity(intent);
        finish();
    }
}