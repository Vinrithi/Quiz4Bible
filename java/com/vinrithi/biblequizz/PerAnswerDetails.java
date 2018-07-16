package com.vinrithi.biblequizz;

/**
 * Created by vinri on 1/5/2018.
 */

public class PerAnswerDetails {

    private String answer,description,verse,text, chapter,isAnswer;
    private boolean isPickedAsAnswer = false;

    public PerAnswerDetails(String answer, String isAnswer,String description, String verse, String text, String chapter) {
        this.answer = answer;
        this.isAnswer = isAnswer;
        this.description = description;
        this.verse = verse;
        this.text = text;
        this.chapter = chapter;
    }

    public String getAnswer() {
        return answer;
    }

    public String getDescription() {
        return description;
    }

    public String getVerse() {
        return verse;
    }

    public String getText() {
        return text;
    }

    public String getChapter() {
        return chapter;
    }

    public void setIsAnswer(String isAnswer) {
        this.isAnswer = isAnswer;
    }

    public String getIsAnswer() {
        return isAnswer;
    }

    public boolean isPickedAsAnswer() {
        return isPickedAsAnswer;
    }

    public void setPickedAsAnswer(boolean pickedAsAnswer) {
        isPickedAsAnswer = pickedAsAnswer;
    }
}
