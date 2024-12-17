    package com.example.fyp;

    import android.app.DatePickerDialog;
    import android.os.Bundle;
    import android.text.TextUtils;
    import android.util.Patterns;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;

    import com.google.android.material.chip.Chip;
    import com.google.android.material.chip.ChipGroup;

    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.List;

    public class ProfileActivity extends AppCompatActivity {

        private EditText editTextName, editTextContact, editTextAddress, editTextDob, editTextEmail;
        private ChipGroup skillsChipGroup;

        private final String[] skills = {"Java", "C++", "HTML", "CSS", "Python", "JavaScript", "SQL", "Android"};

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.profile); // Layout file ko same rakho

            // Initialize views
            editTextName = findViewById(R.id.editTextName);
            editTextEmail = findViewById(R.id.editTextEmail); // Email field
            editTextContact = findViewById(R.id.editTextContact);
            editTextAddress = findViewById(R.id.editTextAddress);
            editTextDob = findViewById(R.id.editTextDob);
            skillsChipGroup = findViewById(R.id.skillsChipGroup);
            Button buttonSaveProfile = findViewById(R.id.buttonSaveProfile);

            //validation
            setFocusListeners();


            // DatePicker for DOB
            editTextDob.setOnClickListener(v -> showDatePicker());

            // Add skills as Chips dynamically
            for (String skill : skills) {
                Chip chip = new Chip(this);
                chip.setText(skill);
                chip.setCheckable(true);
                chip.setChipBackgroundColorResource(R.color.chip_default); // Default color
                chip.setTextColor(getResources().getColor(R.color.black));

                chip.setOnClickListener(v -> {
                    if (chip.isChecked()) {
                        chip.setChipBackgroundColorResource(R.color.chip_selected); // Selected background color
                        chip.setTextColor(getResources().getColor(R.color.white)); // Selected text color
                    } else {
                        chip.setChipBackgroundColorResource(R.color.chip_default); // Default background color
                        chip.setTextColor(getResources().getColor(R.color.black)); // Default text color
                    }
                });

                skillsChipGroup.addView(chip);
            }

            // Save button functionality
            buttonSaveProfile.setOnClickListener(v -> saveProfile());
        }

        private void saveProfile() {
            String name = editTextName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String contact = editTextContact.getText().toString().trim();
            String address = editTextAddress.getText().toString().trim();
            String dob = editTextDob.getText().toString().trim();
            List<String> selectedSkills = getSelectedSkills();

            // Validation
            if (TextUtils.isEmpty(name)) {
                editTextName.setError("Name is required");
                editTextName.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(email)) {
                editTextEmail.setError("Email is required");
                editTextEmail.requestFocus();
                return;
            }

            if (!isValidEmail(email)) {
                editTextEmail.setError("Enter a valid email address");
                editTextEmail.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(contact)) {
                editTextContact.setError("Contact number is required");
                editTextContact.requestFocus();
                return;
            }

            if (!isValidPhoneNumber(contact)) {
                editTextContact.setError("Enter a valid phone number");
                editTextContact.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(address)) {
                editTextAddress.setError("Address is required");
                editTextAddress.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(dob)) {
                editTextDob.setError("Date of Birth is required");
                editTextDob.requestFocus();
                return;
            }

            if (selectedSkills.isEmpty()) {
                Toast.makeText(this, "Please select at least one skill", Toast.LENGTH_SHORT).show();
                return;
            }

            // Success message
            Toast.makeText(this, "Profile Saved Successfully!\nSkills: " + selectedSkills, Toast.LENGTH_LONG).show();

            // Save data logic here
        }

        private boolean isValidPhoneNumber(String phone) {
            return Patterns.PHONE.matcher(phone).matches() && phone.length() >= 10;
        }

        private boolean isValidEmail(String email) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }

        private List<String> getSelectedSkills() {
            List<String> selectedSkills = new ArrayList<>();
            for (int i = 0; i < skillsChipGroup.getChildCount(); i++) {
                Chip chip = (Chip) skillsChipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    selectedSkills.add(chip.getText().toString());
                }
            }
            return selectedSkills;
        }

        private void setFocusListeners() {
            // Name field focus listener
            editTextName.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus && TextUtils.isEmpty(editTextName.getText().toString().trim())) {
                    editTextName.setError("Name is required");
                }
            });

            // Email field focus listener
            editTextEmail.setOnFocusChangeListener((v, hasFocus) -> {
                String email = editTextEmail.getText().toString().trim();
                if (!hasFocus && (TextUtils.isEmpty(email) || !isValidEmail(email))) {
                    editTextEmail.setError("Enter a valid email address");
                }
            });

            // Contact field focus listener
            editTextContact.setOnFocusChangeListener((v, hasFocus) -> {
                String contact = editTextContact.getText().toString().trim();
                if (!hasFocus && (TextUtils.isEmpty(contact) || !isValidPhoneNumber(contact))) {
                    editTextContact.setError("Enter a valid phone number");
                }
            });

            // Address field focus listener
            editTextAddress.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus && TextUtils.isEmpty(editTextAddress.getText().toString().trim())) {
                    editTextAddress.setError("Address is required");
                }
            });

            // DOB field focus listener
            editTextDob.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus && TextUtils.isEmpty(editTextDob.getText().toString().trim())) {
                    editTextDob.setError("Date of Birth is required");
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
                        // Month index starts from 0, so add 1
                        String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        editTextDob.setText(selectedDate);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        }
    }
