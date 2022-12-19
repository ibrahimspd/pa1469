package com.example.myapplication.database.listeners.team;

import com.example.myapplication.entites.Team;

public interface OnAddTeamListener
{
    void onTeamFilled(Boolean added);
    void onError(Exception exception);
}

