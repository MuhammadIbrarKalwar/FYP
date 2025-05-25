package com.example.fyp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    // Contact Info Fields Only
    EditText editTextName, editTextEmail, editTextContact, editTextAddress, editTextDob;
    Button buttonSaveProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // Initialize views
        initializeViews();
        setupListeners();
        setFocusListeners();
    }

    private void initializeViews() {
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextContact = findViewById(R.id.editTextContact);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextDob = findViewById(R.id.editTextDob);
        buttonSaveProfile = findViewById(R.id.buttonSaveProfile);
    }

    private void setupListeners() {
        // Date Picker for DOB
        editTextDob.setOnClickListener(v -> showDatePicker());

        // Save Profile Button
        buttonSaveProfile.setOnClickListener(v -> {
            if (validateContactInfo()) {
                saveContactInfoToApi();
            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    editTextDob.setText(formattedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private boolean validateContactInfo() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String contact = editTextContact.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String dob = editTextDob.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Name is required");
            editTextName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email address");
            editTextEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(contact) || contact.length() < 10) {
            editTextContact.setError("Enter a valid contact number (minimum 10 digits)");
            editTextContact.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(address)) {
            editTextAddress.setError("Address is required");
            editTextAddress.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(dob)) {
            editTextDob.setError("Date of Birth is required");
            editTextDob.requestFocus();
            return false;
        }

        return true;
    }

    private void saveContactInfoToApi() {
        // Show loading state
        buttonSaveProfile.setEnabled(false);
        buttonSaveProfile.setText("Saving...");

        // Create ProfileData with contact info only
        ProfileData profileData = new ProfileData();
        profileData.user_id = 1; // You can get this from SharedPreferences or login session
        profileData.name = editTextName.getText().toString().trim();
        profileData.email = editTextEmail.getText().toString().trim();
        profileData.contact_number = editTextContact.getText().toString().trim();
        profileData.address = editTextAddress.getText().toString().trim();
        profileData.dob = editTextDob.getText().toString().trim();

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseBody> call = apiService.saveUserProfile(profileData);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Profile Saved Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileActivity.this, "Failed! Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setFocusListeners() {
        editTextName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && TextUtils.isEmpty(editTextName.getText().toString().trim())) {
                editTextName.setError("Name is required");
            }
        });

        editTextEmail.setOnFocusChangeListener((v, hasFocus) -> {
            String email = editTextEmail.getText().toString().trim();
            if (!hasFocus && (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
                editTextEmail.setError("Enter a valid email address");
            }
        });

        editTextContact.setOnFocusChangeListener((v, hasFocus) -> {
            String contact = editTextContact.getText().toString().trim();
            if (!hasFocus && (TextUtils.isEmpty(contact) || contact.length() < 10)) {
                editTextContact.setError("Enter a valid contact number (minimum 10 digits)");
            }
        });

        editTextAddress.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && TextUtils.isEmpty(editTextAddress.getText().toString().trim())) {
                editTextAddress.setError("Address is required");
            }
        });

        editTextDob.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && TextUtils.isEmpty(editTextDob.getText().toString().trim())) {
                editTextDob.setError("Date of Birth is required");
            }
        });
    }
}