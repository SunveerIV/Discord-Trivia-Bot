package com.sunveer.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnswerTest {

    @Test
    void testNewInstance() {
        String text = "Yeah";
        boolean correct = true;
        Answer answer = new Answer(text, correct);

        assertEquals(text, answer.toString());
        assertTrue(answer.isCorrect());
    }
}