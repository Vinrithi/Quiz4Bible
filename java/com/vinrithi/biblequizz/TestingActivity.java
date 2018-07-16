package com.vinrithi.biblequizz;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class TestingActivity extends AppCompatActivity {
    String str[] = new String[]{"Vincent", "Murithi","Mithalie"};
    String json;
    Gson gson = new Gson();
    Button btClickMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        String s = gson.toJson(str);
        String m = gson.fromJson(s,String[].class)[0];
        //String m = str[0];

        Toast.makeText(TestingActivity.this,m, Toast.LENGTH_SHORT).show();
        btClickMe = (Button)findViewById(R.id.btClickMe);
        btClickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject object= new JSONObject();
                try {
                    object.put("json",str);
                    json = object.toString();

                    object = new JSONObject(json);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
