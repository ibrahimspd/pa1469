package com.example.myapplication.entites;

public class Invitations {
    private final String teamId;
    private final String fromUser;
    private final String userId;

    public Invitations(String teamId, String fromUser, String userId) {
        this.teamId = teamId;
        this.fromUser = fromUser;
        this.userId = userId;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getUserId() {
        return userId;
    }
}
