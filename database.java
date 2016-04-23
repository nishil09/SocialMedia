package com.example.nishil09.socialmedia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
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


    // Common column names
    private static final String USER_ID = "Number";
    private static final String USER_NAME = "Name";

    // NOTES Table - column nmaes
    private static final String USER_CHAT_ID = "Number";
    private static final String FILE_PATH = "Path";
    private static final String TimeStamp = "TimeStamp";
    private static final String DIRECTION = "Direction";

    // TAGS Table - column names
    private static final String LAST_SYNC = "Lastsync";

    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_TODO = "CREATE TABLE "
            + TABLE_USER + " (" + USER_ID + " TEXT PRIMARY KEY," + USER_NAME
            + " TEXT" + ")";

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
       // int x = addDataToSync("0");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Chat);
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
    HashMap<String,String> SelectDataFromUser() {



        HashMap<String,String> map = new HashMap<String,String>();
        String que = "SELECT Path from User";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cur = db.rawQuery(que, null);
        Log.i("DATA", "" + cur.getCount());
        try {
            int i = 0;
            String[] ar = new String[cur.getCount()];
            cur.moveToFirst();

            while (i < cur.getCount()) {
               map.put(cur.getString(0),cur.getString(1));
                Log.i("DATA", cur.getString(0));
                cur.moveToNext();
                i++;
            }
            return map;
        }
        catch(Exception e)
        {
            Log.i("Errorr",e.toString());
        }
        return map;
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
