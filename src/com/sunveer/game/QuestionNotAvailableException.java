package com.sunveer.game;

public class QuestionNotAvailableException extends Exception {
    public QuestionNotAvailableException() {
        super("Could not get question.");
    }
}
