package com.example.volleyball;

class Round {
    int n;
    long time;
    long match_id;
    boolean team1;
    boolean team2;
    float x;
    float y;

    Round(boolean team1, boolean team2, long tsm) {
        this.team1 = team1;
        this.team2 = team2;
        this.time = tsm;
    }

    Round(int n, int ts, boolean team1, boolean team2) {
        this.n = n;
        this.time = ts;
        this.team1 = team1;
        this.team2 = team2;
    }

    public void setMatchId(long match_id) {
        this.match_id = match_id;
    }
}
