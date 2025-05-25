package com.example.fyp;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendationsActivity extends AppCompatActivity {

    // UI Components
    private EditText editTextYearsCode, editTextWorkExperience;
    private Spinner spinnerEducation, spinnerWorkMode, spinnerCountry;
    private Spinner spinnerSkills, spinnerInterests;
    private Button buttonAddSkill, buttonAddInterest, buttonGetRecommendations, buttonClearAll;
    private LinearLayout selectedSkillsLayout, selectedInterestsLayout;
    private ListView listViewRecommendations;
    private ProgressBar progressBar;
    private TextView textNoRecommendations, textSelectedSkillsLabel, textSelectedInterestsLabel, textProcessing;
    private CardView cardResults, cardNoResults;

    // Data
    private ArrayList<String> selectedSkills = new ArrayList<>();
    private ArrayList<String> selectedInterests = new ArrayList<>();

    // Dropdown options matching your dataset
    private final String[] educationLevels = {
            "Select Education Level",
            "Bachelor's degree (B.A., B.S., B.Eng., etc.)",
            "Master's degree (M.A., M.S., M.Eng., MBA, etc.)",
            "Some college but never finished",
            "Associate degree (A.A., A.S., etc.)",
            "High school graduate",
            "Professional degree (JD, MD, etc.)",
            "Primary/elementary school",
            "Secondary school (e.g. American high school, German Realschule or Gymnasium, etc.)",
            "Something else"
    };

    private final String[] workModes = {"Select Work Mode", "Hybrid", "Remote", "In-person"};

    private final String[] countries = {"Select Country", "USA", "Pakistan", "UK", "India", "Canada", "Germany", "Australia", "Switzerland", "Netherlands", "France"};

    private final String[] allSkills = {
            "Select Skill", "Java", "Python", "JavaScript", "HTML/CSS", "React", "Node.js",
            "Angular", "Vue.js", "Django", "Flask", "SQL", "MySQL", "MongoDB", "PostgreSQL",
            "Amazon Web Services (AWS)", "Google Cloud", "Microsoft Azure", "Docker", "Kubernetes",
            "C++", "C#", "PHP", "Ruby", "Go", "TypeScript", "Swift", "Kotlin", "Rust",
            "Spring Boot", "Laravel", "Express", "Next.js", "React Native", "Flutter",
            "TensorFlow", "PyTorch", "Pandas", "NumPy", "Scikit-Learn"
    };

    private final String[] allInterests = {
            "Select Interests", "Java", "Python", "JavaScript", "HTML/CSS", "React", "Node.js",
            "Angular", "Vue.js", "Django", "Flask", "SQL", "MySQL", "MongoDB", "PostgreSQL",
            "Amazon Web Services (AWS)", "Google Cloud", "Microsoft Azure", "Docker", "Kubernetes",
            "C++", "C#", "PHP", "Ruby", "Go", "TypeScript", "Swift", "Kotlin", "Rust",
            "Spring Boot", "Laravel", "Express", "Next.js", "React Native", "Flutter",
            "TensorFlow", "PyTorch", "Pandas", "NumPy", "Scikit-Learn"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommendations);

        initializeViews();
        setupSpinners();
        setupListeners();

        // Debug: Check view references
        checkViewReferences();
    }

    private void initializeViews() {
        Log.d("INIT_VIEWS", "Initializing views...");

        // Input fields
        editTextYearsCode = findViewById(R.id.editTextYearsCode);
        editTextWorkExperience = findViewById(R.id.editTextWorkExperience);

        // Spinners
        spinnerEducation = findViewById(R.id.spinnerEducation);
        spinnerWorkMode = findViewById(R.id.spinnerWorkMode);
        spinnerCountry = findViewById(R.id.spinnerCountry);
        spinnerSkills = findViewById(R.id.spinnerSkills);
        spinnerInterests = findViewById(R.id.spinnerInterests);

        // Buttons
        buttonAddSkill = findViewById(R.id.buttonAddSkill);
        buttonAddInterest = findViewById(R.id.buttonAddInterest);
        buttonGetRecommendations = findViewById(R.id.buttonGetRecommendations);
        buttonClearAll = findViewById(R.id.buttonClearAll);

        // Layouts and lists
        selectedSkillsLayout = findViewById(R.id.selectedSkillsLayout);
        selectedInterestsLayout = findViewById(R.id.selectedInterestsLayout);
        listViewRecommendations = findViewById(R.id.listViewRecommendations);

        // Status views
        progressBar = findViewById(R.id.progressBar);
        textNoRecommendations = findViewById(R.id.textNoRecommendations);
        textSelectedSkillsLabel = findViewById(R.id.textSelectedSkillsLabel);
        textSelectedInterestsLabel = findViewById(R.id.textSelectedInterestsLabel);
        textProcessing = findViewById(R.id.textProcessing);

        // Card views
        cardResults = findViewById(R.id.cardResults);
        cardNoResults = findViewById(R.id.cardNoResults);

        // Initially hide certain views
        progressBar.setVisibility(View.GONE);
        if (textProcessing != null) textProcessing.setVisibility(View.GONE);
        if (cardResults != null) cardResults.setVisibility(View.GONE);
        if (cardNoResults != null) cardNoResults.setVisibility(View.GONE);
        if (textSelectedSkillsLabel != null) textSelectedSkillsLabel.setVisibility(View.GONE);
        if (textSelectedInterestsLabel != null) textSelectedInterestsLabel.setVisibility(View.GONE);

        Log.d("INIT_VIEWS", "Views initialized successfully");
    }

    private void setupSpinners() {
        Log.d("SETUP_SPINNERS", "Setting up spinners...");

        // Education Spinner
        ArrayAdapter<String> educationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, educationLevels);
        educationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEducation.setAdapter(educationAdapter);

        // Work Mode Spinner
        ArrayAdapter<String> workModeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, workModes);
        workModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWorkMode.setAdapter(workModeAdapter);

        // Country Spinner
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(countryAdapter);

        // Skills Spinner
        ArrayAdapter<String> skillsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allSkills);
        skillsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSkills.setAdapter(skillsAdapter);

        // Interests Spinner
        ArrayAdapter<String> interestsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allInterests);
        interestsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInterests.setAdapter(interestsAdapter);

        Log.d("SETUP_SPINNERS", "Spinners setup completed");
    }

    private void setupListeners() {
        Log.d("SETUP_LISTENERS", "Setting up listeners...");

        // Add Skill Button
        buttonAddSkill.setOnClickListener(v -> {
            String selectedSkill = spinnerSkills.getSelectedItem().toString();
            Log.d("ADD_SKILL", "Selected skill: " + selectedSkill);

            if (!selectedSkill.equals("Select Skill") && !selectedSkills.contains(selectedSkill)) {
                selectedSkills.add(selectedSkill);
                updateSelectedSkillsDisplay();
                Toast.makeText(this, "Added: " + selectedSkill, Toast.LENGTH_SHORT).show();
                Log.d("ADD_SKILL", "Skill added successfully. Total skills: " + selectedSkills.size());
            } else if (selectedSkills.contains(selectedSkill)) {
                Toast.makeText(this, "Skill already added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please select a valid skill!", Toast.LENGTH_SHORT).show();
            }
        });

        // Add Interest Button
        buttonAddInterest.setOnClickListener(v -> {
            String selectedInterest = spinnerInterests.getSelectedItem().toString();
            Log.d("ADD_INTEREST", "Selected interest: " + selectedInterest);

            if (!selectedInterest.equals("Select Interest") && !selectedInterests.contains(selectedInterest)) {
                selectedInterests.add(selectedInterest);
                updateSelectedInterestsDisplay();
                Toast.makeText(this, "Added: " + selectedInterest, Toast.LENGTH_SHORT).show();
                Log.d("ADD_INTEREST", "Interest added successfully. Total interests: " + selectedInterests.size());
            } else if (selectedInterests.contains(selectedInterest)) {
                Toast.makeText(this, "Interest already added!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please select a valid interest!", Toast.LENGTH_SHORT).show();
            }
        });

        // Get Recommendations Button
        buttonGetRecommendations.setOnClickListener(v -> {
            Log.d("GET_RECOMMENDATIONS", "Get recommendations button clicked");
            if (validateInput()) {
                getRecommendations();
            }
        });

        // Clear All Button
        buttonClearAll.setOnClickListener(v -> {
            Log.d("CLEAR_ALL", "Clear all button clicked");
            clearAllSelections();
        });

        Log.d("SETUP_LISTENERS", "Listeners setup completed");
    }

    private void updateSelectedSkillsDisplay() {
        if (selectedSkillsLayout == null) {
            Log.e("UPDATE_SKILLS_DISPLAY", "selectedSkillsLayout is null");
            return;
        }

        selectedSkillsLayout.removeAllViews();
        if (selectedSkills.isEmpty()) {
            if (textSelectedSkillsLabel != null) {
                textSelectedSkillsLabel.setVisibility(View.GONE);
            }
        } else {
            if (textSelectedSkillsLabel != null) {
                textSelectedSkillsLabel.setVisibility(View.VISIBLE);
            }
            for (String skill : selectedSkills) {
                TextView skillView = createSkillInterestView(skill, true);
                selectedSkillsLayout.addView(skillView);
            }
        }
        Log.d("UPDATE_SKILLS_DISPLAY", "Skills display updated. Count: " + selectedSkills.size());
    }

    private void updateSelectedInterestsDisplay() {
        if (selectedInterestsLayout == null) {
            Log.e("UPDATE_INTERESTS_DISPLAY", "selectedInterestsLayout is null");
            return;
        }

        selectedInterestsLayout.removeAllViews();
        if (selectedInterests.isEmpty()) {
            if (textSelectedInterestsLabel != null) {
                textSelectedInterestsLabel.setVisibility(View.GONE);
            }
        } else {
            if (textSelectedInterestsLabel != null) {
                textSelectedInterestsLabel.setVisibility(View.VISIBLE);
            }
            for (String interest : selectedInterests) {
                TextView interestView = createSkillInterestView(interest, false);
                selectedInterestsLayout.addView(interestView);
            }
        }
        Log.d("UPDATE_INTERESTS_DISPLAY", "Interests display updated. Count: " + selectedInterests.size());
    }

    private TextView createSkillInterestView(String text, boolean isSkill) {
        TextView textView = new TextView(this);
        textView.setText("• " + text + " ✕");
        textView.setTextSize(16);
        textView.setTextColor(getResources().getColor(android.R.color.black));
        textView.setPadding(16, 8, 16, 8);
        textView.setBackgroundResource(android.R.drawable.btn_default);

        // Add click listener to remove item
        textView.setOnClickListener(v -> {
            if (isSkill) {
                selectedSkills.remove(text);
                updateSelectedSkillsDisplay();
                Toast.makeText(this, "Removed: " + text, Toast.LENGTH_SHORT).show();
                Log.d("REMOVE_SKILL", "Removed skill: " + text);
            } else {
                selectedInterests.remove(text);
                updateSelectedInterestsDisplay();
                Toast.makeText(this, "Removed: " + text, Toast.LENGTH_SHORT).show();
                Log.d("REMOVE_INTEREST", "Removed interest: " + text);
            }
        });

        return textView;
    }

    private boolean validateInput() {
        Log.d("VALIDATE_INPUT", "Validating user input...");

        String yearsCode = editTextYearsCode.getText().toString().trim();
        String workExp = editTextWorkExperience.getText().toString().trim();

        if (yearsCode.isEmpty()) {
            editTextYearsCode.setError("Years of coding experience is required");
            editTextYearsCode.requestFocus();
            Log.w("VALIDATE_INPUT", "Years of coding is empty");
            return false;
        }

        if (workExp.isEmpty()) {
            editTextWorkExperience.setError("Work experience is required");
            editTextWorkExperience.requestFocus();
            Log.w("VALIDATE_INPUT", "Work experience is empty");
            return false;
        }

        if (spinnerEducation.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select your education level", Toast.LENGTH_SHORT).show();
            Log.w("VALIDATE_INPUT", "Education not selected");
            return false;
        }

        if (spinnerWorkMode.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select your work mode preference", Toast.LENGTH_SHORT).show();
            Log.w("VALIDATE_INPUT", "Work mode not selected");
            return false;
        }

        if (spinnerCountry.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select your country", Toast.LENGTH_SHORT).show();
            Log.w("VALIDATE_INPUT", "Country not selected");
            return false;
        }

        if (selectedSkills.isEmpty()) {
            Toast.makeText(this, "Please add at least one skill", Toast.LENGTH_SHORT).show();
            Log.w("VALIDATE_INPUT", "No skills selected");
            return false;
        }

        if (selectedInterests.isEmpty()) {
            Toast.makeText(this, "Please add at least one interest", Toast.LENGTH_SHORT).show();
            Log.w("VALIDATE_INPUT", "No interests selected");
            return false;
        }

        Log.d("VALIDATE_INPUT", "Input validation passed");
        return true;
    }

    private void getRecommendations() {
        Log.d("GET_RECOMMENDATIONS", "Starting recommendation process...");

        // Show loading state
        progressBar.setVisibility(View.VISIBLE);
        if (textProcessing != null) textProcessing.setVisibility(View.VISIBLE);
        if (cardResults != null) cardResults.setVisibility(View.GONE);
        if (cardNoResults != null) cardNoResults.setVisibility(View.GONE);
        buttonGetRecommendations.setEnabled(false);

        try {
            // Create user profile data
            Map<String, Object> userProfile = createUserProfileData();
            // Create API service and make request
            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
            Call<RecommendationResponse> call = apiService.getRecommendations(userProfile);

            call.enqueue(new Callback<RecommendationResponse>() {
                @Override
                public void onResponse(Call<RecommendationResponse> call, Response<RecommendationResponse> response) {
                    Log.d("API_RESPONSE", "=== API RESPONSE RECEIVED ===");

                    progressBar.setVisibility(View.GONE);
                    if (textProcessing != null) textProcessing.setVisibility(View.GONE);
                    buttonGetRecommendations.setEnabled(true);

                    Log.d("API_RESPONSE", "Response Code: " + response.code());
                    Log.d("API_RESPONSE", "Response successful: " + response.isSuccessful());

                    if (response.isSuccessful() && response.body() != null) {
                        RecommendationResponse recommendationResponse = response.body();

                        Log.d("API_RESPONSE", "Response body received");
                        Log.d("API_RESPONSE", "Response status: " + recommendationResponse.getStatus());
                        Log.d("API_RESPONSE", "Response message: " + recommendationResponse.getMessage());

                        // Check if the response status is success
                        if ("success".equals(recommendationResponse.getStatus())) {
                            List<String> recommendations = recommendationResponse.getRecommendations();

                            Log.d("API_RESPONSE", "Recommendations object: " + recommendations);
                            Log.d("API_RESPONSE", "Recommendations count: " + (recommendations != null ? recommendations.size() : "null"));

                            if (recommendations != null && !recommendations.isEmpty()) {
                                Log.d("API_RESPONSE", "Recommendations received: " + recommendations.toString());

                                // Debug: Print each recommendation individually
                                for (int i = 0; i < recommendations.size(); i++) {
                                    Log.d("API_RESPONSE", "Recommendation " + i + ": '" + recommendations.get(i) + "'");
                                }

                                displayRecommendations(recommendations);

                            } else {
                                Log.w("API_RESPONSE", "Recommendations list is null or empty");
                                showNoRecommendations("No recommendations found for your profile. Try adjusting your skills or interests.");
                            }
                        } else {
                            // Handle error response from server
                            String errorMessage = recommendationResponse.getMessage();
                            Log.e("API_ERROR", "Server returned error: " + errorMessage);
                            showNoRecommendations("Server error: " + (errorMessage != null ? errorMessage : "Unknown error"));
                        }
                    } else {
                        Log.e("API_ERROR", "Response not successful or body is null");
                        Log.e("API_ERROR", "Response code: " + response.code());
                        Log.e("API_ERROR", "Response message: " + response.message());

                        // Try to read error body
                        if (response.errorBody() != null) {
                            try {
                                String errorString = response.errorBody().string();
                                Log.e("API_ERROR", "Error body: " + errorString);
                            } catch (Exception e) {
                                Log.e("API_ERROR", "Could not read error body", e);
                            }
                        }

                        showNoRecommendations("Failed to get recommendations. Server returned: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<RecommendationResponse> call, Throwable t) {
                    Log.e("API_ERROR", "=== API REQUEST FAILED ===");

                    progressBar.setVisibility(View.GONE);
                    if (textProcessing != null) textProcessing.setVisibility(View.GONE);
                    buttonGetRecommendations.setEnabled(true);

                    Log.e("API_ERROR", "Request failed: " + t.getClass().getSimpleName() + " - " + t.getMessage(), t);

                    String errorMessage;
                    if (t instanceof java.net.ConnectException) {
                        errorMessage = "Cannot connect to server. Please check your internet connection.";
                    } else if (t instanceof java.net.SocketTimeoutException) {
                        errorMessage = "Request timed out. Please try again.";
                    } else if (t instanceof java.net.UnknownHostException) {
                        errorMessage = "Cannot find server. Please check server status.";
                    } else {
                        errorMessage = "Network error: " + t.getMessage();
                    }

                    showNoRecommendations(errorMessage);
                    Toast.makeText(RecommendationsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            if (textProcessing != null) textProcessing.setVisibility(View.GONE);
            buttonGetRecommendations.setEnabled(true);

            Log.e("PROFILE_ERROR", "Error creating user profile", e);
            showNoRecommendations("Error processing your profile: " + e.getMessage());
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, Object> createUserProfileData() {
        Log.d("CREATE_PROFILE", "Creating user profile data...");

        Map<String, Object> userProfile = new HashMap<>();

        // Basic information
        userProfile.put("main_branch", "Developer");
        userProfile.put("work_mode", spinnerWorkMode.getSelectedItem().toString());
        userProfile.put("education", spinnerEducation.getSelectedItem().toString());
        userProfile.put("country", spinnerCountry.getSelectedItem().toString());

        // Experience data
        int yearsCode = Integer.parseInt(editTextYearsCode.getText().toString().trim());
        float workExp = Float.parseFloat(editTextWorkExperience.getText().toString().trim());

        userProfile.put("years_code", yearsCode);
        userProfile.put("work_experience", workExp);

        // Skills and interests
        userProfile.put("skills", new ArrayList<>(selectedSkills));
        userProfile.put("interests", new ArrayList<>(selectedInterests));

        Log.d("CREATE_PROFILE", "Profile created: " + userProfile.toString());
        return userProfile;
    }

    private void displayRecommendations(List<String> recommendations) {
        Log.d("DISPLAY_RECOMMENDATIONS", "=== DISPLAYING RECOMMENDATIONS ===");
        Log.d("DISPLAY_RECOMMENDATIONS", "Displaying " + recommendations.size() + " recommendations");

        // Create formatted list for display
        List<String> formattedRecommendations = new ArrayList<>();
        for (int i = 0; i < recommendations.size(); i++) {
            String recommendation = recommendations.get(i);
            Log.d("DISPLAY_RECOMMENDATIONS", "Processing recommendation " + i + ": '" + recommendation + "'");

            // Format and capitalize the recommendation
            String formatted = formatRecommendation(recommendation);
            String displayText = (i + 1) + ". " + formatted;

            formattedRecommendations.add(displayText);
            Log.d("DISPLAY_RECOMMENDATIONS", "Formatted as: '" + displayText + "'");
        }

        // Create adapter with larger text size for better visibility
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, formattedRecommendations) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                if (textView != null) {
                    textView.setTextSize(18); // Larger text
                    textView.setPadding(16, 16, 16, 16); // More padding
                    textView.setTextColor(getResources().getColor(android.R.color.black));
                }
                return view;
            }
        };

        if (listViewRecommendations != null) {
            listViewRecommendations.setAdapter(adapter);
            Log.d("DISPLAY_RECOMMENDATIONS", "ListView adapter set");
        } else {
            Log.e("DISPLAY_RECOMMENDATIONS", "listViewRecommendations is null!");
        }

        // Show results card and hide no results card
        if (cardResults != null) {
            cardResults.setVisibility(View.VISIBLE);
            Log.d("DISPLAY_RECOMMENDATIONS", "Results card made visible");
        } else {
            Log.e("DISPLAY_RECOMMENDATIONS", "cardResults is null!");
        }

        if (cardNoResults != null) {
            cardNoResults.setVisibility(View.GONE);
            Log.d("DISPLAY_RECOMMENDATIONS", "No results card hidden");
        }

        // Show success message
        String successMessage = "Found " + recommendations.size() + " career recommendations!";
        Toast.makeText(this, successMessage, Toast.LENGTH_LONG).show();
        Log.d("DISPLAY_RECOMMENDATIONS", "Success message displayed: " + successMessage);

        Log.d("DISPLAY_RECOMMENDATIONS", "=== DISPLAY COMPLETED ===");
    }

    private void showNoRecommendations(String message) {
        Log.d("SHOW_NO_RECOMMENDATIONS", "Showing no recommendations: " + message);

        if (textNoRecommendations != null) {
            textNoRecommendations.setText(message);
        } else {
            Log.e("SHOW_NO_RECOMMENDATIONS", "textNoRecommendations is null!");
        }

        if (cardNoResults != null) {
            cardNoResults.setVisibility(View.VISIBLE);
            Log.d("SHOW_NO_RECOMMENDATIONS", "No results card made visible");
        } else {
            Log.e("SHOW_NO_RECOMMENDATIONS", "cardNoResults is null!");
        }

        if (cardResults != null) {
            cardResults.setVisibility(View.GONE);
            Log.d("SHOW_NO_RECOMMENDATIONS", "Results card hidden");
        }
    }

    private String formatRecommendation(String recommendation) {
        if (recommendation == null || recommendation.trim().isEmpty()) {
            return "Unknown Role";
        }

        // Clean up and format the recommendation text
        String formatted = recommendation.trim();

        // Remove leading/trailing spaces and handle empty strings
        if (formatted.isEmpty()) {
            return "Unknown Role";
        }

        // Capitalize first letter
        formatted = formatted.substring(0, 1).toUpperCase() + formatted.substring(1);

        // Replace common separators
        formatted = formatted.replace("-", " ");
        formatted = formatted.replace("_", " ");

        // Handle specific role formatting
        String lower = formatted.toLowerCase();
        if (lower.contains("full") && lower.contains("stack")) {
            formatted = "Full Stack Developer";
        } else if (lower.contains("front") && lower.contains("end")) {
            formatted = "Front End Developer";
        } else if (lower.contains("back") && lower.contains("end")) {
            formatted = "Back End Developer";
        } else if (lower.contains("mobile")) {
            formatted = "Mobile Application Developer";
        } else if (lower.contains("web")) {
            formatted = "Web Developer";
        } else if (lower.contains("software")) {
            formatted = "Software Developer";
        } else if (lower.contains("data")) {
            formatted = "Data Scientist";
        } else if (lower.contains("devops")) {
            formatted = "DevOps Engineer";
        }

        // Ensure it ends with "Developer" or "Engineer" if it's a simple term
        if (!formatted.toLowerCase().contains("developer") &&
                !formatted.toLowerCase().contains("engineer") &&
                !formatted.toLowerCase().contains("scientist") &&
                !formatted.toLowerCase().contains("analyst")) {
            formatted += " Developer";
        }

        return formatted;
    }

    private void clearAllSelections() {
        Log.d("CLEAR_ALL", "Clearing all selections...");

        // Clear input fields
        editTextYearsCode.setText("");
        editTextWorkExperience.setText("");

        // Reset spinners
        spinnerEducation.setSelection(0);
        spinnerWorkMode.setSelection(0);
        spinnerCountry.setSelection(0);
        spinnerSkills.setSelection(0);
        spinnerInterests.setSelection(0);

        // Clear selected items
        selectedSkills.clear();
        selectedInterests.clear();

        // Update displays
        updateSelectedSkillsDisplay();
        updateSelectedInterestsDisplay();

        // Hide results
        if (cardResults != null) cardResults.setVisibility(View.GONE);
        if (cardNoResults != null) cardNoResults.setVisibility(View.GONE);

        Toast.makeText(this, "All selections cleared!", Toast.LENGTH_SHORT).show();
        Log.d("CLEAR_ALL", "All selections cleared successfully");
    }

    // Debug method to check view references
    private void checkViewReferences() {
        Log.d("VIEW_CHECK", "=== CHECKING VIEW REFERENCES ===");
        Log.d("VIEW_CHECK", "cardResults: " + (cardResults != null ? "Found" : "NULL"));
        Log.d("VIEW_CHECK", "cardNoResults: " + (cardNoResults != null ? "Found" : "NULL"));
        Log.d("VIEW_CHECK", "listViewRecommendations: " + (listViewRecommendations != null ? "Found" : "NULL"));
        Log.d("VIEW_CHECK", "textNoRecommendations: " + (textNoRecommendations != null ? "Found" : "NULL"));
        Log.d("VIEW_CHECK", "progressBar: " + (progressBar != null ? "Found" : "NULL"));
        Log.d("VIEW_CHECK", "textProcessing: " + (textProcessing != null ? "Found" : "NULL"));
        Log.d("VIEW_CHECK", "selectedSkillsLayout: " + (selectedSkillsLayout != null ? "Found" : "NULL"));
        Log.d("VIEW_CHECK", "selectedInterestsLayout: " + (selectedInterestsLayout != null ? "Found" : "NULL"));
        Log.d("VIEW_CHECK", "=== VIEW CHECK COMPLETED ===");
    }

    // Test method for debugging display issues
    private void testDisplayRecommendations() {
        Log.d("TEST_DISPLAY", "Testing display with hardcoded recommendations...");

        // Test with hardcoded recommendations
        List<String> testRecommendations = Arrays.asList(
                "full-stack",
                "front-end",
                "back-end"
        );

        Log.d("TEST_DISPLAY", "Testing display with: " + testRecommendations.toString());

        // Call the display method directly
        displayRecommendations(testRecommendations);

        // Also test the cards visibility
        Log.d("TEST_DISPLAY", "After display call:");
        Log.d("TEST_DISPLAY", "cardResults visibility: " +
                (cardResults != null && cardResults.getVisibility() == View.VISIBLE ? "VISIBLE" : "GONE/INVISIBLE"));
        Log.d("TEST_DISPLAY", "cardNoResults visibility: " +
                (cardNoResults != null && cardNoResults.getVisibility() == View.VISIBLE ? "VISIBLE" : "GONE/INVISIBLE"));
    }

    // Force show results method for ultimate testing
    private void forceShowResults() {
        Log.d("FORCE_SHOW", "Force showing test results...");

        // Hide loading states
        progressBar.setVisibility(View.GONE);
        if (textProcessing != null) textProcessing.setVisibility(View.GONE);

        // Show results card
        if (cardResults != null) {
            cardResults.setVisibility(View.VISIBLE);
            Log.d("FORCE_SHOW", "Results card forced to visible");
        }
        if (cardNoResults != null) {
            cardNoResults.setVisibility(View.GONE);
            Log.d("FORCE_SHOW", "No results card hidden");
        }

        // Create test data
        List<String> testData = Arrays.asList(
                "1. Full Stack Developer",
                "2. Front End Developer",
                "3. Back End Developer"
        );

        if (listViewRecommendations != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, testData);
            listViewRecommendations.setAdapter(adapter);
            Log.d("FORCE_SHOW", "Test adapter set to ListView");
        } else {
            Log.e("FORCE_SHOW", "ListView is null!");
        }

        Toast.makeText(this, "Force showing test results", Toast.LENGTH_SHORT).show();
        Log.d("FORCE_SHOW", "Force show completed");
    }

    // Add test buttons method (call this in onCreate for debugging)
    private void addTestButtons() {
        Log.d("ADD_TEST_BUTTONS", "Adding test buttons for debugging...");

        // Create test display button
        Button testDisplayButton = new Button(this);
        testDisplayButton.setText("Test Display");
        testDisplayButton.setBackgroundColor(Color.BLUE);
        testDisplayButton.setTextColor(Color.WHITE);
        testDisplayButton.setOnClickListener(v -> testDisplayRecommendations());

        // Create force show button
        Button forceShowButton = new Button(this);
        forceShowButton.setText("Force Show");
        forceShowButton.setBackgroundColor(Color.RED);
        forceShowButton.setTextColor(Color.WHITE);
        forceShowButton.setOnClickListener(v -> forceShowResults());

        // Try to find a layout to add buttons to
        // You might need to adjust this based on your XML layout structure
        try {
            // Look for the main LinearLayout in your ScrollView
            LinearLayout buttonContainer = new LinearLayout(this);
            buttonContainer.setOrientation(LinearLayout.HORIZONTAL);
            buttonContainer.addView(testDisplayButton);
            buttonContainer.addView(forceShowButton);

            // This is a generic approach - you might need to modify based on your layout
            ViewGroup rootLayout = findViewById(android.R.id.content);
            if (rootLayout instanceof ViewGroup) {
                ((ViewGroup) rootLayout).addView(buttonContainer, 0);
                Log.d("ADD_TEST_BUTTONS", "Test buttons added successfully");
            }
        } catch (Exception e) {
            Log.e("ADD_TEST_BUTTONS", "Could not add test buttons", e);
        }
    }
}