package com.sunveer.game;

public class AlreadyAnsweredException extends Exception {
    public AlreadyAnsweredException() {
        super("User already answered correctly.");
    }
}
