package com.vinrithi.biblequizz;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Feedback extends ToolNavSet implements BibleQuizController {
    String username;
    EditText etFeedback;
    Button btSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        etFeedback = (EditText)findViewById(R.id.etFeedback);
        btSend = (Button)findViewById(R.id.btSend);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        m_toolbar = (Toolbar)findViewById(R.id.include_toolbar); tvToolbarTitle = (TextView)m_toolbar.findViewById(R.id.tvToolbarTitle);
        tvToolbarTitle.setText("Feedback");
        expandableListView = (ExpandableListView) findViewById(R.id.expandable_listview);
        setToolbar();
        setupDrawer();
        populateNavDrawer();
        username = mPreferences.getString(PreferenceConstants.USERNAME,"");

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btSend.length()==0)
                {
                    Toast.makeText(Feedback.this, "Please enter feedback first", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject postData = new JSONObject();
                try {
                    postData.put("feedback", etFeedback.getText());
                    postData.put("sender", username);

                    String url = "http://192.168.137.1/quiz4bible/feedback/feedback.php";
                    //dialog.show();
                    new ConnectToServer(Feedback.this,"sendfeedback").execute(url,postData.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public SwipeRefreshLayout getRefresher() {
        return null;
    }
}
