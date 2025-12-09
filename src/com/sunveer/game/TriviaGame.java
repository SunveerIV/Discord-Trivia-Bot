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

    /**
     * Submits an answer for the given id
     * @param id Identification
     * @param answer Answer
     * @throws QuestionExpiredException if there is no question available
     * @throws IncorrectAnswerException if the answer is incorrect
     * @throws AlreadyAnsweredException if the submitter already submitted an answer
     * @throws InternalServerException if something goes wrong internally
     */
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
            if (numAnswers < MAX_ANSWERS) {
                tgs.incrementScore(id, MAX_ANSWERS - numAnswers);
                numAnswers++;
            }

            if (numAnswers == MAX_ANSWERS) {
                tgs.endQuestion();
            }
        } catch (NoQuestionInSessionException e) {
            throw new QuestionExpiredException();
        } catch (StorageException e) {
            throw new InternalServerException();
        }
    }

    /**
     * Starts a new question if there are no other questions available
     * @return the question
     * @throws InternalServerException if something goes wrong internally
     * @throws QuestionRunningException if there is already a question running
     */
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

    /**
     * returns a map of the current question's scores, NOT the total scores
     * @return Map
     * @throws InternalServerException if something goes wrong internally
     */
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
