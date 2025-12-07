package com.sunveer.game.storage;

import com.sunveer.game.Question;
import com.sunveer.game.QuestionCreator;
import com.sunveer.game.QuestionNotAvailableException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class RedisTriviaGameStorage implements TriviaGameStorage{
    private static final String SCORE_KEY = "scores";
    private static final String CURRENT_GAME_KEY = "current_game";
    private static final String CURRENT_QUESTION_TEXT_KEY = "current_question";
    private static final String CURRENT_ANSWER_TEXT_KEY = "current_answer";

    private Jedis jedis;
    private String currentGameCode;

    public RedisTriviaGameStorage(String ip, int port) throws StorageException {
        try {
            jedis = new Jedis(ip, port);
            initializeCurrentGameCode();
        } catch (JedisException e) {
            throw new StorageException();
        }
    }

    private void initializeCurrentGameCode() throws JedisException {
        String currentGameCode = jedis.get(CURRENT_GAME_KEY);
        if (currentGameCode == null) {
            try {
                this.currentGameCode = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                jedis.set(CURRENT_GAME_KEY, this.currentGameCode);
                Question nextQuestion = QuestionCreator.newQuestion();
                jedis.set(CURRENT_QUESTION_TEXT_KEY, nextQuestion.questionText());
                jedis.set(CURRENT_ANSWER_TEXT_KEY, nextQuestion.answerText());
                return;
            } catch (QuestionNotAvailableException e) {
                throw new RuntimeException("Question Not Available", e);
            }
        }

        this.currentGameCode = currentGameCode;
    }

    @Override
    public Question getCurrentQuestion() throws StorageException, NoQuestionInSessionException {
        if (!questionIsInSession()) throw new NoQuestionInSessionException();

        try {
            return new Question() {
                @Override
                public String questionText() {
                    return jedis.get(CURRENT_QUESTION_TEXT_KEY);
                }

                @Override
                public String answerText() {
                    return jedis.get(CURRENT_ANSWER_TEXT_KEY);
                }
            };
        } catch (JedisException e) {
            throw new StorageException();
        }
    }

    @Override
    public void startQuestion(Question question) throws StorageException, QuestionInSessionException {
        if (questionIsInSession()) throw new QuestionInSessionException();

        try {
            jedis.set(CURRENT_GAME_KEY, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            jedis.set(CURRENT_QUESTION_TEXT_KEY, question.questionText());
            jedis.set(CURRENT_ANSWER_TEXT_KEY, question.answerText());
        } catch (JedisException e) {
            throw new StorageException();
        }
    }

    @Override
    public void endQuestion() throws StorageException, NoQuestionInSessionException {
        if (!questionIsInSession()) throw new NoQuestionInSessionException();

        try {
            jedis.del(currentGameCode);
            jedis.del(CURRENT_QUESTION_TEXT_KEY);
            jedis.del(CURRENT_ANSWER_TEXT_KEY);
        } catch (JedisException e) {
            throw new StorageException();
        }
    }

    @Override
    public void incrementScore(String id, int amount) throws StorageException, NoQuestionInSessionException {
        if (!questionIsInSession()) throw new NoQuestionInSessionException();

        try {
            jedis.hincrBy(SCORE_KEY, id, amount);
            jedis.hincrBy(currentGameCode, id, amount);
        } catch (JedisException e) {
            throw new StorageException();
        }
    }

    @Override
    public Map<String, Integer> getTotalScores() throws StorageException {
        try {
            Map<String, String> map = jedis.hgetAll(SCORE_KEY);
            Map<String, Integer> scores = new HashMap<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                scores.put(entry.getKey(), parseOrZero(entry.getValue()));
            }
            return scores;
        } catch (JedisException e) {
            throw new StorageException();
        }
    }

    @Override
    public Map<String, Integer> getCurrentQuestionScores() throws StorageException, NoQuestionInSessionException {
        if (!questionIsInSession()) throw new NoQuestionInSessionException();

        try {
            Map<String, String> map = jedis.hgetAll(currentGameCode);
            Map<String, Integer> scores = new HashMap<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                scores.put(entry.getKey(), parseOrZero(entry.getValue()));
            }
            return scores;
        } catch (JedisException e) {
            throw new StorageException();
        }
    }

    private boolean questionIsInSession() {
        String q = jedis.get(CURRENT_QUESTION_TEXT_KEY);
        String a = jedis.get(CURRENT_ANSWER_TEXT_KEY);
        return q != null || a != null;
    }

    private static int parseOrZero(String s) {
        if (s == null) return 0;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
