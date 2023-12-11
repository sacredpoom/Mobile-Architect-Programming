package com.zybooks.jveneski_eventtracker_project2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class InsertEvent extends AsyncTask<String, Void, Long> {
    private WeakReference<Context> contextRef;
    private String title;
    private String desc;
    private String time;
    private String date;

    public InsertEvent(Context context, String title, String desc, String time, String date) {
        this.contextRef = new WeakReference<>(context);
        this.title = title;
        this.desc = desc;
        this.time = time;
        this.date = date;
    }

    @Override
    protected Long doInBackground(String... params) {
        Context context = contextRef.get();
        if (context == null) {
            return -1L;
        }

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        return databaseHelper.insertEvent(title, desc, time, date);
    }

    @Override
    protected void onPostExecute(Long id) {
        Context context = contextRef.get();
        if (context == null) {
            return;
        }

        if (id != -1) {
            // Event insertion successful
            SharedPreferences preferences = context.getSharedPreferences("EventusPrefs", Context.MODE_PRIVATE);
            boolean permiss = preferences.getBoolean("permiss", false);
            if (permiss) {
                NotificationHelper.setSMSNotification(context, id, date);
            }

            boolean editEvent = true; // we want to go back to the home screen
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("EVENT_CONFIRMED", editEvent);
            context.startActivity(intent);

            ((Activity) context).finish();
        } else {
            // Event insertion failed
            Toast.makeText(context, R.string.failConfirmEvent, Toast.LENGTH_SHORT).show();
        }
    }
}