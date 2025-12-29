package com.example.flashquiz;

public class Flashcard {

    private String question;
    private String answer;
    private String difficulty;

    public Flashcard(String question, String answer, String difficulty) {
        this.question = question;
        this.answer = answer;
        this.difficulty = difficulty;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public String getDifficulty() {
        return difficulty;
    }
}
