package com.example.myapplication.database;


import com.example.myapplication.entites.Credentials;
import com.example.myapplication.entites.Lineup;
import com.example.myapplication.entites.Team;

import java.util.ArrayList;

public interface Database
{

    boolean checkCredentials(Credentials credentials);
    boolean createAccount(Credentials credentials);
    Lineup getLineUp(int teamId);
    Lineup getLineUp(String teamName);
    void getTeam(OnTeamListener listener);
    

}
