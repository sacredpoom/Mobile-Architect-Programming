package com.zybooks.jveneski_eventtracker_project2;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.telephony.SmsManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationHelper {

    @SuppressLint("ScheduleExactAlarm")
    public static void setSMSNotification(Context context, long eventId, String eventDate) {
        // Convert the eventDate string to milliseconds
        long eventDateInMillis = convertEventDateToMillis(eventDate);

        // Set up the Intent for the AlarmReceiver
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.putExtra("EVENT_ID", eventId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                (int) eventId,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Set up the AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Set the alarm based on the specific date and time of the event
        if (alarmManager != null) {
            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    eventDateInMillis,
                    pendingIntent
            );
        }
    }

    private static long convertEventDateToMillis(String eventDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        try {
            Date date = dateFormat.parse(eventDate);
            return date != null ? date.getTime() : -1;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Retrieve the event ID from the intent
            long eventId = intent.getLongExtra("EVENT_ID", -1);

            if (eventId != -1) {
                // The alarm has triggered; handle SMS notification using SmsManager
                sendSMSNotification(context, eventId);
            }
        }
    }

    private static void sendSMSNotification(Context context, long eventId) {
        // Get event details from the database using the event ID
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        Event event = databaseHelper.findEvent(String.valueOf(eventId));

        // Use SmsManager to send SMS notification
        SmsManager smsManager = SmsManager.getDefault();
        String message = "Event Reminder: " + event.getTitle(); // Customize the SMS message
        SharedPreferences preferences = context.getSharedPreferences("EventusPrefs", Context.MODE_PRIVATE);
        String phoneNumber = preferences.getString("phoneNum", "");

        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }
}