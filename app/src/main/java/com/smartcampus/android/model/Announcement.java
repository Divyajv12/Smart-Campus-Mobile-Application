package com.smartcampus.android.model;

import com.google.firebase.firestore.IgnoreExtraProperties;
import java.io.Serializable;

@IgnoreExtraProperties
public class Announcement implements Serializable {
    private String id;
    private String title;
    private String description;
    private String date;
    private long timestamp;
    private String category; // "Academic", "Exams", "Events", "Administrative"
    private boolean isImportant;
    private String author;

    public Announcement() {
    }

    public Announcement(String id, String title, String description, String date, 
                        long timestamp, String category, boolean isImportant, String author) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.timestamp = timestamp;
        this.category = category;
        this.isImportant = isImportant;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title != null ? title : "";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description != null ? description : "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date != null ? date : "";
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCategory() {
        return category != null ? category : "General";
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public String getAuthor() {
        return author != null ? author : "Campus Administration";
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

