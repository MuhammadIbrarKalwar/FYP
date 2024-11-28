package com.example.fyp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class HomeActivity extends AppCompatActivity {

    // CardView declarations for all buttons
    private CardView cardProfile, cardRecommendation, cardCareerPath, cardCommunityForum, cardResume, cardJobInsights, cardProgressTracking, cardSkillAssessment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen); // Set the updated XML layout

        // Initialize CardViews with their IDs
        cardProfile = findViewById(R.id.buttonProfile);
        cardRecommendation = findViewById(R.id.buttonRecommendation);
        cardCareerPath = findViewById(R.id.buttonCareerPath);
        cardCommunityForum = findViewById(R.id.buttonCommunityForum);
        cardResume = findViewById(R.id.buttonResume);
        cardJobInsights = findViewById(R.id.buttonJobInsights);
        cardProgressTracking = findViewById(R.id.buttonProgressTracking);
        cardSkillAssessment = findViewById(R.id.buttonSkillAssessment);

        // Set onClickListeners with try-catch blocks for each CardView
        cardProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToScreen(ProfileActivity.class, "Profile");
            }
        });

        cardRecommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToScreen(RecommendationsActivity.class, "Recommendation");
            }
        });

        cardCareerPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToScreen(CareerPathVisualizationActivity.class, "Career Path");
            }
        });

        cardCommunityForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToScreen(CommunityForumActivity.class, "Community Forum");
            }
        });

        cardResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToScreen(ResumeActivity.class, "Resume");
            }
        });

        cardJobInsights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToScreen(JobInsightsActivity.class, "Job Insights");
            }
        });

        cardProgressTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToScreen(ProgressTrackingActivity.class, "Progress Tracking");
            }
        });

        cardSkillAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToScreen(SkillAssessmentActivity.class, "Skill Assessment");
            }
        });
    }

    // Reusable method to handle navigation with error handling
    private void navigateToScreen(Class<?> targetActivity, String screenName) {
        try {
            Intent intent = new Intent(HomeActivity.this, targetActivity);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(HomeActivity.this, "Error opening " + screenName + " screen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
