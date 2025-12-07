package com.sunveer.game;

public class QuestionExpiredException extends Exception{
    public QuestionExpiredException() {
        super("Answer submitted at inappropriate time.");
    }
}
