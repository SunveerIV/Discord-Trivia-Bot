package com.sunveer.game.storage;

public class QuestionInSessionException extends Exception {
    public QuestionInSessionException() {
        super("There is already a question being played.");
    }
}
