package com.zybooks.jveneski_eventtracker_project2;

import android.content.Context;
import android.os.AsyncTask;

public class FindEvent extends AsyncTask<String, Void, Event> {
    private Context context;
    private OnTaskCompleted listener;

    public FindEvent(Context context, OnTaskCompleted listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected Event doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }
        String eventId = params[0];
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        return databaseHelper.findEvent(eventId);
    }

    @Override
    protected void onPostExecute(Event result) {
        if (listener != null) {
            listener.onTaskCompleted(result);
        }
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(Event result);
    }
}