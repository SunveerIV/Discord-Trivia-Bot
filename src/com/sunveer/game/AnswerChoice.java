package com.sunveer.game;

public enum AnswerChoice {
    A, B, C, D;

    @Override
    public String toString() {
        return name().toUpperCase();
    }
}
