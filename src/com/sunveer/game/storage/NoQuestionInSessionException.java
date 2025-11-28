package com.sunveer.game.storage;

public class NoQuestionInSessionException extends Exception {
    public NoQuestionInSessionException() {
        super("There is no question to end.");
    }
}
