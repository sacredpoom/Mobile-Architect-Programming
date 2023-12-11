package com.zybooks.jveneski_eventtracker_project2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import androidx.annotation.NonNull;
import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.appbar.AppBarLayout;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

// JOSEPH VENESKI 12/10/2023
// PROJECT THREE -- EVENT TRACKING APP
// main controller for app
// implements recycler view for displaying events in grid layout
// Core functionality will be implemented in Project 3
// Use booleans loggedIn, and permiss checked with
// if-statements to navigate screens
//
// App should check if user is logged in, if not prompt login screen
// if notifications are not enabled, prompt with notification screen
// display events on main screen, long-presses will prompt edits to existing events
// use of FAB will allow users to create new events
// some button functionality works (FAB, confirm_event buttons)

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;

    // booleans for testing
    private static boolean loggedIn = false;
    private boolean permiss, ignoreNotif = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // use shared preferences for booleans
        SharedPreferences preferences = getSharedPreferences("EventusPrefs", MODE_PRIVATE);
        loggedIn = preferences.getBoolean("loggedIn", false);

        // switch this flag to see login screen
        if (!loggedIn) {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            return;
        }

        // checks if SMS permissions granted, if not display screen
        // modify boolean to skip screen until button functionality implemented
        permiss = checkPermission();
        ignoreNotif = preferences.getBoolean("ignoreNotif", false);
        if (!permiss && !ignoreNotif) {
            Intent notificationIntent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(notificationIntent);
        }

        // Initialize the AppBarLayout and Toolbar
        AppBarLayout appBarLayout = findViewById(R.id.topBarLayout);
        Toolbar toolbar = findViewById(R.id.topAppBar);

        // Don't show the title in the top bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // utilize brand icon at top of screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.smallawesoemfacecrop);

        // Initialize the RecyclerView and set a GridLayout
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Initialize the list of events
        eventList = new ArrayList<>();

        // temporary listings for testing
        /*for (int i = 1; i <19; i++) {
            eventList.add(new Event("Event" + (i) + "\nDescription\n11/23/2023 12:00:00"));
        }*/

        // RETRIEVE list of stored events from database
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        eventList = databaseHelper.populateEventArray();

        // Initialize the adapter
        eventAdapter = new EventAdapter(this, eventList);

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(eventAdapter);

        // Set the long-click listener for the adapter
        eventAdapter.setOnEventLongClickListener(new EventAdapter.OnEventLongClickListener() {
            @Override
            public void onEventLongClick(int position) {
                //Intent intent = new Intent(MainActivity.this, EditEventActivity.class);

                Event longClickedEvent = eventList.get(position);

                Intent editEventIntent = new Intent(MainActivity.this, EditEventActivity.class);
                editEventIntent.putExtra("NEW_EVENT", false);
                editEventIntent.putExtra("EVENT_ID", longClickedEvent.getId());

                startActivity(editEventIntent);
                // Log event long-press for testing -- will eventually transition screens
                Log.d("MainActivity", "Event long-pressed at position: " + position);
            }
        });

        // initialize floating action button
        FloatingActionButton fab = findViewById(R.id.newEventFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to edit event screen on click
                Intent newEventIntent = new Intent(MainActivity.this, EditEventActivity.class);
                newEventIntent.putExtra("NEW_EVENT", true);
                startActivity(newEventIntent);
            }
        });
    }

    // check for sms permission
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        boolean permiss = result == PackageManager.PERMISSION_GRANTED;

        //store permiss status
        SharedPreferences preferences = this.getSharedPreferences("EventusPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("permiss", permiss);
        editor.apply();
        return permiss;
    }

    // method to update loggedIn boolean
    public static void setLoggedIn(Context context, boolean loggedIn) {
        MainActivity.loggedIn = loggedIn;

        // update in shared preferences
        SharedPreferences preferences = context.getSharedPreferences("EventusPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("loggedIn", loggedIn);
        editor.apply();
    }

    // handles options menu for logging out
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // set the font to black text to make it readable
        MenuItem item = menu.findItem(R.id.action_logout);
        SpannableString s = new SpannableString(item.getTitle());
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
        item.setTitle(s);

        return true;
    }

    // logs user out
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_logout) {// Handle logout
            // Update loggedIn status to false and navigate to the login screen
            MainActivity.setLoggedIn(this, false);
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}