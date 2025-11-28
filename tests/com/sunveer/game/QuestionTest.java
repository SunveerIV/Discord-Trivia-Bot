package com.sunveer.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    @Test
    void testNewInstance() {
        String questionText = "Why?";
        String answerText = "Because.";
        Question q = new Question(new String(questionText), new String(answerText));
        assertEquals(questionText, q.questionText());
        assertEquals(answerText, q.answerText());
    }
}
