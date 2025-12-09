package com.sunveer.game;

import com.github.fppt.jedismock.RedisServer;
import com.sunveer.game.question.MockQuestionCreator;
import com.sunveer.game.question.Question;
import com.sunveer.game.storage.RedisTriviaGameStorage;
import com.sunveer.game.storage.TriviaGameStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TriviaGameTest {

    @Test
    void testSubmittingAnswerWithQuestionInSession() throws Exception {
        RedisServer rs = new RedisServer(1);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort(), new MockQuestionCreator());
        TriviaGame tg = new TriviaGame(tgs);

        tg.startNewQuestion();

        String id = "a";
        String answer = tgs.getCurrentQuestion().answerText();

        assertDoesNotThrow(() -> {
            tg.submitAnswer(id, new String(answer));
        });

        assertEquals(2, tg.getScore(id));
        assertEquals(1, tg.getTotalLeaderboard().size());
        assertDoesNotThrow(() -> {
            tg.getCurrentQuestionLeaderboard();
        });

        rs.stop();
    }
}