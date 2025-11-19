package com.sunveer.game.storage;

import redis.clients.jedis.Jedis;

public class RedisTriviaGameStorage implements TriviaGameStorage{

    private Jedis db;

    public RedisTriviaGameStorage() {

    }

    @Override
    public void incrementScore(String id, int amount) {

    }

    @Override
    public int getScore(String id) {
        return 0;
    }
}
