package com.example.dutmed;


public class Appointment {
    private int id, userId;
    private String campus, date, timeSlot, firstName, lastName;

    public Appointment(int id, int userId, String campus, String date, String timeSlot, String firstName, String lastName) {
        this.id = id;
        this.userId = userId;
        this.campus = campus;
        this.date = date;
        this.timeSlot = timeSlot;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getCampus() { return campus; }
    public String getDate() { return date; }
    public String getTimeSlot() { return timeSlot; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
}
