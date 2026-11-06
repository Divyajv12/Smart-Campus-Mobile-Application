package com.smartcampus.android.model;

import com.google.firebase.firestore.IgnoreExtraProperties;
import java.io.Serializable;

@IgnoreExtraProperties
public class Event implements Serializable {
    private String id;
    private String title;
    private String description;
    private String date; // "Nov 18, 2026"
    private String time; // "10:00 AM - 04:00 PM"
    private String venue; // "Main University Auditorium"
    private String category; // "Technical", "Cultural", "Sports"
    private String organizer;
    private boolean isRegistered;

    public Event() {
    }

    public Event(String id, String title, String description, String date, 
                 String time, String venue, String category, String organizer) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.category = category;
        this.organizer = organizer;
        this.isRegistered = false;
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

    public String getTime() {
        return time != null ? time : "";
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        return venue != null ? venue : "";
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getCategory() {
        return category != null ? category : "Campus";
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOrganizer() {
        return organizer != null ? organizer : "Student Council";
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }
}

