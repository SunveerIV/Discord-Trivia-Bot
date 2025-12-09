package com.sunveer.game.question;

public class QuestionNotAvailableException extends Exception {
    public QuestionNotAvailableException() {
        super("Could not get question.");
    }
}
