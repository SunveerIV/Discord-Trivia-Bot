package com.sunveer.game;

import com.sunveer.game.storage.NoQuestionInSessionException;
import com.sunveer.game.storage.QuestionInSessionException;
import com.sunveer.game.storage.StorageException;
import com.sunveer.game.storage.TriviaGameStorage;

import java.util.*;

public class TriviaGame {
    private static final int MAX_ANSWERS = 2;

    private TriviaGameStorage tgs;

    public TriviaGame(TriviaGameStorage tgs) {
        this.tgs = tgs;
    }

    public String currentQuestion() throws InternalServerException, QuestionExpiredException {
        try {
            return tgs.getCurrentQuestion().questionText();
        } catch (StorageException e) {
            throw new InternalServerException();
        } catch (NoQuestionInSessionException e) {
            throw new QuestionExpiredException();
        }
    }

    public void submitAnswer(String id, String answer)
            throws QuestionExpiredException,
            IncorrectAnswerException,
            AlreadyAnsweredException,
            InternalServerException {
        try {
            Map<String, Integer> currentScores = tgs.getScores().currentQuestionScores();
            if (currentScores.containsKey(id)) throw new AlreadyAnsweredException();
            if (!answerIsCorrect(answer)) throw new IncorrectAnswerException();

            int numAnswers = currentScores.size();
            if (numAnswers + 1 < MAX_ANSWERS) {
                tgs.incrementScore(id, MAX_ANSWERS - numAnswers);
            } else if (numAnswers + 1 == MAX_ANSWERS) {
                tgs.endQuestion();
            } else {
                throw new QuestionExpiredException();
            }
        } catch (NoQuestionInSessionException e) {
            throw new QuestionExpiredException();
        } catch (StorageException e) {
            throw new InternalServerException();
        }
    }

    public String startNewQuestion() throws InternalServerException, QuestionRunningException {
        try {
            return tgs.startNewQuestion();
        } catch (StorageException e) {
            throw new InternalServerException();
        } catch (QuestionInSessionException e) {
            throw new QuestionRunningException(e);
        }
    }

    public int getScore(String id) throws InternalServerException{

        try {
            return tgs.getScores().scoreOfId(id);
        } catch (StorageException e) {
            throw new InternalServerException();
        }
    }

    public Map<String, Integer> getCurrentQuestionLeaderboard() throws InternalServerException {
        try {
            return tgs.getScores().currentQuestionScores();
        } catch (StorageException e) {
            throw new InternalServerException();
        }
    }

    public Map<String, Integer> getTotalLeaderboard() throws InternalServerException {
        try {
            return tgs.getScores().totalScores();
        } catch (StorageException e) {
            throw new InternalServerException();
        }
    }

    private boolean answerIsCorrect(String input) throws StorageException, NoQuestionInSessionException {
        String correct = tgs.getCurrentQuestion().answerText();

        String inputLowerCase = input.toLowerCase();
        String correctLowerCase = correct.toLowerCase();
        return inputLowerCase.contains(correctLowerCase);
    }
}
