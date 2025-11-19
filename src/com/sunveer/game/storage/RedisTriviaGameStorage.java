package com.sunveer.game.storage;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

public class RedisTriviaGameStorage implements TriviaGameStorage{
    private static final String SCORE_KEY = "scores";

    private Jedis jedis;

    public RedisTriviaGameStorage(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public void incrementScore(String id, int amount) {
        jedis.hincrBy(SCORE_KEY, id, amount);
    }

    @Override
    public int getScore(String id) {
        return Integer.parseInt(jedis.hget(SCORE_KEY, id));
    }

    @Override
    public Map<String, Integer> getScores() {
        Map<String, String> map = jedis.hgetAll(SCORE_KEY);
        Map<String, Integer> scores = new HashMap<>();
        for (int i = 0; i < map.size(); i++) {

        }
        return scores;
    }
}
