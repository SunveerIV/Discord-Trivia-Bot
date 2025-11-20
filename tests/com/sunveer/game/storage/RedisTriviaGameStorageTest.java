package com.sunveer.game.storage;

import com.github.fppt.jedismock.RedisServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RedisTriviaGameStorageTest {

    @Test
    void testNewRedisStorageIsEmpty() throws Exception {
        RedisServer rs = new RedisServer(1);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort());

        assertEquals(0, tgs.getScores().size());

        rs.stop();
    }

    @Test
    void testGettingScoreFromEmptyKey() throws Exception {
        RedisServer rs = new RedisServer(1);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort());

        String id = "Sunveer1";

        assertEquals(0, tgs.getScore(id));

        rs.stop();
    }

    @Test
    void testAddingOneScore() throws Exception {
        RedisServer rs = new RedisServer(1);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort());

        String id = "Sunveer2";
        tgs.incrementScore(id, 2);

        assertEquals(2, tgs.getScore(id));

        rs.stop();
    }

    @Test
    void testAddingMultipleScores() throws Exception {
        RedisServer rs = new RedisServer(1);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort());

        String id1 = "Sunveer3";
        String id2 = "Gortiz3";

        tgs.incrementScore(id1, 2);
        tgs.incrementScore(id2, 3);

        assertEquals(2, tgs.getScore(id1));
        assertEquals(3, tgs.getScore(id2));
        assertEquals(2, tgs.getScores().size());

        Map<String, Integer> scores = tgs.getScores();
        assertEquals(2, scores.get(id1));
        assertEquals(3, scores.get(id2));

        rs.stop();
    }
}