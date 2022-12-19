package com.example.myapplication.Controller;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplication.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.appBarMain.toolbar.setTitle("Home");

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;

        Context context = getApplicationContext();

        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each

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
        // menu should be considered as top level destinations.
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
}
