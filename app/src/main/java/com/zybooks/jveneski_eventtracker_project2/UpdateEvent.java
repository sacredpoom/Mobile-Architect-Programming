package com.zybooks.jveneski_eventtracker_project2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class UpdateEvent extends AsyncTask<String, Void, Boolean> {
    private WeakReference<Context> contextRef;
    private String id;
    private String title;
    private String desc;
    private String time;
    private String date;

    public UpdateEvent(Context context, String id, String title, String desc, String time, String date) {
        this.contextRef = new WeakReference<>(context);
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.time = time;
        this.date = date;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        Context context = contextRef.get();
        if (context == null) {
            return false;
        }

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        return databaseHelper.updateEvent(id, title, desc, time, date);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        Context context = contextRef.get();
        if (context == null) {
            return;
        }

        if (success) {
            // Event update successful
            boolean editEvent = true; // we want to go back to the home screen
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("EVENT_CONFIRMED", editEvent);
            context.startActivity(intent);

            ((Activity) context).finish();
        } else {
            // Event update failed
            Toast.makeText(context, R.string.failConfirmEvent, Toast.LENGTH_SHORT).show();
        }
    }
}