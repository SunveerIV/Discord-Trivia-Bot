package com.sunveer.game;

import org.jetbrains.annotations.NotNull;

public class Answer {
    private final String answerText;
    private final boolean correct;

    public Answer(String answerText, boolean correct) {
        if (answerText == null) throw new IllegalArgumentException();
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
