package com.example.myapplication.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.myapplication.R;

public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button discordLoginBtn = findViewById(R.id.loginDiscordBtn);
        showHompageOnClick(discordLoginBtn);

        Button databaseLoginBtn = findViewById(R.id.loginDatabaseBtn);
        showHompageOnClick(databaseLoginBtn);

        Button registerBtn = findViewById(R.id.registerBtn);
        showRegisterPageOnClick(registerBtn);
    }

    private void showHompageOnClick(Button databaseLoginBtn) {
        databaseLoginBtn.setOnClickListener(view -> openLoginActivity());
    }

    private void showRegisterPageOnClick(Button registerBtn) {
        registerBtn.setOnClickListener(view -> openRegisterPageActivity());
    }

    private void openLoginActivity(){
        Intent intent = new Intent(this, GenerateLineupActivity.class);
        startActivity(intent);
    }

    private void openRegisterPageActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
