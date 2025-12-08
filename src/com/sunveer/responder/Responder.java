package com.sunveer.responder;

public interface Responder {
    String initialPrompt();

    String response(String message, String id);
}
