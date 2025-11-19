package com.sunveer.game.storage;

import com.github.fppt.jedismock.RedisServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RedisTriviaGameStorageTest {

    public static RedisServer rs;

    @BeforeAll
    static void setupRedis() throws IOException {
        rs = new RedisServer(1);
        rs.start();
    }

    @AfterAll
    static void teardownRedis() throws IOException {
        if (rs != null) rs.stop();
    }

    private Jedis mockRedis() {
        return new Jedis("localhost", 1);
    }

    @Test
    void testNewRedisStorageIsEmpty() {
        Jedis jedis = mockRedis();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(jedis);

        assertEquals(0, tgs.getScores().size());
    }

    @Test
    void testAddingOneScore() {
        Jedis jedis = mockRedis();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(jedis);
        String id = "Sunveer";
        tgs.incrementScore(id, 2);

        assertEquals(2, tgs.getScore(id));
    }
}