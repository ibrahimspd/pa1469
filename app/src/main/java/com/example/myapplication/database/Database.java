package com.example.myapplication.database;


import com.example.myapplication.database.listeners.player.OnAddPlayerListener;
import com.example.myapplication.database.listeners.player.OnGetPlayerListener;
import com.example.myapplication.database.listeners.team.OnAddTeamListener;
import com.example.myapplication.database.listeners.team.OnGetTeamListener;
import com.example.myapplication.database.listeners.user.OnAddUserListener;
import com.example.myapplication.database.listeners.user.OnGetUserListener;
import com.example.myapplication.entites.Credentials;
import com.example.myapplication.entites.Player;
import com.example.myapplication.entites.Team;

public interface Database
{
    void checkCredentials(Credentials credentials, OnGetUserListener listener);

    boolean createAccount(OnAddUserListener listener, Credentials credentials);

    void getUser(OnGetUserListener listener, String username);

    void addTeam(OnAddTeamListener listener, Team team);

    void getTeam(OnGetTeamListener listener, String teamName);

    void updateTeam(Team team);

    void addPlayer(OnAddPlayerListener listener, Player player);

    void getPlayer(OnGetPlayerListener listener, String teamName);

    boolean deleteAccount(Credentials credentials);
}
