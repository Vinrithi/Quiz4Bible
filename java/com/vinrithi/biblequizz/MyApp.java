package com.vinrithi.biblequizz;

import android.app.Application;
import android.util.SparseArray;

import java.util.HashMap;
import java.util.List;

/**
 * Created by vinri on 1/5/2018.
 */

public class MyApp extends Application {
    private static MyApp singleInstance = null;
    private SparseArray<PerQuestionItems> allQuestionsSparseArray;
    @Override
    public void onCreate() {
        super.onCreate();
        singleInstance = this;
    }

    public static MyApp getSingleInstance() {
        return singleInstance;
    }

    public SparseArray<PerQuestionItems> getAllQuestionsSparseArray() {
        return allQuestionsSparseArray;
    }

    public void setAllQuestionsSparseArray(SparseArray<PerQuestionItems> allQuestionsSparseArray) {
        this.allQuestionsSparseArray = allQuestionsSparseArray;
    }

}