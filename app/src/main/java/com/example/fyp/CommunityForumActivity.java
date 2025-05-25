package com.example.fyp;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Typeface;
import android.graphics.Color;



public class CommunityForumActivity extends AppCompatActivity {

    private EditText etAuthor, etContent;
    private Button btnCreatePost, btnSubmitPost;
    private LinearLayout layoutCreatePost, postContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_forum);

        initViews();
        setupListeners();

    }

    private void initViews() {
        etAuthor = findViewById(R.id.etAuthor);
        etContent = findViewById(R.id.etContent);
        btnCreatePost = findViewById(R.id.btnCreatePost);
        btnSubmitPost = findViewById(R.id.btnSubmitPost);
        layoutCreatePost = findViewById(R.id.layoutCreatePost);
        postContainer = findViewById(R.id.postContainer);
        layoutCreatePost.setVisibility(View.GONE);
    }

    private void setupListeners() {
        btnCreatePost.setOnClickListener(v -> {
            layoutCreatePost.setVisibility(View.VISIBLE);
            etAuthor.requestFocus();
        });

        btnSubmitPost.setOnClickListener(v -> {
            String author = etAuthor.getText().toString().trim();
            String content = etContent.getText().toString().trim();

            if (author.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                addPostToContainer(author, content);
                clearPostForm();
                Toast.makeText(this, "Post added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearPostForm() {
        etAuthor.setText("");
        etContent.setText("");
        layoutCreatePost.setVisibility(View.GONE);
    }

    private void addPostToContainer(String author, String content) {
        LinearLayout postLayout = new LinearLayout(this);
        postLayout.setOrientation(LinearLayout.VERTICAL);
        postLayout.setPadding(32, 32, 32, 32);
        postLayout.setBackgroundColor(Color.parseColor("#F1F1F1"));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 24);
        postLayout.setLayoutParams(params);

        TextView tvAuthor = new TextView(this);
        tvAuthor.setText(author);
        tvAuthor.setTextColor(Color.parseColor("#1565C0"));
        tvAuthor.setTypeface(null, Typeface.BOLD);
        tvAuthor.setTextSize(16);

        TextView tvContent = new TextView(this);
        tvContent.setText(content);
        tvContent.setTextColor(Color.DKGRAY);
        tvContent.setTextSize(14);
        tvContent.setPadding(0, 8, 0, 0);

        postLayout.addView(tvAuthor);
        postLayout.addView(tvContent);

        // Add to the top
        postContainer.addView(postLayout, 0);
    }
}
