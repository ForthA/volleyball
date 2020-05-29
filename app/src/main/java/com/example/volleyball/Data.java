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
    private static final int DB_VERSION = 4;
    private static final String TABLE_GAME = "games";
    private static final String DATE = "date";
    private static final String MATCH_ROW_TEAM_FIRST = "teamfirst";
    private static final String MATCH_ROW_TEAM_SECOND = "teamsecond";
    private static final String SCORE = "score";

    private static final String TABLE_ROUND = "round";
    private static final String ROUND_MATCH_ID = "match_id";
    private static final String TABLE_ROW_FIRST = "teamFirstBOOL";
    private static final String TABLE_ROW_SECOND = "teamSecondBOOL";

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
        contentValues.put(ROUND_MATCH_ID, round.matchid);
        contentValues.put(TABLE_ROW_FIRST,tm);
        contentValues.put(TABLE_ROW_SECOND, sm);
        db.insert(TABLE_ROUND,null,contentValues);
    }
    public void insertGame(GameInfo gameInfo){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DATE,gameInfo.date);
        contentValues.put(MATCH_ROW_TEAM_FIRST,gameInfo.firstTeam);
        contentValues.put(SCORE,gameInfo.score);
        contentValues.put(MATCH_ROW_TEAM_SECOND,gameInfo.secondTeam);
        db.insert(TABLE_GAME,null,contentValues);

    }
   public ArrayList<Round> writeRounds(int id){
    ArrayList<Round> rounds = new ArrayList<>();
       Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ROUND + "WHERE match_id = " + id + "",null);
       if (cursor.moveToFirst()) {
           int matchid = cursor.getColumnIndex(ROUND_MATCH_ID);
           int team1Index = cursor.getColumnIndex(TABLE_ROW_FIRST);
           int team2Index = cursor.getColumnIndex(TABLE_ROW_SECOND);
           do {
               boolean tm = true;
               boolean sm = false;
               if (cursor.getInt(team1Index) == 1) {
                   tm = true;
                   sm = false;
               }
               else {
                   sm = true;
                   tm = false;
               }
               rounds.add(new Round(1,cursor.getInt(matchid),tm,sm));
           } while (cursor.moveToNext());
       } else
           Log.d("mLog","0 rows");

       cursor.close();
       return rounds;

   }
   public ArrayList<GameInfo> writeGames(){
       Cursor cursor = db.query(TABLE_GAME, null, null, null, null, null, null);
       ArrayList<GameInfo> gameInfos = new ArrayList<>();
       if (cursor.moveToFirst()) {
           int dateIndex = cursor.getColumnIndex(DATE);
           int team1Index = cursor.getColumnIndex(MATCH_ROW_TEAM_FIRST);
           int scoreIndex = cursor.getColumnIndex(SCORE);
           int team2Index = cursor.getColumnIndex(MATCH_ROW_TEAM_SECOND);
           do {
               gameInfos.add(new GameInfo(cursor.getString(dateIndex),cursor.getString(team1Index),cursor.getString(scoreIndex),cursor.getString(team2Index)));
           } while (cursor.moveToNext());
       } else
           Log.d("mLog","0 rows");

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
                    + TABLE_ROW_FIRST + " integer not null,"
                    + TABLE_ROW_SECOND + " integer not null)";
            db.execSQL(newTableRoundQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists " + TABLE_GAME);

            onCreate(db);
        }
    }
}
