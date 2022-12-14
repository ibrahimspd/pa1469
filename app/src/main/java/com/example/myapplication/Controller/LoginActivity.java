package com.example.myapplication.Controller;
import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.database.FirestoreImpl;
import com.example.myapplication.database.OnTeamListener;
import com.example.myapplication.entites.Team;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirestoreImpl firestore = new FirestoreImpl();
        OnTeamListener teamListener = new OnTeamListener() {
            @Override
            public void onTeamFilled(ArrayList<Team> teams) {
                System.out.println(teams.get(0).getBackground());
            }

            @Override
            public void onError(Exception exception) {

            }
        };
        firestore.getTeam(teamListener);
        Button discordLoginBtn = findViewById(R.id.loginDiscord);
        showHompageOnClick(discordLoginBtn);

        Button databaseLoginBtn = findViewById(R.id.loginDatabase);
        showHompageOnClick(databaseLoginBtn);

        Button registerBtn = findViewById(R.id.register);
        showRegisterPageOnClick(registerBtn);
    }

    private void showHompageOnClick(Button databaseLoginBtn) {
        databaseLoginBtn.setOnClickListener(view -> openHomePageActivity());
    }


    private void showRegisterPageOnClick(Button registerBtn) {
        registerBtn.setOnClickListener(view -> openRegisterPageActivity());
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
