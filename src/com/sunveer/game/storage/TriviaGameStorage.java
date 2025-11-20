package com.sunveer.game.storage;

public interface TriviaGameStorage {
    void incrementScore(String id, int amount) throws StorageException;

    int getScore(String id) throws StorageException;

    java.util.Map<String, Integer> getScores() throws StorageException;
}
