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
            "1. Which HTML element is used to define important text?",
            "2. What does the placeholder attribute in an HTML input element do?",
            "3. Which CSS property is used to control the spacing between lines of text?",
            "4. What is the correct syntax for applying a CSS class to an HTML element?",
            "5. Which of the following media query will apply styles for devices with a max-width of 768px?",
            "6. How do you make a grid item span 2 rows in CSS Grid?",
            "7. Which CSS property is used to make an element stick to the top of the viewport while scrolling?",
            "8. What is the default value of the z-index property?",
            "9. In Flexbox, which property is used to change the direction of the main axis?",
            "10. Which of the following will create a 2-column layout with equal widths in CSS Grid?",
            "11. How can you apply a transition effect to all properties of an element in CSS?",
            "12. Which pseudo-class is used to style the first <p> element inside a <div>?",
            "13. How can you ensure that an element will maintain its aspect ratio when resized?",
            "14. Which property controls whether an element can be resized by the user?",
            "15. What is the difference between relative and absolute positioning?",
            "16. Which CSS function can be used to apply a mask to an image?",
            "17. How can you apply a gradient border to an element in CSS?",
            "18. What does the contain property do in CSS?",
            "19. How can you style scrollbars in CSS?",
            "20. What is the purpose of will-change in CSS?",
            "21. Which tool is used to monitor Android app performance?",
            "22. What does MVC stand for in software design?",
            "23. What is the primary purpose of a cache in computing?",
            "24. What does UI stand for?",
            "25. What is the main purpose of responsive web design?"
    };

    private final String[][] options = {
            {"<important>", "<strong>", "<em>", "<highlight>"},
            {"Sets a default value for the field.", "Provides a hint or sample text in the input field.", "Validates the input field.", "Changes the field to required."},
            {"letter-spacing", "line-height", "word-spacing", "text-spacing"},
            {"<div class=\"classname\">", "<div id=\"classname\">", "<div css=\"classname\">", "<div style=\"classname\">"},
            {"@media screen and (width <= 768px)", "@media only screen and (max-width: 768px)", "@media (screen-width: 768px)", "@media only screen (max-width <= 768px)"},
            {"grid-column: 2 / span 2;", "grid-row: span 2;", "grid-template: row-span 2;", "grid-item: row-span 2;"},
            {"position: fixed;", "position: absolute;", "position: sticky;", "position: relative;"},
            {"1", "0", "auto", "inherit"},
            {"align-content", "justify-content", "flex-wrap", "flex-direction"},
            {"grid-template-columns: auto auto;", "grid-template-columns: 1fr 1fr;", "grid-template-columns: repeat(2, 1fr);", "Both b) and c)"},
            {"transition: all 0.3s ease;", "transition-property: all;", "animation: all ease 0.3s;", "transition-effect: 0.3s ease;"},
            {"div:first-p", "div>p:first-child", "div p:first-of-type", "div:first-child"},
            {"Set height and width in percentages.", "Use aspect-ratio property.", "Use min-width and min-height.", "Use calc() to set width based on height."},
            {"resize", "overflow", "clip-path", "size-control"},
            {"relative positions an element in relation to its nearest ancestor.", "absolute positions an element in relation to the nearest positioned ancestor.", "absolute positions an element relative to the document root.", "Both b) and c) are correct."},
            {"clip-path()", "mask()", "filter()", "blend-mode()"},
            {"Use border-image property with a gradient.", "Use background-clip for the border.", "Use gradient-border shorthand property.", "None of the above."},
            {"Controls how elements are wrapped inside a parent.", "Improves performance by limiting a container's layout and paint scope.", "Sets an element’s overflow to hidden.", "Ensures all children of an element are styled the same way."},
            {"scrollbar-width and scrollbar-color", "::scrollbar pseudo-element", "::-webkit-scrollbar pseudo-element", "All of the above"},
            {"Indicates which properties are likely to change.", "Locks an element's properties during transitions.", "Boosts rendering speed of unchanged properties.", "Prevents user interaction on the element."},
            {"Android Profiler", "Android Tracker", "App Monitor", "Performance Tester"},
            {"Model-View-Controller", "Monitor-Version-Control", "Multiple-View-Control", "Model-Version-Compile"},
            {"Improve processing speed", "Store frequently accessed data", "Backup system data", "Both A and B"},
            {"User Interface", "Universal Input", "Unified Interaction", "User Interaction"},
            {"Optimize for high performance", "Adapt to different screen sizes", "Improve security", "Reduce development time"}
    };

    private final int[] correctAnswers = {
            1, 1, 1, 0, 1,
            1, 2, 2, 3, 3,
            0, 1, 1, 0, 1,
            1, 0, 1, 3, 0,
            0, 0, 1, 0, 1
    };
    // Correct option indices
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
