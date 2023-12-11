package com.zybooks.jveneski_eventtracker_project2;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// Class handling the login screen

public class LoginActivity extends Activity {
    private EditText editTextUser, editTextPass, editTextConfirm;
    private Button loginButton, newUserButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // init db helper
        databaseHelper = DatabaseHelper.getInstance(this);

        // init ui components
        editTextUser = findViewById(R.id.editTextUsername);
        editTextPass = findViewById(R.id.editTextPassword);
        editTextConfirm = findViewById(R.id.editTextConfirmPass);
        loginButton = findViewById(R.id.loginButton);
        newUserButton = findViewById(R.id.newUserButton);

        // set click listeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextConfirm.isEnabled()) {
                    loginUser();
                } else {
                    newUser();
                }
            }
        });

        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextConfirm.isEnabled()) {
                    // enable text box for user to confirm pass
                    editTextConfirm.setEnabled(true);
                } else {
                    editTextConfirm.setEnabled(false);
                }
        }});
    }

    private void loginUser() {
        String username = editTextUser.getText().toString().trim();
        String password = editTextPass.getText().toString().trim();

        // validate
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.fail_login_toast), Toast.LENGTH_SHORT).show();
            return;
        }

        // check if user exists
        VerifyLogin verifyLogin = new VerifyLogin(this);
        verifyLogin.execute(username, password);
    }

    private void newUser() {
        String username = editTextUser.getText().toString().trim();
        String password = editTextPass.getText().toString().trim();
        String confirm = editTextConfirm.getText().toString().trim();

        // validate
        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, R.string.new_user_empty_field_warn, Toast.LENGTH_SHORT).show();
            return;
        }

        if(!password.equals(confirm)) {
            Toast.makeText(this, "Passwords must match!", Toast.LENGTH_SHORT).show();
            return;
        }

        CreateUser createUser = new CreateUser(this);
        createUser.execute(username, password);
    }
}
