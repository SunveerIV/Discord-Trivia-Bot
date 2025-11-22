package com.sunveer.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionTest {

    @Test
    void testNewInstance() {
        String questionText = "Why?";
        Answer[] answers = {
                new Answer("Yes", true),
                new Answer("No", false)
        };
        Question q1 = new Question(questionText, answers);

        assertEquals(questionText, q1.toString());
        assertEquals(answers[0], q1.getCandidateAnswers()[0]);
        assertEquals(answers[1], q1.getCandidateAnswers()[1]);
    }
}
