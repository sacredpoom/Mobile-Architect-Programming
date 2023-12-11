package com.zybooks.jveneski_eventtracker_project2;

public class Event {
    private String KEY_ID;
    private String KEY_EVENT_TITLE;
    private String KEY_EVENT_DESCRIPTION;
    private String KEY_EVENT_TIME;
    private String KEY_EVENT_DATE;

    public Event(String id, String title, String description, String time, String date) {
        KEY_ID = id;
        KEY_EVENT_TITLE = title;
        KEY_EVENT_DESCRIPTION = description;
        KEY_EVENT_TIME = time;
        KEY_EVENT_DATE = date;
    }

    public String getId() { return KEY_ID; }
    public String getTitle() {
        return KEY_EVENT_TITLE;
    }
    public String getDescription() { return KEY_EVENT_DESCRIPTION; }
    public String getTime() { return KEY_EVENT_TIME; }
    public String getDate() { return KEY_EVENT_DATE; }
}