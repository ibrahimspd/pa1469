package com.example.myapplication.database;

import com.example.myapplication.entites.Team;

import java.util.ArrayList;

public interface OnTeamListener
{
    void onTeamFilled(ArrayList<Team> teams);
    void onError(Exception exception);
}
