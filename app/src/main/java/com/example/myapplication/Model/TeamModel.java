package com.example.myapplication.Model;

import static android.content.ContentValues.TAG;

import android.provider.ContactsContract;
import android.util.Log;

import com.example.myapplication.database.FirestoreImpl;
import com.example.myapplication.entites.Team;
import com.google.firebase.firestore.FirebaseFirestore;

public class TeamModel {
    private Team team;
    private FirebaseFirestore db;

    public TeamModel(FirebaseFirestore db, String teamName) {
        Log.d(TAG, "TeamModel: " + teamName);
        this.db = db;
    }

    public Team getTeam() {
        return new FirestoreImpl(db).getTeam("test");
    }

    public void addTeam(String teamName, String manager){
        Team team = new Team.TeamBuilder()
                .setTeamId(1)
                .setName(teamName)
                .setLanguage("en")
                .setKit("https://media.discordapp.net/attachments/788769960695431178/1045406323778523136/test_logo.png")
                .setGkKit("https://media.discordapp.net/attachments/788769960695431178/1045406323778523136/test_logo.png")
                .setMainColor("#648c2e")
                .setSecondaryColor("#000000")
                .setTeamLogo("https://media.discordapp.net/attachments/788769960695431178/1045406323778523136/test_logo.png")
                .setBackground("https://media.discordapp.net/attachments/996135352240717838/996156253472567336/output.png")
                .setLineupStyle("4-4-2")
                .setFontColor("#ffffff")
                .setFont("rajdhani-bold")
                .setManager(manager)
                .build();
        FirestoreImpl db = new FirestoreImpl(this.db);
        db.addTeam(team);
    }
}
