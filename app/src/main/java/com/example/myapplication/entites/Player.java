package com.example.myapplication.entites;

public class Player
{
    private final String name;
    private String postion;
    private String nationality;
    private int number;
    private int team;
    private boolean isManagger;

    public Player(String name, String postion, String nationality, int number){
        this.name = name;
        this.postion = postion;
        this.nationality = nationality;
        this.number = number;
    }

    public void  setPostion(String postion){
        this.postion = postion;
    }

    public void setNationality(String nationality){
        this.nationality = nationality;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public String getNationality() { return nationality; }

    public String getName() {
        return name;
    }

    public String getPostion() {
        return postion;
    }

    public int getNumber() {
        return number;
    }
}
