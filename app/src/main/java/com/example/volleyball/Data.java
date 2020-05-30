package com.example.volleyball;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

class Data {

    private SQLiteDatabase db;

    public static final String TABLE_ROW_ID = "_id";
    public static final String TABLE_ROW_TSM = "tsm";

    private static final String DB_NAME = "volleyball_db";
    private static final int DB_VERSION = 9;
    private static final String TABLE_GAME = "games";
    private static final String DATE = "date";
    private static final String MATCH_ROW_TEAM_FIRST = "teamfirst";
    private static final String MATCH_ROW_TEAM_SECOND = "teamsecond";
    private static final String SCORE = "score";

    private static final String TABLE_ROUND = "round";
    private static final String ROUND_MATCH_ID = "match_id";
    private static final String ROUND_TEAM1 = "team1";
    private static final String ROUND_TEAM2 = "team2";

    public Data(Context context) {
        CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    public void insertRound(Round round) {
        int tm = 1;
        int sm = 0;
        if (round.team1 == true) {
            tm = 1;
            sm = 0;
        }
        else {
            sm = 1;
            tm = 0;
        }
        ContentValues contentValues = new ContentValues();

        contentValues.put(ROUND_MATCH_ID, round.match_id);
        contentValues.put(ROUND_TEAM1, round.team1);
        contentValues.put(ROUND_TEAM2, round.team2);
        contentValues.put(TABLE_ROW_TSM, round.time);

        db.insert(TABLE_ROUND, null, contentValues);

    }

    public long insertGame(GameInfo gameInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE, gameInfo.date);
        contentValues.put(MATCH_ROW_TEAM_FIRST, gameInfo.firstTeam);
        contentValues.put(SCORE, gameInfo.score);
        contentValues.put(MATCH_ROW_TEAM_SECOND, gameInfo.secondTeam);

        return db.insert(TABLE_GAME, null, contentValues);

    }

    public ArrayList<Round> getRounds(long match_id){
        ArrayList<Round> rounds = new ArrayList<>();

        Cursor cursor = db.query(TABLE_ROUND, null, "match_id = ?", new String[]{"" + match_id}, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(TABLE_ROW_ID);
            int team1Index = cursor.getColumnIndex(ROUND_TEAM1);
            int team2Index = cursor.getColumnIndex(ROUND_TEAM2);
            int tsmIndex = cursor.getColumnIndex(TABLE_ROW_TSM);
            do {
                int id = Integer.parseInt(cursor.getString(idIndex));
                Boolean team1 = cursor.getString(team1Index).equals("1");
                Boolean team2 = cursor.getString(team2Index).equals("1");
                long tsm = Long.parseLong(cursor.getString(tsmIndex));

                rounds.add(new Round(id, team1, team2, tsm));
            } while (cursor.moveToNext());
        }

        return rounds;
    }

    public ArrayList<GameInfo> writeGames(){
        Cursor cursor = db.query(TABLE_GAME, null, null, null, null, null, null);
        ArrayList<GameInfo> gameInfos = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(TABLE_ROW_ID);
            int dateIndex = cursor.getColumnIndex(DATE);
            int team1Index = cursor.getColumnIndex(MATCH_ROW_TEAM_FIRST);
            int scoreIndex = cursor.getColumnIndex(SCORE);
            int team2Index = cursor.getColumnIndex(MATCH_ROW_TEAM_SECOND);
            do {
                int id = Integer.parseInt(cursor.getString(idIndex));
                gameInfos.add(new GameInfo(id, cursor.getString(dateIndex), cursor.getString(team1Index), cursor.getString(scoreIndex), cursor.getString(team2Index)));
            } while (cursor.moveToNext());
        } else {
            Log.d("mLog", "0 rows");
        }

        cursor.close();
        return gameInfos;
    }

    private class CustomSQLiteOpenHelper extends SQLiteOpenHelper {
        public CustomSQLiteOpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String newTableMatchQuery = "create table "
                    + TABLE_GAME + " ("
                    + TABLE_ROW_ID + " integer primary key autoincrement not null, "
                    + DATE + " text,"
                    + MATCH_ROW_TEAM_FIRST + " text,"
                    + SCORE + " text,"
                    + MATCH_ROW_TEAM_SECOND + " text)";
            db.execSQL(newTableMatchQuery);

            String newTableRoundQuery = "create table "
                    + TABLE_ROUND + "("
                    + TABLE_ROW_ID + " integer primary key autoincrement not null, "
                    + ROUND_MATCH_ID + " integer not null, "
                    + ROUND_TEAM1 + " integer not null, "
                    + ROUND_TEAM2 + " integer not null, "
                    + TABLE_ROW_TSM + " int8 not null)";
            db.execSQL(newTableRoundQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists " + TABLE_GAME);
            db.execSQL("drop table if exists " + TABLE_ROUND);

            onCreate(db);
        }
    }
}
