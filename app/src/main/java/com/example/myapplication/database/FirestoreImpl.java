package com.example.myapplication.database;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.entites.Credentials;
import com.example.myapplication.entites.Lineup;
import com.example.myapplication.entites.Team;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
;import java.util.ArrayList;
import java.util.Map;

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
    public void getTeam(OnTeamListener listener) {
        DocumentReference documentReference = DB.collection("teams").document("test");

        final Team[] result = {null};
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        ArrayList<Team> teams = new ArrayList<>();
                        teams.add(documentSnapshot.toObject(Team.class));
                        listener.onTeamFilled(teams);

                        return;
                    }else{
                        Log.d(TAG, "onComplete: no shit");
                    }
                }else{
                    Log.d(TAG, "failed ", task.getException());
                }
            }
        });
        Log.d(TAG, "getTeam: ");
    }

}
