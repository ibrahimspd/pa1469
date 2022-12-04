package com.example.myapplication.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.Model.Team;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.skydoves.colorpickerview.ColorPickerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class TeamInfo extends AppCompatActivity {

    private Context mContext;
    private TeamInfo mActivity;

    private ColorPickerView colorPickerView;

    private RelativeLayout mRelativeLayout;
    private Button mButton;

    private PopupWindow mPopupWindow;

    private BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottonnav);

// Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.teamInfo);

// Perform item selected listener
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

        // read json asset file
        String json = null;
        try {
            InputStream is = getAssets().open("teamData.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println(json);
        // create tost


        //create object from json string
        Gson gson = new Gson(); // Or use new GsonBuilder().create();
        Team teamInfo = gson.fromJson(json, Team.class); // deserializes json into target2

        // set team name
        TextView teamName = findViewById(R.id.teamName);
        teamName.setText("Name: " + teamInfo.getName());

        // set manager

        TextView manager = findViewById(R.id.manager);
        manager.setText("Manager: " + teamInfo.getManager());

        //set language

        TextView language = findViewById(R.id.language);
        language.setText("Language: " + teamInfo.getLanguage());

        // set team logo
        ImageView teamLogo = findViewById(R.id.teamLogo);
        Glide.with(this).asBitmap().load(teamInfo.getTeamLogo()).into(teamLogo);

        // set font
        TextView teamFont = findViewById(R.id.font);
        teamFont.setText("Font: " + teamInfo.getFont());

        // set background
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

