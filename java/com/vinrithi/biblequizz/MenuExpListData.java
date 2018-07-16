package com.vinrithi.biblequizz;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by vinri on 1/3/2018.
 */

public class MenuExpListData {
    public synchronized static LinkedHashMap<String, List<String>> setExpListData()
    {
        LinkedHashMap<String, List<String>>  allLists = new LinkedHashMap<String, List<String>>();
        List<String> listItems1 = new ArrayList<String>();
        listItems1.add("Genesis");
        listItems1.add("Exodus");
        listItems1.add("Leviticus");
        listItems1.add("Numbers");
        listItems1.add("Deuteronomy");
        listItems1.add("Joshua");
        listItems1.add("Judges");
        listItems1.add("Ruth");
        listItems1.add("1 Samuel");
        listItems1.add("2 Samuel");
        listItems1.add("1 Kings");
        listItems1.add("2 Kings");
        listItems1.add("1 Chronicles");
        listItems1.add("2 Chronicles");
        listItems1.add("Ezra");
        listItems1.add("Nehemiah");
        listItems1.add("Esther");
        listItems1.add("Job");
        listItems1.add("Psalms");
        listItems1.add("Proverbs");
        listItems1.add("Ecclesiastes");
        listItems1.add("Song of Solomon");
        listItems1.add("Isaiah");
        listItems1.add("Jeremiah");
        listItems1.add("Lamentations");
        listItems1.add("Ezekiel");
        listItems1.add("Daniel");
        listItems1.add("Hosea");
        listItems1.add("Joel");
        listItems1.add("Amos");
        listItems1.add("Obadiah");
        listItems1.add("Jonah");
        listItems1.add("Micah");
        listItems1.add("Nahum");
        listItems1.add("Habbakkuk");
        listItems1.add("Zephaniah");
        listItems1.add("Haggai");
        listItems1.add("Zachariah");
        listItems1.add("Malachi");

        List<String> listItems2 = new ArrayList<String>();
        listItems2.add("Matthew");
        listItems2.add("Mark");
        listItems2.add("Luke");
        listItems2.add("John");
        listItems2.add("Acts");
        listItems2.add("Romans");
        listItems2.add("1 Corinthians");
        listItems2.add("2 Corinthians");
        listItems2.add("Galatians");
        listItems2.add("Ephesians");
        listItems2.add("Phillipians");
        listItems2.add("Colossians");
        listItems2.add("1 Thessalonians");
        listItems2.add("2 Thessalonians");
        listItems2.add("1 Timothy");
        listItems2.add("2 Timothy");
        listItems2.add("Titus");
        listItems2.add("Philemon");
        listItems2.add("Hebrews");
        listItems2.add("James");
        listItems2.add("1 Peter");
        listItems2.add("2 Peter");
        listItems2.add("1 John");
        listItems2.add("2 John");
        listItems2.add("3 John");
        listItems2.add("Jude");
        listItems2.add("Revelation");

        List<String> listItems3 = new ArrayList<String>();
        listItems3.add("Random selection");
        listItems3.add("General questions");

        allLists.put("Old Testament",listItems1);
        allLists.put("New Testament",listItems2);
        allLists.put("Miscellaneous",listItems3);
        return allLists;
    }
}
