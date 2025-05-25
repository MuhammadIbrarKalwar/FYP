package com.example.fyp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ProgressTrackingActivity extends AppCompatActivity {

    // UI Components
    private TextView textOverallProgress, textSkillsCompleted, textGoalsPending, textLastAssessment;
    private ProgressBar progressOverall, progressSkills, progressGoals;
    private LinearLayout skillsProgressLayout, goalsLayout, reportsLayout;
    private Button buttonTakeAssessment, buttonSetGoals, buttonViewDetailedReport;
    private CardView cardOverview, cardSkillsProgress, cardGoals, cardReports;

    // Data
    private SharedPreferences prefs;
    private List<String> userSkills = new ArrayList<>();
    private List<String> recommendedSkills = new ArrayList<>();
    private List<String> completedGoals = new ArrayList<>();
    private List<String> pendingGoals = new ArrayList<>();

    // Sample data for demonstration
    private String[] allAvailableSkills = {
            "Java", "Python", "JavaScript", "React", "Node.js", "Angular", "Vue.js",
            "Django", "Flask", "SQL", "MongoDB", "Docker", "Kubernetes", "AWS",
            "Machine Learning", "Data Science", "Mobile Development", "DevOps"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_tracking);

        initializeViews();
        loadUserData();
        setupProgressData();
        setupListeners();
        updateUI();
    }

    private void initializeViews() {
        Log.d("PROGRESS_TRACKING", "Initializing views...");

        // Overview section
        textOverallProgress = findViewById(R.id.textOverallProgress);
        textSkillsCompleted = findViewById(R.id.textSkillsCompleted);
        textGoalsPending = findViewById(R.id.textGoalsPending);
        textLastAssessment = findViewById(R.id.textLastAssessment);

        // Progress bars
        progressOverall = findViewById(R.id.progressOverall);
        progressSkills = findViewById(R.id.progressSkills);
        progressGoals = findViewById(R.id.progressGoals);

        // Layouts
        skillsProgressLayout = findViewById(R.id.skillsProgressLayout);
        goalsLayout = findViewById(R.id.goalsLayout);
        reportsLayout = findViewById(R.id.reportsLayout);

        // Buttons
        buttonTakeAssessment = findViewById(R.id.buttonTakeAssessment);
        buttonSetGoals = findViewById(R.id.buttonSetGoals);
        buttonViewDetailedReport = findViewById(R.id.buttonViewDetailedReport);

        // Cards
        cardOverview = findViewById(R.id.cardOverview);
        cardSkillsProgress = findViewById(R.id.cardSkillsProgress);
        cardGoals = findViewById(R.id.cardGoals);
        cardReports = findViewById(R.id.cardReports);

        Log.d("PROGRESS_TRACKING", "Views initialized successfully");
    }

    private void loadUserData() {
        Log.d("PROGRESS_TRACKING", "Loading user data...");

        prefs = getSharedPreferences("UserProfile", MODE_PRIVATE);

        // Load user skills from profile
        String skillsString = prefs.getString("skills", "Java,Python");
        if (!skillsString.isEmpty()) {
            userSkills = new ArrayList<>(Arrays.asList(skillsString.split(",")));
        }

        // Load assessment results
        int lastAssessmentScore = prefs.getInt("last_assessment_score", 0);
        String lastAssessmentDate = prefs.getString("last_assessment_date", "Never taken");

        textLastAssessment.setText("Last Assessment: " +
                (lastAssessmentScore > 0 ? lastAssessmentScore + "/25 on " + lastAssessmentDate : "Never taken"));

        Log.d("PROGRESS_TRACKING", "User skills loaded: " + userSkills.size());
    }

    private void setupProgressData() {
        Log.d("PROGRESS_TRACKING", "Setting up progress data...");

        // Generate recommended skills based on user's current skills
        generateRecommendedSkills();

        // Generate sample goals
        generateSampleGoals();

        // Calculate progress metrics
        calculateProgressMetrics();

        Log.d("PROGRESS_TRACKING", "Progress data setup completed");
    }

    private void generateRecommendedSkills() {
        recommendedSkills.clear();

        // Add some logical skill recommendations based on current skills
        if (userSkills.contains("Java")) {
            recommendedSkills.add("Spring Boot");
            recommendedSkills.add("Hibernate");
        }
        if (userSkills.contains("Python")) {
            recommendedSkills.add("Django");
            recommendedSkills.add("Machine Learning");
        }
        if (userSkills.contains("JavaScript")) {
            recommendedSkills.add("React");
            recommendedSkills.add("Node.js");
        }

        // Add some general recommendations
        recommendedSkills.add("Docker");
        recommendedSkills.add("AWS");
        recommendedSkills.add("Git");

        Log.d("PROGRESS_TRACKING", "Generated " + recommendedSkills.size() + " recommended skills");
    }

    private void generateSampleGoals() {
        completedGoals.clear();
        pendingGoals.clear();

        // Some completed goals
        completedGoals.add("Complete Java Fundamentals");
        completedGoals.add("Build First Web Application");
        completedGoals.add("Learn SQL Basics");

        // Some pending goals
        pendingGoals.add("Master React Framework");
        pendingGoals.add("Deploy Application to Cloud");
        pendingGoals.add("Complete Python Advanced Course");
        pendingGoals.add("Learn Docker Containerization");

        Log.d("PROGRESS_TRACKING", "Generated goals - Completed: " + completedGoals.size() +
                ", Pending: " + pendingGoals.size());
    }

    private void calculateProgressMetrics() {
        // Calculate overall progress (combination of skills and goals)
        int totalItems = userSkills.size() + completedGoals.size() + pendingGoals.size();
        int completedItems = userSkills.size() + completedGoals.size();
        int overallProgress = totalItems > 0 ? (completedItems * 100) / totalItems : 0;

        // Skills progress
        int skillsProgress = userSkills.size() > 0 ? Math.min(100, (userSkills.size() * 100) / 10) : 0;

        // Goals progress
        int totalGoals = completedGoals.size() + pendingGoals.size();
        int goalsProgress = totalGoals > 0 ? (completedGoals.size() * 100) / totalGoals : 0;

        // Update progress bars
        progressOverall.setProgress(overallProgress);
        progressSkills.setProgress(skillsProgress);
        progressGoals.setProgress(goalsProgress);

        // Update text displays
        textOverallProgress.setText(overallProgress + "% Complete");
        textSkillsCompleted.setText(userSkills.size() + " Skills Mastered");
        textGoalsPending.setText(pendingGoals.size() + " Goals Pending");

        Log.d("PROGRESS_TRACKING", "Progress calculated - Overall: " + overallProgress +
                "%, Skills: " + skillsProgress + "%, Goals: " + goalsProgress + "%");
    }

    private void setupListeners() {
        Log.d("PROGRESS_TRACKING", "Setting up listeners...");

        buttonTakeAssessment.setOnClickListener(v -> {
            Toast.makeText(this, "Redirecting to Skill Assessment...", Toast.LENGTH_SHORT).show();
            // In a real app, you would start SkillAssessmentActivity
             Intent intent = new Intent(this, SkillAssessmentActivity.class);
             startActivity(intent);

            // For demo, simulate taking assessment
            simulateAssessmentCompletion();
        });

        buttonSetGoals.setOnClickListener(v -> showSetGoalsDialog());

        buttonViewDetailedReport.setOnClickListener(v -> generateDetailedReport());
    }

    private void updateUI() {
        Log.d("PROGRESS_TRACKING", "Updating UI...");

        // Update skills progress section
        updateSkillsProgressSection();

        // Update goals section
        updateGoalsSection();

        // Update reports section
        updateReportsSection();

        Log.d("PROGRESS_TRACKING", "UI update completed");
    }

    private void updateSkillsProgressSection() {
        skillsProgressLayout.removeAllViews();

        // Add current skills
        TextView skillsHeader = new TextView(this);
        skillsHeader.setText("🎯 Your Current Skills");
        skillsHeader.setTextSize(16);
        skillsHeader.setTextColor(Color.parseColor("#1565C0"));
        skillsHeader.setTextSize(Typeface.BOLD);
        skillsProgressLayout.addView(skillsHeader);

        for (String skill : userSkills) {
            TextView skillItem = createSkillProgressItem(skill, 100, Color.parseColor("#4CAF50"));
            skillsProgressLayout.addView(skillItem);
        }

        // Add recommended skills
        TextView recommendedHeader = new TextView(this);
        recommendedHeader.setText("\n💡 Recommended Skills to Learn");
        recommendedHeader.setTextSize(16);
        recommendedHeader.setTextColor(Color.parseColor("#FF9800"));
        recommendedHeader.setTextSize(Typeface.BOLD);
        skillsProgressLayout.addView(recommendedHeader);

        for (String skill : recommendedSkills) {
            // Random progress for demo (0-30% for recommended skills)
            int progress = new Random().nextInt(31);
            TextView skillItem = createSkillProgressItem(skill, progress, Color.parseColor("#FF9800"));
            skillsProgressLayout.addView(skillItem);
        }
    }

    private TextView createSkillProgressItem(String skillName, int progress, int color) {
        TextView skillItem = new TextView(this);
        String progressText = "• " + skillName + " (" + progress + "%)";
        skillItem.setText(progressText);
        skillItem.setTextSize(14);
        skillItem.setTextColor(color);
        skillItem.setPadding(16, 8, 16, 8);
        return skillItem;
    }

    private void updateGoalsSection() {
        goalsLayout.removeAllViews();

        // Completed goals
        TextView completedHeader = new TextView(this);
        completedHeader.setText("✅ Completed Goals");
        completedHeader.setTextSize(16);
        completedHeader.setTextColor(Color.parseColor("#4CAF50"));
        completedHeader.setTextSize(android.graphics.Typeface.BOLD);
        goalsLayout.addView(completedHeader);

        for (String goal : completedGoals) {
            TextView goalItem = new TextView(this);
            goalItem.setText("✓ " + goal);
            goalItem.setTextSize(14);
            goalItem.setTextColor(Color.parseColor("#4CAF50"));
            goalItem.setPadding(16, 8, 16, 8);
            goalsLayout.addView(goalItem);
        }

        // Pending goals
        TextView pendingHeader = new TextView(this);
        pendingHeader.setText("\n⏳ Pending Goals");
        pendingHeader.setTextSize(16);
        pendingHeader.setTextColor(Color.parseColor("#FF9800"));
        pendingHeader.setTextSize(android.graphics.Typeface.BOLD);
        goalsLayout.addView(pendingHeader);

        for (String goal : pendingGoals) {
            TextView goalItem = new TextView(this);
            goalItem.setText("○ " + goal);
            goalItem.setTextSize(14);
            goalItem.setTextColor(Color.parseColor("#666666"));
            goalItem.setPadding(16, 8, 16, 8);
            goalsLayout.addView(goalItem);
        }
    }

    private void updateReportsSection() {
        reportsLayout.removeAllViews();

        // Add report summary
        TextView reportSummary = new TextView(this);
        String summary = "📊 Progress Summary:\n\n" +
                "• Skills Acquired: " + userSkills.size() + "\n" +
                "• Goals Completed: " + completedGoals.size() + "\n" +
                "• Goals Pending: " + pendingGoals.size() + "\n" +
                "• Overall Progress: " + progressOverall.getProgress() + "%\n\n" +
                "🎯 Recommendations:\n" +
                "• Focus on completing pending goals\n" +
                "• Take skill assessments regularly\n" +
                "• Learn recommended skills to advance career";

        reportSummary.setText(summary);
        reportSummary.setTextSize(14);
        reportSummary.setTextColor(Color.parseColor("#333333"));
        reportSummary.setPadding(16, 16, 16, 16);
        reportSummary.setBackgroundColor(Color.parseColor("#F5F5F5"));
        reportsLayout.addView(reportSummary);
    }

    private void simulateAssessmentCompletion() {
        // Simulate assessment score
        Random random = new Random();
        int score = 15 + random.nextInt(11); // Score between 15-25

        // Save assessment result
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("last_assessment_score", score);
        editor.putString("last_assessment_date", getCurrentDate());
        editor.apply();

        // Show result
        Toast.makeText(this, "Assessment Completed! Score: " + score + "/25", Toast.LENGTH_LONG).show();

        // Refresh data
        loadUserData();
        calculateProgressMetrics();
        updateUI();
    }

    private void showSetGoalsDialog() {
        Toast.makeText(this, "Goal Setting Feature Coming Soon!", Toast.LENGTH_SHORT).show();

        // For demo, add a random goal
        String[] newGoals = {
                "Learn Cloud Computing",
                "Master Database Design",
                "Complete Cybersecurity Course",
                "Build Mobile Application",
                "Learn Data Visualization"
        };

        Random random = new Random();
        String newGoal = newGoals[random.nextInt(newGoals.length)];

        if (!pendingGoals.contains(newGoal)) {
            pendingGoals.add(newGoal);
            Toast.makeText(this, "Added Goal: " + newGoal, Toast.LENGTH_SHORT).show();
            calculateProgressMetrics();
            updateUI();
        }
    }

    private void generateDetailedReport() {
        // Create detailed report
        StringBuilder report = new StringBuilder();
        report.append("📈 DETAILED PROGRESS REPORT\n");
        report.append("Generated on: ").append(getCurrentDate()).append("\n\n");

        report.append("🎯 SKILLS ANALYSIS:\n");
        report.append("Current Skills: ").append(userSkills.size()).append("\n");
        for (String skill : userSkills) {
            report.append("✓ ").append(skill).append("\n");
        }

        report.append("\n💡 RECOMMENDED SKILLS:\n");
        for (String skill : recommendedSkills) {
            report.append("○ ").append(skill).append("\n");
        }

        report.append("\n🎯 GOALS STATUS:\n");
        report.append("Completed: ").append(completedGoals.size()).append("\n");
        report.append("Pending: ").append(pendingGoals.size()).append("\n");

        report.append("\n📊 PERFORMANCE METRICS:\n");
        report.append("Overall Progress: ").append(progressOverall.getProgress()).append("%\n");
        report.append("Skills Progress: ").append(progressSkills.getProgress()).append("%\n");
        report.append("Goals Progress: ").append(progressGoals.getProgress()).append("%\n");

        // Show report (in a real app, you might create a new activity or dialog)
        Toast.makeText(this, "Detailed report generated! Check logs for full report.", Toast.LENGTH_SHORT).show();
        Log.i("DETAILED_REPORT", report.toString());
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to this activity
        loadUserData();
        calculateProgressMetrics();
        updateUI();
    }
}