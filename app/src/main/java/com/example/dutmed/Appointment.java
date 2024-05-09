package com.example.dutmed;

public class Appointment {
    private String campus;
    private String date;
    private String timeSlot;

    public Appointment(String campus, String date, String timeSlot) {
        this.campus = campus;
        this.date = date;
        this.timeSlot = timeSlot;
    }

    public String getCampus() {
        return campus;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return timeSlot;
    }
}
