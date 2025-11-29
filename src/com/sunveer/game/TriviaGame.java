package com.sunveer.game;

import com.sunveer.game.storage.TriviaGameStorage;

import java.util.*;

public class TriviaGame {

    private TriviaGameStorage tgs;

    public TriviaGame(TriviaGameStorage tgs) {
        
    }

    public void submitAnswer(String id, String answer)
            throws QuestionExpiredException,
            IncorrectAnswerException,
            InternalServerException {

    }

    public int getScore(String id) {
        return 0;
    }

    public Map<String, Integer> getCurrentQuestionLeaderboard() throws InternalServerException {
        return null;
    }

    public Map<String, Integer> getTotalLeaderboard() throws InternalServerException {
        return null;
    }
}
