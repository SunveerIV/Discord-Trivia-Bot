package com.sunveer.game;

public class IncorrectAnswerException extends Exception {
    public IncorrectAnswerException() {
        super("Incorrect Answer Submitted.");
    }
}
