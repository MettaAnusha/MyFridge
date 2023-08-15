package com.example.myfridge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.myfridge.Common.Common;
import com.example.myfridge.Common.KeywordsUtils;
import com.example.myfridge.DatabaseHelper;
import com.example.myfridge.Models.Usermodel;
import com.example.myfridge.databinding.ActivityLogInBinding;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;

    private ActivityLogInBinding binding;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Initialize the binding
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Initialize the EditText fields
        etUsername = binding.loginEdtUsername;
        etPassword = binding.loginEdtPassword;

        Button btnLogin = binding.loginBtnLogin;
        TextView tvSignUp = binding.loginTvSignUp;

        // Initialize the DBHelper instance
        dbHelper = new DatabaseHelper(this);

        // Set up a test user's credentials


        // Save the test user details in shared preferences (for simplicity; consider using a database for a real app)

        // Set up onClickListener for the login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // Set up onClickListener for the sign-up text
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the sign-up activity
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Handle the login process
    private void login() {

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            // Show an alert dialog indicating that fields are empty
          Common.showToast(this,KeywordsUtils.error_allFieldsRequired_message);

        }


        else {
            // Verify user credentials in the database
            try {
                DatabaseHelper userDatabaseHelper = new DatabaseHelper(this);
                Usermodel user = userDatabaseHelper.verifyCredentials(username, password);

                if (user != null) {
                    // Convert Usermodel to JSON and store in SharedPreferences

                    setDatabase(user);

                } else {
                    // Show an alert dialog for invalid credentials
                    Common.showToast(this,KeywordsUtils.error_invalidCredential_message);

                }
            } catch (Exception e) {
                Log.d("LoginActivity", "Login successful for username: " + e);
            }
        }
    }

    void  setDatabase(Usermodel user){
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        SharedPreferences sharedPreferences = getSharedPreferences(KeywordsUtils.SP_userSession, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KeywordsUtils.SP_userName, userJson);

        // Navigate to the home activity
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
        editor.commit();
    }
}
