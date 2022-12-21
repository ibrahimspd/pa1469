package com.example.myapplication.entites;

public class Player
{
    private final String name;
    private String postion;
    private String avatar;
    private String nationality;
    private int number;
    private int team;
    private boolean isManagger;

    public Player(PlayerBuilder builder) {
        this.name = builder.name;
        this.postion = builder.postion;
        this.avatar = builder.avatar;
        this.nationality = builder. nationality;
        this.number = builder.number;
        this.team = builder.team;
        this.isManagger = builder.isManagger;
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

    public void setTeam(int team){
        this.team = team;
    }

    public void setIsManagger(boolean isManagger){
        this.isManagger = isManagger;
    }

    public String getAvatar(){
        return avatar;
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

    // create builder class
    public static class PlayerBuilder{
        private String name;
        private String postion;
        private String avatar;
        private String nationality;
        private int number;
        private int team;
        private boolean isManagger;

        public PlayerBuilder setName(String name){
            this.name = name;
            return this;
        }

        public PlayerBuilder setPostion(String postion){
            this.postion = postion;
            return this;
        }

        public PlayerBuilder setAvatar(String avatar){
            this.avatar = avatar;
            return this;
        }

        public PlayerBuilder setNationality(String nationality){
            this.nationality = nationality;
            return this;
        }

        public PlayerBuilder setNumber(int number){
            this.number = number;
            return this;
        }

        public PlayerBuilder setTeam(int team){
            this.team = team;
            return this;
        }

        public PlayerBuilder setIsManagger(boolean isManagger){
            this.isManagger = isManagger;
            return this;
        }

        public Player build(){
            return new Player(this);
        }

    }


}
