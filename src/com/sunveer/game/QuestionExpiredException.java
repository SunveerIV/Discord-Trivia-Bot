package com.sunveer.game;

public class QuestionExpiredException extends Exception{
    public QuestionExpiredException() {
        super("Answer submitted after max number of answered allowed.");
    }
}
