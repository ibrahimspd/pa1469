package com.example.myapplication.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.Model.Team;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TeamInfo extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottonnav);

        bottomNavigationView.setSelectedItemId(R.id.teamInfo);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.teamInfo:
                        return true;
                    case R.id.generateLineup:
                        startActivity(new Intent(getApplicationContext(), GenerateLineupActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.players:
                        startActivity(new Intent(getApplicationContext(), PlayersActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        String json = null;
        try {
            InputStream is = getAssets().open("teamData.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Gson gson = new Gson();
        Team teamInfo = gson.fromJson(json, Team.class);

        TextView teamName = findViewById(R.id.teamName);
        teamName.setText("Name: " + teamInfo.getName());

        TextView manager = findViewById(R.id.manager);
        manager.setText("Manager: " + teamInfo.getManager());

        TextView language = findViewById(R.id.language);
        language.setText("Language: " + teamInfo.getLanguage());

        ImageView teamLogo = findViewById(R.id.teamLogo);
        Glide.with(this).asBitmap().load(teamInfo.getTeamLogo()).into(teamLogo);

        TextView teamFont = findViewById(R.id.font);
        teamFont.setText("Font: " + teamInfo.getFont());

        ImageView teamBackground = findViewById(R.id.teamBackground);
        Glide.with(this).asBitmap().load(teamInfo.getBackground()).into(teamBackground);

        ImageView kit = findViewById(R.id.teamKit);
        Glide.with(this).asBitmap().load(teamInfo.getKit()).into(kit);

        ImageView mainColor = findViewById(R.id.mainColor);
        mainColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamInfo.this, ColorPicker.class);
                intent.putExtra("colorType", "mainColor");
                intent.putExtra("color", teamInfo.getMainColor());
                startActivity(intent);
            }
        });

        ImageView secondaryColor = findViewById(R.id.secondaryColor);
        secondaryColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamInfo.this, ColorPicker.class);
                intent.putExtra("colorType", "secondaryColor");
                intent.putExtra("color", teamInfo.getSecondaryColor());
                startActivity(intent);
            }
        });

        ImageView fontColor = findViewById(R.id.fontColor);
        fontColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamInfo.this, ColorPicker.class);
                intent.putExtra("colorType", "fontColor");
                intent.putExtra("color", teamInfo.getFontColor());
                startActivity(intent);
            }
        });
    }
}

