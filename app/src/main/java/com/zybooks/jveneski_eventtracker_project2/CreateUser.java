package com.zybooks.jveneski_eventtracker_project2;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class CreateUser extends AsyncTask<String, Void, Boolean> {
    private WeakReference<Context> contextRef;

    public CreateUser(Context context) {
        this.contextRef = new WeakReference<>(context);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        Context context = contextRef.get();
        if (context == null) {
            return false;
        }

        String username = params[0];
        String password = params[1];

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);

        // Insert new user
        return databaseHelper.insertUser(username, password);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        Context context = contextRef.get();
        if (context == null) {
            return;
        }

        if (success) {
            Toast.makeText(context, "User created successfully!", Toast.LENGTH_SHORT).show();
            MainActivity.setLoggedIn(context, true);
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.putExtra("loggedIn", true);
            context.startActivity(mainIntent);
            ((Activity) context).finish();
        } else {
            Toast.makeText(context, "Failed to create user.", Toast.LENGTH_SHORT).show();
        }
    }
}