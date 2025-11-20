package com.sunveer.game;

public class Answer {
    private final String answerText;
    private final boolean correct;

    public Answer(String answerText, boolean correct) {
        this.answerText = answerText;
        this.correct = correct;
    }

    public boolean isCorrect() {
        return correct;
    }

    @Override
    public String toString() {
        return answerText;
    }
}
