package com.zybooks.jveneski_eventtracker_project2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "eventus_database.db";
    private static final int DATABASE_VERSION = 1;

    // table names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_EVENTS = "events";

    // common column names between tables
    private static final String KEY_ID = "id";

    // User table columns
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    // Events table columns
    private static final String KEY_EVENT_TITLE = "event_title";
    private static final String KEY_EVENT_DESCRIPTION = "event_description";
    private static final String KEY_EVENT_TIME = "event_time";
    private static final String KEY_EVENT_DATE = "event_date";

    // singleton pattern for db helper
    private static DatabaseHelper instance;

    // Create db tables
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_USERNAME + " TEXT,"
            + KEY_PASSWORD + " TEXT"
            + ")";

    private static final String CREATE_TABLE_EVENTS = "CREATE TABLE " + TABLE_EVENTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_EVENT_TITLE + " TEXT,"
            + KEY_EVENT_DESCRIPTION + " TEXT,"
            + KEY_EVENT_TIME + " TEXT,"
            + KEY_EVENT_DATE + " TEXT"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Getters for table names and column names
    public static String getTableUsers() {
        return TABLE_USERS;
    }

    public static String getTableEvents() { return TABLE_EVENTS; }

    public static String getKeyId() {
        return KEY_ID;
    }

    public static String getKeyUsername() {
        return KEY_USERNAME;
    }

    public static String getKeyPassword() {
        return KEY_PASSWORD;
    }

    public static String getKeyEventTitle() {
        return KEY_EVENT_TITLE;
    }

    public static String getKeyEventDescription() {
        return KEY_EVENT_DESCRIPTION;
    }

    public static String getKeyEventTime() {
        return KEY_EVENT_TIME;
    }

    public static String getKeyEventDate() {
        return KEY_EVENT_DATE;
    }

    // check for existing instance of db
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create the tables
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_EVENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop old tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);

        onCreate(db);
    }

    // ADD user to USER table
    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD, password);
        db.insert(TABLE_USERS, null, values);
        db.close();
        return true;
    }

    // check for match
    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.getTableUsers() +
                " WHERE " + DatabaseHelper.getKeyUsername() + " = ? AND " +
                DatabaseHelper.getKeyPassword() + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        boolean result = cursor.moveToFirst();
        cursor.close();
        return result;
    }

    // ADD event to EVENTS table
    public long insertEvent(String eventTitle, String eventDesc, String eventTime, String eventDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_TITLE, eventTitle);
        values.put(KEY_EVENT_DESCRIPTION, eventDesc);
        values.put(KEY_EVENT_TIME, eventTime);
        values.put(KEY_EVENT_DATE, eventDate);
        long id = db.insert(TABLE_EVENTS, null, values);
        db.close();
        return id;
    }

    // RETRIEVE all events in table
    public List<Event> populateEventArray() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        List<Event> eventList = new ArrayList<>();

        try (Cursor result = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_EVENTS, null)) {
            if (result.getCount() != 0) {
                while (result.moveToNext()) {
                    long id = result.getInt(result.getColumnIndexOrThrow(KEY_ID));
                    String title = result.getString(result.getColumnIndexOrThrow(KEY_EVENT_TITLE));
                    String description = result.getString(result.getColumnIndexOrThrow(KEY_EVENT_DESCRIPTION));
                    String time = result.getString(result.getColumnIndexOrThrow(KEY_EVENT_TIME));
                    String date = result.getString(result.getColumnIndexOrThrow(KEY_EVENT_DATE));
                    Event event = new Event(String.valueOf(id), title, description, time, date);
                    eventList.add(event);
                }
            }
        }
        return eventList;
    }

    // UPDATE event information
    public boolean updateEvent(String id, String title, String desc, String time, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_EVENT_TITLE, title);
        values.put(KEY_EVENT_DESCRIPTION, desc);
        values.put(KEY_EVENT_TIME, time);
        values.put(KEY_EVENT_DATE, date);

        // Updating row
        int rowsAffected = db.update(TABLE_EVENTS, values, KEY_ID + " = ?", new String[]{id});

        db.close();
        return true;
    }

    // Find Event by ID
    public Event findEvent(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Event event = null;

        try (Cursor cursor = db.query(
                TABLE_EVENTS,
                null,
                KEY_ID + " = ?",
                new String[]{id},
                null,
                null,
                null
        )) {
            if (cursor != null && cursor.moveToFirst()) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EVENT_TITLE));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow((KEY_EVENT_DESCRIPTION)));
                String time = cursor.getString(cursor.getColumnIndexOrThrow((KEY_EVENT_TIME)));
                String date = cursor.getString(cursor.getColumnIndexOrThrow((KEY_EVENT_DATE)));

                event = new Event(id, title, desc, time, date);
            }
        }
        return event;
    }

    // DELETE Event entry
    public boolean deleteEvent(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try (Cursor cursor = db.query(
                TABLE_EVENTS,
                null,
                KEY_ID + " = ?",
                new String[]{id},
                null,
                null,
                null
        )) {
            if (cursor != null && cursor.moveToFirst()) {
                int deletedRows = db.delete(TABLE_EVENTS, KEY_ID + " = ?", new String[]{id});

                if (deletedRows > 0)
                    return true;
            }
        }
        return false;
    }
}
