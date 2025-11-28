package com.sunveer.game.storage;

import com.github.fppt.jedismock.RedisServer;
import com.sunveer.game.Question;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RedisTriviaGameStorageTest {

    static final Question MOCK_QUESTION = new Question() {
        @Override
        public String questionText() {
            return "Why?";
        }

        @Override
        public String answerText() {
            return "Because.";
        }
    };

    @Test
    void testNewRedisStorageIsEmpty() throws Exception {
        RedisServer rs = new RedisServer(1);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort());

        assertEquals(0, tgs.getTotalScores().size());

        rs.stop();
    }

    @Test
    void testGettingScoreFromEmptyKey() throws Exception {
        RedisServer rs = new RedisServer(2);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort());

        String id = "Sunveer1";

        assertEquals(0, tgs.getTotalScore(id));
        assertEquals(0, tgs.getTotalScores().size());

        rs.stop();
    }

    @Test
    void testStartingQuestionTwiceBeforeEnding() throws StorageException, IOException {
        RedisServer rs = new RedisServer(4352);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort());

        assertDoesNotThrow(() -> {
            tgs.startQuestion(MOCK_QUESTION);
        });
        assertThrows(QuestionInSessionException.class, () -> {
            tgs.startQuestion(MOCK_QUESTION);
        });

        rs.stop();
    }

    @Test
    void testEndingQuestionBeforeStarting() throws StorageException, IOException{
        RedisServer rs = new RedisServer(432);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort());

        assertThrows(NoQuestionInSessionException.class, () -> {
            tgs.endQuestion();
        });

        rs.stop();
    }

    @Test
    void testStartingThenEndingThenAttemptingScoreIncrements() throws StorageException, IOException {
        RedisServer rs = new RedisServer(43233);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort());

        assertDoesNotThrow(() -> {
            tgs.startQuestion(MOCK_QUESTION);
        });
        assertDoesNotThrow(() -> {
            tgs.endQuestion();
        });

        assertThrows(NoQuestionInSessionException.class, () -> {
            tgs.incrementScore("a", 3);
        });

        rs.stop();
    }

    @Test
    void testAddingOneScoreWhenNoQuestionInSession() throws StorageException, IOException {
        RedisServer rs = new RedisServer(3);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort());

        String id = "Sunveer2";

        assertThrows(NoQuestionInSessionException.class, () -> {
            tgs.incrementScore(id, 2);
        });

        rs.stop();
    }

    @Test
    void testAddingOneScoreWhenQuestionInSession() throws Exception {
        RedisServer rs = new RedisServer(4);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort());

        tgs.startQuestion(MOCK_QUESTION);
        String id = "Sunveer3";
        tgs.incrementScore(id, 2);

        assertEquals(2, tgs.getCurrentQuestionScore(id));
        assertEquals(2, tgs.getTotalScore(id));

        rs.stop();
    }

    @Test
    void testAddingMultipleScoresWhenQuestionInSession() throws Exception {
        RedisServer rs = new RedisServer(5);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort());

        tgs.startQuestion(MOCK_QUESTION);

        String id1 = "Sunveer3";
        String id2 = "Gortiz3";

        tgs.incrementScore(id1, 2);
        tgs.incrementScore(id2, 3);

        assertEquals(2, tgs.getTotalScore(id1));
        assertEquals(3, tgs.getTotalScore(id2));
        assertEquals(2, tgs.getTotalScores().size());

        Map<String, Integer> scores = tgs.getTotalScores();
        assertEquals(2, scores.get(id1));
        assertEquals(3, scores.get(id2));

        Map<String, Integer> currentScores = tgs.getCurrentQuestionScores();
        assertEquals(2, currentScores.get(id1));
        assertEquals(3, currentScores.get(id2));

        rs.stop();
    }

    @Test
    void testFullGameWithTwoQuestions() throws Exception {
        RedisServer rs = new RedisServer(93);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort());

        tgs.startQuestion(MOCK_QUESTION);

        String id1 = "Sunveer3";
        String id2 = "Gortiz3";

        tgs.incrementScore(id1, 2);
        tgs.incrementScore(id2, 3);

        tgs.endQuestion();

        tgs.startQuestion(MOCK_QUESTION);
        tgs.incrementScore(id1, 1);
        tgs.incrementScore(id2, 3);

        assertEquals(1, tgs.getCurrentQuestionScore(id1));
        assertEquals(3, tgs.getCurrentQuestionScore(id2));
        assertEquals(3, tgs.getTotalScore(id1));
        assertEquals(6, tgs.getTotalScore(id2));

        rs.stop();
    }
}