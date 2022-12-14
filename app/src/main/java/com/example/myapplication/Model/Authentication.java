package com.example.myapplication.Model;

import com.example.myapplication.database.FirestoreImpl;
import com.example.myapplication.database.OnUserListener;
import com.example.myapplication.entites.Credentials;
import com.google.firebase.firestore.FirebaseFirestore;

public class Authentication {

    private Credentials credentials;
    private FirestoreImpl db = new FirestoreImpl(FirebaseFirestore.getInstance());

    public boolean authenticateUser(Credentials credentials, OnUserListener listener){
        return credentials != null;
    }

    public boolean createUser(String username, String password){
        return db.createAccount(new Credentials.CredentialsBuilder()
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
