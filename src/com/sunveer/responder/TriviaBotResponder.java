package com.sunveer.responder;

import com.sunveer.game.TriviaGame;

public class TriviaBotResponder implements Responder {

    private final TriviaGame game;

    public TriviaBotResponder(TriviaGame game) {
        this.game = game;
    }

    public String response(String message) {
        if (message.toLowerCase().startsWith("hi")) {
            return "hello";
        }
        return "Invalid message!";
    }
}
