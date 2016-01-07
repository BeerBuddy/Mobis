package de.fh_dortmund.beerbuddy_44.dao.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;

public class BeerBuddyDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int BOOLEAN_TRUE = 0;
    public static final int BOOLEAN_FALSE= 1;
    public static final SimpleDateFormat DATE_FORMAT= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BeerBuddy.db";
    public static String SQL_DELETE_ENTRIES =
            "DROP TABLE friendlistperson;" +
                    "DROP TABLE friendinvitation;" +
                    "DROP TABLE drinkinginvitation;" +
                    "DROP TABLE drinkingspotperson;" +
                    "DROP TABLE drinkingspot;" +
                    "DROP TABLE friendlist;" +
                    "DROP TABLE person;";


    public static String SQL_CREATE_PERSON =
            "CREATE TABLE person (" +
                    "id REAL PRIMARY KEY AUTOINCREMENT," +
                    "email TEXT," +
                    "username TEXT," +
                    "image BLOB," +
                    "password TEXT" +
                    "gender INTEGER" +
                    "dateOfBirth TEXT" +
                    "interests TEXT" +
                    "prefers TEXT" +
                    " );";
    public static String SQL_CREATE_FriendList =
            "CREATE TABLE friendlist (" +
                    "id REAL PRIMARY KEY AUTOINCREMENT," +
                    "personid REAL," +
                    "FOREIGN KEY(id) REFERENCES person(id)" +
                    " );";
    public static String SQL_CREATE_FriendListPerson =
            "CREATE TABLE friendlistperson (" +
                    "id REAL PRIMARY KEY AUTOINCREMENT," +
                    "friendlistid REAL," +
                    "personid REAL," +
                    "FOREIGN KEY(personid) REFERENCES person(id)," +
                    "FOREIGN KEY(friendlistid) REFERENCES friendlist(id)" +
                    " );";
    public static String SQL_CREATE_FriendInvitation =
            "CREATE TABLE friendinvitation (" +
                    "id REAL PRIMARY KEY AUTOINCREMENT," +
                    "einladerId REAL," +
                    "eingeladenerId REAL," +
                    "freitext TEXT," +
                    "FOREIGN KEY(einladerId) REFERENCES person(id)," +
                    "FOREIGN KEY(eingeladenerId) REFERENCES person(id)" +
                    " );";
    public static String SQL_CREATE_DrinkingInvitation =
            "CREATE TABLE drinkinginvitation (" +
                    "id REAL PRIMARY KEY AUTOINCREMENT," +
                    "einladerId REAL," +
                    "eingeladenerId REAL," +
                    "drinkingSpotId REAL," +
                    "freitext TEXT," +
                    "FOREIGN KEY(einladerId) REFERENCES person(id)," +
                    "FOREIGN KEY(eingeladenerId) REFERENCES person(id)" +
                    "FOREIGN KEY(drinkingSpotId) REFERENCES drinkingspot(id)" +
                    " );";
    public static String SQL_CREATE_DrinkingSpot =
            "CREATE TABLE drinkingspot (" +
                    "id REAL PRIMARY KEY AUTOINCREMENT," +
                    "creatorid REAL," +
                    "beschreibung TEXT," +
                    "startTime TEXT," +
                    "ageFrom INTEGER," +
                    "ageTo INTEGER," +
                    "gps TEXT," +
                    "amountMaleWithoutBeerBuddy INTEGER," +
                    "amountFemaleWithoutBeerBuddy INTEGER," +
                    "active INTEGER," +
                    "FOREIGN KEY(creatorid) REFERENCES person(id)" +
                    " );";
    public static String SQL_CREATE_DrinkingSpotPerson =
            "CREATE TABLE drinkingspotperson (" +
                    "id REAL PRIMARY KEY AUTOINCREMENT," +
                    "drinkingSpotId REAL," +
                    "personid REAL," +
                    "FOREIGN KEY(drinkingSpotId) REFERENCES drinkingspot(id)," +
                    "FOREIGN KEY(personid) REFERENCES person(id)" +
                    " );";


    public BeerBuddyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_PERSON);
        db.execSQL(SQL_CREATE_FriendList);
        db.execSQL(SQL_CREATE_DrinkingSpot);
        db.execSQL(SQL_CREATE_DrinkingSpotPerson);
        db.execSQL(SQL_CREATE_DrinkingInvitation);
        db.execSQL(SQL_CREATE_FriendInvitation);
        db.execSQL(SQL_CREATE_FriendListPerson);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public SQLiteDatabase getDatabase() {
        return this.getWritableDatabase();
    }
}