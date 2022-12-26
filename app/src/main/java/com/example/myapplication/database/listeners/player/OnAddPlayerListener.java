package com.example.myapplication.database.listeners.player;

import com.example.myapplication.entites.Player;

public interface OnAddPlayerListener {
    void onPlayerAdded(Boolean added);
    void onError(Exception exception);
}
