package com.example.volleyball;

public class GameInfo {
    int id;
     String date;
     String firstTeam;
     String score;
     String secondTeam;
     int leftScore;
     int rightScore;
     long timeMillis;


    GameInfo(String date,String firstTeam,String score,String secondTeam){
        this.date = date;
        this.firstTeam = firstTeam;
        this.score = score;
        this.secondTeam = secondTeam;
    }

    public GameInfo(String team1Name, String team2Name) {
        this.firstTeam = team1Name;
        this.secondTeam = team2Name;
    }

    public GameInfo(int id, String string, String string1, String string2, String string3) {
        this(string, string1, string2, string3);
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public String getFirstTeam() {
        return firstTeam;
    }

    public String getScore() {
        return score;
    }

    public String getSecondTeam() {
        return secondTeam;
    }

    public void setTime(long timeMillis) {
        this.timeMillis = timeMillis;
    }
}
