package com.example.m4project;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    TextView resultTextView;
    TextView scoreTextView;
    Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        resultTextView = findViewById(R.id.Result);
        scoreTextView = findViewById(R.id.Score);
        homeButton = findViewById(R.id.play);

        // Retrieve the score from Quiz activity
        Intent intent = getIntent();
        int score = intent.getIntExtra("SCORE", 0);

        // Display the result and score
        if (score == 0) {
            resultTextView.setText("Sorry!");
            scoreTextView.setText("You won nothing.");
        } else {
            resultTextView.setText("Congratulations!");
            scoreTextView.setText("Your score: $" + score);
        }

        // Set OnClickListener for the button
        homeButton.setOnClickListener(view -> {
            // Go back to the main activity (Quiz activity)
            Intent intent1 = new Intent(ResultsActivity.this, Quiz.class);
            startActivity(intent1);
            finish(); // Finish this activity
        });
    }
}