package com.vinrithi.biblequizz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Splashscreen extends AppCompatActivity {
    private Class classToOpen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splashscreen);

        SharedPreferences mPreferences = getSharedPreferences(PreferenceConstants.PREFERENCES, 0);
        boolean skip_login= mPreferences.getBoolean(PreferenceConstants.SKIP_LOGIN, false);
        classToOpen = skip_login? MainActivity.class : Login.class;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splashscreen.this, classToOpen);
                startActivity(i);
                finish();
            }
        }, 2500);
    }
}
