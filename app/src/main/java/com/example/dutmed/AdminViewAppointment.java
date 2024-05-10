package com.example.dutmed;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdminViewAppointment extends AppCompatActivity {

    private AppointmentsAdapter appointmentsAdapter;
    private DutMedDbHelper dbHelper;
    private Spinner campusSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_booking);

        RecyclerView recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        campusSpinner = findViewById(R.id.campusSpinner);
        Button refreshButton = findViewById(R.id.refreshButton);

        dbHelper = new DutMedDbHelper(this);

        // Initialize adapter with empty list or default campus
        appointmentsAdapter = new AppointmentsAdapter(new ArrayList<>());
        recyclerView.setAdapter(appointmentsAdapter);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.campus_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campusSpinner.setAdapter(adapter);

        // Default selection handling
        campusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCampus = parent.getItemAtPosition(position).toString();
                filterAppointmentsByCampus(selectedCampus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case where no selection is made if needed
            }
        });

        refreshButton.setOnClickListener(v -> {
            String selectedCampus = campusSpinner.getSelectedItem().toString();
            filterAppointmentsByCampus(selectedCampus);
        });

        // Initial load with the default selected campus or the first item in the spinner
        if (campusSpinner.getSelectedItem() != null) {
            filterAppointmentsByCampus(campusSpinner.getSelectedItem().toString());
        }
    }

    private void filterAppointmentsByCampus(String campus) {
        List<Appointment> filteredBookings = dbHelper.getAppointmentsByCampus(campus);
        appointmentsAdapter.updateData(filteredBookings);
    }
}
