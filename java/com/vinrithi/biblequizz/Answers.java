package com.vinrithi.biblequizz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Answers extends ToolbarSet implements View.OnClickListener,View.OnTouchListener{
    MyApp myApp = MyApp.getSingleInstance();
    TextView tvCorrectOrIncorrect,tvQuestion,tvQuestionNumber,tvTotalQuestions;
    LinearLayout llAnswers,llDescription;
    LinearLayout btPrev,btNext;
    TextView bbbtNext;
    Bundle bundle;
    String book;
    int question_number = 0;
    SparseArray<PerQuestionItems> userAnswersArray;
    int arraysize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        m_toolbar = (Toolbar)findViewById(R.id.include_toolbar);
        tvToolbarTitle = (TextView)m_toolbar.findViewById(R.id.tvToolbarTitle);
        setToolbar();
        tvCorrectOrIncorrect = (TextView)findViewById(R.id.tvCorrectOrIncorrect);
        tvQuestion = (TextView)findViewById(R.id.tvQuestion);
        tvQuestionNumber = (TextView)findViewById(R.id.tvQuestionNumber);
        tvTotalQuestions = (TextView)findViewById(R.id.tvAllQuestions);

        llAnswers = (LinearLayout) findViewById(R.id.llAnswers);
        llDescription = (LinearLayout) findViewById(R.id.llDescription);
        btPrev = (LinearLayout) findViewById(R.id.btPrev);
        btNext = (LinearLayout) findViewById(R.id.btNext);
        bbbtNext = (TextView) findViewById(R.id.bbbtNext);
        btPrev.setOnTouchListener(this);
        btPrev.setOnClickListener(this);
        btNext.setOnTouchListener(this);
        btNext.setOnClickListener(this);

        bundle = getIntent().getExtras();
        book = bundle.getString("Book");
        tvToolbarTitle.setText(book);
        userAnswersArray = myApp.getAllQuestionsSparseArray();
        arraysize = userAnswersArray.size();
        tvTotalQuestions.setText(String.valueOf(arraysize));

        setLlAnswers();
    }


    public void btNextFunction() {
        
        btPrev.setVisibility(View.VISIBLE);
        question_number++;
        if(question_number<arraysize)
        {
            setLlAnswers();
            if(question_number==arraysize-1) {
                bbbtNext.setText("Home");
            }
        }
        else
        {
            btPrev.setVisibility(View.GONE);
            btNext.setVisibility(View.GONE);
            startActivity(new Intent(this,MainActivity.class));
        }

    }

    public void btPrevFunction() {
        bbbtNext.setText("Next");
        if(question_number >= 1)
        {
            question_number--;
            setLlAnswers();
            if(question_number==0)
                btPrev.setVisibility(View.GONE);
        }
    }


    public void setLlAnswers()
    {
        llAnswers.removeAllViews();
        llDescription.removeAllViews();

        PerQuestionItems question = userAnswersArray.get(question_number);
        if(question==null) return;
        
        tvQuestionNumber.setText(String.valueOf(question_number+1));
        tvQuestion.setText(question.getQuestion());

        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout allLLAns = (LinearLayout) inflater.inflate(R.layout.colored_answers, llAnswers, false);

        LinearLayout llAns1 = (LinearLayout)allLLAns.findViewById(R.id.llAns1);
        final TextView tvAns1 = (TextView)llAns1.findViewById(R.id.tvAnswer1);
        final ImageView imgAns1 = (ImageView) llAns1.findViewById(R.id.imgAnswer1);

        LinearLayout llAns2 = (LinearLayout)allLLAns.findViewById(R.id.llAns2);
        final TextView tvAns2 = (TextView)llAns2.findViewById(R.id.tvAnswer2);
        final ImageView imgAns2 = (ImageView) llAns2.findViewById(R.id.imgAnswer2);

        LinearLayout llAns3 = (LinearLayout)allLLAns.findViewById(R.id.llAns3);
        final TextView tvAns3 = (TextView)llAns3.findViewById(R.id.tvAnswer3);
        final ImageView imgAns3 = (ImageView) llAns3.findViewById(R.id.imgAnswer3);

        LinearLayout llAns4 = (LinearLayout)allLLAns.findViewById(R.id.llAns4);
        final TextView tvAns4 = (TextView)llAns4.findViewById(R.id.tvAnswer4);
        final ImageView imgAns4 = (ImageView) llAns4.findViewById(R.id.imgAnswer4);

        LinearLayout llAns5 = (LinearLayout)allLLAns.findViewById(R.id.llAns5);
        final TextView tvAns5 = (TextView)llAns5.findViewById(R.id.tvAnswer5);
        final ImageView imgAns5 = (ImageView) llAns5.findViewById(R.id.imgAnswer5);


        SparseArray<PerAnswerDetails> choicesArray = question.getChoicesArray();
        PerAnswerDetails pad1 = choicesArray.get(0);
        PerAnswerDetails pad2 = choicesArray.get(1);
        PerAnswerDetails pad3 = choicesArray.get(2);
        PerAnswerDetails pad4 = choicesArray.get(3);
        PerAnswerDetails pad5 = choicesArray.get(4);

        String[] details1 = {pad1.getDescription(),book,pad1.getChapter(),pad1.getVerse(),pad1.getText()};
        String[] details2 = {pad2.getDescription(),book,pad2.getChapter(),pad2.getVerse(),pad2.getText()};
        String[] details3 = {pad3.getDescription(),book,pad3.getChapter(),pad3.getVerse(),pad3.getText()};
        String[] details4 = {pad4.getDescription(),book,pad4.getChapter(),pad4.getVerse(),pad4.getText()};
        String[] details5 = {pad5.getDescription(),book,pad5.getChapter(),pad5.getVerse(),pad5.getText()};

        tvAns1.setText(pad1.getAnswer());
        tvAns2.setText(pad2.getAnswer());
        tvAns3.setText(pad3.getAnswer());
        tvAns4.setText(pad4.getAnswer());
        tvAns5.setText(pad5.getAnswer());

        if(pad1.isPickedAsAnswer())
        {
            if(pad1.getIsAnswer().equals("true"))
            {
                tvAns1.setTextColor(Color.parseColor("#00B050"));
                imgAns1.setVisibility(View.VISIBLE);
                imgAns1.setImageResource(R.drawable.correct_answer);
                setLlDescription(0, details1);
            }
            else
            {
                tvAns1.setTextColor(Color.parseColor("#FF0000"));
                imgAns1.setVisibility(View.VISIBLE);
                imgAns1.setImageResource(R.drawable.wrong_answer);
                setLlDescription(1, details1);
            }
        }
        else
        {
            if(pad1.getIsAnswer().equals("true"))
            {
                tvAns1.setTextColor(Color.parseColor("#3999f9"));
                imgAns1.setVisibility(View.VISIBLE);
                imgAns1.setImageResource(R.drawable.correct_answer);
                setLlDescription(2, details1);
            }
            else
            {
                tvAns1.setTextColor(Color.parseColor("#2e4066"));
            }
        }

        if(pad2.isPickedAsAnswer())
        {
            if(pad2.getIsAnswer().equals("true"))
            {
                tvAns2.setTextColor(Color.parseColor("#00B050"));
                imgAns2.setVisibility(View.VISIBLE);
                imgAns2.setImageResource(R.drawable.correct_answer);
                setLlDescription(0, details2);
            }
            else
            {
                tvAns2.setTextColor(Color.parseColor("#FF0000"));
                imgAns2.setVisibility(View.VISIBLE);
                imgAns2.setImageResource(R.drawable.wrong_answer);
                setLlDescription(1, details2);
            }
        }
        else
        {
            if(pad2.getIsAnswer().equals("true"))
            {
                tvAns2.setTextColor(Color.parseColor("#3999f9"));
                imgAns2.setVisibility(View.VISIBLE);
                imgAns2.setImageResource(R.drawable.correct_answer);
                setLlDescription(2, details2);
            }
            else
            {
                tvAns2.setTextColor(Color.parseColor("#2e4066"));
            }
        }


        if(pad3.isPickedAsAnswer())
        {
            if(pad3.getIsAnswer().equals("true"))
            {
                tvAns3.setTextColor(Color.parseColor("#00B050"));
                imgAns3.setVisibility(View.VISIBLE);
                imgAns3.setImageResource(R.drawable.correct_answer);
                setLlDescription(0, details3);
            }
            else
            {
                tvAns3.setTextColor(Color.parseColor("#FF0000"));
                imgAns3.setVisibility(View.VISIBLE);
                imgAns3.setImageResource(R.drawable.wrong_answer);
                setLlDescription(1, details3);
            }
        }
        else
        {
            if(pad3.getIsAnswer().equals("true"))
            {
                tvAns3.setTextColor(Color.parseColor("#3999f9"));
                imgAns3.setVisibility(View.VISIBLE);
                imgAns3.setImageResource(R.drawable.correct_answer);
                setLlDescription(2, details3);
            }
            else
            {
                tvAns3.setTextColor(Color.parseColor("#2e4066"));
            }
        }

        if(pad4.isPickedAsAnswer())
        {
            if(pad4.getIsAnswer().equals("true"))
            {
                tvAns4.setTextColor(Color.parseColor("#00B050"));
                imgAns4.setVisibility(View.VISIBLE);
                imgAns4.setImageResource(R.drawable.correct_answer);
                setLlDescription(0, details4);
            }
            else
            {
                tvAns4.setTextColor(Color.parseColor("#FF0000"));
                imgAns4.setVisibility(View.VISIBLE);
                imgAns4.setImageResource(R.drawable.wrong_answer);
                setLlDescription(1, details4);
            }
        }
        else
        {
            if(pad4.getIsAnswer().equals("true"))
            {
                tvAns4.setTextColor(Color.parseColor("#3999f9"));
                imgAns4.setVisibility(View.VISIBLE);
                imgAns4.setImageResource(R.drawable.correct_answer);
                setLlDescription(2, details4);
            }
            else
            {
                tvAns4.setTextColor(Color.parseColor("#2e4066"));
            }
        }


        if(pad5.isPickedAsAnswer())
        {
            if(pad5.getIsAnswer().equals("true"))
            {
                tvAns5.setTextColor(Color.parseColor("#00B050"));
                imgAns5.setVisibility(View.VISIBLE);
                imgAns5.setImageResource(R.drawable.correct_answer);
                setLlDescription(0, details5);
            }
            else
            {
                tvAns5.setTextColor(Color.parseColor("#FF0000"));
                imgAns5.setVisibility(View.VISIBLE);
                imgAns5.setImageResource(R.drawable.wrong_answer);
                setLlDescription(1, details5);
            }
        }
        else
        {
            if(pad5.getIsAnswer().equals("true"))
            {
                tvAns5.setTextColor(Color.parseColor("#3999f9"));
                imgAns5.setVisibility(View.VISIBLE);
                imgAns5.setImageResource(R.drawable.correct_answer);
                setLlDescription(2, details5);
            }
            else
            {
                tvAns5.setTextColor(Color.parseColor("#2e4066"));
            }
        }



        if(!question.wasAnswered())
        {
            tvCorrectOrIncorrect.setText("Not Answered");
            tvCorrectOrIncorrect.setTextColor(Color.parseColor("#FF0000"));
        }
        else
        {
            if(question.isCorrectQuestion())
            {
                tvCorrectOrIncorrect.setText("Correct");
                tvCorrectOrIncorrect.setTextColor(Color.parseColor("#00B050"));
            }
            else
            {
                tvCorrectOrIncorrect.setText("Incorrect");
                tvCorrectOrIncorrect.setTextColor(Color.parseColor("#FF0000"));
            }
        }

        llAnswers.addView(allLLAns);
    }


    public void setLlDescription(int color, String[] details)
    {
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout allLLAns = (LinearLayout) inflater.inflate(R.layout.textview_answer_description, llDescription, false);
        if(color==0)
            allLLAns.setBackgroundResource(R.drawable.tv_correct_answer);
        else if(color==1)
            allLLAns.setBackgroundResource(R.drawable.tv_wrong_answer);
        else
            allLLAns.setBackgroundResource(R.drawable.tv_correct_unpicked);

        TextView tvDescription = (TextView)allLLAns.findViewById(R.id.tvDescription);

        LinearLayout llIsVerse = (LinearLayout)allLLAns.findViewById(R.id.llIsVerse);
        TextView tvChapterVerse = (TextView)llIsVerse.findViewById(R.id.tvChapterVerse);
        TextView tvText = (TextView)llIsVerse.findViewById(R.id.tvText);

        for(String i:details)
            Toast.makeText(myApp, i, Toast.LENGTH_SHORT).show();

        if(details[0].equals("-"))
        {
            tvDescription.setVisibility(View.GONE);
            String verse = details[1] +" "+ details[2]+ ":" +details[3];
            tvChapterVerse.setText(verse);
            tvText.setText(details[4]);
        }
        else
        {
            llIsVerse.setVisibility(View.GONE);
            tvDescription.setText(details[0]);
        }

        llDescription.addView(allLLAns);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btNext:
                btNextFunction();
                break;
            case R.id.btPrev:
                btPrevFunction();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(v.getId()) {
            case R.id.btPrev:
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
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        btPrevFunction();
    }
}
