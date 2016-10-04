package com.yvesmatta.hindgametracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.yvesmatta.hindgametracker.Models.Game;

public class DBOpenHelper extends SQLiteOpenHelper {

    // Constants for db name and version
    private static final String DATABASE_NAME = "hind.db";
    private static final int DATABASE_VERSION = 1;

    // Constant for all column queries
    public static final String[] ALL_COLUMNS = { "*" };

    // Constants for identifying table and columns
    // player
    public static final String TABLE_PLAYER = "player";
    public static final String PLAYER_ID = "_id";
    public static final String PLAYER_NAME = "name";
    public static final String PLAYER_TOTAL_SCORE = "total_score";
    public static final String PLAYER_CREATED = "created";

    // SQL to create the table
    private static final String PLAYER_TABLE_CREATE =
            "CREATE TABLE " + TABLE_PLAYER + " (" +
                    PLAYER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PLAYER_NAME + " TEXT NOT NULL, " +
                    PLAYER_TOTAL_SCORE + " INTEGER, " +
                    PLAYER_CREATED + "  TEXT" +
                    ")";

    // game
    public static final String TABLE_GAME = "game";
    public static final String GAME_ID = "_id";
    public static final String GAME_PLAYER_COUNT = "player_count";
    public static final String GAME_PLAYER_WINNER = "winner";
    public static final String GAME_PLAYER_COMPLETED = "completed";
    public static final String GAME_CREATED = "created";

    // Constructor
    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Execute create table queries in correct order
        sqLiteDatabase.execSQL(PLAYER_TABLE_CREATE);
        sqLiteDatabase.execSQL(generateGameTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Execute drop table queries in correct order
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_GAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER);

        // Create the database
        onCreate(sqLiteDatabase);
    }

    private String generateGameTableQuery() {
        // Player columns query string
        String playerColumns = "";

        // For the number of players, add query for each player column
        for (int i = 1; i <= Game.MAX_NUMBER_OF_PLAYERS; i++) {
           playerColumns += "player_" + i + " TEXT, ";
        }

        // Foreign key constraints query string
        String playerForeignKeyConstraints = "";

        // For the number of players, add foreign key constraint query to the player table
        for (int i = 1; i <= Game.MAX_NUMBER_OF_PLAYERS; i++) {
            if (i == Game.MAX_NUMBER_OF_PLAYERS) {
                playerForeignKeyConstraints += " FOREIGN KEY (player_" + i + ") REFERENCES " + TABLE_PLAYER + "(" + PLAYER_ID + ")";
            } else {
                playerForeignKeyConstraints += " FOREIGN KEY (player_"  + i + ") REFERENCES " + TABLE_PLAYER + "(" + PLAYER_ID + "), ";
            }
        }

        // return the game table query
        return "CREATE TABLE " + TABLE_GAME + " (" +
                GAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GAME_PLAYER_COUNT + " INTEGER NOT NULL, " +
                playerColumns +
                GAME_PLAYER_WINNER + " TEXT, " +
                GAME_PLAYER_COMPLETED + " BIT, " +
                GAME_CREATED + " TEXT, " +
                playerForeignKeyConstraints +
                ")";
    }
}
