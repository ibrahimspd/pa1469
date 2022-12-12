package com.example.myapplication.Model;

public class Player {

    private String username;
    private String position;
    private int course_image;

    // Constructor
    public Player(String username, String position, int course_image) {
        this.username = username;
        this.position = position;
        this.course_image = course_image;
    }

    // Getter and Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getCourse_image() {
        return course_image;
    }

    public void setCourse_image(int course_image) {
        this.course_image = course_image;
    }
}
