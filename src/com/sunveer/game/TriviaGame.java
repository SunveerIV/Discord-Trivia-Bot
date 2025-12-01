package com.sunveer.game;

import com.sunveer.game.storage.NoQuestionInSessionException;
import com.sunveer.game.storage.StorageException;
import com.sunveer.game.storage.TriviaGameStorage;

import java.util.*;

public class TriviaGame {

    private TriviaGameStorage tgs;

    public TriviaGame(TriviaGameStorage tgs) {
        this.tgs = tgs;
    }

    public void submitAnswer(String id, String answer)
            throws QuestionExpiredException,
            IncorrectAnswerException,
            InternalServerException {
        try {
            if (!answerIsCorrect(answer, tgs.getCurrentQuestion().answerText())) {
                throw new IncorrectAnswerException();
            }

            tgs.incrementScore(id, 1);
        } catch (NoQuestionInSessionException e) {
            throw new QuestionExpiredException();
        } catch (StorageException e) {
            throw new InternalServerException();
        }
    }

    public int getScore(String id) throws InternalServerException{
        try {
            return tgs.getTotalScores().get(id);
        } catch (NullPointerException e) {
            return 0;
        } catch (StorageException e) {
            throw new InternalServerException();
        }
    }

    public Map<String, Integer> getCurrentQuestionLeaderboard() throws InternalServerException, QuestionExpiredException {
        try {
            return tgs.getCurrentQuestionScores();
        } catch (NoQuestionInSessionException e) {
            throw new QuestionExpiredException();
        } catch (StorageException e) {
            throw new InternalServerException();
        }
    }

    public Map<String, Integer> getTotalLeaderboard() throws InternalServerException {
        try {
            return tgs.getTotalScores();
        } catch (StorageException e) {
            throw new InternalServerException();
        }
    }

    private static boolean answerIsCorrect(String input, String correct) {
        return input.equals(correct);
    }
}
