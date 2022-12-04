package com.example.myapplication.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.flag.BubbleFlag;
import com.skydoves.colorpickerview.flag.FlagMode;
import com.skydoves.colorpickerview.sliders.AlphaSlideBar;
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar;

public class PlayersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottonnav);

// Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.players);

// Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.teamInfo:
                        startActivity(new Intent(getApplicationContext(), TeamInfo.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.generateLineup:
                        startActivity(new Intent(getApplicationContext(), GenerateLineupActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.players:
                        return true;
                }
                return false;
            }
        });
    }
}