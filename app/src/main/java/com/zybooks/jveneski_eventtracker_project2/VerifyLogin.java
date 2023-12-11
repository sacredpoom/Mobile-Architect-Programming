package com.zybooks.jveneski_eventtracker_project2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class VerifyLogin extends AsyncTask<String, Void, Boolean> {
    private WeakReference<Context> contextRef;

    public VerifyLogin(Context context) {
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

        // Verify login credentials
        return databaseHelper.checkUserCredentials(username, password);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        Context context = contextRef.get();
        if (context == null) {
            return;
        }

        if (success) {
            Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show();
            //update shared pref for logged in boolean
            MainActivity.setLoggedIn(context, true);
            // goto main
            Intent mainIntent = new Intent(context, MainActivity.class);
            context.startActivity(mainIntent);
            ((Activity) context).finish();
        } else {
            Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}