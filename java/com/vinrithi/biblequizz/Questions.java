package com.vinrithi.biblequizz;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Questions extends ToolbarSet implements View.OnClickListener,View.OnTouchListener{

    int pStatus = 0;
    private Handler handler = new Handler();
    String book,totalQuestions,level;
    TextView tvBook,tvTime,tvQuestionNumber,tvAllQuestions,tvQuestion;
    LinearLayout llAnswers;
    LinearLayout btPause,btNext;
    TextView bbtPause,bbtNext;

    int paused=0;
    MyApp myApp = MyApp.getSingleInstance();
    int question_number = 0;
    SparseArray<PerQuestionItems> sparseArray;
    CountDownTimer timer;
    ProgressBar mProgress;
    Thread timeCount;
    long timeRemaining=31000,timeTaken = 0;
    int progress=0;
    Drawable drawable;
    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        m_toolbar = (Toolbar)findViewById(R.id.include_toolbar);
        tvToolbarTitle = (TextView)m_toolbar.findViewById(R.id.tvToolbarTitle);
        setToolbar();

        Bundle bundle = getIntent().getExtras();
        book = bundle.getString("Book");
        totalQuestions = bundle.getString("AllQuestions");
        level = bundle.getString("Level");
        sparseArray = myApp.getAllQuestionsSparseArray();


        tvTime = (TextView)findViewById(R.id.tvTime);
        tvQuestionNumber = (TextView)findViewById(R.id.tvQuestionNumber);
        tvAllQuestions = (TextView)findViewById(R.id.tvAllQuestions);
        tvQuestion = (TextView)findViewById(R.id.tvQuestion);
        llAnswers = (LinearLayout)findViewById(R.id.llAnswers);
        btPause = (LinearLayout) findViewById(R.id.btPause);
        btNext = (LinearLayout)findViewById(R.id.btNext);
        bbtPause = (TextView) findViewById(R.id.bbtPause);
        bbtNext = (TextView) findViewById(R.id.bbtNext);


        mProgress = (ProgressBar) findViewById(R.id.timerProgressbar);

        res = getResources();
        if(Build.VERSION.SDK_INT<21)
            drawable = res.getDrawable(R.drawable.progressbar);
        else
            drawable = res.getDrawable(R.drawable.progressbar,null);

        tvToolbarTitle.setText(book);
        tvAllQuestions.setText(totalQuestions);

        btPause.setOnClickListener(this);
        btPause.setOnTouchListener(this);
        btNext.setOnClickListener(this);
        btNext.setOnTouchListener(this);
        setLlAnswers();
    }

    void startTimer(final long startMillis,int prog)
    {
        final int[] p = {prog};
        timer = new CountDownTimer(startMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = millisUntilFinished;
                p[0] +=1;
                progress = p[0];
                mProgress.setProgress(p[0]);
                tvTime.setText(String.valueOf(millisUntilFinished/1000));

                if(millisUntilFinished>15000)
                    tvTime.setTextColor(Color.parseColor("#ffffff"));
                else if(millisUntilFinished<=15000)
                {
                    if(millisUntilFinished<=5000)
                        tvTime.setTextColor(Color.parseColor("#FF0000"));
                    else
                        tvTime.setTextColor(Color.parseColor("#FFBB00"));
                }
            }

            @Override
            public void onFinish() {
                //timeRemaining = startMillis;
                cancelTimer();
                setBtNextFunction();
            }
        };
        timer.start();
    }

    void cancelTimer()
    {
        if(timer!=null){
            timer.cancel();
        }

    }

    public void setBtNextFunction() {
        int size = sparseArray.size();
        int correct=0, incorrect = 0;
        timeTaken += (30000-Long.parseLong(tvTime.getText().toString())+2);
        if(question_number < size)
        {
            setLlAnswers();
            if(question_number== size)
            {
                bbtNext.setText("Finish");
            }
        }
        else
        {
            for(int i=0;i<size;i++) {
                PerQuestionItems pQ = sparseArray.get(i);
                SparseArray<PerAnswerDetails> choicesArray = pQ.getChoicesArray();
                int temp_incorrect = incorrect;
                boolean wasAnswad = false,shouldLoop=true;

                int shouldBeCorrect = 0,werePickedAsCorrect=0;

                for (int j = 0; j < choicesArray.size(); j++) {
                    PerAnswerDetails details = choicesArray.get(j);
                    if(details.isPickedAsAnswer()) {
                        wasAnswad = true;
                        if (!details.getIsAnswer().equals("true")) {
                            incorrect++;
                            shouldLoop = false;
                            break;
                        }
                    }
                }

                if(pQ.getQuestionType().equals("checkboxed") && shouldLoop && wasAnswad)
                {
                    for (int j = 0; j < choicesArray.size(); j++) {
                        PerAnswerDetails details = choicesArray.get(j);

                        if(details.isPickedAsAnswer())
                        {
                            if(details.getIsAnswer().equals("true"))
                            {
                                werePickedAsCorrect++;
                                shouldBeCorrect++;
                            }
                        }
                        else
                        {
                            if(details.getIsAnswer().equals("true"))
                                shouldBeCorrect++;
                        }
                    }

                    if(shouldBeCorrect!=werePickedAsCorrect)
                        incorrect++;

                }

                if(wasAnswad)
                    pQ.setWasAnswered(true);
                else
                    incorrect++;



                if (temp_incorrect == incorrect)
                {
                    correct++;
                    pQ.setIsCorrectQuestion(true);
                }

            }
            cancelTimer();
            startActivity(new Intent(Questions.this,Results.class)
                    .putExtra("Level",level)
                    .putExtra("Book",book)
                    .putExtra("AllQuestions",totalQuestions)
                    .putExtra("Correct",String.valueOf(correct))
                    .putExtra("Incorrect",String.valueOf(incorrect))
                    .putExtra("TimeTaken",String.valueOf(timeTaken))
            );
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btPause:
                if(paused==0)
                {
                    cancelTimer();
                    mProgress.setProgress(progress-2);   // Main Progress
                    bbtPause.setText("Resume");
                    paused = 1;
                }
                else if(paused==1)
                {
                    startTimer(timeRemaining,progress-2);
                    btPause.setVisibility(View.GONE);
                }

                break;
            case R.id.btNext:
                setBtNextFunction();
                break;

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(v.getId()) {
            case R.id.btPause:
            case R.id.btNext:
               /* switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.parseColor("#00B050"));
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_MOVE:
                        v.setBackgroundResource(R.drawable.buttons_bg);
                        break;
                }*/
                break;
            case R.id.rdAns1:
            case R.id.rdAns2:
            case R.id.rdAns3:
            case R.id.rdAns4:
            case R.id.rdAns5:
            case R.id.cbAns1:
            case R.id.cbAns2:
            case R.id.cbAns3:
            case R.id.cbAns4:
            case R.id.cbAns5:
                /*switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.parseColor("#00B050"));
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_MOVE:
                        v.setBackgroundColor(Color.parseColor("#0000B050"));
                        break;
                }*/
                break;

        }
        return false;
    }

    public void setLlAnswers(){
        PerQuestionItems question = sparseArray.get(question_number);
        if(question!=null)
        {
            btNext.setOnClickListener(this);
            if(question.getQuestionType().equals("checkboxed"))
            {
                setCheckBoxedQuestions(question);
            }
            else
            {
                setRadioButtonedQuestions(question);
            }
            tvQuestion.setText(question.getQuestion());
            question_number++;
            tvQuestionNumber.setText(String.valueOf(question_number));

            mProgress.setProgress(0);   // Main Progress
            mProgress.setSecondaryProgress(30); // Secondary Progress
            mProgress.setMax(30); // Maximum Progress
            mProgress.setProgressDrawable(drawable);
            cancelTimer();
            startTimer(31000,0);
        }
        else
        {
            tvQuestion.setText("No questions available");
            Toast.makeText(myApp, "No questions available", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Questions.this,MainActivity.class));
        }
    }


    public void setCheckBoxedQuestions(PerQuestionItems question)
    {
        llAnswers.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout allLLAns = (LinearLayout) inflater.inflate(R.layout.checkboxes_answers, llAnswers, false);
        
        AppCompatCheckBox cbAns1 = (AppCompatCheckBox) allLLAns.findViewById(R.id.cbAns1);
        AppCompatCheckBox cbAns2 = (AppCompatCheckBox) allLLAns.findViewById(R.id.cbAns2);
        AppCompatCheckBox cbAns3 = (AppCompatCheckBox) allLLAns.findViewById(R.id.cbAns3);
        AppCompatCheckBox cbAns4 = (AppCompatCheckBox) allLLAns.findViewById(R.id.cbAns4);
        AppCompatCheckBox cbAns5 = (AppCompatCheckBox) allLLAns.findViewById(R.id.cbAns5);

        SparseArray<PerAnswerDetails> choicesArray = question.getChoicesArray();
        final PerAnswerDetails pad1 = choicesArray.get(0);
        cbAns1.setText(pad1.getAnswer());

        final PerAnswerDetails pad2 = choicesArray.get(1);
        cbAns2.setText(pad2.getAnswer());

        final PerAnswerDetails pad3 = choicesArray.get(2);
        cbAns3.setText(pad3.getAnswer());

        final PerAnswerDetails pad4 = choicesArray.get(3);
        cbAns4.setText(pad4.getAnswer());

        final PerAnswerDetails pad5 = choicesArray.get(4);
        cbAns5.setText(pad5.getAnswer());

        cbAns1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pad1.setPickedAsAnswer(isChecked);
            }
        });

        cbAns2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pad2.setPickedAsAnswer(isChecked);
            }
        });

        cbAns3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pad3.setPickedAsAnswer(isChecked);
            }
        });

        cbAns4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pad4.setPickedAsAnswer(isChecked);
            }
        });

        cbAns5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pad5.setPickedAsAnswer(isChecked);
            }
        });

        cbAns1.setOnTouchListener(this);
        cbAns2.setOnTouchListener(this);
        cbAns3.setOnTouchListener(this);
        cbAns4.setOnTouchListener(this);
        cbAns5.setOnTouchListener(this);
        llAnswers.addView(allLLAns);
    }


    public void setRadioButtonedQuestions(PerQuestionItems question)
    {
        llAnswers.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(Questions.this);
        LinearLayout allLLAns = (LinearLayout)inflater.inflate(R.layout.radiobuttons_answers, llAnswers, false);
        RadioGroup rdGroup =  (RadioGroup)allLLAns.findViewById(R.id.rdGroup);

        AppCompatRadioButton rdAns1 = (AppCompatRadioButton)rdGroup.findViewById(R.id.rdAns1);
        AppCompatRadioButton rdAns2 = (AppCompatRadioButton)rdGroup.findViewById(R.id.rdAns2);
        AppCompatRadioButton rdAns3 = (AppCompatRadioButton)rdGroup.findViewById(R.id.rdAns3);
        AppCompatRadioButton rdAns4 = (AppCompatRadioButton)rdGroup.findViewById(R.id.rdAns4);
        AppCompatRadioButton rdAns5 = (AppCompatRadioButton)rdGroup.findViewById(R.id.rdAns5);
        
        SparseArray<PerAnswerDetails> choicesArray = question.getChoicesArray();
        final PerAnswerDetails pad1 = choicesArray.get(0);
        rdAns1.setText(pad1.getAnswer());

        final PerAnswerDetails pad2 = choicesArray.get(1);
        rdAns2.setText(pad2.getAnswer());

        final PerAnswerDetails pad3 = choicesArray.get(2);
        rdAns3.setText(pad3.getAnswer());

        final PerAnswerDetails pad4 = choicesArray.get(3);
        rdAns4.setText(pad4.getAnswer());

        final PerAnswerDetails pad5 = choicesArray.get(4);
        rdAns5.setText(pad5.getAnswer());

        rdAns1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pad1.setPickedAsAnswer(isChecked);
            }
        });

        rdAns2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pad2.setPickedAsAnswer(isChecked);
            }
        });

        rdAns3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pad3.setPickedAsAnswer(isChecked);
            }
        });

        rdAns4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pad4.setPickedAsAnswer(isChecked);
            }
        });

        rdAns5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pad5.setPickedAsAnswer(isChecked);
            }
        });

        rdAns1.setOnTouchListener(this);
        rdAns2.setOnTouchListener(this);
        rdAns3.setOnTouchListener(this);
        rdAns4.setOnTouchListener(this);
        rdAns5.setOnTouchListener(this);

        llAnswers.addView(allLLAns);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cancelTimer();
        startTimer(timeRemaining,progress-=1);
        if(bbtPause.getText().toString().equals("Resume"))
        {
            btPause.setVisibility(View.GONE);
        }

    }
}
