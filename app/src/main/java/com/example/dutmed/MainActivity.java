package com.example.dutmed;

import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    // Delegate the back press to the system.
                    setEnabled(false);
                    onBackPressed();
                }
            }
        };

        // Note: It's crucial to add the callback to the OnBackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);


        // Set up the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Set up the ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Handle navigation item clicks here
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_history) {
                // Transition to the History fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HistoryFragment())
                        .addToBackStack(null)
                        .commit();
            } else if (id == R.id.nav_appointment) {
                // Transition to the Appointments fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AppointmentsFragment())
                        .addToBackStack(null)
                        .commit();
            } else if (id == R.id.nav_health_topics) {
                // Transition to the Health Topics fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new HealthTopicsFragment())
                        .addToBackStack(null)
                        .commit();
            } else if (id == R.id.nav_chatbot) {
                // Transition to the Clinics fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ChatbotFragment())
                        .addToBackStack(null)
                        .commit();
            } else if (id == R.id.nav_feedback) {
                // Transition to the Feedback fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new FeedbackFragment())
                        .addToBackStack(null)
                        .commit();
            } else if (id == R.id.nav_about) {
                // Transition to the About fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AboutFragment())
                        .addToBackStack(null)
                        .commit();
            } else if (id == R.id.nav_exit) {
                // Handle exit logic, such as closing the app
                finish();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

    }

}

