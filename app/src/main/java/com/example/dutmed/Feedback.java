package com.example.dutmed;

public class Feedback {
    private int id, userId, entryId;
    private String feedbackText, suggestions, dateSubmitted;

    public Feedback(int id, int userId, int entryId, String feedbackText, String suggestions, String dateSubmitted) {
        this.id = id;
        this.userId = userId;
        this.entryId = entryId;
        this.feedbackText = feedbackText;
        this.suggestions = suggestions;
        this.dateSubmitted = dateSubmitted;
    }

    // Getters and possibly setters
}

