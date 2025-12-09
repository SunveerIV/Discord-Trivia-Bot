package com.sunveer.game.storage;

import com.sunveer.game.Scores;

public interface TriviaGameStorage {
    /**
     * Returns the current question of the game
     * @return question
     * @throws StorageException if something goes wrong internally
     * @throws NoQuestionInSessionException if there is no question being played currently
     */
    com.sunveer.game.question.Question getCurrentQuestion() throws StorageException, NoQuestionInSessionException;

    /**
     * Starts a new question
     * @return the question
     * @throws StorageException if something goes wrong internally
     * @throws QuestionInSessionException if there is already a question being played
     */
    String startNewQuestion() throws StorageException, QuestionInSessionException;

    /**
     * Ends the current question
     * @throws StorageException if something goes wrong internally
     */
    void endQuestion() throws StorageException;

    /**
     *
     * @param id submitter's identification
     * @param amount amount to add
     * @throws StorageException if something goes wrong internally
     * @throws NoQuestionInSessionException
     */
    void incrementScore(String id, int amount) throws StorageException, NoQuestionInSessionException;

    /**
     *
     * @return Scores object that holds scoring information
     * @throws StorageException if something goes wrong internally
     */
    Scores getScores() throws StorageException;
}
