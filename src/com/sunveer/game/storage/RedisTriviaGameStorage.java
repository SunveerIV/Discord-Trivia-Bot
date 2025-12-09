package com.sunveer.game.storage;

import com.sunveer.game.Scores;
import com.sunveer.game.question.Question;
import com.sunveer.game.question.QuestionCreator;
import com.sunveer.game.question.QuestionNotAvailableException;
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
    private QuestionCreator questionCreator;

    public RedisTriviaGameStorage(String ip, int port, QuestionCreator questionCreator) throws StorageException {
        this.questionCreator = questionCreator;

        try {
            jedis = new Jedis(ip, port);
            initializeCurrentGameCode();
        } catch (JedisException | QuestionNotAvailableException e) {
            throw new StorageException();
        }
    }

    private void printQuestionAndAnswer() {
        try {
            System.out.println(getCurrentQuestion().questionText());
            System.out.println(getCurrentQuestion().answerText());
        } catch (Exception e) {
            System.out.println("Could not get current question to print internally");
        }
    }

    private void initializeCurrentGameCode() throws JedisException, QuestionNotAvailableException {
        String currentGameCode = jedis.get(CURRENT_GAME_KEY);
        if (currentGameCode == null) {
            printQuestionAndAnswer();
            return;
        }

        this.currentGameCode = currentGameCode;
        printQuestionAndAnswer();
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
    public String startNewQuestion() throws StorageException, QuestionInSessionException {
        if (questionIsInSession()) throw new QuestionInSessionException();

        try {
            return setQuestion(questionCreator.newQuestion());
        } catch (JedisException | QuestionNotAvailableException e) {
            throw new StorageException();
        }
    }

    private String setQuestion(Question question) throws JedisException {
        String formattedTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        this.currentGameCode = formattedTime;
        jedis.set(CURRENT_GAME_KEY, formattedTime);
        jedis.set(CURRENT_QUESTION_TEXT_KEY, question.questionText());
        jedis.set(CURRENT_ANSWER_TEXT_KEY, question.answerText());

        printQuestionAndAnswer();
        return question.questionText();
    }

    @Override
    public void endQuestion() throws StorageException {
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
    public Scores getScores() throws StorageException {
        try {
            Map<String, Integer> totalScores = formatStringMap(jedis.hgetAll(SCORE_KEY));

            Map<String, Integer> currentQuestionScores;
            if (currentGameCode == null) {
                currentQuestionScores = new HashMap<>();
            } else {
                currentQuestionScores = formatStringMap(jedis.hgetAll(currentGameCode));
            }

            return new Scores(totalScores, currentQuestionScores);
        } catch (JedisException e) {
            throw new StorageException();
        }
    }

    private Map<String, Integer> formatStringMap(Map<String, String> input) {
        Map<String, Integer> totalScores = new HashMap<>();
        for (Map.Entry<String, String> entry : input.entrySet()) {
            totalScores.put(entry.getKey(), parseOrZero(entry.getValue()));
        }
        return totalScores;
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
