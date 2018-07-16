package com.vinrithi.biblequizz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends ToolbarSet implements View.OnClickListener,View.OnTouchListener, BibleQuizController {
    EditText etUsername, etPassword;
    Button btLogin,btSignUp;
    SwitchCompat swSkipLogin;
    ProgressDialog dialog;
    boolean skiplogin = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //m_toolbar = (Toolbar)findViewById(R.id.include_toolbar);
        //setToolbar();

        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btLogin = (Button) findViewById(R.id.btLogin);
        btSignUp = (Button) findViewById(R.id.btSignUp);
        swSkipLogin = (SwitchCompat)findViewById(R.id.swSkipLogin);

        etUsername.setText(mPreferences.getString(PreferenceConstants.USERNAME,""));

        swSkipLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                skiplogin = isChecked;
            }
        });

        btLogin.setOnClickListener(this);
        btLogin.setOnTouchListener(this);
        btSignUp.setOnClickListener(this);
        btSignUp.setOnTouchListener(this);


        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Authenticating");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btLogin:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if(username.equals("")|| password.equals(""))
                {
                    Toast.makeText(this, "Please enter all credentials", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject postData = new JSONObject();
                try {
                    postData.put("username", username);
                    postData.put("password", password);
                    dialog.show();
                    String url = "http://192.168.137.1/quiz4bible/login_signup/login.php";
                    new ConnectToServer(Login.this,"doLogin").execute(url,postData.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btSignUp:
                startActivity(new Intent(Login.this,SignUp.class));
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(v.getId()) {
            case R.id.btLogin:
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

            if(object.getInt("login_success")==0) {
                Toast.makeText(this, "Your credentials are wrong", Toast.LENGTH_SHORT).show();
            }
            else
            {
                mPreferences.edit().putBoolean(PreferenceConstants.SKIP_LOGIN, skiplogin).apply();
                mPreferences.edit().putString(PreferenceConstants.USERNAME, etUsername.getText().toString()).apply();
                startActivity(new Intent(this,MainActivity.class));
            }
        }
        else
        {
            Toast.makeText(this, "Login process could not be completed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public SwipeRefreshLayout getRefresher() {
        return null;
    }
}
