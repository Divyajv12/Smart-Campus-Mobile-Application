package com.smartcampus.android.model;

import com.google.firebase.firestore.IgnoreExtraProperties;
import java.io.Serializable;

@IgnoreExtraProperties
public class TimetableSlot implements Serializable {
    private String id;
    private String dayOfWeek; // Monday, Tuesday, Wednesday, Thursday, Friday, Saturday
    private String startTime; // "09:00 AM"
    private String endTime;   // "10:00 AM"
    private String subjectName;
    private String subjectCode;
    private String classroom;
    private String facultyName;
    private int slotOrder;

    public TimetableSlot() {
    }

    public TimetableSlot(String id, String dayOfWeek, String startTime, String endTime, 
                         String subjectName, String subjectCode, String classroom, 
                         String facultyName, int slotOrder) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.classroom = classroom;
        this.facultyName = facultyName;
        this.slotOrder = slotOrder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDayOfWeek() {
        return dayOfWeek != null ? dayOfWeek : "";
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getStartTime() {
        return startTime != null ? startTime : "";
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime != null ? endTime : "";
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSubjectName() {
        return subjectName != null ? subjectName : "";
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode != null ? subjectCode : "";
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getClassroom() {
        return classroom != null ? classroom : "";
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getFacultyName() {
        return facultyName != null ? facultyName : "";
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public int getSlotOrder() {
        return slotOrder;
    }

    public void setSlotOrder(int slotOrder) {
        this.slotOrder = slotOrder;
    }

    public String getFormattedTiming() {
        return startTime + " - " + endTime;
    }
}

