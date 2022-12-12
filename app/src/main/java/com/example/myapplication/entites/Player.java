package com.example.myapplication.entites;

public class Player
{
    private final String name;
    private final String postion;
    private final int number;
    public Player(String name, String postion, int number){
        this.name = name;
        this.postion = postion;
        this.number = number;
    }

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
