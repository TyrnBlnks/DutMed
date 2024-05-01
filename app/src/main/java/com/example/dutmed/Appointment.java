package com.example.dutmed;


public class Appointment {
    private final int id;
    private final int userId;
    private final String campus;
    private final String date;
    private final String timeSlot;
    private final String firstName;
    private final String lastName;

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
