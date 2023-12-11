package com.zybooks.jveneski_eventtracker_project2;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.appbar.AppBarLayout;

// Handles notification screen including top app bar with icon
public class NotificationActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 111;
    private Button allowButton;
    private Button denyButton;
    private EditText phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);

        allowButton = findViewById(R.id.confirmNotificationButton);
        denyButton = findViewById(R.id.refuseNotificationButton);
        phoneNum = findViewById(R.id.phoneNumInput);

        // Initialize the AppBarLayout and Toolbar
        AppBarLayout appBarLayout = findViewById(R.id.topBarLayout);
        Toolbar toolbar = findViewById(R.id.topAppBar);

        // Don't show the title in the top bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // utilize brand icon at top of screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.smallawesoemfacecrop);

        allowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneNum.getText().toString();
                if (!phone.isEmpty()) {

                    int phoneInt = Integer.parseInt(phone);
                    SharedPreferences preferences = getSharedPreferences("EventusPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("phoneNum", phoneInt);
                    editor.apply();

                    // enable permission
                    requestSmsPermission();
                    finish();
                } else {
                    return;
                }
            }
        });

        denyButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set ignore flag
                setIgnoreNotifFlag();
                finish();
            }
        }));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestSmsPermission() {
        // check if already granted
        if (checkSelfPermission(Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            // permission already granted
        } else {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_REQUEST_CODE);
        }
    }

    private void setIgnoreNotifFlag() {
        // use shared preferences to not keep bringing user back to this screen
        SharedPreferences preferences = getSharedPreferences("EventusPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("ignoreNotif", true);
        editor.apply();
    }
}