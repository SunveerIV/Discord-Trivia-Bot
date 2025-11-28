package com.sunveer.game.storage;

import com.sunveer.game.Question;
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
            this.currentGameCode = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            return;
        }

        this.currentGameCode = currentGameCode;
    }

    @Override
    public void startQuestion(Question question) throws StorageException, QuestionInSessionException {
        if (questionIsInSession()) throw new QuestionInSessionException();

        try {
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
            jedis.del(CURRENT_GAME_KEY);
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
            jedis.hincrBy(CURRENT_GAME_KEY, id, amount);
        } catch (JedisException e) {
            throw new StorageException();
        }
    }

    @Override
    public int getTotalScore(String id) throws StorageException {
        try {
            String value = jedis.hget(SCORE_KEY, id);
            return value != null ? Integer.parseInt(value) : 0;
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
                scores.put(entry.getKey(), Integer.parseInt(entry.getValue()));
            }
            return scores;
        } catch (JedisException e) {
            throw new StorageException();
        }
    }

    @Override
    public int getCurrentQuestionScore(String id) throws StorageException, NoQuestionInSessionException {
        if (!questionIsInSession()) throw new NoQuestionInSessionException();

        try {
            String value = jedis.hget(CURRENT_GAME_KEY, id);
            return value != null ? Integer.parseInt(value) : 0;
        } catch (JedisException e) {
            throw new StorageException();
        }
    }

    @Override
    public Map<String, Integer> getCurrentQuestionScores() throws StorageException, NoQuestionInSessionException {
        if (!questionIsInSession()) throw new NoQuestionInSessionException();

        try {
            Map<String, String> map = jedis.hgetAll(CURRENT_GAME_KEY);
            Map<String, Integer> scores = new HashMap<>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                scores.put(entry.getKey(), Integer.parseInt(entry.getValue()));
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
}
