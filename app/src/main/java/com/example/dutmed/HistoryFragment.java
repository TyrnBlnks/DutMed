package com.example.dutmed;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.Nullable;

public class HistoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // Initialize your button using the inflated view
        Button HistoryButton = view.findViewById(R.id.btnHistory);

        // Set onClickListener to navigate to the UserAppointmentActivity
        HistoryButton.setOnClickListener(v -> {
            // Navigate to Patient Login Activity
            Intent intent = new Intent(getActivity(), UserAppointmentActivity.class);
            intent.putExtra("userRole", "Patient"); // Add role as an extra in the intent
            startActivity(intent);
        });

        // Return the inflated view at the end
        return view;
    }
}

