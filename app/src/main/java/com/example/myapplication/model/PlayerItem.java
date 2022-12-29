package com.example.myapplication.model;

public class PlayerItem {
    private String username;
    private String imageUrl;
    private String position;
    private String nationality;
    private int number;
    private String gamerTag;

    public PlayerItem(String username, String imageUrl, String position, String nationality, Integer number, String gamerTag) {
        this.username = username;
        this.imageUrl = imageUrl;
        this.position = position;
        this.nationality = nationality;
        this.number = number;
        this.gamerTag = gamerTag;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getGamerTag() {
        return gamerTag;
    }

    public void setGamerTag(String gamerTag) {
        this.gamerTag = gamerTag;
    }
}
