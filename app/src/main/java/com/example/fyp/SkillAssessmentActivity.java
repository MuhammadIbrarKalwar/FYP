package com.example.fyp;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle; import android.view.View; import android.widget.*; import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

public class SkillAssessmentActivity extends AppCompatActivity {

    private Spinner roleSpinner, skillSpinner;
    private TextView questionText, resultSummary, textJobTitleJob, textAssessmentType, instructions;
    private RadioGroup answerOptions;
    private RadioButton option1, option2, option3, option4;
    private Button buttonStartAssessment, buttonNext, buttonBack, buttonRestart, buttonBackToRole;
    private ProgressBar progressBar;

    private List<MCQ> currentQuestions = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int[] userAnswers;

    private final Map<String, List<MCQ>> assessmentBank = new HashMap<>();
    private final List<SkillAssessment> assessments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skill_assessment);

        initializeViews();
        loadAssessments();
        setupSpinners();
        setupListeners();
    }

    private void initializeViews() {
        instructions = findViewById(R.id.instructions);
        textJobTitleJob = findViewById(R.id.textJobTitle);
        textAssessmentType = findViewById(R.id.textAssessmentType);
        roleSpinner = findViewById(R.id.roleSpinner);
        skillSpinner = findViewById(R.id.skillSpinner);
        questionText = findViewById(R.id.questionText);
        answerOptions = findViewById(R.id.answerOptions);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        buttonStartAssessment = findViewById(R.id.buttonStartAssessment);
        buttonNext = findViewById(R.id.buttonNext);
        buttonBack = findViewById(R.id.buttonBack);
        buttonRestart = findViewById(R.id.buttonRestart);
        buttonBackToRole = findViewById(R.id.buttonBackToRole);
        progressBar = findViewById(R.id.progressBar);
        resultSummary = findViewById(R.id.resultSummary);
    }

    private void setupListeners() {
        buttonStartAssessment.setOnClickListener(v -> startAssessment());
        buttonNext.setOnClickListener(v -> loadNextQuestion());
        buttonBack.setOnClickListener(v -> goToPreviousQuestion());
        buttonRestart.setOnClickListener(v -> restartAssessment());
        buttonBackToRole.setOnClickListener(v -> returnToRoleSelection());
    }

    private void setupSpinners() {
        Set<String> roles = new HashSet<>();
        Map<String, List<String>> roleToSkills = new HashMap<>();

        for (SkillAssessment assessment : assessments) {
            roles.add(assessment.role);
            if (!roleToSkills.containsKey(assessment.role)) {
                roleToSkills.put(assessment.role, new ArrayList<>());
            }
            roleToSkills.get(assessment.role).add(assessment.skill);
            assessmentBank.put(assessment.role + "-" + assessment.skill, assessment.questions);
        }

        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(roles));
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(roleAdapter);

        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRole = roleSpinner.getSelectedItem().toString();
                List<String> skills = roleToSkills.get(selectedRole);
                if (skills != null) {
                    ArrayAdapter<String> skillAdapter = new ArrayAdapter<>(SkillAssessmentActivity.this, android.R.layout.simple_spinner_item, skills);
                    skillAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    skillSpinner.setAdapter(skillAdapter);
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void startAssessment() {
        String role = roleSpinner.getSelectedItem().toString();
        String skill = skillSpinner.getSelectedItem().toString();
        String key = role + "-" + skill;

        if (!assessmentBank.containsKey(key)) {
            Toast.makeText(this, "No assessment found for selected role and skill", Toast.LENGTH_SHORT).show();
            return;
        }

        currentQuestions = assessmentBank.get(key);
        userAnswers = new int[currentQuestions.size()];
        Arrays.fill(userAnswers, -1);
        currentQuestionIndex = 0;

        updateUIForAssessment(true);
        loadQuestion();
    }

    private void updateUIForAssessment(boolean start) {
        roleSpinner.setVisibility(start ? View.GONE : View.VISIBLE);
        skillSpinner.setVisibility(start ? View.GONE : View.VISIBLE);
        buttonStartAssessment.setVisibility(start ? View.GONE : View.VISIBLE);

        questionText.setVisibility(start ? View.VISIBLE : View.GONE);
        textJobTitleJob.setVisibility(View.GONE);
        textAssessmentType.setVisibility(View.GONE);
        answerOptions.setVisibility(start ? View.VISIBLE : View.GONE);
        buttonNext.setVisibility(start ? View.VISIBLE : View.GONE);
        buttonBack.setVisibility(start ? View.VISIBLE : View.GONE);
        progressBar.setVisibility(start ? View.VISIBLE : View.GONE);
        resultSummary.setVisibility(View.GONE);

        buttonRestart.setVisibility(!start ? View.VISIBLE : View.GONE);
        buttonBackToRole.setVisibility(!start ? View.VISIBLE : View.GONE);
    }

    private void loadQuestion() {
        if (currentQuestionIndex < currentQuestions.size()) {
            MCQ q = currentQuestions.get(currentQuestionIndex);
            questionText.setText(q.question);
            option1.setText(q.options.get(0));
            option2.setText(q.options.get(1));
            option3.setText(q.options.get(2));
            option4.setText(q.options.get(3));
            answerOptions.clearCheck();

            if (userAnswers[currentQuestionIndex] != -1) {
                ((RadioButton) answerOptions.getChildAt(userAnswers[currentQuestionIndex])).setChecked(true);
            }

            updateProgressBar();
        } else {
            calculateScoreAndShowResults();
        }
    }

    private void loadNextQuestion() {
        int selectedId = answerOptions.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
        } else {
            View selected = findViewById(selectedId);
            int selectedOptionIndex = answerOptions.indexOfChild(selected);
            userAnswers[currentQuestionIndex] = selectedOptionIndex;
            currentQuestionIndex++;
            loadQuestion();
        }
    }

    private void goToPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            loadQuestion();
        }
    }

    private void updateProgressBar() {
        int progress = (currentQuestionIndex * 100) / currentQuestions.size();
        progressBar.setProgress(progress);
    }

    @SuppressLint("SetTextI18n")
    private void calculateScoreAndShowResults() {
        int score = 0;
        for (int i = 0; i < userAnswers.length; i++) {
            if (userAnswers[i] != -1 && userAnswers[i] == currentQuestions.get(i).correctOption) {
                score++;
            }
        }

        updateUIForAssessment(false);

        resultSummary.setVisibility(View.VISIBLE);
        roleSpinner.setVisibility(View.GONE);
        skillSpinner.setVisibility(View.GONE);
        buttonStartAssessment.setVisibility(View.GONE);

        resultSummary.setText("Your Score: " + score + "/" + currentQuestions.size());

        // Save completed skill to SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserProfile", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

// Get current skill from spinner
        String completedSkill = skillSpinner.getSelectedItem().toString();

// Save assessment result
        Set<String> completedSkills = prefs.getStringSet("completed_skills", new HashSet<>());
        completedSkills = new HashSet<>(completedSkills); // clone to avoid immutable issues
        completedSkills.add(completedSkill);

        editor.putStringSet("completed_skills", completedSkills);
        editor.putInt("last_assessment_score", score);
        editor.putString("last_assessment_date", getCurrentDate());
        editor.apply();
    }

    private String getCurrentDate() {
        return "getCurrentDate()";
    }

    private void restartAssessment() {
        startAssessment();
    }

    private void returnToRoleSelection() {
        updateUIForAssessment(false);
    }



    private void loadAssessments() {

        // === FULL-STACK DEVELOPER ===

        assessments.add(new SkillAssessment("JavaScript & DOM Basics", "Full-Stack Developer", Arrays.asList( new MCQ("What does document.getElementById(demo) return?", Arrays.asList("The first element with tag ", "An array of all demo elements", "The element with the id demo", "Null always"), 2), new MCQ("Which of the following is NOT a JavaScript data type?", Arrays.asList("Undefined", "Float", "Boolean", "Object"), 1), new MCQ("What is the purpose of use strict in JavaScript?", Arrays.asList("It enables legacy code compatibility", "It disables ES6 features", "It catches common coding mistakes", "It disables JavaScript"), 2), new MCQ("What will typeof null return?", Arrays.asList("null", "undefined", "object", "boolean"), 2), new MCQ("Which event occurs when a user clicks on an HTML element?", Arrays.asList("onmouseover", "onclick", "onchange", "onhover"), 1) )));

        assessments.add(new SkillAssessment("Backend Fundamentals (Node.js & APIs)", "Full-Stack Developer", Arrays.asList(
                new MCQ("Which module is used to create a server in Node.js?", Arrays.asList("HTTP", "EXPRESS", "URL", "FILE"), 0),
                new MCQ("What status code means “Created” in RESTful APIs?", Arrays.asList("200", "201", "204", "400"), 1),
                new MCQ("What does req.params contain in Express?", Arrays.asList("Query parameters", "Route parameters", "Cookies", "Headers"), 1),
                new MCQ("Which command initializes a Node.js project?", Arrays.asList("node init", "npm init", "yarn start", "node start"), 1),
                new MCQ("Which HTTP method is used to update a resource completely?", Arrays.asList("GET", "POST", "PATCH", "PUT"), 3)
        )));
        assessments.add(new SkillAssessment("Database Knowledge (SQL/MongoDB)", "Full-Stack Developer", Arrays.asList(
                new MCQ("What is a primary key?", Arrays.asList("A key that can have NULL values", "A key to store foreign tables", "A unique identifier for a record", "A random column"), 2),
                new MCQ("In MongoDB, which function is used to fetch all documents from a collection?", Arrays.asList("findOne()", "get()", "find()", "fetch()"), 2),
                new MCQ("Which SQL command is used to remove records?", Arrays.asList("DROP", "REMOVE", "DELETE", "CUT"), 2),
                new MCQ("What does LIMIT 5 OFFSET 2 mean?", Arrays.asList("Fetch 5 rows after skipping 2", "Fetch 2 rows from 5", "Skip first 5 rows", "Nothing"), 0),
                new MCQ("MongoDB is a:", Arrays.asList("Relational Database", "Object-Oriented Database", "NoSQL Database", "Graph Database"), 2)
        )));

        assessments.add(new SkillAssessment("Full-Stack Integration & Architecture", "Full-Stack Developer", Arrays.asList(
                new MCQ("What does MVC stand for?", Arrays.asList("Model, View, Controller", "Main, View, Component", "Module, Visual, Call", "Model, Visual, Code"), 0),
                new MCQ("What tool is commonly used to test REST APIs?", Arrays.asList("Visual Studio", "Postman", "Excel", "GitHub"), 1),
                new MCQ("Which architecture is best for microservices?", Arrays.asList("Monolithic", "Client-Server", "Layered", "Distributed"), 3),
                new MCQ("Which protocol does HTTPS use under the hood?", Arrays.asList("SSH", "FTP", "SSL/TLS", "DNS"), 2),
                new MCQ("What is the purpose of JWT in full-stack apps?", Arrays.asList("Logging", "Error tracking", "Authentication", "Deployment"), 2)
        )));

        assessments.add(new SkillAssessment("Git, CI/CD & Deployment", "Full-Stack Developer", Arrays.asList(
                new MCQ("What is the purpose of git clone?", Arrays.asList("Copy a local branch", "Clone a remote repository", "Commit changes", "Delete a repo"), 1),
                new MCQ("Which of the following is a CI/CD tool?", Arrays.asList("Docker", "Jenkins", "Git", "Yarn"), 1),
                new MCQ("What is a Docker container?", Arrays.asList("Virtual machine", "Cloud instance", "Lightweight isolated environment", "Database"), 2),
                new MCQ("What does .gitignore do?", Arrays.asList("Adds files to Git", "Ignores files during pull", "Prevents tracking of specific files", "Tracks file history"), 2),
                new MCQ("What does npm run build generally do?", Arrays.asList("Compiles code for production", "Starts local server", "Deletes code", "Downloads dependencies"), 0)
        )));

        // === BACK-END DEVELOPER ===
        assessments.add(new SkillAssessment("Server-Side Programming (Java/Python/Node.js)", "Back-End Developer", Arrays.asList(
                new MCQ("Which of the following is used to handle HTTP requests in Node.js?", Arrays.asList("Axios", "Express", "Mongoose", "Sequelize"), 1),
                new MCQ("What does the @RestController annotation do in Spring Boot?", Arrays.asList("Registers a service", "Creates a RESTful controller", "Configures database", "Starts the application"), 1),
                new MCQ("In Python Flask, what does @app.route('/') do?", Arrays.asList("Runs the app", "Defines the base URL endpoint", "Stops the app", "Initializes the database"), 1),
                new MCQ("What is the main responsibility of the back-end in web development?", Arrays.asList("UI design", "Business logic and data handling", "Styling", "SEO optimization"), 1),
                new MCQ("What is a middleware in Express.js?", Arrays.asList("A database connector", "A router", "A function executed before reaching route handler", "A template engine"), 2)
        )));

        assessments.add(new SkillAssessment("Database & Query Optimization", "Back-End Developer", Arrays.asList(
                new MCQ("What does an SQL index improve?", Arrays.asList("Insert speed", "Query performance", "Data visualization", "Code readability"), 1),
                new MCQ("What is normalization in databases?", Arrays.asList("Speeding up queries", "Reducing data redundancy", "Replicating tables", "Formatting data"), 1),
                new MCQ("In SQL, SELECT COUNT(*) FROM users; does what?", Arrays.asList("Adds a user", "Updates records", "Returns total user records", "Deletes users"), 2),
                new MCQ("What is a foreign key?", Arrays.asList("A backup column", "A field that refers to primary key of another table", "A private field", "A key not related to primary"), 1),
                new MCQ("What does ACID stand for in database transactions?", Arrays.asList("Access, Control, Integrity, Dependency", "Atomicity, Consistency, Isolation, Durability", "Abstract, Class, Inheritance, Dependency", "None of the above"), 1)
        )));
        assessments.add(new SkillAssessment("API Design & REST Principles", "Back-End Developer", Arrays.asList(
                new MCQ("What does REST stand for?", Arrays.asList("Reliable Event Sequence Transfer", "Representational State Transfer", "Remote Endpoint Service Tracker", "Real-time Event Streaming"), 1),
                new MCQ("What is the correct HTTP method to update partial data?", Arrays.asList("PUT", "POST", "PATCH", "GET"), 2),
                new MCQ("Which format is most commonly used in REST APIs?", Arrays.asList("CSV", "XML", "YAML", "JSON"), 3),
                new MCQ("What does a 404 status code indicate?", Arrays.asList("Server Error", "Resource Not Found", "Success", "Unauthorized"), 1),
                new MCQ("What is an idempotent HTTP method?", Arrays.asList("One that changes data every time", "One that doesn’t return the same result", "One that has no effect after the first call", "One that returns a cookie"), 2)
        )));

        assessments.add(new SkillAssessment("Authentication & Security", "Back-End Developer", Arrays.asList(
                new MCQ("Which one is used for stateless authentication?", Arrays.asList("Session", "JWT", "Cookies", "Local storage"), 1),
                new MCQ("What is SQL Injection?", Arrays.asList("Database creation", "Authentication method", "A hacking technique to alter SQL queries", "Data backup"), 2),
                new MCQ("What does bcrypt do?", Arrays.asList("Encrypt data", "Hash passwords", "Compress strings", "Compare schemas"), 1),
                new MCQ("In OAuth2, what is a refresh token?", Arrays.asList("A way to change password", "A token to revoke sessions", "A token to get new access token", "A JWT"), 2),
                new MCQ("HTTPS ensures:", Arrays.asList("Faster load speed", "Obfuscation", "Encrypted communication", "Browser caching"), 2)
        )));

        assessments.add(new SkillAssessment("Performance & Scalability", "Back-End Developer", Arrays.asList(
                new MCQ("What is caching used for?", Arrays.asList("Logging data", "Secure login", "Reduce database load", "Improve SEO"), 2),
                new MCQ("What does load balancing do?", Arrays.asList("Balance data entries", "Equally distribute traffic", "Secure the back-end", "Create backups"), 1),
                new MCQ("Redis is used as:", Arrays.asList("Web framework", "NoSQL document DB", "Key-value store/cache", "Mail server"), 2),
                new MCQ("Which algorithm is used for horizontal scaling?", Arrays.asList("Sharding", "Encryption", "Normalization", "Deduplication"), 0),
                new MCQ("What is the bottleneck in monolithic architecture?", Arrays.asList("Single point of failure", "Too many users", "High memory", "Small database"), 0)
        )));

        // === FRONT-END DEVELOPER ===
        assessments.add(new SkillAssessment("HTML & CSS Basics", "Front-End Developer", Arrays.asList(
                new MCQ("What does the <a> tag do in HTML?", Arrays.asList("Inserts an image", "Creates a hyperlink", "Adds a table", "Formats text as bold"), 1),
                new MCQ("What is the purpose of the alt attribute in an <img> tag?", Arrays.asList("Add animation", "Define source", "Provide alternative text", "Resize image"), 2),
                new MCQ("Which property changes the background color of an element in CSS?", Arrays.asList("color", "bgColor", "background-color", "fill"), 2),
                new MCQ("What does position: absolute; mean in CSS?", Arrays.asList("Fixed relative to screen", "Sticks to the top of viewport", "Positioned relative to nearest positioned ancestor", "Randomly placed"), 2),
                new MCQ("What HTML tag is used to define an unordered list?", Arrays.asList("<ol>", "<li>", "<ul>", "<list>"), 2)
        )));

        assessments.add(new SkillAssessment("JavaScript Fundamentals", "Front-End Developer", Arrays.asList(
                new MCQ("What will console.log(typeof []) print?", Arrays.asList("array", "object", "undefined", "list"), 1),
                new MCQ("What is the result of 2 + '2' in JavaScript?", Arrays.asList("4", "22", "NaN", "Error"), 1),
                new MCQ("What is the purpose of addEventListener()?", Arrays.asList("Change styles", "Handle events dynamically", "Load images", "Store data"), 1),
                new MCQ("What does === do in JavaScript?", Arrays.asList("Converts before comparing", "Compares without type checking", "Compares with type checking", "Always returns true"), 2),
                new MCQ("Which one is a falsy value?", Arrays.asList("\"false\"", "true", "1", "0"), 3)
        )));

        assessments.add(new SkillAssessment("React.js Essentials", "Front-End Developer", Arrays.asList(
                new MCQ("What is a React component?", Arrays.asList("HTML file", "Reusable piece of UI", "Style sheet", "Database"), 1),
                new MCQ("What hook is used for managing state?", Arrays.asList("useProps", "useState", "useRef", "useEffect"), 1),
                new MCQ("What is JSX?", Arrays.asList("Java Standard Extension", "JSON Extension", "JavaScript XML", "A data type"), 2),
                new MCQ("What happens when setState is called?", Arrays.asList("UI crashes", "State is ignored", "Component re-renders", "Nothing happens"), 2),
                new MCQ("What is a key in React lists used for?", Arrays.asList("Styling", "Uniquely identifying list items", "Encryption", "HTML parsing"), 1)
        )));

        assessments.add(new SkillAssessment("Responsive Design & Media Queries", "Front-End Developer", Arrays.asList(
                new MCQ("What does a media query do?", Arrays.asList("Fetch data", "Define animation", "Apply styles conditionally based on screen size", "Load media"), 2),
                new MCQ("Which unit is relative to the parent element?", Arrays.asList("px", "em", "vh", "%"), 1),
                new MCQ("What is the mobile-first approach?", Arrays.asList("Designing for desktop first", "Writing media queries for print", "Designing the smallest screen version first", "Adding mobile features last"), 2),
                new MCQ("Which property is used to make a flex container?", Arrays.asList("display: block", "display: grid", "display: flex", "position: flex"), 2),
                new MCQ("What does max-width: 768px target?", Arrays.asList("Screens wider than 768px", "Exactly 768px", "Screens up to 768px", "Screen height"), 2)
        )));

        assessments.add(new SkillAssessment("Browser APIs & Debugging Tools", "Front-End Developer", Arrays.asList(
                new MCQ("What is localStorage used for?", Arrays.asList("Upload files", "Save small data persistently in browser", "Send server requests", "Display toast messages"), 1),
                new MCQ("Which tab in browser dev tools shows JavaScript errors?", Arrays.asList("Elements", "Sources", "Console", "Network"), 2),
                new MCQ("What does navigator.geolocation do?", Arrays.asList("Tracks cookies", "Sends files", "Accesses device location", "Loads images"), 2),
                new MCQ("How to stop JavaScript code execution in DevTools?", Arrays.asList("Reload", "breakpoint", "Pause script", "Refresh memory"), 2),
                new MCQ("What does the Network tab show in browser dev tools?", Arrays.asList("Java code", "Event listeners", "HTTP requests/responses", "Local variables"), 2)
        )));

        // === MOBILE DEVELOPER ===
        assessments.add(new SkillAssessment("Android Basics (Java/Kotlin)", "Mobile Developer", Arrays.asList(
                new MCQ("Which file contains the app's metadata and permissions in Android?", Arrays.asList("strings.xml", "AndroidManifest.xml", "MainActivity.java", "build.gradle"), 1),
                new MCQ("What does onCreate() method do in an Android Activity?", Arrays.asList("Destroys the activity", "Initializes the activity when it starts", "Loads the XML layout file", "Starts a background service"), 1),
                new MCQ("What is the purpose of an Intent in Android?", Arrays.asList("Display notifications", "Launch activities or send data", "Play audio files", "Load images"), 1),
                new MCQ("What layout arranges elements relative to each other?", Arrays.asList("LinearLayout", "RelativeLayout", "ConstraintLayout", "FrameLayout"), 1),
                new MCQ("What is a RecyclerView used for?", Arrays.asList("Sending API requests", "Displaying large, scrollable lists", "Creating forms", "Playing videos"), 1)
        )));

        assessments.add(new SkillAssessment("iOS Basics (Swift/UIKit)", "Mobile Developer", Arrays.asList(
                new MCQ("What is the primary programming language for iOS app development?", Arrays.asList("Kotlin", "Objective-C", "Swift", "Dart"), 2),
                new MCQ("What is @IBOutlet used for in Swift?", Arrays.asList("Storing images", "Connecting UI elements to code", "Declaring a constant", "Hiding UI"), 1),
                new MCQ("What does UIApplicationDelegate handle?", Arrays.asList("Media playback", "Core ML operations", "Application lifecycle events", "Bluetooth control"), 2),
                new MCQ("What is Storyboard in iOS development?", Arrays.asList("A logging file", "An app icon set", "A visual way to design app screens", "Database file"), 2),
                new MCQ("Which method is called when a view appears on screen?", Arrays.asList("viewDidLoad()", "viewWillAppear()", "application(_:didFinishLaunchingWithOptions:)", "loadView()"), 1)
        )));
        assessments.add(new SkillAssessment("Cross-Platform Development (Flutter/React Native)", "Mobile Developer", Arrays.asList(
                new MCQ("What is the programming language used in Flutter?", Arrays.asList("Java", "Dart", "TypeScript", "Kotlin"), 1),
                new MCQ("What command is used to run a Flutter app?", Arrays.asList("flutter start", "flutter init", "flutter run", "dart serve"), 2),
                new MCQ("What is the equivalent of a component in React Native?", Arrays.asList("Module", "Fragment", "Widget", "Layout"), 2),
                new MCQ("What is used for styling in React Native?", Arrays.asList("XML", "CSS", "Inline JavaScript objects", "HTML"), 2),
                new MCQ("Which widget is used for scrollable lists in Flutter?", Arrays.asList("ListTile", "ScrollBar", "ListView", "Column"), 2)
        )));

        assessments.add(new SkillAssessment("Mobile APIs & Sensors", "Mobile Developer", Arrays.asList(
                new MCQ("What does the accelerometer sensor detect?", Arrays.asList("Temperature", "Vibration", "Device orientation and motion", "WiFi signal strength"), 2),
                new MCQ("What API is used to access location in Android?", Arrays.asList("Bluetooth API", "Google Maps SDK", "LocationManager", "Camera API"), 2),
                new MCQ("Which permission is needed for camera access in AndroidManifest.xml?", Arrays.asList("ACCESS_CAMERA", "CAMERA", "USE_CAMERA", "OPEN_CAMERA"), 1),
                new MCQ("What API is used for fingerprint authentication in Android?", Arrays.asList("BiometricPrompt", "AuthManager", "FingerAuth", "IDManager"), 0),
                new MCQ("What iOS framework handles location-based services?", Arrays.asList("CoreMotion", "CoreBluetooth", "CoreLocation", "SensorKit"), 2)
        )));

        assessments.add(new SkillAssessment("Mobile App Deployment & Performance", "Mobile Developer", Arrays.asList(
                new MCQ("What is the purpose of ProGuard in Android?", Arrays.asList("UI testing", "Debugging", "Code shrinking and obfuscation", "Caching data"), 2),
                new MCQ("Which file defines build variants in Android?", Arrays.asList("AndroidManifest.xml", "build.gradle", "MainActivity.java", "strings.xml"), 1),
                new MCQ("What is TestFlight used for?", Arrays.asList("Android beta testing", "UI testing in React", "iOS beta testing", "Windows app testing"), 2),
                new MCQ("Which format is used for publishing Android apps?", Arrays.asList(".ipa", ".apk", ".dex", ".bundle"), 1),
                new MCQ("What does “ANR” stand for in Android?", Arrays.asList("Android Network Request", "Application Not Responding", "App Name Registration", "Android Native Render"), 1)
        )));

        // === DATA SCIENTIST OR MACHINE LEARNING SPECIALIST ===
        assessments.add(new SkillAssessment("Python for Data Science", "Data Scientist or Machine Learning Specialist", Arrays.asList(
                new MCQ("What does df.head() return in pandas?", Arrays.asList("Last 5 rows", "First 5 rows", "Column names", "Random rows"), 1),
                new MCQ("What is the output of len(set([1, 2, 2, 3, 4]))?", Arrays.asList("5", "4", "3", "2"), 1),
                new MCQ("Which library is used for data visualization in Python?", Arrays.asList("pandas", "matplotlib", "numpy", "re"), 1),
                new MCQ("What does axis=1 represent in pandas operations?", Arrays.asList("Operate on columns", "Operate on rows", "Operate on cells", "Deletes the DataFrame"), 0),
                new MCQ("What is the purpose of np.array()?", Arrays.asList("Create dictionary", "Create string", "Create numerical array", "Create file"), 2)
        )));

        assessments.add(new SkillAssessment("Supervised Machine Learning", "Data Scientist or Machine Learning Specialist", Arrays.asList(
                new MCQ("What type of ML problem is predicting house price?", Arrays.asList("Classification", "Clustering", "Regression", "Dimensionality Reduction"), 2),
                new MCQ("What is the output of a logistic regression model?", Arrays.asList("Continuous value", "Image", "Class probability", "Text summary"), 2),
                new MCQ("What does overfitting mean?", Arrays.asList("Model performs poorly on training data", "Model fits training data too well and performs badly on test data", "Model runs slowly", "Model has no parameters"), 1),
                new MCQ("What is used to split data into training and testing sets?", Arrays.asList("cross_val_score", "train_test_split", "fit_transform", "OneHotEncoder"), 1),
                new MCQ("What metric is best for classification evaluation?", Arrays.asList("MAE", "Accuracy", "R-squared", "MSE"), 1)
        )));


// Assessment 3: Unsupervised Learning & Clustering assessments.add(new SkillAssessment("Unsupervised Learning & Clustering", "Data Scientist or Machine Learning Specialist", Arrays.asList( new MCQ("Which algorithm is used for clustering?", Arrays.asList("Linear Regression", "Decision Trees", "K-Means", "Naive Bayes"), 2), new MCQ("What is the goal of PCA (Principal Component Analysis)?", Arrays.asList("Classification", "Regression", "Dimensionality Reduction", "Text Analysis"), 2), new MCQ("What does silhouette score measure?", Arrays.asList("Training accuracy", "Clustering performance", "Error rate", "Runtime"), 1), new MCQ("What is an elbow plot used for?", Arrays.asList("Hyperparameter tuning", "Choosing number of clusters in K-Means", "Finding outliers", "Drawing bar plots"), 1), new MCQ("In clustering, labels are:", Arrays.asList("Predefined", "Unknown", "One-hot encoded", "Based on training data"), 1) )));

// Assessment 4: Model Evaluation & Validation assessments.add(new SkillAssessment("Model Evaluation & Validation", "Data Scientist or Machine Learning Specialist", Arrays.asList( new MCQ("What does cross-validation help with?", Arrays.asList("Speed up model", "Train on test set", "Reduce overfitting", "Label generation"), 2), new MCQ("What does a confusion matrix show?", Arrays.asList("Random predictions", "True/False Positives and Negatives", "Loss curve", "Feature selection"), 1), new MCQ("Which metric is suitable for imbalanced classification?", Arrays.asList("Accuracy", "Precision and Recall", "RMSE", "R²"), 1), new MCQ("What does AUC-ROC represent?", Arrays.asList("Overfitting control", "Area under curve measuring classification performance", "Data cleaning", "Label balancing"), 1), new MCQ("What happens in k-fold cross-validation?", Arrays.asList("Data is trained k times on the same set", "Dataset is divided into k parts and trained/tested k times", "All data is tested", "Output labels are predicted"), 1) )));

// Assessment 5: Deep Learning & Neural Networks assessments.add(new SkillAssessment("Deep Learning & Neural Networks", "Data Scientist or Machine Learning Specialist", Arrays.asList( new MCQ("What is a neuron in neural networks?", Arrays.asList("Storage unit", "Processing unit mimicking the brain", "Data type", "CPU"), 1), new MCQ("Which library is most popular for deep learning in Python?", Arrays.asList("pandas", "keras", "seaborn", "sklearn"), 1), new MCQ("What is backpropagation used for?", Arrays.asList("Forward pass", "Updating weights", "Loading data", "Training dataset"), 1), new MCQ("What is a common activation function?", Arrays.asList("relu", "dropout", "mse", "fit()"), 0), new MCQ("What is overfitting in deep learning often solved by?", Arrays.asList("Adding neurons", "Increasing epochs", "Using dropout", "Using more layers"), 2) )));

        assessments.add(new SkillAssessment("CI/CD and Automation", "DevOps Specialist", Arrays.asList(
                new MCQ("What does CI/CD stand for?", Arrays.asList("Code Integration / Cloud Deployment", "Continuous Integration / Continuous Deployment", "Central Infrastructure / Continuous Development", "Core Interface / Code Delivery"), 1),
                new MCQ("What is the purpose of a CI pipeline?", Arrays.asList("Monitor database", "Automate code testing and integration", "Compress files", "Encrypt code"), 1),
                new MCQ("Which tool is commonly used for CI/CD?", Arrays.asList("PowerPoint", "Jenkins", "Excel", "MS Paint"), 1),
                new MCQ("What does a successful pipeline usually end with?", Arrays.asList("Database dump", "Docker cleanup", "Deployment to staging or production", "Screenshot capture"), 2),
                new MCQ("What is a common trigger for CI pipelines?", Arrays.asList("File deletion", "Manual data entry", "Git commit or pull request", "Restart server"), 2)
        )));

        assessments.add(new SkillAssessment("Containerization (Docker & Kubernetes)", "DevOps Specialist", Arrays.asList(
                new MCQ("What is Docker used for?", Arrays.asList("Image processing", "Cloud storage", "Containerizing applications", "File encryption"), 2),
                new MCQ("Which command builds a Docker image?", Arrays.asList("docker init", "docker image", "docker build", "docker edit"), 2),
                new MCQ("What does Kubernetes primarily manage?", Arrays.asList("API keys", "Container orchestration", "Front-end deployment", "Windows apps"), 1),
                new MCQ("What is a Pod in Kubernetes?", Arrays.asList("A storage unit", "The smallest deployable unit", "A load balancer", "A container image"), 1),
                new MCQ("Which command lists all running Docker containers?", Arrays.asList("docker ps", "docker run", "docker start", "docker deploy"), 0)
        )));

        assessments.add(new SkillAssessment("Infrastructure as Code (IaC)", "DevOps Specialist", Arrays.asList(
                new MCQ("What is Infrastructure as Code?", Arrays.asList("Writing back-end APIs", "Scripting UI animations", "Managing infrastructure using code", "Running SQL queries"), 2),
                new MCQ("Which tool is commonly used for IaC?", Arrays.asList("Git", "Terraform", "MongoDB", "Jenkins"), 1),
                new MCQ("What is a “terraform plan” command used for?", Arrays.asList("Destroy resources", "Apply immediately", "Preview changes before applying", "Schedule updates"), 2),
                new MCQ("What is the file format used in Terraform configuration?", Arrays.asList("JSON", "XML", "HCL (HashiCorp Configuration Language)", "YAML only"), 2),
                new MCQ("What is Ansible?", Arrays.asList("Monitoring tool", "Configuration management tool", "Container runtime", "Network protocol"), 1)
        )));

        assessments.add(new SkillAssessment("Monitoring & Logging", "DevOps Specialist", Arrays.asList(
                new MCQ("What is Prometheus used for?", Arrays.asList("Building UIs", "Database optimization", "Monitoring systems and metrics", "Encrypting logs"), 2),
                new MCQ("What does Grafana do?", Arrays.asList("Stores data", "Builds mobile apps", "Visualizes metrics and dashboards", "Deploys software"), 2),
                new MCQ("What are logs used for?", Arrays.asList("Building containers", "Diagnosing errors and debugging", "Compiling code", "Enhancing UI speed"), 1),
                new MCQ("What is an alert rule in monitoring systems?", Arrays.asList("Git command", "Rule that triggers a notification when thresholds are met", "Password recovery script", "Build command"), 1),
                new MCQ("What is the ELK stack composed of?", Arrays.asList("Elasticsearch, Logstash, Kibana", "Electron, Laravel, Kotlin", "Express, Lodash, Kubernetes", "Eclipse, Linux, Kafka"), 0)
        )));

        assessments.add(new SkillAssessment("Cloud Fundamentals", "DevOps Specialist", Arrays.asList(
                new MCQ("Which of the following is a cloud computing provider?", Arrays.asList("Visual Studio", "AWS", "MongoDB Compass", "React"), 1),
                new MCQ("What is IaaS in cloud terminology?", Arrays.asList("Infrastructure as a Service", "Interface as a Service", "Image as a System", "Identity and Authentication System"), 0),
                new MCQ("What is an EC2 instance in AWS?", Arrays.asList("A storage container", "A virtual server", "A file manager", "A billing tool"), 1),
                new MCQ("What is the purpose of an S3 bucket?", Arrays.asList("Host applications", "Store data objects", "Create virtual machines", "Build UIs"), 1),
                new MCQ("What is autoscaling in cloud infrastructure?", Arrays.asList("Manual upgrade", "Automatically adjusting computing resources based on demand", "Increasing app size", "Adding databases"), 1)
        )));

    }



    public static class MCQ {

        String question;

        List<String> options;

        int correctOption;



        public MCQ(String question, List<String> options, int correctOption) {

            this.question = question;

            this.options = options;

            this.correctOption = correctOption;

        }

    }



    public static class SkillAssessment {

        String skill;

        String role;

        List<MCQ> questions;

        //boolean isCompleted = false;



        public SkillAssessment(String skill, String role, List<MCQ> questions) {

            this.skill = skill;

            this.role = role;

            this.questions = questions;

        }

    }

}

