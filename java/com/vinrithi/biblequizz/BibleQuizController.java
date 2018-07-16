package com.vinrithi.biblequizz;

import android.app.ProgressDialog;
import android.support.v4.widget.SwipeRefreshLayout;

import org.json.JSONException;

/**
 * Created by vinri on 1/4/2018.
 */

public interface BibleQuizController {
    public ProgressDialog getProgressDialog();
    public void resetPreferences();
    public void retrieveData(String result) throws JSONException;
    public SwipeRefreshLayout getRefresher();
}
