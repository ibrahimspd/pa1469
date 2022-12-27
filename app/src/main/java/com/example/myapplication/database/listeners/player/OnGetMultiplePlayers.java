package com.example.myapplication.database.listeners.player;

import com.example.myapplication.entites.Player;

import java.util.List;

public interface OnGetMultiplePlayers {
    void onTeamFilled(List<Player> players);
    void onError(Exception exception);
}
