package com.example.dutmed;

public class Feedback {
    private int id;
    private final int userId;
    private final int entryId;
    private final String feedbackText;
    private final String suggestions;
    private final String dateSubmitted;

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

