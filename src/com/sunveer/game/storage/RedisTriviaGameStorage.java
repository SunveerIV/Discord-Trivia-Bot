package com.sunveer.game.storage;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.util.HashMap;
import java.util.Map;

public class RedisTriviaGameStorage implements TriviaGameStorage{
    private static final String SCORE_KEY = "scores";

    private Jedis jedis;

    public RedisTriviaGameStorage(String ip, int port) throws StorageException {
        try {
            jedis = new Jedis(ip, port);
        } catch (JedisException e) {
            throw new StorageException("Internal Storage Error");
        }
    }

    @Override
    public void incrementScore(String id, int amount) throws StorageException {
        try {
            jedis.hincrBy(SCORE_KEY, id, amount);
        } catch (JedisException e) {
            throw new StorageException("Internal Storage Error");
        }
    }

    @Override
    public int getScore(String id) throws StorageException {
        try {
            String value = jedis.hget(SCORE_KEY, id);
            return value != null ? Integer.parseInt(value) : 0;
        } catch (JedisException e) {
            throw new StorageException("Internal Storage Error");
        }
    }

    @Override
    public Map<String, Integer> getScores() throws StorageException {
        try {
            Map<String, String> map = jedis.hgetAll(SCORE_KEY);
            Map<String, Integer> scores = new HashMap<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                scores.put(entry.getKey(), Integer.parseInt(entry.getValue()));
            }
            return scores;
        } catch (JedisException e) {
            throw new StorageException("Internal Storage Error");
        }
    }
}
