package com.example.myapplication.entites;

import java.util.List;

public class Lineup
{
    public List<Player> players;
    public String manager;
    public Lineup(List<Player> players, String manager){
        this.players = players;
        this.manager = manager;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getManager() {
        return manager;
    }
}
