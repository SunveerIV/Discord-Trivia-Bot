package com.sunveer.game;

import com.sunveer.game.storage.TriviaGameStorage;

import java.util.*;

public class TriviaGame {
    private TriviaGameStorage tgs;
    private Map<String, Integer> scores;
    private Queue<Question> questions;

    public TriviaGame(Set<Question> questions) {
        
    }

    public void submitAnswer(String player) {

    }

    public Map<String, Integer> getScores() throws OutOfQuestionsException {
        return null;
    }

    public Question nextQuestion() throws OutOfQuestionsException {
        return null;
    }
}
