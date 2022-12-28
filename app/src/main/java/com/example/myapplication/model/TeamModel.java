package com.example.myapplication.model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.myapplication.database.FirestoreImpl;
import com.example.myapplication.database.listeners.team.OnAddTeamListener;
import com.example.myapplication.database.listeners.team.OnGetTeamListener;
import com.example.myapplication.entites.Team;

import java.util.Date;

public class TeamModel {

    private final FirestoreImpl db = new FirestoreImpl();
    private Team team;

    public TeamModel(String teamName) {
        Log.d(TAG, "TeamModel: " + teamName);
    }

    public void getTeam(String username) {
        OnGetTeamListener listener = new OnGetTeamListener() {
            @Override
            public void onTeamFilled(Team fetchedTeam) {
                Log.d(TAG, "onTeamFilled: " + fetchedTeam.getName());
                team = fetchedTeam;
            }

            @Override
            public void onError(Exception exception) {
                Log.d(TAG, "onError: " + exception.getMessage());
            }
        };
        db.getTeam(listener,username);
    }

    public void addTeam(String teamName, String playerId) {
        Team team = new Team.TeamBuilder()
                .setTeamId(new Date().getTime() + "")
                .setName(teamName)
                .setLanguage("en")
                .setKit("https://media.discordapp.net/attachments/788769960695431178/1045406323778523136/test_logo.png")
                .setGkKit("https://media.discordapp.net/attachments/788769960695431178/1045406323778523136/test_logo.png")
                .setMainColor("#648c2e")
                .setSecondaryColor("#000000")
                .setTeamLogo("https://media.discordapp.net/attachments/788769960695431178/1045406323778523136/test_logo.png")
                .setBackground("https://media.discordapp.net/attachments/996135352240717838/996156253472567336/output.png")
                .setLineupStyle("4-4-2")
                .setFontColor("#ffffff")
                .setFont("rajdhani-bold")
                .setManagerId(playerId)
                .build();
        OnAddTeamListener listener = new OnAddTeamListener() {
            @Override
            public void onTeamFilled(Boolean added) {
                if (added) {
                    Log.d(TAG, "onTeamFilled: " + team.getName());
                }
                else
                    Log.d(TAG, "onTeamFilled: " + "Team not added");
            }

            @Override
            public void onError(Exception exception) {
                Log.d(TAG, "onError: " + exception.getMessage());
            }
        };
        db.addTeam(listener, team);
    }
}
