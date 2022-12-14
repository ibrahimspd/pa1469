package com.example.myapplication.database;

import com.example.myapplication.entites.Credentials;

public interface OnUserListener {
    void onUserFilled(Credentials credentials);
    void onError(Exception exception);
}
