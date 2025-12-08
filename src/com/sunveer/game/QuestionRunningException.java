package com.sunveer.game;

public class QuestionRunningException extends Exception {
    public QuestionRunningException(Throwable throwable) {
        super("There is a question already running", throwable);
    }
}
