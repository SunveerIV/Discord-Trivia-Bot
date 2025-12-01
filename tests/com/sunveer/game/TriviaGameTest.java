package com.sunveer.game;

import com.github.fppt.jedismock.RedisServer;
import com.sunveer.game.storage.RedisTriviaGameStorage;
import com.sunveer.game.storage.TriviaGameStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TriviaGameTest {

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
    void testSubmittingAnswerWithNoQuestionInSession() throws Exception {
        RedisServer rs = new RedisServer(1);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort());
        TriviaGame tg = new TriviaGame(tgs);

        String id = "a";

        assertThrows(QuestionExpiredException.class, () -> {
            tg.submitAnswer(id, "b");
        });

        assertEquals(0, tg.getScore(id));
        assertEquals(0, tg.getTotalLeaderboard().size());
        assertThrows(QuestionExpiredException.class, () -> {
            tg.getCurrentQuestionLeaderboard();
        });

        rs.stop();
    }

    @Test
    void testSubmittingAnswerWithQuestionInSession() throws Exception {
        RedisServer rs = new RedisServer(1);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort());
        TriviaGame tg = new TriviaGame(tgs);

        String id = "a";


        rs.stop();
    }
}