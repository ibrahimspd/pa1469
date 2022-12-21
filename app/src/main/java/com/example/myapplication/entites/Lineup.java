package com.example.myapplication.entites;

import java.util.List;

public class Lineup
{
    public List<Player> players;
    public Team team;
    public String formation;

    public Lineup(){}
    public Lineup(List<Player> players, Team team, String formation){
        this.players = players;
        this.team = team;
        this.formation = formation;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getFormation() {
        return formation;
    }

    public Team getTeam() {
        return team;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
