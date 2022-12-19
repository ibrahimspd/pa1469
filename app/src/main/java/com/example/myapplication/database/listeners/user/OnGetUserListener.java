package com.example.myapplication.database.listeners.user;

import com.example.myapplication.entites.Credentials;
import com.google.firebase.firestore.auth.User;

public interface OnGetUserListener
{
    void onUserFilled(Credentials credentials);
    void onError(Exception exception);
}
