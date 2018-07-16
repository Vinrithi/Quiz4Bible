package com.vinrithi.biblequizz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SignUp extends ToolbarSet implements View.OnClickListener,View.OnTouchListener,BibleQuizController {

    EditText etUsername,etPassword,etYearOfBirth;
    Button btSignUp;
    Spinner spGender;
    String gender = "";
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        m_toolbar = (Toolbar)findViewById(R.id.include_toolbar);
        btSignUp = (Button) findViewById(R.id.btSignUp);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etYearOfBirth = (EditText) findViewById(R.id.etYearOfBirth);
        spGender = (Spinner)findViewById(R.id.spGender);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Creating Account");

        setToolbar();
        setGenderSpinner();

        btSignUp.setOnClickListener(this);
        btSignUp.setOnTouchListener(this);
    }

    public void setGenderSpinner() {
        List<String> genderList = new ArrayList<>();
        genderList.add("Male");
        genderList.add("Female");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, genderList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        // dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(new CustomSpinnerAdapter(dataAdapter, R.layout.spinner_show_nothing_selected, this));
        //spCounty.setSelection(1,false);
        spGender.setPrompt("Gender Selection");
        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0 && position !=-1) {
                    gender = (String) parent.getSelectedItem();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public boolean isValidYear(String year) {
        String regEx = "^[12][0-9]{3}";

        Calendar calendar = Calendar.getInstance();
        final int currYear = calendar.get(Calendar.YEAR);

        if(!year.matches(regEx))
        {
            Toast.makeText(this, "Year must be four digits", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(Integer.parseInt(year)>currYear)
        {
            Toast.makeText(this, "Future years not allowed", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btSignUp:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String yearofbirth = etYearOfBirth.getText().toString();

                if(username.equals("") ||password.equals("") || gender.equals(""))
                {
                    Toast.makeText(this, "Please fill in all the details", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isValidYear(yearofbirth))return;

                JSONObject postData = new JSONObject();
                try {
                    postData.put("username", username);
                    postData.put("password", password);
                    postData.put("gender", gender);
                    postData.put("yearofbirth", yearofbirth);

                    dialog.show();
                    String url = "http://192.168.137.1/quiz4bible/login_signup/signup.php";
                    new ConnectToServer(SignUp.this,"signup").execute(url,postData.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(v.getId()) {
            case R.id.btSignUp:
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
    public ProgressDialog getProgressDialog() {
        return dialog;
    }

    @Override
    public void resetPreferences() {

    }

    @Override
    public void retrieveData(String result) throws JSONException {
        if(result!=null) {
            JSONObject object = new JSONObject(result);
            String message    = object.getString("message");
            int msgId         = object.getInt("msgId");

            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            if(msgId==1)
            {
                startActivity(new Intent(this,Login.class));
            }
        }
        else
        {
            Toast.makeText(this, "Account creation could not be completed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public SwipeRefreshLayout getRefresher() {
        return null;
    }
}
