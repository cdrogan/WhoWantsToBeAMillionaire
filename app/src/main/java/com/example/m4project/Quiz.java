package com.example.m4project;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Quiz extends AppCompatActivity implements View.OnClickListener {

    TextView totalQuestions;
    TextView questionTextView;
    Button ansA, ansB, ansC, ansD;
    Button submitbtn;
    TextView currentScoreTextView;

    int score = 0;
    int currentQuestionIndex = 0;
    String selectedAnswer = "";
    boolean safeHaven5Reached = false;
    boolean safeHaven10Reached = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // initializes fields
        totalQuestions = findViewById(R.id.totalquestions);
        questionTextView = findViewById(R.id.question);
        ansA = findViewById(R.id.Ans_A);
        ansB = findViewById(R.id.Ans_B);
        ansC = findViewById(R.id.Ans_C);
        ansD = findViewById(R.id.Ans_D);
        submitbtn = findViewById(R.id.submit_button);
        currentScoreTextView = findViewById(R.id.currentScore);

        // initializes answers and buttons
        ansA.setOnClickListener(this);
        ansB.setOnClickListener(this);
        ansC.setOnClickListener(this);
        ansD.setOnClickListener(this);
        submitbtn.setOnClickListener(this);


        // method for loading next question
        loadNewQuestion();
    }


    void loadNewQuestion() {
        if (currentQuestionIndex >= QuestionsAnswers.questions.length) {
            finishQuiz();
            return;
        }

        // Reset background color of answer buttons to light grey
        resetButtonBackground();

        totalQuestions.setText("Question " + (currentQuestionIndex + 1));
        questionTextView.setText(QuestionsAnswers.questions[currentQuestionIndex]);
        ansA.setText(QuestionsAnswers.choices[currentQuestionIndex][0]);
        ansB.setText(QuestionsAnswers.choices[currentQuestionIndex][1]);
        ansC.setText(QuestionsAnswers.choices[currentQuestionIndex][2]);
        ansD.setText(QuestionsAnswers.choices[currentQuestionIndex][3]);

        // Update current score TextView
        int questionNumber = currentQuestionIndex + 1;
        int questionValue = calculateQuestionValue(questionNumber);
        currentScoreTextView.setText("Current Score: $" + questionValue);//based on WWTBAM Rules
    }

    void resetButtonBackground() {
        ansA.setBackgroundColor(Color.LTGRAY);
        ansB.setBackgroundColor(Color.LTGRAY);
        ansC.setBackgroundColor(Color.LTGRAY);
        ansD.setBackgroundColor(Color.LTGRAY);
    }

    void finishQuiz() {
        // Start the results activity and pass the score
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("SCORE", score);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        Button clickedButton = (Button) view;

        // Reset background color of all answer buttons to light grey when a new choice is clicked
        resetButtonBackground();

        if (clickedButton.getId() == R.id.submit_button) {
            checkAnswer();
            currentQuestionIndex++;
            loadNewQuestion();
        } else {
            // Choices button clicked
            selectedAnswer = clickedButton.getText().toString();
            clickedButton.setBackgroundColor(Color.GREEN);
        }
    }

    void checkAnswer() {
        if (selectedAnswer.equals(QuestionsAnswers.correctAnswers[currentQuestionIndex])) {
            // Award points based on question number
            int questionNumber = currentQuestionIndex + 1;
            int questionValue = calculateQuestionValue(questionNumber);
            score += questionValue;

            // If the safe haven is reached, update the corresponding flag
            if (questionNumber == 5 && !safeHaven5Reached) {
                safeHaven5Reached = true;
            } else if (questionNumber == 10 && !safeHaven10Reached) {
                safeHaven10Reached = true;
            }
            // Show a Toast message for correct answer
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            // If answer is incorrect, navigate to ResultsActivity with the safe haven score
            Intent intent = new Intent(this, ResultsActivity.class);
            if (safeHaven10Reached) {
                intent.putExtra("SCORE", 32000);
            } else if (safeHaven5Reached) {
                intent.putExtra("SCORE", 1000);
            } else {
                intent.putExtra("SCORE", 0); // No safe haven reached, no points
            }
            startActivity(intent);
            finish();
            return; // Return to prevent further execution
        }
        // If all questions are answered correctly or the player reaches the end of the quiz, navigate to ResultsActivity
        if (currentQuestionIndex == 14 || score == 1000000) {
            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra("SCORE", 1000000);
            startActivity(intent);
            finish();
            return; // Return to prevent further execution
        }
        // Load the next question
        loadNewQuestion();
    }

//Assigns Who Wants to Be A Millionaire Scoring System
    int calculateQuestionValue(int questionNumber) {
        switch (questionNumber) {
            case 1:
                return 0;
            case 2:
                return 100;
            case 3:
                return 200;
            case 4:
                return 300;
            case 5:
                return 500;
            case 6:
                return 1000;// Safe Haven
            case 7:
                return 2000;
            case 8:
                return 4000;
            case 9:
                return 8000;
            case 10:
                return 16000;
            case 11:
                return 32000; // Safe Haven
            case 12:
                return 64000;
            case 13:
                return 125000;
            case 14:
                return 250000;
            case 15:
                return 500000;
            default:
                return 0;
        }
    }
}
