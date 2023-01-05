package com.example.myapplication.entites;

public class Player
{
    private String name;
    private String position;
    private String avatar;
    private String nationality;
    private int number;
    private int team;
    private boolean isManager;
    private String uuid;
    private String id;
    private String teamId;

    public Player(){}

    public Player(PlayerBuilder builder) {
        this.name = builder.name;
        this.position = builder.position;
        this.avatar = builder.avatar;
        this.nationality = builder. nationality;
        this.number = builder.number;
        this.team = builder.team;
        this.isManager = builder.isManager;
        this.uuid = builder.uuid;
        this.id = builder.id;
        this.teamId = builder.teamId;
    }

    public void  setPosition(String position){
        this.position = position;
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

    public void setIsManager(boolean isManager){
        this.isManager = isManager;
    }

    public String getAvatar(){
        return avatar;
    }

    public void setAvatar(String avatar){
        this.avatar = avatar;
    }

    public String getNationality() { return nationality; }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public int getNumber() {
        return number;
    }

    public int getTeam() {
        return team;
    }

    public boolean getIsManager() {
        return isManager;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public static class PlayerBuilder{
        private String name;
        private String position;
        private String avatar;
        private String nationality;
        private int number;
        private int team;
        private boolean isManager;
        private String uuid;
        private String id;
        private String teamId;

        public PlayerBuilder setName(String name){
            this.name = name;
            return this;
        }

        public PlayerBuilder setPosition(String position){
            this.position = position;
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

        public PlayerBuilder setIsManager(boolean isManager){
            this.isManager = isManager;
            return this;
        }

        public PlayerBuilder setUuid(String uuid){
            this.uuid = uuid;
            return this;
        }

        public PlayerBuilder setId(String id){
            this.id = id;
            return this;
        }

        public PlayerBuilder setTeamId(String teamId){
            this.teamId = teamId;
            return this;
        }

        public Player build(){
            return new Player(this);
        }
    }
}
