package com.sunveer.game;

public class InternalServerException extends Exception {
    public InternalServerException() {
        super("Error while accessing internal server.");
    }
}
