package com.example.fyp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CareerPathVisualizationActivity extends AppCompatActivity {

    private LinearLayout pathContainer;
    private Button btnFullStack, btnFrontEnd, btnBackEnd, btnDataScience, btnDevOps, btnMobile;
    private TextView textSelectedPath, textPathDescription;
    private ScrollView scrollViewPaths;

    // Career path data
    private Map<String, CareerPath> careerPaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.career_path_visualization);

        initializeViews();
        initializeCareerPaths();
        setupListeners();

        // Show default path
        showCareerPath("Full Stack Developer");
    }

    private void initializeViews() {
        pathContainer = findViewById(R.id.pathContainer);
        scrollViewPaths = findViewById(R.id.scrollViewPaths);

        // Career buttons
        btnFullStack = findViewById(R.id.btnFullStack);
        btnFrontEnd = findViewById(R.id.btnFrontEnd);
        btnBackEnd = findViewById(R.id.btnBackEnd);
        btnDataScience = findViewById(R.id.btnDataScience);
        btnDevOps = findViewById(R.id.btnDevOps);
        btnMobile = findViewById(R.id.btnMobile);

        // Info text views
        textSelectedPath = findViewById(R.id.textSelectedPath);
        textPathDescription = findViewById(R.id.textPathDescription);
    }

    private void initializeCareerPaths() {
        careerPaths = new HashMap<>();

        // Full Stack Developer Path
        CareerPath fullStackPath = new CareerPath(
                "Full Stack Developer",
                "Master both frontend and backend technologies to build complete web applications",
                Arrays.asList(
                        new CareerLevel("Junior Full Stack Developer", "0-2 years",
                                Arrays.asList("HTML/CSS", "JavaScript", "Basic React", "Node.js", "Git"),
                                "Learn fundamentals of web development, understand both client and server side"),
                        new CareerLevel("Full Stack Developer", "2-4 years",
                                Arrays.asList("Advanced React/Vue", "Express/Django", "Database Design", "API Development", "Cloud Basics"),
                                "Build complete applications, work with databases, deploy to cloud"),
                        new CareerLevel("Senior Full Stack Developer", "4-7 years",
                                Arrays.asList("System Architecture", "Microservices", "DevOps", "Team Leadership", "Performance Optimization"),
                                "Design system architecture, mentor junior developers, optimize performance"),
                        new CareerLevel("Full Stack Architect", "7+ years",
                                Arrays.asList("Enterprise Architecture", "Technology Strategy", "Team Management", "Business Analysis"),
                                "Make high-level technical decisions, lead development teams, align tech with business goals")
                )
        );

        // Frontend Developer Path
        CareerPath frontEndPath = new CareerPath(
                "Frontend Developer",
                "Specialize in user interfaces and user experience development",
                Arrays.asList(
                        new CareerLevel("Junior Frontend Developer", "0-2 years",
                                Arrays.asList("HTML/CSS", "JavaScript", "Responsive Design", "Basic React", "Git"),
                                "Create beautiful user interfaces, ensure responsive design across devices"),
                        new CareerLevel("Frontend Developer", "2-4 years",
                                Arrays.asList("Advanced React/Vue/Angular", "State Management", "Testing", "Build Tools", "API Integration"),
                                "Build complex SPAs, manage application state, implement testing strategies"),
                        new CareerLevel("Senior Frontend Developer", "4-7 years",
                                Arrays.asList("Performance Optimization", "Accessibility", "Mentoring", "Architecture Design", "UX/UI Collaboration"),
                                "Optimize app performance, ensure accessibility, guide technical decisions"),
                        new CareerLevel("Frontend Architect", "7+ years",
                                Arrays.asList("Micro-frontends", "Design Systems", "Technology Strategy", "Team Leadership"),
                                "Design scalable frontend architecture, establish development standards")
                )
        );

        // Backend Developer Path
        CareerPath backEndPath = new CareerPath(
                "Backend Developer",
                "Focus on server-side logic, databases, and system architecture",
                Arrays.asList(
                        new CareerLevel("Junior Backend Developer", "0-2 years",
                                Arrays.asList("Python/Java/Node.js", "SQL", "REST APIs", "Git", "Basic Cloud"),
                                "Build REST APIs, work with databases, understand server fundamentals"),
                        new CareerLevel("Backend Developer", "2-4 years",
                                Arrays.asList("Advanced Frameworks", "Database Optimization", "Caching", "Security", "Microservices"),
                                "Design scalable APIs, optimize database queries, implement security measures"),
                        new CareerLevel("Senior Backend Developer", "4-7 years",
                                Arrays.asList("System Design", "Distributed Systems", "Performance Tuning", "Mentoring", "DevOps"),
                                "Architect backend systems, handle high-scale challenges, mentor team members"),
                        new CareerLevel("Backend Architect", "7+ years",
                                Arrays.asList("Enterprise Architecture", "Technology Strategy", "Scalability Planning", "Team Leadership"),
                                "Design enterprise-level systems, make strategic technology decisions")
                )
        );

        // Data Science Path
        CareerPath dataSciencePath = new CareerPath(
                "Data Scientist",
                "Extract insights from data using statistical analysis and machine learning",
                Arrays.asList(
                        new CareerLevel("Junior Data Analyst", "0-2 years",
                                Arrays.asList("Python/R", "SQL", "Statistics", "Data Visualization", "Excel"),
                                "Clean and analyze data, create basic visualizations, generate reports"),
                        new CareerLevel("Data Scientist", "2-4 years",
                                Arrays.asList("Machine Learning", "Deep Learning", "Statistical Modeling", "Big Data Tools", "Business Intelligence"),
                                "Build predictive models, implement ML algorithms, derive business insights"),
                        new CareerLevel("Senior Data Scientist", "4-7 years",
                                Arrays.asList("Advanced ML", "Model Deployment", "Team Leadership", "Business Strategy", "MLOps"),
                                "Lead data science projects, deploy models to production, drive business decisions"),
                        new CareerLevel("Principal Data Scientist", "7+ years",
                                Arrays.asList("Research Leadership", "Strategy Development", "Cross-functional Collaboration", "Innovation"),
                                "Lead research initiatives, develop data strategy, influence product direction")
                )
        );

        // DevOps Engineer Path
        CareerPath devOpsPath = new CareerPath(
                "DevOps Engineer",
                "Bridge development and operations, focusing on automation and deployment",
                Arrays.asList(
                        new CareerLevel("Junior DevOps Engineer", "0-2 years",
                                Arrays.asList("Linux", "Git", "Docker", "Basic Cloud", "Scripting"),
                                "Automate basic tasks, understand CI/CD concepts, work with containers"),
                        new CareerLevel("DevOps Engineer", "2-4 years",
                                Arrays.asList("Kubernetes", "Infrastructure as Code", "Monitoring", "Security", "Advanced Cloud"),
                                "Manage container orchestration, implement IaC, set up monitoring systems"),
                        new CareerLevel("Senior DevOps Engineer", "4-7 years",
                                Arrays.asList("System Architecture", "Security Engineering", "Performance Optimization", "Team Leadership"),
                                "Design infrastructure architecture, implement security best practices"),
                        new CareerLevel("DevOps Architect", "7+ years",
                                Arrays.asList("Enterprise DevOps Strategy", "Multi-cloud Architecture", "Organizational Transformation"),
                                "Define DevOps strategy, lead organizational transformation, architect enterprise solutions")
                )
        );

        // Mobile Developer Path
        CareerPath mobilePath = new CareerPath(
                "Mobile Developer",
                "Create mobile applications for iOS and Android platforms",
                Arrays.asList(
                        new CareerLevel("Junior Mobile Developer", "0-2 years",
                                Arrays.asList("Swift/Kotlin", "Mobile UI", "Basic APIs", "App Store Deployment", "Git"),
                                "Build basic mobile apps, understand mobile UI patterns, integrate with APIs"),
                        new CareerLevel("Mobile Developer", "2-4 years",
                                Arrays.asList("Advanced Frameworks", "Cross-platform", "Performance Optimization", "Testing", "Push Notifications"),
                                "Develop complex mobile apps, optimize performance, implement advanced features"),
                        new CareerLevel("Senior Mobile Developer", "4-7 years",
                                Arrays.asList("Architecture Design", "Team Leadership", "Code Review", "Mentoring", "Technical Strategy"),
                                "Design mobile app architecture, lead development teams, establish best practices"),
                        new CareerLevel("Mobile Architect", "7+ years",
                                Arrays.asList("Mobile Strategy", "Platform Architecture", "Technology Innovation", "Cross-platform Leadership"),
                                "Define mobile technology strategy, architect scalable mobile solutions")
                )
        );

        careerPaths.put("Full Stack Developer", fullStackPath);
        careerPaths.put("Frontend Developer", frontEndPath);
        careerPaths.put("Backend Developer", backEndPath);
        careerPaths.put("Data Scientist", dataSciencePath);
        careerPaths.put("DevOps Engineer", devOpsPath);
        careerPaths.put("Mobile Developer", mobilePath);
    }

    private void setupListeners() {
        btnFullStack.setOnClickListener(v -> {
            showCareerPath("Full Stack Developer");
            highlightSelectedButton(btnFullStack);
        });

        btnFrontEnd.setOnClickListener(v -> {
            showCareerPath("Frontend Developer");
            highlightSelectedButton(btnFrontEnd);
        });

        btnBackEnd.setOnClickListener(v -> {
            showCareerPath("Backend Developer");
            highlightSelectedButton(btnBackEnd);
        });

        btnDataScience.setOnClickListener(v -> {
            showCareerPath("Data Scientist");
            highlightSelectedButton(btnDataScience);
        });

        btnDevOps.setOnClickListener(v -> {
            showCareerPath("DevOps Engineer");
            highlightSelectedButton(btnDevOps);
        });

        btnMobile.setOnClickListener(v -> {
            showCareerPath("Mobile Developer");
            highlightSelectedButton(btnMobile);
        });
    }

    private void showCareerPath(String pathName) {
        CareerPath path = careerPaths.get(pathName);
        if (path == null) return;

        // Update header info
        textSelectedPath.setText(path.title);
        textPathDescription.setText(path.description);

        // Clear existing path
        pathContainer.removeAllViews();

        // Add career levels
        for (int i = 0; i < path.levels.size(); i++) {
            CareerLevel level = path.levels.get(i);

            // Create card for this level
            CardView levelCard = createLevelCard(level, i);
            pathContainer.addView(levelCard);

            // Add connector arrow (except for last item)
            if (i < path.levels.size() - 1) {
                TextView arrow = createArrow();
                pathContainer.addView(arrow);
            }
        }

        Toast.makeText(this, "Showing " + pathName + " career path", Toast.LENGTH_SHORT).show();
    }

    private CardView createLevelCard(CareerLevel level, int levelIndex) {
        CardView card = new CardView(this);

        // Card styling
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(16, 16, 16, 16);
        card.setLayoutParams(cardParams);
        card.setCardElevation(8f);
        card.setRadius(12f);

        // Set card background color based on level
        int[] colors = {
                Color.parseColor("#E3F2FD"), // Light Blue
                Color.parseColor("#F3E5F5"), // Light Purple
                Color.parseColor("#E8F5E8"), // Light Green
                Color.parseColor("#FFF3E0")  // Light Orange
        };
        card.setCardBackgroundColor(colors[levelIndex % colors.length]);

        // Create card content
        LinearLayout cardContent = new LinearLayout(this);
        cardContent.setOrientation(LinearLayout.VERTICAL);
        cardContent.setPadding(24, 24, 24, 24);

        // Level title
        TextView titleText = new TextView(this);
        titleText.setText(level.title);
        titleText.setTextSize(20f);
        titleText.setTextColor(Color.parseColor("#1565C0"));
        titleText.setTypeface(null, android.graphics.Typeface.BOLD);
        cardContent.addView(titleText);

        // Experience
        TextView experienceText = new TextView(this);
        experienceText.setText("Experience: " + level.experience);
        experienceText.setTextSize(14f);
        experienceText.setTextColor(Color.parseColor("#666666"));
        experienceText.setPadding(0, 8, 0, 8);
        cardContent.addView(experienceText);

        // Skills
        TextView skillsLabel = new TextView(this);
        skillsLabel.setText("Key Skills:");
        skillsLabel.setTextSize(16f);
        skillsLabel.setTextColor(Color.parseColor("#333333"));
        skillsLabel.setTypeface(null, android.graphics.Typeface.BOLD);
        skillsLabel.setPadding(0, 12, 0, 8);
        cardContent.addView(skillsLabel);

        for (String skill : level.skills) {
            TextView skillText = new TextView(this);
            skillText.setText("• " + skill);
            skillText.setTextSize(14f);
            skillText.setTextColor(Color.parseColor("#444444"));
            skillText.setPadding(16, 4, 0, 4);
            cardContent.addView(skillText);
        }

        // Description
        TextView descriptionText = new TextView(this);
        descriptionText.setText(level.description);
        descriptionText.setTextSize(14f);
        descriptionText.setTextColor(Color.parseColor("#555555"));
        descriptionText.setPadding(0, 12, 0, 0);
        descriptionText.setLineSpacing(4f, 1f);
        cardContent.addView(descriptionText);

        card.addView(cardContent);
        return card;
    }

    private TextView createArrow() {
        TextView arrow = new TextView(this);
        arrow.setText("⬇");
        arrow.setTextSize(32f);
        arrow.setTextColor(Color.parseColor("#1565C0"));
        arrow.setGravity(android.view.Gravity.CENTER);

        LinearLayout.LayoutParams arrowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        arrowParams.setMargins(0, 8, 0, 8);
        arrow.setLayoutParams(arrowParams);

        return arrow;
    }

    private void highlightSelectedButton(Button selectedButton) {
        // Reset all buttons
        Button[] buttons = {btnFullStack, btnFrontEnd, btnBackEnd, btnDataScience, btnDevOps, btnMobile};
        for (Button button : buttons) {
            button.setBackgroundColor(Color.parseColor("#1565C0"));
            button.setTextColor(Color.WHITE);
        }

        // Highlight selected button
        selectedButton.setBackgroundColor(Color.parseColor("#0D47A1"));
        selectedButton.setTextColor(Color.WHITE);
    }

    // Data classes
    private static class CareerPath {
        String title;
        String description;
        List<CareerLevel> levels;

        CareerPath(String title, String description, List<CareerLevel> levels) {
            this.title = title;
            this.description = description;
            this.levels = levels;
        }
    }

    private static class CareerLevel {
        String title;
        String experience;
        List<String> skills;
        String description;

        CareerLevel(String title, String experience, List<String> skills, String description) {
            this.title = title;
            this.experience = experience;
            this.skills = skills;
            this.description = description;
        }
    }
}