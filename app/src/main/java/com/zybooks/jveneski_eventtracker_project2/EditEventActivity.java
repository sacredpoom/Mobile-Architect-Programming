package com.zybooks.jveneski_eventtracker_project2;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import com.google.android.material.appbar.AppBarLayout;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

// Class for handling event edits/creation
// will implement functionality in Project 3

public class EditEventActivity extends AppCompatActivity implements FindEvent.OnTaskCompleted {
    private InsertEvent insertEvent;
    private UpdateEvent updateEvent;
    private EditText eventTitleInput;
    private EditText eventDescInput;
    private TextView eventDateTextView;
    private Button selectDateButton;
    private TextView eventTimeTextView;
    private Button selectTimeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event_activity);

        // set views for components
        eventTitleInput = findViewById(R.id.eventTitleInput);
        eventDescInput = findViewById(R.id.eventDescriptionInput);
        eventDateTextView = findViewById(R.id.eventDateTextView);
        selectDateButton = findViewById(R.id.selectDateButton);
        eventTimeTextView = findViewById(R.id.eventTimeTextView);
        selectTimeButton = findViewById(R.id.selectTimeButton);

        // Retrieve passed event details
        Intent intent = getIntent();
        boolean isNewEvent = intent.getBooleanExtra("NEW_EVENT", true);
        final String eventId;

        if (!isNewEvent) { // if user is editing an existing event
            eventId = intent.getStringExtra("EVENT_ID");
            // asynch task to run db query in background
            new FindEvent(this, this).execute(eventId);

        } else {
            eventId = null; // set to null to avoid compiler errors later
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

        // handle confirm button for either new event or edit event
        Button confirmEventButton = findViewById(R.id.confirmEventButton);
        confirmEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNewEvent) {
                    confirmEvent();
                } else {
                    confirmEditedEvent(eventId);
                }
            }
        });

        // handle delete button
        Button deleteEventButton = findViewById(R.id.deleteEventButton);
        deleteEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNewEvent) {
                    deleteEvent(eventId, EditEventActivity.this);
                }
            }
        });
    }
    @Override
    public void onTaskCompleted(Event result) {
        if (result != null) {
            // Populate UI with event details
            eventTitleInput.setText(result.getTitle());
            eventDescInput.setText(result.getDescription());
            eventDateTextView.setText(result.getTime());
            eventTimeTextView.setText(result.getDate());
        }
    }
    // confirm event and go to home
    private void confirmEvent() {
        String title = eventTitleInput.getText().toString();
        String desc = eventDescInput.getText().toString();
        String time = eventTimeTextView.getText().toString();
        String date = eventDateTextView.getText().toString();

        // make sure title/desc are not blank
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(desc)) {
            Toast.makeText(this, R.string.failConfirmEvent, Toast.LENGTH_SHORT).show();
            return;
        }
        insertEvent = new InsertEvent(this, title, desc, time, date);
        insertEvent.execute();
    }

    // confirm event edits and go back to home
    private void confirmEditedEvent(String id) {
        String title = eventTitleInput.getText().toString();
        String desc = eventDescInput.getText().toString();
        String time = eventTimeTextView.getText().toString();
        String date = eventDateTextView.getText().toString();

        updateEvent = new UpdateEvent(this, id, title, desc, time, date);
        updateEvent.execute();
    }

    // delete event and return to home
    private void deleteEvent(String id, Context context) {
        DeleteEvent deleteEvent = new DeleteEvent(EditEventActivity.this);
        deleteEvent.execute(id);
    }

    // when Select Date button is clicked, show calendar for user to choose date
    public void showDatePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
        int year= c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    String selectedDate = (month + 1) + "/" + day + "/" + year;
                    eventDateTextView.setText(selectedDate);
                }
            }, year, month, day );

        datePickerDialog.show();
    }

    // when Select Time button is clicked, show time picker widget
    public void showTimePickerDialog(View v) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        eventTimeTextView.setText(selectedTime);
                    }
               }, hour, minute, true
        );

        timePickerDialog.show();
    }

    // handle user backing out of screen by using image in app bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // handle back button press (discards changes)
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (insertEvent != null && insertEvent.getStatus() == AsyncTask.Status.RUNNING) {
            insertEvent.cancel(true);
        }
        if (updateEvent != null && updateEvent.getStatus() == AsyncTask.Status.RUNNING) {
            updateEvent.cancel(true);
        }
    }
}