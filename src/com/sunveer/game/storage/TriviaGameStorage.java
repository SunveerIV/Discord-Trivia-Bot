package com.sunveer.game.storage;

public interface TriviaGameStorage {
    void incrementScore(String id, int amount);

    int getScore(String id);

    java.util.Map<String, Integer> getScores();
}
