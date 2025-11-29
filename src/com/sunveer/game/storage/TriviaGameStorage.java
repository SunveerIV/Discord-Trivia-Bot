package com.sunveer.game.storage;

public interface TriviaGameStorage {
    com.sunveer.game.Question getCurrentQuestion() throws StorageException, NoQuestionInSessionException;

    void startQuestion(com.sunveer.game.Question question) throws StorageException, QuestionInSessionException;

    void endQuestion() throws StorageException, NoQuestionInSessionException;

    void incrementScore(String id, int amount) throws StorageException, NoQuestionInSessionException;

    int getTotalScore(String id) throws StorageException;

    java.util.Map<String, Integer> getTotalScores() throws StorageException;

    int getCurrentQuestionScore(String id) throws StorageException, NoQuestionInSessionException;

    java.util.Map<String, Integer> getCurrentQuestionScores() throws StorageException, NoQuestionInSessionException;
}
