package com.example.dutmed;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class DutMedDbHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "DutMed.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_SYMPTOM_CHECKER = "symptom_checker";
    public static final String TABLE_HEALTH_RECORDS = "health_records";
    public static final String TABLE_FEEDBACK = "feedback";
    public static final String TABLE_HEALTH_RESOURCES = "health_resources";
    public static final String TABLE_APPOINTMENTS = "appointments";


    // Symptom Checker Table Columns
    public static final String COLUMN_QUESTION = "question";
    public static final String COLUMN_ANSWER = "answer";
    public static final String COLUMN_NEXT_QUESTION = "next_question";


    // Health Resources Table Columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_TYPE = "type"; // Article, Video, FAQ


    // Appointments Table Columns
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_STATUS = "status"; // Booked, Rescheduled, Cancelled


    // Health Records Table Columns
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_MEDICATION = "medication";
    public static final String COLUMN_BASIC_INFO = "basic_info";


    // Feedback Table Columns
    public static final String COLUMN_FEEDBACK_TEXT = "feedback_text";
    public static final String COLUMN_SUGGESTIONS = "suggestions";


    public static final String TABLE_USERS = "users";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE = "role"; // Admin, Patient

    private static final String CREATE_TABLE_USERS = "create table "
            + TABLE_USERS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_EMAIL
            + " text not null, " + COLUMN_PASSWORD + " text not null, "
            + COLUMN_ROLE + " text not null);";


    // Creating Additional Tables
    private static final String CREATE_TABLE_SYMPTOM_CHECKER = "create table "
            + TABLE_SYMPTOM_CHECKER + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_QUESTION
            + " text not null, " + COLUMN_ANSWER + " text not null, "
            + COLUMN_NEXT_QUESTION + " integer);";

    private static final String CREATE_TABLE_HEALTH_RECORDS = "create table "
            + TABLE_HEALTH_RECORDS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_USER_ID
            + " integer not null, " + COLUMN_MEDICATION + " text not null, "
            + COLUMN_BASIC_INFO + " text not null);";

    private static final String CREATE_TABLE_FEEDBACK = "create table "
            + TABLE_FEEDBACK + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_FEEDBACK_TEXT
            + " text not null, " + COLUMN_SUGGESTIONS + " text not null);";

    // Creating Tables
    private static final String CREATE_TABLE_HEALTH_RESOURCES = "create table "
            + TABLE_HEALTH_RESOURCES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TITLE
            + " text not null, " + COLUMN_CONTENT + " text not null, "
            + COLUMN_TYPE + " text not null);";

    private static final String CREATE_TABLE_APPOINTMENTS = "create table "
            + TABLE_APPOINTMENTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_DATE
            + " text not null, " + COLUMN_TIME + " text not null, "
            + COLUMN_STATUS + " text not null);";

    public DutMedDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DutMedDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_HEALTH_RESOURCES);
        db.execSQL(CREATE_TABLE_APPOINTMENTS);
        db.execSQL(CREATE_TABLE_SYMPTOM_CHECKER);
        db.execSQL(CREATE_TABLE_HEALTH_RECORDS);
        db.execSQL(CREATE_TABLE_FEEDBACK);

    }
    public long insertUser(String email, String password, String role, String firstName, String lastName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, hashPassword(password)); // Hashing password
        values.put(COLUMN_ROLE, role);

        long id = db.insert(TABLE_USERS, null, values);
        // Consider not closing the database here if you plan to perform more operations
        return id;
    }

    public long insertSymptomChecker(String question, String answer, int nextQuestion) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION, question);
        values.put(COLUMN_ANSWER, answer);
        values.put(COLUMN_NEXT_QUESTION, nextQuestion);

        long id = db.insert(TABLE_SYMPTOM_CHECKER, null, values);
        // Consider not closing the database here if you plan to perform more operations
        return id;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement logic to handle database upgrades
        if (oldVersion < 2) {
            // Perform schema migration if necessary
            db.execSQL("ALTER TABLE users ADD COLUMN new_column TEXT");
        }
    }

    private String hashPassword(String password) {
        String salt = "random_salt"; // This should be unique per user and securely stored.
        password = salt + password; // Append or prepend salt
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // Consider handling this more gracefully
        }
    }



    // Method to check if a username already exists (useful in registration to avoid duplicate usernames)
    public boolean checkUserExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { COLUMN_ID };
        String selection = COLUMN_EMAIL + " =?";
        String[] selectionArgs = { email };
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    // Method to verify user credentials (useful for login)
    public boolean checkUserCredentials(String email, String password, String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ? AND " + COLUMN_ROLE + " = ?";
        String[] selectionArgs = {email, hashPassword(password), role};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        boolean login = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return login;
    }


}



