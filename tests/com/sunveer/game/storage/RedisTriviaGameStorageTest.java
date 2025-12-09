package com.sunveer.game.storage;

import com.github.fppt.jedismock.RedisServer;
import com.sunveer.game.question.APIQuestionCreator;
import com.sunveer.game.question.MockQuestionCreator;
import com.sunveer.game.question.Question;
import com.sunveer.game.question.QuestionCreator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RedisTriviaGameStorageTest {

    @Test
    void testNewRedisStorageIsEmpty() throws Exception {
        RedisServer rs = new RedisServer(1);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort(), new MockQuestionCreator());

        assertEquals(0, tgs.getScores().totalScores().size());

        rs.stop();
    }

    @Test
    void testGettingScoreFromEmptyKey() throws Exception {
        RedisServer rs = new RedisServer(2);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort(), new MockQuestionCreator());

        String id = "Sunveer1";

        assertNull(tgs.getScores().totalScores().get(id));
        assertEquals(0, tgs.getScores().totalScores().size());

        rs.stop();
    }

    @Test
    void testStartingQuestionTwiceBeforeEnding() throws StorageException, IOException {
        RedisServer rs = new RedisServer(4352);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort(), new MockQuestionCreator());

        assertDoesNotThrow(() -> {
            tgs.startNewQuestion();
        });
        assertThrows(QuestionInSessionException.class, tgs::startNewQuestion);

        rs.stop();
    }

    @Test
    void testStartingThenEndingThenAttemptingScoreIncrements() throws StorageException, IOException {
        RedisServer rs = new RedisServer(43233);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort(), new MockQuestionCreator());

        assertDoesNotThrow(tgs::startNewQuestion);

        tgs.endQuestion();

        assertThrows(NoQuestionInSessionException.class, () -> {
            tgs.incrementScore("a", 3);
        });

        rs.stop();
    }

    @Test
    void testAddingOneScoreWhenNoQuestionInSession() throws StorageException, IOException {
        RedisServer rs = new RedisServer(3);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort(), new MockQuestionCreator());

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
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort(), new MockQuestionCreator());

        tgs.startNewQuestion();
        String id = "Sunveer3";
        tgs.incrementScore(id, 2);

        assertEquals(2, tgs.getScores().currentQuestionScores().get(id));
        assertEquals(2, tgs.getScores().scoreOfId(id));

        rs.stop();
    }

    @Test
    void testAddingMultipleScoresWhenQuestionInSession() throws Exception {
        RedisServer rs = new RedisServer(5);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort(), new MockQuestionCreator());

        tgs.startNewQuestion();

        String id1 = "Sunveer3";
        String id2 = "Gortiz3";

        tgs.incrementScore(id1, 2);
        tgs.incrementScore(id2, 3);

        assertEquals(2, tgs.getScores().scoreOfId(id1));
        assertEquals(3, tgs.getScores().scoreOfId(id2));
        assertEquals(2, tgs.getScores().totalScores().size());

        Map<String, Integer> scores = tgs.getScores().totalScores();
        assertEquals(2, scores.get(id1));
        assertEquals(3, scores.get(id2));

        Map<String, Integer> currentScores = tgs.getScores().currentQuestionScores();
        assertEquals(2, currentScores.get(id1));
        assertEquals(3, currentScores.get(id2));

        rs.stop();
    }

    @Test
    void testFullGameWithTwoQuestions() throws Exception {
        RedisServer rs = new RedisServer(93);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort(), new MockQuestionCreator());

        tgs.startNewQuestion();

        String id1 = "Sunveer3";
        String id2 = "Gortiz3";

        tgs.incrementScore(id1, 2);
        tgs.incrementScore(id2, 3);

        tgs.endQuestion();

        tgs.startNewQuestion();
        tgs.incrementScore(id1, 1);
        tgs.incrementScore(id2, 3);

        assertEquals(1, tgs.getScores().currentQuestionScores().get(id1));
        assertEquals(3, tgs.getScores().currentQuestionScores().get(id2));
        assertEquals(3, tgs.getScores().scoreOfId(id1));
        assertEquals(6, tgs.getScores().scoreOfId(id2));

        rs.stop();
    }

    @Test
    void testQuestionGetsDeletedAfterEndingQuestion() throws Exception{
        RedisServer rs = new RedisServer(93);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort(), new MockQuestionCreator());

        assertDoesNotThrow(tgs::startNewQuestion);
        Question currentQuestion = tgs.getCurrentQuestion();
        assertEquals("Why?", currentQuestion.questionText());
        assertEquals("Because.", currentQuestion.answerText());

        assertDoesNotThrow(tgs::endQuestion);
        assertThrows(NoQuestionInSessionException.class, tgs::getCurrentQuestion);

        rs.stop();
    }
}