package com.example.dutmed;

public class HealthResource {
    private int id;
    private String title;
    private String content;
    private String type;
    private String imageUrl;

    public HealthResource(int id, String title, String content, String type, String imageUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
        this.imageUrl = imageUrl;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

