package com.example.fyp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JobInsightsActivity extends AppCompatActivity {

    // UI Components
    private ChipGroup chipSkills, chipJobType;
    private Spinner spinnerExperience, spinnerLocation, spinnerCountry;
    private RangeSlider salaryRangeSlider;
    private Button btnApplyFilters, btnClearFilters;
    private ImageButton btnFilterOptions;
    private TextView textJobCount, textNoJobs;
    private ProgressBar progressBar;
    private RecyclerView recyclerJobResults;
    private CardView cardNoJobs;

    // Data
    private JobAdapter jobAdapter;
    private List<JobInsightResponse> jobsList = new ArrayList<>();

    // API Configuration
    private static final String APP_ID = "49313ed2";
    private static final String APP_KEY = "02b57e84603cadd31d4f8d3b10f6d5cc";

    private String[] countryNames = {
            "United Kingdom", "United States", "Austria", "Australia", "Belgium",
            "Brazil", "Canada", "Switzerland", "Germany", "Spain",
            "France", "India", "Italy", "Mexico", "Netherlands",
            "New Zealand", "Poland", "Singapore", "South Africa"
    };

    private String[] countryCodes = {
            "gb", "us", "at", "au", "be",
            "br", "ca", "ch", "de", "es",
            "fr", "in", "it", "mx", "nl",
            "nz", "pl", "sg", "za"
    };

    private String selectedCountryCode = "gb"; // default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_insights);

        initializeViews();
        setupSpinners();
        setupRecyclerView();
        setupListeners();
    }

    private void initializeViews() {
        Log.d("JOB_INSIGHTS", "Initializing views...");

        // Chip Groups
        chipSkills = findViewById(R.id.chipSkills);
        chipJobType = findViewById(R.id.chipJobType);

        // Spinners
        spinnerExperience = findViewById(R.id.spinnerExperience);
        spinnerLocation = findViewById(R.id.spinnerLocation);
        spinnerCountry = findViewById(R.id.spinnerCountry);

        // Slider
        salaryRangeSlider = findViewById(R.id.salaryRangeSlider);

        // Buttons
        btnApplyFilters = findViewById(R.id.btnApplyFilters);
        btnClearFilters = findViewById(R.id.btnClearFilters);
        btnFilterOptions = findViewById(R.id.btnFilterOptions);

        // Text Views
        textJobCount = findViewById(R.id.textJobCount);
        textNoJobs = findViewById(R.id.textNoJobs);

        // Progress Bar
        progressBar = findViewById(R.id.progressBar);

        // RecyclerView
        recyclerJobResults = findViewById(R.id.recyclerJobResults);

        // Initialize empty adapter
        JobAdapter jobAdapter = new JobAdapter(this, new ArrayList<>());

        recyclerJobResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerJobResults.setAdapter(jobAdapter);

        // Cards
        cardNoJobs = findViewById(R.id.cardNoJobs);

        // Initially hide results views
        progressBar.setVisibility(View.GONE);
        recyclerJobResults.setVisibility(View.GONE);
        cardNoJobs.setVisibility(View.GONE);

        Log.d("JOB_INSIGHTS", "Views initialized successfully");

    }

private void setupSpinners() {

    Log.d("JOB_INSIGHTS", "Setting up spinners...");

    String[] experienceLevels = {
            "Any Experience", "Entry Level", "Mid-Level", "Senior Level", "Executive"
    };
    ArrayAdapter<String> experienceAdapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_dropdown_item, experienceLevels);
    spinnerExperience.setAdapter(experienceAdapter);

    String[] locations = {
            "Any Location", "London", "Manchester", "Birmingham", "Edinburgh",
            "Glasgow", "Liverpool", "Bristol", "Leeds", "Sheffield"
    };
    ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_dropdown_item, locations);
    spinnerLocation.setAdapter(locationAdapter);

    ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, Arrays.asList(countryNames));
    countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerCountry.setAdapter(countryAdapter);

    spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedCountryCode = countryCodes[position];
            Log.d("COUNTRY_SELECTED", "Code: " + selectedCountryCode);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    });
    Log.d("JOB_INSIGHTS","Spinners setup completed");
}

    private void setupRecyclerView() {
        Log.d("JOB_INSIGHTS", "Setting up RecyclerView...");

        jobAdapter = new JobAdapter(this, jobsList);
        recyclerJobResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerJobResults.setAdapter(jobAdapter);

        Log.d("JOB_INSIGHTS", "RecyclerView setup completed");
    }

    private void setupListeners() {
        Log.d("JOB_INSIGHTS", "Setting up listeners...");

        // Apply Filters Button
        btnApplyFilters.setOnClickListener(v -> {
            Log.d("JOB_INSIGHTS", "Apply filters button clicked");
            applyFilters();
        });

        // Clear Filters Button
        btnClearFilters.setOnClickListener(v -> {
            Log.d("JOB_INSIGHTS", "Clear filters button clicked");
            clearFilters();
        });

        // Filter Options Button
        btnFilterOptions.setOnClickListener(v -> {
            Toast.makeText(this, "Advanced filters coming soon!", Toast.LENGTH_SHORT).show();
        });

        Log.d("JOB_INSIGHTS", "Listeners setup completed");
    }

    private void applyFilters() {
        Log.d("JOB_INSIGHTS", "=== APPLYING FILTERS ===");

        // Show loading state
        showLoadingState();

        // Collect filter data
        List<String> selectedSkills = getSelectedSkills();
        List<String> selectedJobTypes = getSelectedJobTypes();
        String experience = spinnerExperience.getSelectedItem().toString();
        String location = spinnerLocation.getSelectedItem().toString();
        List<Float> salaryRange = salaryRangeSlider.getValues();

        Log.d("JOB_INSIGHTS", "Selected skills: " + selectedSkills.toString());
        Log.d("JOB_INSIGHTS", "Selected job types: " + selectedJobTypes.toString());
        Log.d("JOB_INSIGHTS", "Experience level: " + experience);
        Log.d("JOB_INSIGHTS", "Location: " + location);
        Log.d("JOB_INSIGHTS", "Salary range: " + salaryRange.toString());

        // Build search query
        String searchQuery = buildSearchQuery(selectedSkills, selectedJobTypes);
        String searchLocation = location.equals("Any Location") ? "" : location.toLowerCase();

        Log.d("JOB_INSIGHTS", "Search query: " + searchQuery);
        Log.d("JOB_INSIGHTS", "Search location: " + searchLocation);

        // Make API call
        searchJobs(searchQuery, searchLocation);
    }

    private List<String> getSelectedSkills() {
        List<String> selectedSkills = new ArrayList<>();
        for (int i = 0; i < chipSkills.getChildCount(); i++) {
            Chip chip = (Chip) chipSkills.getChildAt(i);
            if (chip.isChecked()) {
                selectedSkills.add(chip.getText().toString());
            }
        }
        return selectedSkills;
    }

    private List<String> getSelectedJobTypes() {
        List<String> selectedJobTypes = new ArrayList<>();
        for (int i = 0; i < chipJobType.getChildCount(); i++) {
            Chip chip = (Chip) chipJobType.getChildAt(i);
            if (chip.isChecked()) {
                selectedJobTypes.add(chip.getText().toString());
            }
        }
        return selectedJobTypes;
    }

    private String buildSearchQuery(List<String> skills, List<String> jobTypes) {
        StringBuilder query = new StringBuilder();

        // Add skills to query
        if (!skills.isEmpty()) {
            query.append(String.join(" ", skills));
        }

        // Add job types to query
        if (!jobTypes.isEmpty()) {
            if (query.length() > 0) {
                query.append(" ");
            }
            query.append(String.join(" ", jobTypes));
        }

        // Default query if nothing selected
        if (query.length() == 0) {
            query.append("developer");
        }

        return query.toString();
    }

    private void searchJobs(String query, String location) {
        Log.d("JOB_INSIGHTS", "Searching jobs with query: '" + query + "', location: '" + location + "'");

        JobInsightRequest jobInsightRequest=new JobInsightRequest("gb","developer",1);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Use "gb" as default country for UK jobs
        //Call<JobListResponse> call = apiService.getJobInsights("pk","developer");
        Call<JobListResponse> call = apiService.getJobInsights(selectedCountryCode, query);


        call.enqueue(new Callback<JobListResponse>() {
            @Override
            public void onResponse(Call<JobListResponse> call, Response<JobListResponse> response) {
                Log.d("JOB_INSIGHTS", "=== API RESPONSE RECEIVED ===");
                Log.d("JOB_INSIGHTS", "Response code: " + response.code());
                Log.d("JOB_INSIGHTS", "Response successful: " + response.isSuccessful());

                hideLoadingState();

                if (response.isSuccessful() && response.body() != null) {
                    JobListResponse jobResponse = response.body();

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String json = gson.toJson(jobResponse);
                    Log.d("JOB_INSIGHTS_JSON", json);


                    List<JobInsightResponse> jobs = jobResponse.getResults();
                    recyclerJobResults.setVisibility(View.VISIBLE); // 👈 Show the list
                    jobAdapter.updateJobs(jobs);
                    Log.d("response", "###response" + response);

                }
                else
                {
                    Log.d("JOB_INSIGHTS", "API response error: " + response.message());
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.d("JOB_INSIGHTS", "Error body: " + errorBody);
                        } catch (Exception e) {
                            Log.d("JOB_INSIGHTS", "Could not read error body", e);
                        }
                    }
                    showNoJobsFound("Failed to fetch jobs. Please try again.");
                    Toast.makeText(JobInsightsActivity.this,
                            "Error fetching jobs: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JobListResponse> call, Throwable t) {
                Log.d("JOB_INSIGHTS", "=== API REQUEST FAILED ===");
                Log.d("JOB_INSIGHTS", "Error: " + t.getClass().getSimpleName() + " - " + t.getMessage(), t);

                hideLoadingState();

                String errorMessage;
                if (t instanceof java.net.ConnectException) {
                    errorMessage = "Cannot connect to job service. Please check your internet connection.";
                } else if (t instanceof java.net.SocketTimeoutException) {
                    errorMessage = "Request timed out. Please try again.";
                } else if (t instanceof java.net.UnknownHostException) {
                    errorMessage = "Cannot reach job service. Please check your connection.";
                } else {
                    errorMessage = "Network error: " + t.getMessage();
                }

                showNoJobsFound(errorMessage);
                Toast.makeText(JobInsightsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void displayJobs(List<JobInsightResponse> jobs) {
        Log.d("JOB_INSIGHTS", "Displaying " + jobs.size() + " jobs");

        // Update job count
        String countText = "Found " + jobs.size() + " job" + (jobs.size() != 1 ? "s" : "");
        textJobCount.setText(countText);

        // Update adapter
        jobsList.clear();
        jobsList.addAll(jobs);
        jobAdapter.notifyDataSetChanged();

        // Show results
        recyclerJobResults.setVisibility(View.VISIBLE);
        cardNoJobs.setVisibility(View.GONE);

        // Show success message
        Toast.makeText(this, countText + " matching your criteria!", Toast.LENGTH_SHORT).show();

        Log.d("JOB_INSIGHTS", "Jobs displayed successfully");
    }

    private void showNoJobsFound(String message) {
        Log.d("JOB_INSIGHTS", "Showing no jobs found: " + message);

        textJobCount.setText("No jobs found");
        textNoJobs.setText(message);

        recyclerJobResults.setVisibility(View.GONE);
        cardNoJobs.setVisibility(View.VISIBLE);
    }

    private void showLoadingState() {
        Log.d("JOB_INSIGHTS", "Showing loading state");

        progressBar.setVisibility(View.VISIBLE);
        textJobCount.setText("Searching for jobs...");
        recyclerJobResults.setVisibility(View.GONE);
        cardNoJobs.setVisibility(View.GONE);
        btnApplyFilters.setEnabled(false);
    }

    private void hideLoadingState() {
        Log.d("JOB_INSIGHTS", "Hiding loading state");

        progressBar.setVisibility(View.GONE);
        btnApplyFilters.setEnabled(true);
    }

    private void clearFilters() {
        Log.d("JOB_INSIGHTS", "Clearing all filters");

        // Clear skill chips
        for (int i = 0; i < chipSkills.getChildCount(); i++) {
            Chip chip = (Chip) chipSkills.getChildAt(i);
            chip.setChecked(false);
        }

        // Clear job type chips
        for (int i = 0; i < chipJobType.getChildCount(); i++) {
            Chip chip = (Chip) chipJobType.getChildAt(i);
            chip.setChecked(false);
        }

        // Reset spinners
        spinnerExperience.setSelection(0);
        spinnerLocation.setSelection(0);

        // Reset salary slider
        salaryRangeSlider.setValues(100000f, 200000f);

        // Reset display
        textJobCount.setText("Search for jobs using filters above");
        recyclerJobResults.setVisibility(View.GONE);
        cardNoJobs.setVisibility(View.GONE);

        // Clear jobs list
        jobsList.clear();
        jobAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Filters cleared", Toast.LENGTH_SHORT).show();
        Log.d("JOB_INSIGHTS", "All filters cleared successfully");
    }
}