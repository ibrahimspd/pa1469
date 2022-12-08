package com.example.myapplication.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button discordLoginBtn = findViewById(R.id.loginDiscord);
        showHompageOnClick(discordLoginBtn);

        Button databaseLoginBtn = findViewById(R.id.loginDatabase);
        showHompageOnClick(databaseLoginBtn);

        Button registerBtn = findViewById(R.id.register);
        showRegisterPageOnClick(registerBtn);


    }

    private void showHompageOnClick(Button databaseLoginBtn) {
        databaseLoginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openHomePageActivity();
            }
        });
    }


    private void showRegisterPageOnClick(Button registerBtn) {
        registerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { openRegisterPageActivity();}
        });
    }

    private void openHomePageActivity(){
        Intent intent = new Intent(this, GenerateLineupActivity.class);
        startActivity(intent);
    }

    private void openRegisterPageActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
