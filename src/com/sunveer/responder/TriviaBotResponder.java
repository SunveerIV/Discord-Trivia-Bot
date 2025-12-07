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
            if (message.toLowerCase().startsWith("!help")) {
                return help();
            } else if (message.toLowerCase().startsWith("!sub")) {
                return submit(message.substring(5), id);
            } else {
                return "Invalid message! Type `!help` for a list of commands.";
            }
        } catch (InternalServerException e) {
            return "Internal error. Please try again later. Sorry!";
        }
    }

    private String help() {
        return "`!help` - Shows all commands you can use.\n" +
                "`!rules` - Shows all rules for the game.\n" +
                "`!sub` - submits an answer for the question.\n";
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
