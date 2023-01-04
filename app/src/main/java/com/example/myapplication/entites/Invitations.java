package com.example.myapplication.entites;

public class Invitations {
    private String teamId;
    private String fromUserId;
    private String userId;
    private String fromUsername;
    private String toUsername;

    public Invitations(String teamId, String fromUser, String userId, String fromUsername, String toUsername) {
        this.teamId = teamId;
        this.fromUserId = fromUser;
        this.userId = userId;
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
    }

    public Invitations() {

    }

    @Override
    public String toString() {
        return "Invitations{" +
                "teamId='" + teamId + '\'' +
                ", fromUserId='" + fromUserId + '\'' +
                ", userId='" + userId + '\'' +
                ", fromUsername='" + fromUsername + '\'' +
                ", toUsername='" + toUsername + '\'' +
                '}';
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public String getToUsername() {
        return toUsername;
    }

    public String getTeamId() {
        return teamId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public String getUserId() {
        return userId;
    }
}
