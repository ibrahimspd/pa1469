package com.example.myapplication.database;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.database.listeners.player.OnAddPlayerListener;
import com.example.myapplication.database.listeners.player.OnGetPlayerListener;
import com.example.myapplication.database.listeners.team.OnAddTeamListener;
import com.example.myapplication.database.listeners.team.OnGetTeamListener;
import com.example.myapplication.database.listeners.user.OnAddUserListener;
import com.example.myapplication.database.listeners.user.OnGetUserListener;
import com.example.myapplication.entites.Credentials;
import com.example.myapplication.entites.Player;
import com.example.myapplication.entites.Team;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

public class FirestoreImpl implements Database {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FirestoreImpl() {
    }

    @Override
    public void checkCredentials(Credentials credentials, OnGetUserListener listener) {
        DocumentReference documentReference = db.collection("users").document(credentials.getEmail());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Credentials user = documentSnapshot.toObject(Credentials.class);
                        listener.onUserFilled(user);
                    } else {
                        Log.d(TAG, "onComplete: no shit");
                    }
                } else {
                    Log.d(TAG, "failed ", task.getException());
                }
            }
        });
    }

    @Override
    public boolean createAccount(OnAddUserListener listener, Credentials credentials) {
        CollectionReference users = db.collection("users");
        users.document(credentials.getEmail()).set(credentials).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onUserAdded(true);
                } else {
                    Log.d(TAG, "onComplete: failed");
                }
            }
        });
        return true;
    }

    @Override
    public void getUser(OnGetUserListener listener, String username) {
        DocumentReference documentReference = db.collection("users").document(username);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Credentials user = documentSnapshot.toObject(Credentials.class);
                        listener.onUserFilled(user);
                    } else {
                        Log.d(TAG, "onComplete: no shit");
                    }
                } else {
                    Log.d(TAG, "failed ", task.getException());
                }
            }
        });
    }

    @Override
    public void getTeam(OnGetTeamListener listener, String teamName) {
        DocumentReference documentReference = db.collection("teams").document(teamName);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Team teams = documentSnapshot.toObject(Team.class);
                        listener.onTeamFilled(teams);
                    } else {
                        Log.d(TAG, "onComplete: no shit");
                    }
                } else {
                    Log.d(TAG, "failed ", task.getException());
                }
            }
        });
    }

    @Override
    public void addTeam(OnAddTeamListener listener, Team team) {
        CollectionReference teams = db.collection("teams");
        teams.document(team.getName()).set(team);
    }

    @Override
    public void updateTeam(Team team) {
        CollectionReference teams = db.collection("teams");
        teams.document(team.getName()).set(team).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onComplete: success");
                } else {
                    Log.d(TAG, "onComplete: failed");
                }
            }
        });
    }


    @Override
    public void addPlayer(OnAddPlayerListener listener, Player player) {
        CollectionReference players = db.collection("players");
        players.document(player.getName()).set(player).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onPlayerAdded(true);
                } else {
                    Log.d(TAG, "onComplete: failed");
                }
            }
        });

    }

    @Override
    public void getPlayer(OnGetPlayerListener listener, String username) {
        DocumentReference documentReference = db.collection("players").document(username);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Player player = documentSnapshot.toObject(Player.class);
                        listener.onPlayerFilled(player);
                    } else {
                        Log.d(TAG, "onComplete: no shit");
                    }
                } else {
                    Log.d(TAG, "failed ", task.getException());
                }
            }
        });
    }

    @Override
    public boolean deleteAccount(Credentials credentials) {
        return false;
    }
}
