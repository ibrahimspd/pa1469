package com.example.myapplication.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PlayersActivity extends AppCompatActivity {

    private static final int GENERATE_LINEUP = R.id.generateLineup;
    private static final int PLAYERS = R.id.players;
    private static final int TEAM_INFO = R.id.teamInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottonnav);

        bottomNavigationView.setSelectedItemId(PLAYERS);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case TEAM_INFO:
                        startActivity(new Intent(getApplicationContext(), TeamInfo.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case GENERATE_LINEUP:
                        startActivity(new Intent(getApplicationContext(), GenerateLineupActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case PLAYERS:
                        return true;
                }
                return false;
            }
        });
    }
}
