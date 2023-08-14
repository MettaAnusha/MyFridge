package com.example.myfridge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.example.myfridge.Common.Common;
import com.example.myfridge.Common.KeywordsUtils;
import com.example.myfridge.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding; // ViewBinding instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Button btnSignup = binding.btnSignup;
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the signup method
                signup();
            }
        });

        binding.signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the login screen when the "Already have an account? Login" link is clicked
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signup() {
        String username = binding.etUsername.getText().toString();
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();

        // Validate the input fields
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            // Show an alert dialog indicating that fields are empty
            Common.showToast(this, KeywordsUtils.error_allFieldsRequired_message);


        }
        else if (!isValidEmail(email)) {
            Common.showToast(this, KeywordsUtils.error_invalidEmail_message);
            // Show an alert dialog indicating that the email is invalid

        }

        else {
            // Save the new user details in the User table of the database
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            long result = dbHelper.insertUser(username, email, password);

            if (result != -1) {
                // Signup successful, show a toast message
                Common.showToast(this, KeywordsUtils.signUpSuccessful);

                // Optionally, navigate the user to the login screen after successful signup
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish(); // Optional: finish this signup activity so that pressing back won't take the user back to the signup screen
            } else {
                // Signup failed, show an alert dialog

                Common.showToast(this, KeywordsUtils.error_signUpFailed_message);

            }
        }
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}