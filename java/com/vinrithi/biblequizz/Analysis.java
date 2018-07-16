package com.vinrithi.biblequizz;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

public class Analysis extends ToolNavSet {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        m_toolbar = (Toolbar)findViewById(R.id.include_toolbar);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        setToolbar();
        setupDrawer();
        //populateNavDrawer();
    }

}
