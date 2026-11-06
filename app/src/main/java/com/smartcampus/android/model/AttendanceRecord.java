package com.smartcampus.android.model;

import com.google.firebase.firestore.IgnoreExtraProperties;
import java.io.Serializable;

@IgnoreExtraProperties
public class AttendanceRecord implements Serializable {
    private String id;
    private String subjectCode;
    private String subjectName;
    private String facultyName;
    private int totalClasses;
    private int attendedClasses;

    public AttendanceRecord() {
    }

    public AttendanceRecord(String id, String subjectCode, String subjectName, 
                            String facultyName, int totalClasses, int attendedClasses) {
        this.id = id;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.facultyName = facultyName;
        this.totalClasses = totalClasses;
        this.attendedClasses = attendedClasses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectCode() {
        return subjectCode != null ? subjectCode : "";
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName != null ? subjectName : "";
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getFacultyName() {
        return facultyName != null ? facultyName : "";
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public int getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(int totalClasses) {
        this.totalClasses = totalClasses;
    }

    public int getAttendedClasses() {
        return attendedClasses;
    }

    public void setAttendedClasses(int attendedClasses) {
        this.attendedClasses = attendedClasses;
    }

    // Calculated percentage
    public double getPercentage() {
        if (totalClasses <= 0) return 0.0;
        return (double) attendedClasses / totalClasses * 100.0;
    }

    public String getFormattedPercentage() {
        return String.format(java.util.Locale.US, "%.1f%%", getPercentage());
    }

    public boolean isLowAttendance() {
        return getPercentage() < 75.0;
    }

    public int getMissedClasses() {
        return Math.max(0, totalClasses - attendedClasses);
    }

    // Returns how many consecutive classes are needed to reach 75%
    public int getClassesNeededFor75() {
        if (getPercentage() >= 75.0) return 0;
        // Formula: (attended + x) / (total + x) >= 0.75
        // attended + x >= 0.75 * total + 0.75 * x
        // 0.25 * x >= 0.75 * total - attended
        // x >= (3 * total - 4 * attended)
        int needed = (3 * totalClasses) - (4 * attendedClasses);
        return Math.max(0, needed);
    }
}

