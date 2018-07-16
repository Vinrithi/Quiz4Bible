package com.vinrithi.biblequizz;

import android.util.SparseArray;

import java.util.HashMap;
import java.util.List;

/**
 * Created by vinri on 1/5/2018.
 */

public class PerQuestionItems {
    private String question,questionType;
    private boolean wasAnswered = false,isCorrectQuestion=false;
    private SparseArray<PerAnswerDetails> choicesArray = new SparseArray<>();
    public PerQuestionItems(String question, String questionType,SparseArray<PerAnswerDetails> choicesArray) {
        this.question = question;
        this.questionType = questionType;
        this.choicesArray = choicesArray;
    }

    public String getQuestion() {
        return question;
    }

    public String getQuestionType() {
        return questionType;
    }

    public SparseArray<PerAnswerDetails> getChoicesArray() {
        return choicesArray;
    }

    public boolean wasAnswered() {
        return wasAnswered;
    }

    public void setWasAnswered(boolean wasAnswered) {
        this.wasAnswered = wasAnswered;
    }

    public boolean isCorrectQuestion() {
        return isCorrectQuestion;
    }

    public void setIsCorrectQuestion(boolean correctQuestion) {
        isCorrectQuestion = correctQuestion;
    }
}
