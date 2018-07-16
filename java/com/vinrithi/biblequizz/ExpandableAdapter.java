package com.vinrithi.biblequizz;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

public class ExpandableAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> expListHeaders;
    private LinkedHashMap<String, List<String>> expListItems;
    private DrawerLayout drawerLayout;
    ExpandableAdapter(Context context, LinkedHashMap<String, List<String>> expListItems,DrawerLayout layout){
        this.context = context;
        this.expListHeaders = new ArrayList<>(expListItems.keySet());
        this.expListItems = expListItems;
        this.drawerLayout = layout;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition){
        return this.expListItems.get(this.expListHeaders.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition){
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childValue = (String) getChild(listPosition, expandedListPosition);
        //int drawable_id = switchFunction(childValue);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.explist_items, null);
        }

        final TextView eltItem = (TextView) convertView.findViewById(R.id.listItems);
        eltItem.setText(childValue);
        eltItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (eltItem.getText().toString())
                {
                    case "Home":
                        if(!checkCurrentActivity("MainActivity"))
                        {
                            context.startActivity(new Intent(context,MainActivity.class));
                        }
                        break;
                    case "Results":
                        if(!checkCurrentActivity("AllResults"))
                        {
                            context.startActivity(new Intent(context,AllResults.class));
                        }
                        break;
                    case "Analysis":
                        if(!checkCurrentActivity("Analysis"))
                        {
                            context.startActivity(new Intent(context,Analysis.class));
                        }
                        break;
                    case "Feedback":
                        if(!checkCurrentActivity("Feedback"))
                        {
                            context.startActivity(new Intent(context,Feedback.class));
                        }
                        break;
                }
                drawerLayout.closeDrawers();
            }
        });

        eltItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundColor(Color.parseColor("#60628C"));
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        v.setBackgroundColor(Color.parseColor("#003999f9"));
                        break;
                }
                return false;
            }
        });
        //m_icon.setImageResource(drawable_id);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expListItems.get(this.expListHeaders.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expListHeaders.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expListHeaders.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.explist_group, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listHeader);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public boolean checkCurrentActivity(String activity_name)
    {
        ActivityManager am = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        String full_classname = "com.vinrithi.biblequizz."+ activity_name;
        return componentInfo.getClassName().equals(full_classname);
    }

}

