package com.example.myapplication.database;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.entites.Credentials;
import com.example.myapplication.entites.Player;
import com.example.myapplication.entites.Team;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreImpl implements Database {
    private FirebaseFirestore db;

    public FirestoreImpl(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public void checkCredentials(Credentials credentials, OnUserListener listener) {
        DocumentReference documentReference = db.collection("users").document(credentials.getEmail());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        Credentials user = documentSnapshot.toObject(Credentials.class);
                        listener.onUserFilled(user);
                    }else{
                        Log.d(TAG, "onComplete: no shit");
                    }
                }else{
                    Log.d(TAG, "failed ", task.getException());
                }
            }
        });
    }

    @Override
    public boolean createAccount(Credentials credentials) {
        CollectionReference users = db.collection("users");
        users.document(credentials.getEmail()).set(credentials);
        return true;
    }



    @Override
    public void getTeam(OnTeamListener listener) {
        DocumentReference documentReference = db.collection("teams").document("test");
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        Team teams = documentSnapshot.toObject(Team.class);
                        listener.onTeamFilled(teams);
                    }else{
                        Log.d(TAG, "onComplete: no shit");
                    }
                }else{
                    Log.d(TAG, "failed ", task.getException());
                }
            }
        });
    }

    @Override
    public Team getTeam(String teamName) {
        return null;
    }

    @Override
    public void addTeam(Team team) {
        CollectionReference teams = db.collection("teams");
        teams.document(team.getName()).set(team);
    }

    @Override
    public Player getPlayer(String name) {
        return null;
    }

    @Override
    public void addPlayer(Player player) {

    }

    @Override
    public boolean deleteAccount(Credentials credentials) {
        return false;
    }

    @Override
    public Team getTeam(int teamId) {
        return null;
    }
}
