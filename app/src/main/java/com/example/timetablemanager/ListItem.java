package com.example.timetablemanager;

public class ListItem {
    private String mName;
    private String mTime;

    public ListItem(String Name, String Time) {
        mName = Name;
        mTime = Time;
    }

    public void changeItem(String Name, String Time) {
        mName = Name;
        mTime = Time;
    }

    public String getName() {
        return mName;
    }

    public String getTime() {
        return mTime;
    }
}
