package com.example.dutmed;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private String userRole;  // Store user role
    private DutMedDbHelper dbHelper;  // Database helper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DutMedDbHelper(this);  // Initialize the database helper

        // Initialize the EditText fields
        email = findViewById(R.id.email_input);
        pass = findViewById(R.id.password_input);
        userRole = getIntent().getStringExtra("userRole");  // Get the role passed from RoleSelectionActivity
        Log.d("Login", "Received Role: " + userRole);

        Button signUpButton = findViewById(R.id.signup_btn);
        if (signUpButton != null) {
            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigateToRegister();  // Navigate to Register activity with role
                }
            });
        } else {
            Toast.makeText(this, "Sign Up button not initialized", Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin(email.getText().toString(), pass.getText().toString());
            }
        });
    }

    private void performLogin(String email, String password) {
        int userId = dbHelper.loginUser(email, password, userRole); // Adjust loginUser to return user ID
        if (userId != -1) {
            // Store user ID in SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("userId", userId);
            editor.apply();

            // Navigate to MainActivity
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(Login.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean areCredentialsValid(String email, String password) {
        return dbHelper.checkUserCredentials(email, password, userRole);
    }


    private void navigateToRegister() {
        Intent intent = new Intent(Login.this, Register.class);
        intent.putExtra("userRole", userRole);  // Ensure role is included in the intent
        Log.d("Login", "Sending Role to Register: " + userRole);
        startActivity(intent);
    }
}
