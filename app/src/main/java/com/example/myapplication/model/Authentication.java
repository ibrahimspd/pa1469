package com.example.myapplication.model;

import com.example.myapplication.database.FirestoreImpl;
import com.example.myapplication.database.listeners.user.OnAddUserListener;
import com.example.myapplication.entites.Credentials;

public class Authentication {

    private Credentials credentials;
    private FirestoreImpl db = new FirestoreImpl();

    public boolean authenticateUser(Credentials credentials, OnAddUserListener listener){
        return credentials != null;
    }

    public boolean createUser(OnAddUserListener listener,  String username, String password){
        return db.createAccount(listener ,new Credentials.CredentialsBuilder()
                .email(username)
                .password(password)
                .build());
    }

    public boolean deleteUser(String username, String password){
        return db.deleteAccount(new Credentials.CredentialsBuilder()
                .email(username)
                .password(password)
                .build());
    }
}
