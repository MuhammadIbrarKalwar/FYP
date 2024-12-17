package com.example.fyp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SkillAssessmentActivity extends AppCompatActivity {

    private TextView questionText, resultSummary;
    private RadioGroup answerOptions;
    private RadioButton option1, option2, option3, option4;
    private Button buttonStartAssessment, buttonNext, buttonBack;
    private ProgressBar progressBar;

    private final String[] questions = {
            "What does CPU stand for?",
            "Which language is primarily used for web development?",
            "What is the shortcut for copy in Windows?",
            "Which of the following is an operating system?"
    };

    private final String[][] options = {
            {"Central Processing Unit", "Central Program Unit", "Computer Personal Unit", "Control Panel Unit"},
            {"Python", "HTML", "C++", "Java"},
            {"Ctrl + X", "Ctrl + C", "Ctrl + V", "Ctrl + Z"},
            {"Windows", "Intel", "BIOS", "NVIDIA"}
    };

    private final int[] correctAnswers = {0, 1, 1, 0}; // Correct option indices
    private int currentQuestionIndex = 0;

    // Array to track user's selected answers (-1 means no answer selected)
    private final int[] userAnswers = new int[questions.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skill_assessment);

        // Initialize views
        questionText = findViewById(R.id.questionText);
        answerOptions = findViewById(R.id.answerOptions);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        buttonStartAssessment = findViewById(R.id.buttonStartAssessment);
        buttonNext = findViewById(R.id.buttonNext);
        buttonBack = findViewById(R.id.buttonBack);
        progressBar = findViewById(R.id.progressBar);
        resultSummary = findViewById(R.id.resultSummary);

        // Initialize user answers array with -1
        for (int i = 0; i < userAnswers.length; i++) {
            userAnswers[i] = -1;
        }

        // Start Assessment Button
        buttonStartAssessment.setOnClickListener(view -> startAssessment());

        // Next Button
        buttonNext.setOnClickListener(view -> loadNextQuestion());

        // Back Button
        buttonBack.setOnClickListener(view -> goToPreviousQuestion());
    }

    private void startAssessment() {
        buttonStartAssessment.setVisibility(View.GONE);
        questionText.setVisibility(View.VISIBLE);
        answerOptions.setVisibility(View.VISIBLE);
        buttonNext.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        buttonBack.setVisibility(View.VISIBLE);

        loadQuestion();
    }

    private void loadQuestion() {
        if (currentQuestionIndex < questions.length) {
            questionText.setText(questions[currentQuestionIndex]);
            option1.setText(options[currentQuestionIndex][0]);
            option2.setText(options[currentQuestionIndex][1]);
            option3.setText(options[currentQuestionIndex][2]);
            option4.setText(options[currentQuestionIndex][3]);

            // Clear any previous selection
            answerOptions.clearCheck();

            // Restore the user's previous selection, if any
            if (userAnswers[currentQuestionIndex] != -1) {
                ((RadioButton) answerOptions.getChildAt(userAnswers[currentQuestionIndex])).setChecked(true);
            }

            updateProgressBar();
        } else {
            calculateScoreAndShowResults();
        }
    }

    private void loadNextQuestion() {
        int selectedOptionIndex = answerOptions.indexOfChild(findViewById(answerOptions.getCheckedRadioButtonId()));

        if (selectedOptionIndex == -1) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
        } else {
            // Save the user's answer
            userAnswers[currentQuestionIndex] = selectedOptionIndex;

            // Move to the next question
            currentQuestionIndex++;
            loadQuestion();
        }
    }

    private void goToPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            // Move to the previous question
            currentQuestionIndex--;

            loadQuestion();
        }
    }

    private void updateProgressBar() {
        int progress = (currentQuestionIndex * 100) / questions.length;
        progressBar.setProgress(progress);
    }

    @SuppressLint("SetTextI18n")
    private void calculateScoreAndShowResults() {
        int score = 0;

        // Calculate score based on userAnswers
        for (int i = 0; i < userAnswers.length; i++) {
            if (userAnswers[i] != -1 && userAnswers[i] == correctAnswers[i]) {
                score++;
            }
        }

        // Display results
        questionText.setVisibility(View.GONE);
        answerOptions.setVisibility(View.GONE);
        buttonNext.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        buttonBack.setVisibility(View.GONE);
        resultSummary.setVisibility(View.VISIBLE);
        resultSummary.setText("Your Score: " + score + "/" + questions.length);
    }
}
