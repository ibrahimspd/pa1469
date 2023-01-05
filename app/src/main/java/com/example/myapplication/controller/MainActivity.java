package com.example.myapplication.controller;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.database.FirestoreImpl;
import com.example.myapplication.database.listeners.player.OnGetMultiplePlayers;
import com.example.myapplication.database.listeners.player.OnGetPlayerListener;
import com.example.myapplication.database.listeners.team.OnGetTeamListener;
import com.example.myapplication.entites.Player;
import com.example.myapplication.entites.Team;
import com.example.myapplication.model.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private List<Player> playersToInvite;
    private Player player;
    private Team team;
    private List<Player> players;
    private boolean isSandbox = false;
    private final Gson gson = new Gson();

    private FirestoreImpl firestore = new FirestoreImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        isSandbox = intent.getBooleanExtra("isSandbox", false);
        OnGetMultiplePlayers onGetMultiplePlayersInvite = new OnGetMultiplePlayers() {
            @Override
            public void onTeamFilled(List<Player> players) {
                MainActivity.this.playersToInvite = players;
                Toast.makeText(MainActivity.this, "Fetched players you can invite", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(MainActivity.this, "Error fetching players", Toast.LENGTH_SHORT).show();
            }
        };
        firestore.getPlayersByTeam(onGetMultiplePlayersInvite, "0");
        OnGetMultiplePlayers onGetMultiplePlayersListener = new OnGetMultiplePlayers() {
            @Override
            public void onTeamFilled(List<Player> players) {
                MainActivity.this.players = players;
                showScreen();
                Toast.makeText(MainActivity.this, "Fetched Players Successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception exception) {
                showScreen();
                Toast.makeText(MainActivity.this, "Error fetching players", Toast.LENGTH_SHORT).show();
            }
        };

        OnGetTeamListener onGetTeamListener = new OnGetTeamListener() {
            @Override
            public void onTeamFilled(Team team) {
                if(team != null) {
                    MainActivity.this.team = team;
                    firestore.getPlayersByTeam(onGetMultiplePlayersListener, team.getTeamId());
                }else {
                    showScreen();
                }
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(MainActivity.this, "Error getting team", Toast.LENGTH_SHORT).show();
            }
        };

        OnGetPlayerListener onGetPlayerListener = new OnGetPlayerListener() {
            @Override
            public void onPlayerFilled(Player player) {
                MainActivity.this.player = player;
                firestore.getTeamByPlayerId(onGetTeamListener, player.getTeamId());
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(MainActivity.this, "Error getting player", Toast.LENGTH_SHORT).show();
            }
        };

        if(isSandbox) {
            String sandboxString = Utils.getJsonFromAssets(MainActivity.this, "teamData.json");
            Team sandboxTeam = gson.fromJson(sandboxString, Team.class);
            team = sandboxTeam;
            showScreen();
        }
        else
        {
            firestore.getPlayerByUuid(onGetPlayerListener, id);
        }
    }

    private void showScreen() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.appBarMain.toolbar.setTitle("Home");

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;

        NavigationView navigationView = binding.navView;

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottonnav);

        GenerateLineupFragment generateLineupFragment = new GenerateLineupFragment();
        TeamInfoFragment teamInfoFragment = new TeamInfoFragment();
        PlayersFragment playersFragment = new PlayersFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, generateLineupFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (isSandbox)
                {
                    switch (item.getItemId()) {
                        case R.id.generateLineup:
                            binding.appBarMain.toolbar.setTitle("Generate Lineup");
                            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, generateLineupFragment).commit();
                            return true;
                        case R.id.teamInfo:
                            Toast.makeText(MainActivity.this, "You Are Not Logged In", Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.players:
                            Toast.makeText(MainActivity.this, "You Are Not Logged In", Toast.LENGTH_SHORT).show();
                            return true;
                    }
                    return false;
                }
                else
                {
                    switch (item.getItemId()) {
                        case R.id.generateLineup:
                            binding.appBarMain.toolbar.setTitle("Generate Lineup");
                            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, generateLineupFragment).commit();
                            return true;
                        case R.id.teamInfo:
                            binding.appBarMain.toolbar.setTitle("Team Info");
                            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, teamInfoFragment).commit();
                            return true;
                        case R.id.players:
                            binding.appBarMain.toolbar.setTitle("Players");
                            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, playersFragment).commit();
                            return true;
                    }
                    return false;
                }
            }
        });
        if(!isSandbox){
            bottomNavigationView.setSelectedItemId(R.id.generateLineup);

            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.profileFragment, R.id.inboxFragment, R.id.aboutFragment, R.id.adminFragment)
                    .setOpenableLayout(drawer)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public Player getPlayer() {
        return player;
    }

    public Team getTeam() {
        return team;
    }

    public List<Player> getPlayers() { return this.players; }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<Player> getPlayersToInvite() {
        return playersToInvite;
    }
}
