package com.example.dutmed;
import android.app.Application;
import android.util.Log;


public class DutMedApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize anything here for the whole app
        initializeAdminAccounts();
    }

    private void initializeAdminAccounts() {
        DutMedDbHelper dbHelper = new DutMedDbHelper(this);
        try {
            String adminEmail = "Admin@dut.com";
            String adminPassword = "12345";
            String salt = dbHelper.generateSalt();
            String hashedPassword = dbHelper.hashPassword(adminPassword, salt);
            String adminFirstName = "Fantastic";
            String adminLastName = "Admin";

            if (!dbHelper.adminExists(adminEmail)) {
                long adminId = dbHelper.insertAdminUser(adminEmail, hashedPassword, salt, adminFirstName, adminLastName);
                if (adminId == -1) {
                    Log.e("DutMedApplication", "Failed to insert new admin user.");
                } else {
                    Log.i("DutMedApplication", "New admin user inserted successfully.");
                }
            }
        } catch (Exception e) {
            Log.e("DutMedApplication", "Error initializing admin accounts", e);
        } finally {
            dbHelper.close();
        }
    }


}


