package com.example.dutmed;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;
import java.util.Random;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DutMedDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DutMed.db";
    private static final int DATABASE_VERSION = 4;
    private Context context; // Add this line to store the context

    // Table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_PROFILES = "profiles";
    public static final String TABLE_APPOINTMENTS = "appointments";
    public static final String TABLE_SYMPTOM_CHECKER = "symptom_checker";
    public static final String TABLE_HEALTH_RESOURCES = "health_resources";
    public static final String TABLE_FEEDBACK = "feedback";
    private static final String COLUMN_SALT = "salt";

    // Users table columns
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_ROLE = "role";

    // Profiles table columns
    public static final String COLUMN_PROFILE_ID = "profile_id";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_CURRENT_TREATMENTS = "current_treatments";
    public static final String COLUMN_ALLERGIES = "allergies";

    // Appointments table columns
    public static final String COLUMN_APPOINTMENT_ID = "appointment_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME_SLOT = "time_slot";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_DOCTOR_ASSIGNED = "doctor_assigned";
    public static final String COLUMN_DIAGNOSIS = "diagnosis";
    public static final String COLUMN_PRESCRIBED_MEDICATION = "prescribed_medication";

    // Symptom Checker table columns
    public static final String COLUMN_ENTRY_ID = "entry_id";
    public static final String COLUMN_SYMPTOMS = "symptoms";
    public static final String COLUMN_SEVERITY_SCORE = "severity_score";
    public static final String COLUMN_ADVICE = "advice";

    // Health Resources table columns
    public static final String COLUMN_RESOURCE_ID = "resource_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_IMAGE_URL = "image_url";

    // Feedback table columns
    public static final String COLUMN_FEEDBACK_ID = "feedback_id";
    public static final String COLUMN_FEEDBACK_TEXT = "feedback_text";
    public static final String COLUMN_SUGGESTIONS = "suggestions";
    public static final String COLUMN_DATE_SUBMITTED = "date_submitted";

    // SQL statements to create tables
    private static final String CREATE_TABLE_USERS = "CREATE TABLE users (" +
            "user_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "email TEXT UNIQUE NOT NULL, " +
            "password TEXT NOT NULL, " +
            "salt TEXT NOT NULL, " + // Ensure this line is added
            "first_name TEXT, " +
            "last_name TEXT, " +
            "role TEXT)";

    private static final String CREATE_TABLE_PROFILES = "CREATE TABLE profiles (" +
            "profile_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "user_id INTEGER NOT NULL, " +
            "gender TEXT, " +
            "age INTEGER, " +
            "current_treatments TEXT, " +
            "allergies TEXT, " +
            "FOREIGN KEY(user_id) REFERENCES users(user_id))";

    private static final String CREATE_TABLE_APPOINTMENTS = "CREATE TABLE appointments (" +
            "appointment_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "user_id INTEGER NOT NULL, " +
            "date TEXT NOT NULL, " +
            "time_slot TEXT NOT NULL, " +
            "status TEXT NOT NULL, " +
            "doctor_assigned TEXT, " +
            "diagnosis TEXT, " +
            "prescribed_medication TEXT, " +
            "FOREIGN KEY(user_id) REFERENCES users(user_id))";

    private static final String CREATE_TABLE_SYMPTOM_CHECKER = "CREATE TABLE symptom_checker (" +
            "entry_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "user_id INTEGER NOT NULL, " +
            "symptoms TEXT, " +
            "severity_score INTEGER, " +
            "advice TEXT, " +
            "FOREIGN KEY(user_id) REFERENCES users(user_id))";

    private static final String CREATE_TABLE_HEALTH_RESOURCES = "CREATE TABLE health_resources (" +
            "resource_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "title TEXT NOT NULL, " +
            "content TEXT NOT NULL, " +
            "type TEXT NOT NULL, " +
            "image_url TEXT)";

    private static final String CREATE_TABLE_FEEDBACK = "CREATE TABLE feedback (" +
            "feedback_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "user_id INTEGER NOT NULL, " +
            "entry_id INTEGER NOT NULL, " +
            "feedback_text TEXT, " +
            "suggestions TEXT, " +
            "date_submitted TEXT NOT NULL, " +
            "FOREIGN KEY(user_id) REFERENCES users(user_id), " +
            "FOREIGN KEY(entry_id) REFERENCES symptom_checker(entry_id))";

    //Constructor
    public DutMedDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context; // Store the context
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
    //void methods
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_PROFILES);
        db.execSQL(CREATE_TABLE_APPOINTMENTS);
        db.execSQL(CREATE_TABLE_SYMPTOM_CHECKER);
        db.execSQL(CREATE_TABLE_HEALTH_RESOURCES);
        db.execSQL(CREATE_TABLE_FEEDBACK);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Assume adding a new column to the 'users' table in version 2
            db.execSQL("ALTER TABLE users ADD COLUMN salt TEXT NOT NULL DEFAULT ''");
        }

        if (newVersion > oldVersion) {
            // Drop and recreate might be a last resort if incremental updates are not feasible
            dropAllTables(db);
            onCreate(db);
        }
    }

    private void dropAllTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS profiles");
        db.execSQL("DROP TABLE IF EXISTS appointments");
        db.execSQL("DROP TABLE IF EXISTS symptom_checker");
        db.execSQL("DROP TABLE IF EXISTS health_resources");
        db.execSQL("DROP TABLE IF EXISTS feedback");
    }


    String generateSalt() {
        Random random = new Random();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }

    public String hashPassword(String password, String salt) {
        // Ensure this method is public if called outside the DBHelper class
        try {
            password = salt + password; // Combine salt and password
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }


    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }



    //display
    public void displayHealthResources() {
        DutMedDbHelper dbHelper = new DutMedDbHelper(getContext());
        Cursor cursor = dbHelper.getAllHealthResources();
        if (cursor != null && cursor.moveToFirst()) {
            int titleIndex = cursor.getColumnIndex(DutMedDbHelper.COLUMN_TITLE);
            int contentIndex = cursor.getColumnIndex(DutMedDbHelper.COLUMN_CONTENT);

            // Check if either index is -1, which means the column does not exist in the cursor
            if (titleIndex == -1 || contentIndex == -1) {
                throw new IllegalArgumentException("The column does not exist in the cursor.");
            }
            cursor.close();
        }
        dbHelper.close();
    }


    public boolean checkUserCredentials(String email, String password, String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_PASSWORD, COLUMN_SALT};
        String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_ROLE + " = ?";
        String[] selectionArgs = {email, role};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
            int saltIndex = cursor.getColumnIndex(COLUMN_SALT);

            if (passwordIndex == -1 || saltIndex == -1) {
                cursor.close();
                db.close();
                return false;
            }

            String storedPassword = cursor.getString(passwordIndex);
            String salt = cursor.getString(saltIndex);
            cursor.close();
            db.close();

            return storedPassword.equals(hashPassword(password, salt));
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return false;
    }


    //void classes (add)

    public long insertUser(String email, String password, String role, String firstName, String lastName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Generate a new salt
        String salt = generateSalt();

        // Hash the password along with the salt
        String hashedPassword = hashPassword(password, salt);

        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, hashedPassword);
        values.put(COLUMN_SALT, salt); // Correctly store the generated salt
        values.put(COLUMN_ROLE, role);
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);

        long id = db.insert(TABLE_USERS, null, values);
        db.close(); // Close the database connection to prevent leaks
        return id;
    }




    public long addProfile(int userId, String gender, int age, String treatments, String allergies) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_GENDER, gender);
        values.put(COLUMN_AGE, age);
        values.put(COLUMN_CURRENT_TREATMENTS, treatments);
        values.put(COLUMN_ALLERGIES, allergies);

        long result = db.insert(TABLE_PROFILES, null, values);
        db.close();
        return result;
    }

    public long addSymptomData(int userId, String symptoms, int severityScore, String advice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_SYMPTOMS, symptoms);
        values.put(COLUMN_SEVERITY_SCORE, severityScore);
        values.put(COLUMN_ADVICE, advice);

        long result = db.insert(TABLE_SYMPTOM_CHECKER, null, values);
        db.close();
        return result;
    }

    public long addHealthResource(String title, String content, String type, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_CONTENT, content);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_IMAGE_URL, imageUrl);

        long result = db.insert(TABLE_HEALTH_RESOURCES, null, values);
        db.close();
        return result;
    }

    public long addFeedback(int userId, int entryId, String feedbackText, String suggestions, String dateSubmitted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_ENTRY_ID, entryId);
        values.put(COLUMN_FEEDBACK_TEXT, feedbackText);
        values.put(COLUMN_SUGGESTIONS, suggestions);
        values.put(COLUMN_DATE_SUBMITTED, dateSubmitted);

        long result = db.insert(TABLE_FEEDBACK, null, values);
        db.close();
        return result;
    }

    public long insertAdminUser(String email, String hashedPassword, String salt, String firstName, String lastName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, hashedPassword);
        values.put(COLUMN_SALT, salt);
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_ROLE, "admin"); // Assuming the role is hardcoded as 'admin' for this method

        long userId = db.insert(TABLE_USERS, null, values);
        db.close();
        return userId;
    }


    public boolean adminExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID},
                COLUMN_EMAIL + " = ? AND " + COLUMN_ROLE + " = 'admin'",
                new String[]{email}, null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }





    //retrieval (get)
    @SuppressLint("Range")
    public String getSaltByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String salt = null;
        Cursor cursor = db.query(
                TABLE_USERS,         // The table to query
                new String[]{COLUMN_SALT}, // The columns to return
                COLUMN_EMAIL + " = ?", // The columns for the WHERE clause
                new String[]{email}, // The values for the WHERE clause
                null,  // don't group the rows
                null,  // don't filter by row groups
                null   // The sort order
        );

        if (cursor != null && cursor.moveToFirst()) {
            salt = cursor.getString(cursor.getColumnIndex(COLUMN_SALT));
            cursor.close();
        }
        db.close();
        return salt;
    }


    public Cursor getProfileByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_PROFILE_ID, COLUMN_GENDER, COLUMN_AGE, COLUMN_CURRENT_TREATMENTS, COLUMN_ALLERGIES};
        String selection = COLUMN_USER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        return db.query(TABLE_PROFILES, columns, selection, selectionArgs, null, null, null);
    }
    public Cursor getAllFeedback() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_FEEDBACK, null, null, null, null, null, null);
    }

    public Cursor getAllHealthResources() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_HEALTH_RESOURCES, null, null, null, null, null, null);
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}



