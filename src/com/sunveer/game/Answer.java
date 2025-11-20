package com.sunveer.game;

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

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (getClass() != other.getClass()) return false;

        Answer a = (Answer) other;
        return this.correct == a.correct &&
                this.answerText.equals(a.answerText);
    }
}
