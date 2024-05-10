package com.example.dutmed;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class QuestionAnswerActivity extends AppCompatActivity {
    private TextView textViewQuestion;
    private RadioGroup radioGroupAnswers;
    private Button buttonConfirm;
    private final String[] questions = {
            "Having stomach problems?",
            "Having constant coughing?",
            "Are you feeling fatigue?",
            "Do you have a fever?",
            "Do you feel nausea?",
            "Are you experiencing headaches?",
            "Is your temperature above 30 degrees?",
            "Experiencing chest pains?",
            "Having a sore throat?",
            "Are your eyes sore/itching?"
    }; // Add more questions as needed
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer);

        textViewQuestion = findViewById(R.id.textViewQuestion);
        radioGroupAnswers = findViewById(R.id.radioGroupAnswers);
        buttonConfirm = findViewById(R.id.buttonConfirm);

        setupQuestionView();
    }

    @SuppressLint("SetTextI18n")
    private void setupQuestionView() {
        textViewQuestion.setText(questions[currentQuestionIndex]);

        RadioButton radioButtonYes = new RadioButton(this);
        radioButtonYes.setId(View.generateViewId());
        radioButtonYes.setText("Yes");
        radioButtonYes.setButtonDrawable(R.drawable.radio_button_selector); // Set the custom drawable
        radioButtonYes.setPadding(32, 32, 32, 32); // Add padding for better touch area
        styleRadioButton(radioButtonYes);

        RadioButton radioButtonNo = new RadioButton(this);
        radioButtonNo.setId(View.generateViewId());
        radioButtonNo.setText("No");
        radioButtonNo.setButtonDrawable(R.drawable.radio_button_selector); // Set the custom drawable
        radioButtonNo.setPadding(32, 32, 32, 32); // Add padding for better touch area
        styleRadioButton(radioButtonNo);

        radioGroupAnswers.addView(radioButtonYes);
        radioGroupAnswers.addView(radioButtonNo);

        buttonConfirm.setOnClickListener(v -> {
            int checkedRadioButtonId = radioGroupAnswers.getCheckedRadioButtonId();
            if (checkedRadioButtonId == -1) {
                Toast.makeText(QuestionAnswerActivity.this, "Please select an option to continue.", Toast.LENGTH_SHORT).show();
            } else {
                displayNextQuestion();
            }
        });
    }

    private void displayNextQuestion() {
        if (currentQuestionIndex < questions.length - 1) {
            currentQuestionIndex++;
            textViewQuestion.setText(questions[currentQuestionIndex]);
            radioGroupAnswers.clearCheck();
        } else {
            finishQuestions();
        }
    }

    private void finishQuestions() {
        int yesCount = 0;
        if (yesCount >= 7) {
            // Show a Toast message when the condition is severe
            Toast.makeText(QuestionAnswerActivity.this, "Based on your responses, it is advisable to see a doctor. Redirecting to appointment booking...", Toast.LENGTH_LONG).show();
            // Delay the redirection by 3 seconds
            new Handler().postDelayed(this::showDoctorDialog, 6000); // 6000 milliseconds = 6 seconds
        } else {
            // Show a Toast message when the condition is not severe
            Toast.makeText(QuestionAnswerActivity.this, "Your condition may not be severe. Redirecting to symptom checker for possible conditions and remedies...", Toast.LENGTH_LONG).show();
            // Delay the redirection by 3 seconds
            new Handler().postDelayed(this::navigateToSymptomChecker, 6000); // 6000 milliseconds = 6 seconds
        }
    }

    private void styleRadioButton(RadioButton radioButton) {
        radioButton.setTextColor(getResources().getColor(R.color.black)); // Set text color
        radioButton.setTextSize(18); // Set text size
    }

    private void showDoctorDialog() {
        Intent intent = new Intent(QuestionAnswerActivity.this, AppointmentActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToSymptomChecker() {
        Intent intent = new Intent(QuestionAnswerActivity.this, SymptomChecker.class);
        startActivity(intent);
        finish();
    }
}
