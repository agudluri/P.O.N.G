package com.cs442.svaccaro.pong;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;

import java.util.*;

public class DBHandler extends SQLiteOpenHelper
{
    Context context;
    String myLog="DBHANDLER";
    // Database Info
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MainPageListView";

    // Tables
    private static final String TABLE_SAVES = "SavedSearches";
    // Owner Menu View Columns
    private static final String KEY_ID="SearchID";
    private static final String GAME_NAME = "GameName";
    private static final String GAME_ID="GameID";

    // Tables
    private static final String TABLE_TOP = "TopRated";
    // Owner Menu View Columns
    private static final String ID="NumID";
    private static final String TOP_GAME_NAME = "GameName";
    private static final String TOP_GAME_ID="GameID";

    public DBHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_TABLE_SAVES_TABLE ="CREATE TABLE IF NOT EXISTS " + TABLE_SAVES
                + "("
                +KEY_ID+" INT PRIMARY KEY,"
                +GAME_ID+" INT,"
                +GAME_NAME +" TEXT "
                +")";
        db.execSQL(CREATE_TABLE_SAVES_TABLE);

        String CREATE_TABLE_TOP_TABLE ="CREATE TABLE IF NOT EXISTS " + TABLE_TOP
                + "("
                +TOP_GAME_NAME+" INT PRIMARY KEY,"
                +ID+" INT,"
                +TOP_GAME_ID +" TEXT "
                +")";
        db.execSQL(CREATE_TABLE_TOP_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop Tables
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_SAVES);
        // Recreat Tables
        onCreate(db);
    }

    //Add to Saves
    public void addSave(Game game)
    {
        Log.i(myLog,"addSave()");
        int numSaved=countSaves();
        Log.i(myLog,"SAVED NEW: "+numSaved);
        String gameToSave=game.getGamename();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(numSaved == 0)//If none added
        {
            int newMax=numSaved+1;
            values.put(KEY_ID, newMax);
            values.put(GAME_NAME, gameToSave);
            values.put(GAME_ID,game.getDbID());
            db.insert(TABLE_SAVES, null, values);
            Log.i(myLog,"SAVED NEW: "+newMax);
        }
        else {
            String max = "SELECT MAX(" + KEY_ID + ") FROM " + TABLE_SAVES;
            Cursor cursor1 = db.rawQuery(max, null);
            int nextIndx = -1;
            if (cursor1.moveToFirst()) {
                nextIndx = Integer.parseInt(cursor1.getString(0));
            } else {
                Log.i(myLog, "ERROR: MAX VALUE NOT FOUND");
            }

            if (IsSaved(gameToSave))//If Game is Already a Recent
            {
                values.put(KEY_ID, nextIndx + 1);
                values.put(GAME_NAME, gameToSave);
                values.put(GAME_ID, game.getDbID());
                db.update(TABLE_SAVES, values, GAME_NAME + " = ?", new String[]{gameToSave});
            } else if (numSaved <= 10)//Else if New and < 10 Saves
            {
                int newMax = nextIndx + 1;
                values.put(KEY_ID, newMax);
                values.put(GAME_NAME, gameToSave);
                values.put(GAME_ID, game.getDbID());
                db.insert(TABLE_SAVES, null, values);
                Log.i(myLog, "SAVED NEW: " + newMax);
            } else //If New and >10 Saves
            {
                String toRemove = "SELECT MIN(" + KEY_ID + ") FROM " + TABLE_SAVES;
                Cursor cursor2 = db.rawQuery(toRemove, null);
                int got = -1;
                if (cursor2.moveToFirst()) {
                    got = Integer.parseInt(cursor2.getString(0));
                }
                int newMax = nextIndx + 1;
                values.put(KEY_ID, newMax);
                values.put(GAME_NAME, gameToSave);
                values.put(GAME_ID, game.getDbID());
                db.update(TABLE_SAVES, values, KEY_ID + " = ?", new String[]{cursor2.getString(0)});
                Log.i(myLog, "OVERWRITTEN: " + got + " TO " + newMax);
            }
        }
        db.close();
    }

    //Retrieve Saves
    public ArrayList<String> getSaves()
{
    ArrayList<String>savesgot=new ArrayList();
    SQLiteDatabase db = this.getWritableDatabase();
    String getAll = "SELECT "+GAME_NAME+" FROM " + TABLE_SAVES;
    Cursor cursor2 = db.rawQuery(getAll, null);
    if (cursor2.moveToFirst())
    {
        do
        {
            savesgot.add(cursor2.getString(0));
        } while (cursor2.moveToNext());
    }
    return savesgot;
}

    //Added new method
    public ArrayList<Game> getSavedGames()
    {
        ArrayList<Game>savesgot=new ArrayList();
        SQLiteDatabase db = this.getWritableDatabase();
        String getAll = "SELECT * FROM " + TABLE_SAVES;
        Cursor cursor2 = db.rawQuery(getAll, null);
        if (cursor2.moveToFirst())
        {
            do
            {
                Game game = new Game();
                game.setGamename(cursor2.getString(2));
                game.setDbID(Integer.valueOf(cursor2.getString(1)));
                savesgot.add(game);
            } while (cursor2.moveToNext());
        }
        return savesgot;
    }
    //Retrieve Saves
    public int countSaves()
    {
        Log.i(myLog,"getNumOrders()");
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT DISTINCT "+KEY_ID+ " FROM " + TABLE_SAVES;
        Cursor cursor = db.rawQuery(countQuery, null);
        int numGames=cursor.getCount();
        cursor.close();
        return numGames;
    }

    //Modified
    public boolean IsSaved(String game)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String check = "SELECT "+GAME_NAME+ " FROM " +TABLE_SAVES+ " WHERE "+GAME_NAME+"=?";
        Cursor cursor = db.rawQuery(check, new String[]{game});
        if (cursor.moveToFirst())
        {
            return true;
        }
        return false;
    }


    public int TopExists()
    {
        Log.i(myLog,"getNumOrders()");
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT DISTINCT "+ID+ " FROM " + TABLE_TOP;
        Cursor cursor = db.rawQuery(countQuery, null);
        int numGames=cursor.getCount();
        cursor.close();
        return numGames;
    }

    public void addTopRated(ArrayList<Game>topRated)
    {
        int checkExists=TopExists();
        for(int i = 0;i<topRated.size();i++)
        {
            Game g = topRated.get(i);
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ID, i);
            values.put(TOP_GAME_NAME,g.getGamename() );
            values.put(TOP_GAME_ID,g.getDbID());
            if(checkExists>0)
            {
                db.update(TABLE_TOP, values, ID + " = ?", new String[]{String.valueOf(i)});
            }
            else
            {
                db.insert(TABLE_TOP, null, values);
            }

        }
    }
}


