package com.example.dutmed;

public class Appointment {
    private int user_id;
    private String campus;
    private String date;
    private String timeSlot;

    public Appointment(int user_id, String campus, String date, String timeSlot) {
        this.user_id = user_id;
        this.campus = campus;
        this.date = date;
        this.timeSlot = timeSlot;
    }

    public int getUser_id() {return user_id;}
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
