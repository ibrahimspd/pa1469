package com.example.myapplication.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.Player;
import com.example.myapplication.Model.PlayerInfoListAdapter;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class PlayersActivity extends AppCompatActivity {

    private static final int GENERATE_LINEUP = R.id.generateLineup;
    private static final int PLAYERS = R.id.players;
    private static final int TEAM_INFO = R.id.teamInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);

        RecyclerView courseRV = findViewById(R.id.recycler_view);

        ArrayList<Player> courseModelArrayList = new ArrayList<Player>();
        courseModelArrayList.add(new Player("Hazel", "GK", R.drawable.default_lineup));
        courseModelArrayList.add(new Player("Hytos", "ST", R.drawable.test_logo));
        courseModelArrayList.add(new Player("Sn1k3", "LB", R.drawable.test_logo));
        courseModelArrayList.add(new Player("Guss", "CB", R.drawable.test_logo));
        courseModelArrayList.add(new Player("Polo", "CM", R.drawable.test_logo));

        PlayerInfoListAdapter courseAdapter = new PlayerInfoListAdapter(this, courseModelArrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        courseRV.setLayoutManager(linearLayoutManager);
        courseRV.setAdapter(courseAdapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottonnav);

        bottomNavigationView.setSelectedItemId(PLAYERS);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
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
        });
    }
}
