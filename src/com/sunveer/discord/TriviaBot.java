package com.sunveer.discord;

public class TriviaBot implements ChatBot {
    @Override
    public String getResponse(String message) {
        if (message.toLowerCase().startsWith("hi")) {
            return "hello";
        }
        return "Invalid message!";
    }
}
