package com.vinrithi.biblequizz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ChooseLevel extends ToolbarSet implements View.OnClickListener,View.OnTouchListener,CompoundButton.OnCheckedChangeListener, BibleQuizController{
    String book="";
    RadioGroup rdGroup;
    RadioButton rdEasy,rdMedium,rdHard;
    EditText etNumberofQuestions;
    Button btStart;
    ProgressDialog dialog;
    String level = "Easy";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_level);

        m_toolbar = (Toolbar)findViewById(R.id.include_toolbar);
        setToolbar();

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            book = bundle.getString("Book");
        }

        rdGroup = (RadioGroup)findViewById(R.id.rdGroup);
        rdEasy = (RadioButton)findViewById(R.id.rdEasy);
        rdMedium = (RadioButton)findViewById(R.id.rdMedium);
        rdHard = (RadioButton)findViewById(R.id.rdHard);
        etNumberofQuestions = (EditText) findViewById(R.id.etNumberofQuestions);
        btStart = (Button)findViewById(R.id.btStart);
        btStart.setOnClickListener(this);
        btStart.setOnTouchListener(this);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Retrieving questions");

        rdEasy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    level = buttonView.getText().toString();
                    Toast.makeText(ChooseLevel.this, level, Toast.LENGTH_SHORT).show();
                }
            }
        });
        rdMedium.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    level = buttonView.getText().toString();
                    Toast.makeText(ChooseLevel.this, level, Toast.LENGTH_SHORT).show();
                }
            }
        });
        rdHard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    level = buttonView.getText().toString();
                    Toast.makeText(ChooseLevel.this, level, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btStart:
                String number_of_quiz = etNumberofQuestions.getText().toString();
                if(number_of_quiz.equals(""))
                {
                    Toast.makeText(this, "Please enter number of questions to answer", Toast.LENGTH_SHORT).show();
                    return;
                }
                int no = Integer.parseInt(number_of_quiz);
                if(no < 15 || no > 30)
                {
                    Toast.makeText(this, "Please enter number of questions between 15 and 30", Toast.LENGTH_SHORT).show();
                    return;
                }


                JSONObject postData = new JSONObject();
                try {
                    postData.put("book", book);
                    postData.put("level", level);
                    postData.put("numberOfQuestions", number_of_quiz);

                    String url = "http://192.168.137.1/quiz4bible/questions_answers/questions.php";
                    dialog.show();
                    new ConnectToServer(ChooseLevel.this,"retrieveQuestions").execute(url,postData.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(v.getId()) {
            case R.id.btStart:
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        buttonView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }


    @Override
    public ProgressDialog getProgressDialog() {
        return dialog;
    }

    @Override
    public void resetPreferences() {

    }

    @Override
    public void retrieveData(String result) throws JSONException {
        if(result!=null)
        {
         
            Gson gson = new Gson();
            String[] jsonArray = gson.fromJson(result,String[].class);
            int len = jsonArray.length/7;
            int pos0=0,pos1=1,pos2=2,pos3=3,pos4=4,pos5=5,pos6=6;
            SparseArray<PerQuestionItems> allQuestionsSparseArray = new SparseArray<>();

            MyApp myApp = MyApp.getSingleInstance();

            for(int i=0;i<len;i++)
            {
                String question = jsonArray[pos0];
                String questionType = jsonArray[pos6];
                List<Integer>positions = new ArrayList<>();
                positions.add(pos1);
                positions.add(pos2);
                positions.add(pos3);
                positions.add(pos4);
                positions.add(pos5);

                Collections.shuffle(positions);
                String options[] = {jsonArray[positions.get(0)],jsonArray[positions.get(1)],
                        jsonArray[positions.get(2)],jsonArray[positions.get(3)],jsonArray[positions.get(4)]};

                SparseArray<PerAnswerDetails> choicesArray = new SparseArray<>();
                int j=0;
                for (String option : options) {
                    JSONObject object = new JSONObject(option);
                    PerAnswerDetails perAnswerDetails = new PerAnswerDetails(
                            object.getString("Answer"),
                            object.getString("IsAnswer"),
                            object.getString("Description"),
                            object.getString("Verse"),
                            object.getString("Text"),
                            object.getString("Chapter"));
                    choicesArray.put(j++, perAnswerDetails);
                }

                PerQuestionItems perQuestionItems = new PerQuestionItems(question,questionType,choicesArray);
                allQuestionsSparseArray.put(i,perQuestionItems);

                pos0+=7;     pos1+=7;   pos2+=7;    pos3+=7;    pos4+=7;    pos5+=7;    pos6+=7;
            }
            
            myApp.setAllQuestionsSparseArray(allQuestionsSparseArray);

            dialog.dismiss();
            startActivity(new Intent(this,Questions.class).putExtra("Book",book)
                    .putExtra("AllQuestions",etNumberofQuestions.getText().toString())
                    .putExtra("Level",level));
        }
        else
        {
            Toast.makeText(this, "Could not complete retrieving the questions", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public SwipeRefreshLayout getRefresher() {
        return null;
    }


}
