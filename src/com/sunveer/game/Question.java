package com.sunveer.game;

public class Question {
    private String questionText;
    private Answer[] candidateAnswers;

    public Question(String questionText, Answer[] candidateAnswers) {
        this.questionText = questionText;
        this.candidateAnswers = candidateAnswers;
    }

    public Answer[] getCandidateAnswers() {
        return candidateAnswers;
    }

    @Override
    public String toString() {
        return questionText;
    }
}
