package com.example.myapplication.Controller;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.database.FirestoreImpl;
import com.example.myapplication.database.OnTeamListener;
import com.example.myapplication.entites.Team;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class TeamInfo extends AppCompatActivity {

    private static final int TEAM_INFO = R.id.teamInfo;
    private static final int GENERATE_LINEUP = R.id.generateLineup;
    private static final int PLAYERS = R.id.players;

    private Context context;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private TextView teamName;
    private TextView manager;
    private TextView language;
    private ImageView teamLogo;
    private TextView font;
    private ImageView teamBackground;
    private ImageView teamKit;
    private ImageView mainColor;
    private ImageView secondaryColor;
    private ImageView fontColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);

        context = this;

        teamName = findViewById(R.id.teamName);
        manager = findViewById(R.id.manager);
        language = findViewById(R.id.language);
        teamLogo = findViewById(R.id.teamLogo);
        font = findViewById(R.id.font);
        teamBackground = findViewById(R.id.teamBackground);
        teamKit = findViewById(R.id.teamKit);
        mainColor = findViewById(R.id.mainColor);
        secondaryColor = findViewById(R.id.secondaryColor);
        fontColor = findViewById(R.id.fontColor);

        FirestoreImpl firestore = new FirestoreImpl(db);
        OnTeamListener teamListener = new OnTeamListener() {
            @Override
            public void onTeamFilled(Team team) {
                displayTeamInfo(team);
            }
            @Override
            public void onError(Exception exception) {
                Log.d(TAG, "onError: " + exception.getMessage());
            }
        };
        firestore.getTeam(teamListener);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottonnav);

        bottomNavigationView.setSelectedItemId(TEAM_INFO);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case TEAM_INFO:
                    return true;
                case GENERATE_LINEUP:
                    startActivity(new Intent(getApplicationContext(), GenerateLineupActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                case PLAYERS:
                    startActivity(new Intent(getApplicationContext(), PlayersActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });
    }

    public void displayTeamInfo(Team team) {
        manager.setText("Manager: " + team.getManager());
        teamName.setText("Name: " + team.getName());

        language.setText("Language: " + team.getLanguage());

        font.setText("Font: " + team.getFont());

        Glide.with(context).asBitmap().load(team.getTeamLogo()).into(teamLogo);
        Glide.with(context).asBitmap().load(team.getBackground()).into(teamBackground);
        Glide.with(context).asBitmap().load(team.getKit()).into(teamKit);

        mainColor.setBackgroundColor(Color.parseColor(team.getMainColor()));
        mainColor.setOnClickListener(view -> {
            Intent intent = new Intent(TeamInfo.this, ColorPicker.class);
            intent.putExtra("colorType", "mainColor");
            intent.putExtra("color", team.getMainColor());
            startActivity(intent);
        });

        secondaryColor.setBackgroundColor(Color.parseColor(team.getSecondaryColor()));
        secondaryColor.setOnClickListener(view -> {
            Intent intent = new Intent(TeamInfo.this, ColorPicker.class);
            intent.putExtra("colorType", "secondaryColor");
            intent.putExtra("color", team.getSecondaryColor());
            startActivity(intent);
        });

        fontColor.setBackgroundColor(Color.parseColor(team.getFontColor()));
        fontColor.setOnClickListener(view -> {
            Intent intent = new Intent(TeamInfo.this, ColorPicker.class);
            intent.putExtra("colorType", "fontColor");
            intent.putExtra("color", team.getFontColor());
            startActivity(intent);
        });
    }
}

