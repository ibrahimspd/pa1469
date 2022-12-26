package com.example.myapplication.database.listeners.player;

import com.example.myapplication.entites.Player;

public interface OnGetPlayerListener {
    void onPlayerFilled(Player player);
    void onError(Exception exception);
}
