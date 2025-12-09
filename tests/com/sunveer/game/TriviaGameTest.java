package com.sunveer.game;

import com.github.fppt.jedismock.RedisServer;
import com.sunveer.game.question.MockQuestionCreator;
import com.sunveer.game.storage.RedisTriviaGameStorage;
import com.sunveer.game.storage.TriviaGameStorage;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TriviaGameTest {

    @Test
    void testStartingQuestionWhenQuestionAlreadyInSession() throws Exception {
        RedisServer rs = new RedisServer(1);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort(), new MockQuestionCreator());
        TriviaGame tg = new TriviaGame(tgs);

        tg.startNewQuestion();

        assertThrows(QuestionRunningException.class, tg::startNewQuestion);

        rs.stop();
    }

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

    @Test
    void testSamePersonSubmittingMultipleTimes() throws Exception {
        RedisServer rs = new RedisServer(1);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort(), new MockQuestionCreator());
        TriviaGame tg = new TriviaGame(tgs);

        tg.startNewQuestion();

        String id1 = "a";

        assertThrows(IncorrectAnswerException.class, () -> {
            tg.submitAnswer(id1, "Incorrect Answer oauwroagrgoaeugauorghnboug");
        });

        assertDoesNotThrow(() -> {
            tg.submitAnswer(id1, "Because.");
        });

        assertThrows(AlreadyAnsweredException.class, () -> {
            tg.submitAnswer(id1, "Because.");
        });

        rs.stop();
    }

    @Test
    void testTwoPeoplePlayingNormally() throws Exception {
        RedisServer rs = new RedisServer(2);
        rs.start();
        TriviaGameStorage tgs = new RedisTriviaGameStorage(rs.getHost(), rs.getBindPort(), new MockQuestionCreator());
        TriviaGame game = new TriviaGame(tgs);

        game.startNewQuestion();

        String id1 = "a";
        String id2 = "b";
        String id3 = "c";

        assertDoesNotThrow(() -> {
            game.submitAnswer(id1, "Because.");
        });

        assertDoesNotThrow(() -> {
            game.submitAnswer(id2, "Because.");
        });

        assertThrows(QuestionExpiredException.class, () -> {
            game.submitAnswer(id3, "Because.");
        });

        Map<String, Integer> totalScores = game.getTotalLeaderboard();

        for(Map.Entry<String, Integer> entry : totalScores.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }

        assertEquals(2, totalScores.size());
        assertEquals(2, totalScores.get(id1));
        assertEquals(1, totalScores.get(id2));

        rs.stop();
    }
}