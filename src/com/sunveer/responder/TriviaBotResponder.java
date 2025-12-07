package com.sunveer.responder;

import com.sunveer.game.IncorrectAnswerException;
import com.sunveer.game.InternalServerException;
import com.sunveer.game.QuestionExpiredException;
import com.sunveer.game.TriviaGame;

public class TriviaBotResponder implements Responder {

    private final TriviaGame game;

    public TriviaBotResponder(TriviaGame game) {
        this.game = game;
    }

    public String response(String message, String id) {
        try {
            if (message.toLowerCase().startsWith("!sub")) {
                return submit(message.substring(5), id);
            }

        } catch (InternalServerException e) {
            return "Internal error. Please try again later. Sorry!";
        }
        return "Invalid message! Type `!help` for a list of commands.";
    }

    private String submit(String answer, String id) throws InternalServerException {
        try {
            game.submitAnswer(id, answer);
        } catch (IncorrectAnswerException e) {
            return "Incorrect Answer.";
        } catch (QuestionExpiredException e) {
            return "No Question In Session Right Now!";
        }
        return String.format("Correct, %s!", id);
    }
}
