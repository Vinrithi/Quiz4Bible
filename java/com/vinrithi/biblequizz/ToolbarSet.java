package com.vinrithi.biblequizz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by vinri on 1/5/2018.
 */

public class ToolbarSet extends AppCompatActivity {
    Toolbar m_toolbar;
    SharedPreferences mPreferences;
    TextView tvToolbarTitle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = getSharedPreferences(PreferenceConstants.PREFERENCES, 0);
    }

    public void setToolbar()
    {
        setSupportActionBar(m_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //getSupportActionBar().setTitle("Quiz4Bible");
        getSupportActionBar().setHomeButtonEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Action Settings selected", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        // Activate the navigation drawer toggle
        return super.onOptionsItemSelected(item);
    }
}
