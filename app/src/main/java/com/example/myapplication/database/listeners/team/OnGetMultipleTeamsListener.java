package com.example.myapplication.database.listeners.team;

import com.example.myapplication.entites.Team;

import java.util.List;

public interface OnGetMultipleTeamsListener {
    void onTeamsFilled(List<Team> teams);
    void onError(Exception exception);
}
