package com.sunveer.game.storage;

import java.util.Map;

public class Scores {
    private final Map<String, Integer> totalScores;
    private final Map<String, Integer> currentQuestionScores;

    public Scores(Map<String, Integer> totalScores, Map<String, Integer> currentQuestionScores) {
        this.totalScores = totalScores;
        this.currentQuestionScores = currentQuestionScores;
    }

    public int scoreOfId(String id) {
        Integer score = totalScores.get(id);
        if (score == null) return 0;
        return score;
    }

    public Map<String, Integer> totalScores() {
        return Map.copyOf(totalScores);
    }

    public Map<String, Integer> currentQuestionScores() {
        return Map.copyOf(currentQuestionScores);
    }
}
