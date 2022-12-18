package com.example.myapplication.database;


import com.example.myapplication.entites.Credentials;
import com.example.myapplication.entites.Lineup;
import com.example.myapplication.entites.Player;
import com.example.myapplication.entites.Team;

public interface Database
{
    void checkCredentials(Credentials credentials, OnUserListener listener);

    boolean createAccount(Credentials credentials);

    Team getTeam(int teamId);

    void getTeam(OnTeamListener listener);

    Team getTeam(String teamName);

    void addTeam(Team team);

    void updateTeam(Team team);

    Player getPlayer(String name);

    void addPlayer(Player player);

    boolean deleteAccount(Credentials credentials);
}
