package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText editTextCode, editTextPassword, editTextConfirmPassword;

    private EditText editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);




        setContentView(R.layout.forgot_password);
        TextView signupLink = findViewById(R.id.signupLink);
        Button buttonLogin = findViewById(R.id.forgotbutton);

        editTextEmail = findViewById(R.id.editTextEmail);

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Signup screen
                Intent intent = new Intent(ForgotPasswordActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                Toast.makeText(ForgotPasswordActivity.this, email, Toast.LENGTH_SHORT).show();
                if (validateInputs(email)) {

                    Toast.makeText(ForgotPasswordActivity.this, "Code Send Successfuly!", Toast.LENGTH_SHORT).show();

                    // Optionally, navigate to the main screen
                    Intent intent = new Intent(ForgotPasswordActivity.this, UpdatePasswordActivity.class);

                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish(); // Close LoginActivity
                }
            }
        });
    }
    private boolean validateInputs(String email) {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}