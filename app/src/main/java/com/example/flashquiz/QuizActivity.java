package com.example.flashquiz;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;

public class QuizActivity extends AppCompatActivity {

    int index = 0;
    TextView questionText, answerText, difficultyText;
    Button showBtn, nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionText = findViewById(R.id.questionText);
        answerText = findViewById(R.id.answerText);
        difficultyText = findViewById(R.id.difficultyText);
        showBtn = findViewById(R.id.showBtn);
        nextBtn = findViewById(R.id.nextBtn);

        Collections.shuffle(UploadActivity.flashcards);
        loadFlashcard();

        showBtn.setOnClickListener(v ->
                answerText.setText(
                        UploadActivity.flashcards.get(index).getAnswer()
                )
        );

        nextBtn.setOnClickListener(v -> {
            index++;
            if (index < UploadActivity.flashcards.size()) {
                loadFlashcard();
            } else {
                Toast.makeText(this, "Quiz Completed!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void loadFlashcard() {
        Flashcard card = UploadActivity.flashcards.get(index);
        questionText.setText(card.getQuestion());
        answerText.setText("");
        difficultyText.setText("Difficulty: " + card.getDifficulty());
    }
}
