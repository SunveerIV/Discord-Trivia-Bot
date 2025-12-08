package com.sunveer.responder;

import com.sunveer.game.*;

public class TriviaBotResponder implements Responder {
    private static final String ERROR_MESSAGE = "Internal error. Please try again later. Sorry!";

    private final TriviaGame game;

    public TriviaBotResponder(TriviaGame game) {
        this.game = game;
    }

    @Override
    public String initialPrompt() {
        try {
            return "Bot unexpectedly shut down during a game. The last quesion was:\n\n" + game.currentQuestion();
        } catch (InternalServerException e) {
            return ERROR_MESSAGE;
        } catch (QuestionExpiredException e) {
            return "Welcome! Type `!start` to start a game!";
        }
    }

    @Override
    public String response(String message, String id) {
        try {
            if (message.toLowerCase().startsWith("!help")) {
                return help();
            } else if (message.toLowerCase().startsWith("!sub")) {
                return submit(message.substring(5), id);
            } else if (message.toLowerCase().startsWith("!rules")) {
                return rules();
            } else if (message.toLowerCase().startsWith("!start")) {
                return start();
            } else if (message.toLowerCase().startsWith("!score")) {
                return score(id);
            } else {
                return "Invalid message! Type `!help` for a list of commands.";
            }
        } catch (InternalServerException e) {
            return ERROR_MESSAGE;
        }
    }

    private String start() throws InternalServerException {
        try {
            return game.startNewQuestion();
        } catch (QuestionRunningException e) {
            return "There is already a question running!";
        }
    }

    private String help() {
        return "`!help` - Shows all commands you can use.\n" +
                "`!rules` - Shows all rules for the game.\n" +
                "`!sub` - submits an answer for the question.\n" +
                "`!start` - Starts a game if one isn't already running." +
                "`!score` - States your individual overall score.";

    }

    private String rules() {
        return "The rules are simple: Everything is allowed!\n\n" +
                "This includes:\n" +
                " ⋅ AI\n" +
                " ⋅ Google\n" +
                " ⋅ Your brain\n" +
                " ⋅ Other people's answers\n\n" +
                "The logic is that if you already know the answer, you have an advantage, which takes less time than AI.";
    }

    private String submit(String answer, String id) throws InternalServerException {
        try {
            game.submitAnswer(id, answer);
            return String.format(":white_check_mark: Correct, %s!", id);
        } catch (IncorrectAnswerException e) {
            return "Incorrect Answer.";
        } catch (QuestionExpiredException e) {
            return "No Question In Session Right Now!";
        } catch (AlreadyAnsweredException e) {
            return String.format("%s, you already answered correctly!", id);
        }
    }

    private String score(String id) {
        try {
            Integer score = game.getTotalLeaderboard().get(id);
            if (score == null) return "0";

            return String.format("%s, your score is: %s", id, score.toString());
        } catch (InternalServerException e) {
            return ERROR_MESSAGE;
        }
    }
}
