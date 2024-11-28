package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePasswordActivity extends AppCompatActivity {

    private EditText editTextCode, editTextPassword, editTextConfirmPassword;
    private Button buttonSignUp;
    private ApiService apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.update_password);
        String email = getIntent().getStringExtra("email");

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Initialize UI elements
        editTextCode = findViewById(R.id.editTextCode);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        // Set onClickListener for Sign-Up button
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = editTextCode.getText().toString().trim();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                if (validateInputs(code, password, confirmPassword)) {

                    if(!code.equals("1234"))
                    {
                        Toast.makeText(UpdatePasswordActivity.this, "otp is incorrect", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        UpdatePassword(email,password);
                    }


                }
            }
        });




    }

    private boolean validateInputs(String code, String password, String confirmPassword) {
        if (TextUtils.isEmpty(code)) {
            editTextCode.setError("Code is required");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password is required");
            return false;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Password must be at least 6 characters");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Passwords do not match");
            return false;
        }
        return true;
    }


    private void UpdatePassword(String email, String password) {
        // Create LoginRequest object
        LoginRequest request = new LoginRequest(email, password);

        // Make the API call
        Call<LoginResponse> call = apiService.updatePassword(request);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(UpdatePasswordActivity.this, "Password Updated!", Toast.LENGTH_SHORT).show();
                    // Optionally, navigate to LoginActivity or HomeActivity
                    Intent intent = new Intent(UpdatePasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(UpdatePasswordActivity.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(UpdatePasswordActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}