package com.example.myapplication.database.listeners.player;

public interface OnUpdatePlayerListener {
    void onPlayerAdded(Boolean added);
    void onError(Exception exception);
}
