package com.vinrithi.biblequizz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ToolNavSet extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expListHeaders;
    LinkedHashMap<String, List<String>> expListItems;
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
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, m_toolbar, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Quiz4Bible");
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
       // mDrawerToggle.setDrawerIndicatorEnabled(false);
    }

    public void setupDrawer() {

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    void populateNavDrawer()
    {
        expListItems = ExpListData.setExpListData();
        expListHeaders = getListHeaders();
        expandableListAdapter = new ExpandableAdapter(this, expListItems,mDrawerLayout);
        expandableListView.setAdapter(expandableListAdapter);

        for(int n=0; n<expListItems.size();n++)
            expandableListView.expandGroup(n);

        expandableListView.setGroupIndicator(null);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View clickedView, int groupPosition, long rowId) {
                ImageView groupIndicator = (ImageView) clickedView.findViewById(R.id.img_indicator);
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                    groupIndicator.setImageResource(R.drawable.open_exp_listview);
                } else {
                    parent.expandGroup(groupPosition);
                    groupIndicator.setImageResource(R.drawable.closed_exp_listview);
                }
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String s  = expListItems.get(expListHeaders.get(groupPosition)).get(childPosition);
                return false;
            }
        });

       /* expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);

                    String itemGotten = expListItems.get(expListHeaders.get(groupPosition)).get(childPosition);
                    return true;
                }
                return false;
            }
        });*/
    }

    public synchronized List<String> getListHeaders() {
        List<String> headers = new ArrayList<String>();
        for (String key : expListItems.keySet()) {
            headers.add(key);
        }
        return headers;
    }

}
