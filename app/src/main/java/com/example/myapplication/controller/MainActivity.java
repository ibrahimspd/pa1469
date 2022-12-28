package com.example.myapplication.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.database.FirestoreImpl;
import com.example.myapplication.database.listeners.player.OnGetPlayerListener;
import com.example.myapplication.database.listeners.team.OnGetTeamListener;
import com.example.myapplication.entites.Player;
import com.example.myapplication.entites.Team;
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

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private Player player;
    private Team team;

    private FirestoreImpl firestore = new FirestoreImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.appBarMain.toolbar.setTitle("Home");

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        OnGetTeamListener onGetTeamListener = new OnGetTeamListener() {
            @Override
            public void onTeamFilled(Team team) {
                MainActivity.this.team = team;
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
                firestore.getTeamByPlayerId(onGetTeamListener, player.getId());
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(MainActivity.this, "Error getting player", Toast.LENGTH_SHORT).show();
            }
        };

        firestore.getPlayerByUuid(onGetPlayerListener, id);

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;

        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

        NavigationView navigationView = binding.navView;

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottonnav);

        GenerateLineupFragment generateLineupFragment = new GenerateLineupFragment();
        TeamInfoFragment teamInfoFragment = new TeamInfoFragment();
        PlayersFragment playersFragment = new PlayersFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main, generateLineupFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
        });
        bottomNavigationView.setSelectedItemId(R.id.generateLineup);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.teamInfoFragment, R.id.generateLineupFragment, R.id.playersFragment)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}