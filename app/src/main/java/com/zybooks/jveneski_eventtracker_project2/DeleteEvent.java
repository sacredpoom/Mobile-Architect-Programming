package com.zybooks.jveneski_eventtracker_project2;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public class DeleteEvent extends AsyncTask<String, Void, Boolean> {
    private WeakReference<Activity> activityRef;

    public DeleteEvent(Activity activity) {
        this.activityRef = new WeakReference<>(activity);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        Activity activity = activityRef.get();
        if (activity == null) {
            return false;
        }

        String eventId = params[0];
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(activity);
        return databaseHelper.deleteEvent(eventId);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        Activity activity = activityRef.get();
        if (activity == null) {
            return;
        }

        if (success) {
            // Deletion successful
            boolean editEvent = true; // we want to go back to the home screen
            Intent intent = new Intent(activity, MainActivity.class);
            intent.putExtra("EVENT_CONFIRMED", editEvent);
            activity.startActivity(intent);
            activity.finish();
        } else {

        }
    }
}