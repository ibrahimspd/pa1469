package com.example.myapplication.database.listeners.team;

import com.example.myapplication.entites.Team;

public interface OnGetTeamListener {
    void onTeamFilled(Team teams);
    void onError(Exception exception);
}
