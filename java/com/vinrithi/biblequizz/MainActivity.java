package com.vinrithi.biblequizz;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends ToolNavSet {
    ExpandableListView menuExpandableListView;
    ExpandableListAdapter menuExpandableListAdapter;
    List<String> menuExpListHeaders;
    LinkedHashMap<String, List<String>> menuExpListItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        m_toolbar = (Toolbar)findViewById(R.id.include_toolbar);
        expandableListView = (ExpandableListView) findViewById(R.id.expandable_listview);
        menuExpandableListView = (ExpandableListView) findViewById(R.id.menuExpandableListview);

        setToolbar();
        setupDrawer();
        populateNavDrawer();
        populateMenuList();
    }

    void populateMenuList()
    {
        menuExpListItems = MenuExpListData.setExpListData();
        menuExpListHeaders = getMenuListHeaders();
        menuExpandableListAdapter = new MenuExpandableAdapter(this, menuExpListItems);
        menuExpandableListView.setAdapter(menuExpandableListAdapter);

        for(int n=0; n<menuExpListItems.size();n++)
            menuExpandableListView.expandGroup(n);

        menuExpandableListView.setGroupIndicator(null);
        menuExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View clickedView, int groupPosition, long rowId) {
                ImageView groupIndicator = (ImageView) clickedView.findViewById(R.id.img_indicator);
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition);
                    groupIndicator.setImageResource(R.drawable.closed_exp_listview);
                } else {
                    parent.expandGroup(groupPosition);
                    groupIndicator.setImageResource(R.drawable.open_exp_listview);
                }
                return true;
            }
        });

        menuExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String s  = menuExpListItems.get(menuExpListHeaders.get(groupPosition)).get(childPosition);
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,ChooseLevel.class).putExtra("Book",s));
                return false;
            }
        });

    }
    public synchronized List<String> getMenuListHeaders() {
        List<String> headers = new ArrayList<String>();
        for (String key : menuExpListItems.keySet()) {
            headers.add(key);
        }
        return headers;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit?")
                .setCancelable(true)
                .setPositiveButton(this.getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })
                .setNegativeButton(this.getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.setCancelable(true);
        alert.show();
    }
}
