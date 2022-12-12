package com.example.myapplication.database;

import com.example.myapplication.entites.Credentials;
import com.example.myapplication.entites.Lineup;
import com.example.myapplication.entites.Team;
import com.google.firebase.firestore.FirebaseFirestore;
;

public class FirestoreImpl implements Database {
    private static final FirebaseFirestore DB = FirebaseFirestore.getInstance();
    public FirestoreImpl() {

    }

    @Override
    public boolean checkCredentials(Credentials credentials) {
        return false;
    }

    @Override
    public boolean createAccount(Credentials credentials) {
        
        return false;
    }

    @Override
    public Lineup getLineUp(int teamId) {
        return null;
    }

    @Override
    public Lineup getLineUp(String teamName) {
        return null;
    }

    @Override
    public Team getTeam(int teamId) {
        return null;
    }

    @Override
    public Team getTeam(String teamName) {
        return null;
    }
}
