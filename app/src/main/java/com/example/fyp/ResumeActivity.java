package com.example.fyp;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class ResumeActivity extends AppCompatActivity {

    private WebView resumeWebView;
    private Spinner templateSpinner;
    private EditText nameInput, emailInput, phoneInput, skillsInput, experienceInput,summaryInput, educationInput, projectsInput;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resume);

        resumeWebView = findViewById(R.id.resumeWebView);
        resumeWebView.getSettings().setJavaScriptEnabled(true);
        resumeWebView.setWebViewClient(new WebViewClient());

        templateSpinner = findViewById(R.id.templateSpinner);
        nameInput = findViewById(R.id.inputName);
        emailInput = findViewById(R.id.inputEmail);
        phoneInput = findViewById(R.id.inputPhone);
        skillsInput = findViewById(R.id.inputSkills);
        experienceInput = findViewById(R.id.inputExperience);
        Button generateButton = findViewById(R.id.generateBtn);
        summaryInput = findViewById(R.id.inputSummary);
        educationInput = findViewById(R.id.inputEducation);
        projectsInput = findViewById(R.id.inputProjects);


        // Example: load 8 templates named resume1.html to resume8.html
        String[] templateLabels = {"Modern", "Classic", "Elegant", "Creative",
                "minimal","professional","bold", "simple"};
        String[] templateFiles = {"resume_modern.html", "resume_classic.html", "resume_elegant.html", "resume_creative.html",
                "resume_minimal.html","professional.html","resume_bold.html", "resume_simple.html"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, templateLabels);
        templateSpinner.setAdapter(adapter);

        generateButton.setOnClickListener(v -> {
            int selectedIndex = templateSpinner.getSelectedItemPosition();
            String selectedTemplate = templateFiles[selectedIndex];
            generateResume(selectedTemplate);
        });

    }

    private void generateResume(String selectedTemplate) {
        try {

            AssetManager assetManager = getAssets();
            InputStream input = assetManager.open("resumes/" + selectedTemplate);
            String htmlTemplate = new Scanner(input).useDelimiter("\\A").next();
            input.close();

            // Replace placeholders with user input
            htmlTemplate = htmlTemplate.replace("{{NAME}}", nameInput.getText().toString());
            htmlTemplate = htmlTemplate.replace("{{EMAIL}}", emailInput.getText().toString());
            htmlTemplate = htmlTemplate.replace("{{PHONE}}", phoneInput.getText().toString());
            htmlTemplate = htmlTemplate.replace("{{SKILLS}}", skillsInput.getText().toString());
            htmlTemplate = htmlTemplate.replace("{{EXPERIENCE}}", experienceInput.getText().toString());
            htmlTemplate = htmlTemplate.replace("{{SUMMARY}}", summaryInput.getText().toString());
            htmlTemplate = htmlTemplate.replace("{{EDUCATION}}", educationInput.getText().toString());
            htmlTemplate = htmlTemplate.replace("{{PROJECTS}}", projectsInput.getText().toString());

            resumeWebView.loadDataWithBaseURL(null, htmlTemplate, "text/html", "UTF-8", null);

            // Save the resume as an HTML file
            saveHtmlToFile("resume_" + System.currentTimeMillis() + ".html", htmlTemplate);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveHtmlToFile(String fileName, String htmlContent) {
        File path = getFilesDir(); // Internal storage
        File file = new File(path, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(htmlContent.getBytes());
            Toast.makeText(this, "Resume saved: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}