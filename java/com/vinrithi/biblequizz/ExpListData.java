package com.vinrithi.biblequizz;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by vinri on 1/3/2018.
 */

public class ExpListData
{
    public synchronized static LinkedHashMap<String, List<String>> setExpListData()
    {
        LinkedHashMap<String, List<String>>  allLists = new LinkedHashMap<String, List<String>>();
        List<String> listItems1 = new ArrayList<String>();
        listItems1.add("Home");
        listItems1.add("Results");
        listItems1.add("Analysis");
        listItems1.add("Feedback");

        List<String> listItems2 = new ArrayList<String>();
        listItems2.add("App Version");

        allLists.put("Menu",listItems1);
        allLists.put("About quiz4bible",listItems2);
        return allLists;
    }
}