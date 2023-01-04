package com.example.myapplication.database;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.database.listeners.player.OnAddPlayerListener;
import com.example.myapplication.database.listeners.player.OnGetMultiplePlayers;
import com.example.myapplication.database.listeners.player.OnGetPlayerListener;
import com.example.myapplication.database.listeners.player.OnUpdatePlayerListener;
import com.example.myapplication.database.listeners.team.OnAddTeamListener;
import com.example.myapplication.database.listeners.team.OnGetMultipleTeamsListener;
import com.example.myapplication.database.listeners.team.OnGetTeamListener;
import com.example.myapplication.database.listeners.user.OnAddUserListener;
import com.example.myapplication.database.listeners.user.OnGetUserListener;
import com.example.myapplication.entites.Credentials;
import com.example.myapplication.entites.FcmToken;
import com.example.myapplication.entites.Player;
import com.example.myapplication.entites.Team;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

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
    public void getUserByUsername(OnGetUserListener listener, String username) {
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
    public void getTeamByPlayerId(OnGetTeamListener listener, String playerId) {
        Query query = db.collection("teams").whereEqualTo("teamId", playerId);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot.isEmpty()) {
                        listener.onTeamFilled(null);
                    } else {
                        Team team = querySnapshot.getDocuments().get(0).toObject(Team.class);
                        listener.onTeamFilled(team);
                    }
                }
            }
        });
    }

    @Override
    public void addTeam(OnAddTeamListener listener, Team team) {
        CollectionReference teams = db.collection("teams");
        teams.document(team.getName()).set(team);
        listener.onTeamFilled(true);
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
    public void getPlayerByUsername(OnGetPlayerListener listener, String username) {
        DocumentReference documentReference = db.collection("players").document(username);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Player player = documentSnapshot.toObject(Player.class);
                        listener.onPlayerFilled(player);
                    }
                } else {
                    Log.d(TAG, "failed ", task.getException());
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
    public void getPlayerByUuid(OnGetPlayerListener listener, String uuid) {
        Query documentReference = db.collection("players").whereEqualTo("uuid", uuid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        Player player = documentSnapshot.getDocuments().get(0).toObject(Player.class);
                        listener.onPlayerFilled(player);
                    } else {
                        Log.d(TAG, "Failed to get player by uuid: " + uuid);
                    }
                }
            }
        });
    }

    @Override
    public void getPlayersToInvite(OnGetMultiplePlayers listener, String searchString) {
        Query query = db.collection("players").whereEqualTo("name", searchString);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                listener.onTeamFilled(task.getResult().toObjects(Player.class));
            }
        });
    }

    @Override
    public void getPlayersByTeam(OnGetMultiplePlayers listener, String teamId) {
        Query query = db.collection("players").whereEqualTo("teamId", teamId);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    listener.onTeamFilled(task.getResult().toObjects(Player.class));
                }
            }
        });
    }

    @Override
    public boolean deleteAccount(Credentials credentials) {
        return false;
    }

    @Override
    public void addUserFcm() {
        OnGetPlayerListener listener = new OnGetPlayerListener() {
            @Override
            public void onPlayerFilled(Player player) {

                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {

                            CollectionReference collectionReference = db.collection("fcm");
                            Log.d(TAG, "onComplete: getting collection FCM");
                            FcmToken fcmToken = new FcmToken(task.getResult());
                            collectionReference.document(player.getUuid()).set(fcmToken).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "fcm inneronComplete: shitty database");
                                    }
                                }
                            });
                            Log.d(TAG, "onComplete: ADDING USER FCM");
                        }
                    }
                });
            }

            @Override
            public void onError(Exception exception) {

            }
        };

        String uuid = FirebaseAuth.getInstance().getUid();
        if (uuid != null) {
            getPlayerByUuid(listener, uuid);
        }
    }

    @Override
    public void updatePlayer(OnUpdatePlayerListener listener, Player player) {
        CollectionReference players = db.collection("players");
        players.document(player.getName()).set(player).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onPlayerAdded(true);
                    Log.d(TAG, "onComplete: success");
                } else {
                    listener.onPlayerAdded(false);
                    Log.d(TAG, "onComplete: failed");
                }
            }
        });
    }

    @Override
    public void getAllTeams(OnGetMultipleTeamsListener listener) {
        Query query = db.collection("teams");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    listener.onTeamsFilled(task.getResult().toObjects(Team.class));
                }
            }
        });
    }
}
