package com.sunveer.game;

import com.sunveer.game.storage.NoQuestionInSessionException;
import com.sunveer.game.storage.QuestionInSessionException;
import com.sunveer.game.storage.StorageException;
import com.sunveer.game.storage.TriviaGameStorage;

import java.util.*;

public class TriviaGame {
    private static final int MAX_ANSWERS = 3;

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
            InternalServerException {
        try {
            if (!answerIsCorrect(answer)) throw new IncorrectAnswerException();

            int numAnswers = tgs.getCurrentQuestionScores().size();
            if (numAnswers >= MAX_ANSWERS) {
                tgs.endQuestion();
                startNewQuestion();
                throw new QuestionExpiredException();
            }
            tgs.incrementScore(id, MAX_ANSWERS - numAnswers);
        } catch (NoQuestionInSessionException | QuestionRunningException e) {
            throw new QuestionExpiredException();
        } catch (StorageException e) {
            throw new InternalServerException();
        }
    }

    public String startNewQuestion() throws InternalServerException, QuestionRunningException {
        try {
            Question newQuestion = QuestionCreator.newQuestion();
            tgs.startQuestion(newQuestion);
            return newQuestion.questionText();
        } catch (QuestionNotAvailableException | StorageException e) {
            throw new InternalServerException();
        } catch (QuestionInSessionException e) {
            throw new QuestionRunningException(e);
        }
    }

    public int getScore(String id) throws InternalServerException{

        try {
            Integer value = tgs.getTotalScores().get(id);
            return value == null ? 0 : value;
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

    private boolean answerIsCorrect(String input) throws StorageException, NoQuestionInSessionException {
        String correct = tgs.getCurrentQuestion().answerText();

        String inputLowerCase = input.toLowerCase();
        String correctLowerCase = correct.toLowerCase();
        return inputLowerCase.contains(correctLowerCase);
    }
}
