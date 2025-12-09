package com.sunveer.game.storage;

public interface TriviaGameStorage {
    com.sunveer.game.question.Question getCurrentQuestion() throws StorageException, NoQuestionInSessionException;

    void startQuestion(com.sunveer.game.question.Question question) throws StorageException, QuestionInSessionException;

    void endQuestion() throws StorageException;

    void incrementScore(String id, int amount) throws StorageException, NoQuestionInSessionException;

    java.util.Map<String, Integer> getTotalScores() throws StorageException;

    java.util.Map<String, Integer> getCurrentQuestionScores() throws StorageException, NoQuestionInSessionException;
}
