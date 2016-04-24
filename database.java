package com.example.nishil09.socialmedia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by nishil09 on 4/2/16.
 */


public class database extends SQLiteOpenHelper {

    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "SocialMedia.db";

    // Table Names
    private static final String TABLE_USER = "User";
    private static final String TABLE_Chat = "Chat";
    private static final String TABLE_Sync = "Sync";
    private static final String TABLE_Group = "groupgroup";
    private static final String TABLE_Group_Chat = "GroupChat";


    // Common column names
    private static final String USER_ID = "Number";
    private static final String USER_NAME = "Name";

    // NOTES Table - column nmaes
    private static final String USER_CHAT_ID = "Number";
    private static final String FILE_PATH = "Path";
    private static final String TimeStamp = "TimeStamp";
    private static final String DIRECTION = "Direction";


    //Group Table Column
    private static final String GroupName = "Name";
    private static final String User1 = "Friend1";
    private static final String User2 = "Friend2";
    private static final String User3 = "Friend3";
    private static final String User4 = "Friend4";
    private static final String TimeStampGroup = "TimeStamp";

    //Group Table Chat

    private static final String GroupNameChat = "Name";
    private static final String GroupChatMessage = "Path";
    private static final String GroupMessageTimeStamp = "TimeStamp";
    // TAGS Table - column names
    private static final String LAST_SYNC = "Lastsync";

    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_TODO = "CREATE TABLE "
            + TABLE_USER + " (" + USER_ID + " TEXT," + USER_NAME
            + " TEXT" + ")";
//Create A Group
    private static final String CREATE_TABLE_GROUP= "CREATE TABLE "
            + TABLE_Group+ " (" + GroupName + " TEXT," + User1
            + " TEXT," + User2 + " TEXT," + User3 +  " TEXT,"+ User4 +  " TEXT,"+ TimeStampGroup +  " TEXT" + ")";

    // Create A group Chat

    private static final String CREATE_TABLE_GroupChat = "CREATE TABLE "
            + TABLE_Group_Chat + " (" + GroupNameChat + " TEXT," + GroupChatMessage
            + " TEXT"  + GroupMessageTimeStamp +  " TEXT"+ ")";
    // Tag table create statement

    private static final String CREATE_TABLE_TAG = "CREATE TABLE " + TABLE_Chat
            + " (" + USER_CHAT_ID + " TEXT," + FILE_PATH + " TEXT," + TimeStamp + " TEXT,"
            + DIRECTION + " TEXT" + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_SYNC = "CREATE TABLE " + TABLE_Sync
            + " (" + LAST_SYNC + " TEXT" + ")";

    public database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_TODO);
        db.execSQL(CREATE_TABLE_TAG);
        db.execSQL(CREATE_TABLE_SYNC);
        db.execSQL(CREATE_TABLE_GROUP);
        db.execSQL(CREATE_TABLE_GroupChat);

       // int x = addDataToSync("0");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Chat);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Sync);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Group);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Group_Chat);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO_TAG);

        // create new tables
        onCreate(db);
    }

    int addDataToUser(String Number,String Name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Number", Number);
        values.put("Name", Name);

        db.insertOrThrow("User", null, values);

        return 1;
    }
    int addDataToChat(String From,String Path,String Timestamp,String Direction)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Number", From);
        values.put("Path", Path);
        values.put("TimeStamp", Timestamp);
        values.put("Direction", Direction);
        db.insertOrThrow("Chat", null, values);

        return 1;
    }

    int addDataToGroup(String Name,String[] f1,String timestamp)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Name", Name);
        values.put("Friend1", f1[0]);
        values.put("Friend2", f1[1]);
        values.put("Friend3", f1[2]);
        values.put("Friend4", f1[3]);
        values.put("TimeStamp", timestamp);
        db.insertOrThrow("groupgroup", null, values);

        return 1;
    }


    int addDataToSync(String Timestamp)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put("Lastsync", Timestamp);

        db.insertOrThrow("Sync", null, values);

        return 1;
    }
    String[] SelectDataFromChat(String From) {


        String que = "SELECT Path from Chat where Number = '" + From + "' ORDER BY TimeStamp ASC";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cur = db.rawQuery(que, null);
        Log.i("DATA", "" + cur.getCount());
        try {
            int i = 0;
            String[] ar = new String[cur.getCount()];
            cur.moveToFirst();

            while (i < cur.getCount()) {
                ar[i] = cur.getString(0);
                Log.i("DATA", cur.getString(0));
                cur.moveToNext();
                i++;
            }
            return ar;
        }
        catch(Exception e)
        {
            Log.i("Errorr",e.toString());
        }
        return new String[]{};
    }
    String[] SelectDataFromGroup() {


        String que = "SELECT Name from groupgroup ORDER BY TimeStamp ASC";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cur = db.rawQuery(que, null);
        Log.i("DATA", "" + cur.getCount());
        try {
            int i = 0;
            String[] ar = new String[cur.getCount()];
            cur.moveToFirst();

            while (i < cur.getCount()) {
                ar[i] = cur.getString(0);
                Log.i("DATA", cur.getString(0));
                cur.moveToNext();
                i++;
            }
            return ar;
        }
        catch(Exception e)
        {
            Log.i("Errorr",e.toString());
        }
        return new String[]{};
    }
    helper SelectDataFromUser() {



        helper obj = new helper();
        obj.name = new HashSet<String>();
        obj.number = new HashSet<String>();
        HashMap<String,String> map = new HashMap<String,String>();
        String que = "SELECT Number,Name from User";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cur = db.rawQuery(que, null);
        Log.i("DATA", "" + cur.getCount());
        try {
            int i = 0;
            String[] ar = new String[cur.getCount()];
            cur.moveToFirst();

            while (i < cur.getCount()) {
                obj.number.add(cur.getString(0));
                obj.name.add(cur.getString(1));

                Log.i("DATA", cur.getString(0));
                cur.moveToNext();
                i++;
            }
            return obj;
        }
        catch(Exception e)
        {
            Log.i("Errorr",e.toString());
        }
        return obj;
    }
    String[] SelectDataFromSync() {


        String que = "SELECT Lastsync from Sync";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cur = db.rawQuery(que, null);
        Log.i("DATA", "" + cur.getCount());
        try {
            int i = 0;
            String[] ar = new String[cur.getCount()];
            cur.moveToFirst();

            while (i < cur.getCount()) {
                ar[i] = cur.getString(0);
                Log.i("DATA", cur.getString(0));
                cur.moveToNext();
                i++;
            }
            return ar;
        }
        catch(Exception e)
        {
            Log.i("Errorr",e.toString());
        }
        return new String[]{};
    }
}
