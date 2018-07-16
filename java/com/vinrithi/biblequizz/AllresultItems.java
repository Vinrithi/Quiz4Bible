package com.vinrithi.biblequizz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by vinri on 1/5/2018.
 */

public class AllresultItems {
    private int score,noOfQuestions;
    private String book,level,dateTaken;
    private long timeTaken;
    private SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");;
    private SimpleDateFormat sdf2= new SimpleDateFormat("dd MMM yyyy HH:mm:ss");;
    public AllresultItems(int score, int noOfQuestions, String book, String level, String dateTaken, long timeTaken) {
        this.score = score;
        this.noOfQuestions = noOfQuestions;
        this.book = book;
        this.level = level;
        this.dateTaken = dateTaken;
        this.timeTaken = timeTaken;
    }

    public int getScore() {
        return score;
    }

    public int getNoOfQuestions() {
        return noOfQuestions;
    }

    public String getBook() {
        return book;
    }

    public String getLevel() {
        return level;
    }

    public String getDateTaken() {
        String str_date = dateTaken;
        try {
            Date date = sdf.parse(dateTaken);
            str_date = sdf2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str_date;
    }

    public String getTimeTaken() {
        long minutes = timeTaken/60000;
        long seconds = (timeTaken%60000)/1000;
        return String.valueOf(minutes) + "min(s) " + String.valueOf(seconds) + "sec";
    }
}
