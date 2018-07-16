package com.vinrithi.biblequizz;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Results extends ToolbarSet implements View.OnClickListener,View.OnTouchListener,BibleQuizController{
    String book,level,incorrect,correct,totalQuestions,username,timeTaken;
    TextView tvScore,tvLevel,tvAllQuestions,tvIncorrect,tvCorrect,tvBook;
    Button btAnswers,btFinish;
    SwipeRefreshLayout spRefresh;
    int score = 0;
    float alquest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        m_toolbar = (Toolbar)findViewById(R.id.include_toolbar);
        tvToolbarTitle = (TextView)m_toolbar.findViewById(R.id.tvToolbarTitle);
        setToolbar();
        Bundle bundle = getIntent().getExtras();

        book = bundle.getString("Book");
        totalQuestions = bundle.getString("AllQuestions");
        level = bundle.getString("Level");
        incorrect = bundle.getString("Incorrect");
        correct = bundle.getString("Correct");
        timeTaken = bundle.getString("TimeTaken");
        tvToolbarTitle.setText("Results");
        username = mPreferences.getString(PreferenceConstants.USERNAME,"");

        float cor = Integer.parseInt(correct);
        float incor = Integer.parseInt(incorrect);
        alquest = cor+incor;
        if(cor!=0)
        {
            score = Math.round((cor/alquest)*100);
        }


        String scr = String.valueOf(score)+"%";

        sendData();

        tvScore = (TextView)findViewById(R.id.tvScore);
        tvLevel = (TextView)findViewById(R.id.tvLevel);
        tvAllQuestions = (TextView)findViewById(R.id.tvAllQuestions);
        tvIncorrect = (TextView)findViewById(R.id.tvIncorrect);
        tvCorrect = (TextView)findViewById(R.id.tvCorrect);
        tvBook = (TextView)findViewById(R.id.tvBook);
        spRefresh = (SwipeRefreshLayout) findViewById(R.id.spRefresh);

        btAnswers = (Button) findViewById(R.id.btAnswers);
        btFinish = (Button)findViewById(R.id.btFinish);

        btAnswers.setOnClickListener(this);
        btAnswers.setOnTouchListener(this);
        btFinish.setOnClickListener(this);
        btFinish.setOnTouchListener(this);
        spRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendData();
            }
        });

        tvAllQuestions.setText(totalQuestions);
        tvLevel.setText(level);
        tvBook.setText(book);
        tvIncorrect.setText(String.valueOf(incorrect));
        tvCorrect.setText(String.valueOf(correct));

        tvScore.setText(scr);
    }

    public void sendData()
    {
        JSONObject postData = new JSONObject();
        try {
            postData.put("book", book);
            postData.put("level", level);
            postData.put("username", username);
            postData.put("score", score);
            postData.put("timeTaken", timeTaken);
            postData.put("numberOfQuestions", alquest);

            String url = "http://192.168.137.1/quiz4bible/questions_answers/saveresults.php";
            new ConnectToServer(this,"saveResults").execute(url,postData.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btAnswers:
                startActivity(new Intent(Results.this,Answers.class).putExtra("Book",book));
                break;
            case R.id.btFinish:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("It is recommended that you view the answers first for you to get more knowledge on where you went wrong. Do you still wish to leave?")
                        .setCancelable(true)
                        .setPositiveButton(this.getResources().getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        startActivity(new Intent(Results.this,MainActivity.class));
                                    }
                                })
                        .setNegativeButton(this.getResources().getString(R.string.no),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.setCancelable(true);
                alert.show();

                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(v.getId()) {
            case R.id.btAnswers:
            case R.id.btFinish:
                /*switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.parseColor("#00B050"));
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        v.setBackgroundResource(R.drawable.buttons_bg);
                        break;
                }*/
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
    }

    @Override
    public ProgressDialog getProgressDialog() {
        return null;
    }

    @Override
    public void resetPreferences() {

    }

    @Override
    public void retrieveData(String result) throws JSONException {
        if (spRefresh.isRefreshing()) {
            spRefresh.setRefreshing(false);
        }

        if (result == null) {
            Toast.makeText(this, "Your results have not been saved due internet connectivity problems. Please pull downwards to refresh", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public SwipeRefreshLayout getRefresher() {
        return spRefresh;
    }
}
