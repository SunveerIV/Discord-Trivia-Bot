package com.sunveer.game.question;

public interface QuestionCreator {
    Question newQuestion() throws QuestionNotAvailableException;
}
