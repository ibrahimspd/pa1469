package com.example.myapplication.database.listeners.user;

public interface OnAddUserListener {
    void onUserAdded(Boolean added);
    void onError(Exception exception);
}
