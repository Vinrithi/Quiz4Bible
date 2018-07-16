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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by vinri on 1/3/2018.
 */

public class MenuExpandableAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> expListHeaders;
    private LinkedHashMap<String, List<String>> expListItems;

    MenuExpandableAdapter(Context context, LinkedHashMap<String, List<String>> expListItems){
        this.context = context;
        this.expListHeaders = new ArrayList<>(expListItems.keySet());
        this.expListItems = expListItems;
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
            convertView = layoutInflater.inflate(R.layout.menu_explist_items, null);
        }

        final TextView eltItem = (TextView) convertView.findViewById(R.id.listItems);
        final RelativeLayout rlItem = (RelativeLayout) convertView.findViewById(R.id.rlMenuItems);
        eltItem.setText(childValue);

        parent.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);


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
            convertView = layoutInflater.inflate(R.layout.menu_explist_group, null);
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

}
