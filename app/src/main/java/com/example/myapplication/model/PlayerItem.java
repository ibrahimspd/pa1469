package com.example.myapplication.model;

public class PlayerItem {
    private String username;
    private String imageUrl;
    private String position;

    public PlayerItem(String username, String imageUrl, String position) {
        this.username = username;
        this.imageUrl = imageUrl;
        this.position = position;
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public String getPosition() {
        return position;
    }
}
