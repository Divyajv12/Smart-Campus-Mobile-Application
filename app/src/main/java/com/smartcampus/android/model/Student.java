package com.smartcampus.android.model;

import com.google.firebase.firestore.IgnoreExtraProperties;
import java.io.Serializable;

@IgnoreExtraProperties
public class Student implements Serializable {
    private String uid;
    private String fullName;
    private String email;
    private String studentId;
    private String department;
    private String academicYear;
    private String phoneNumber;
    private String bio;
    private String avatarUrl;
    private long createdAt;

    // Required empty constructor for Firestore
    public Student() {
    }

    public Student(String uid, String fullName, String email, String studentId, 
                   String department, String academicYear) {
        this.uid = uid;
        this.fullName = fullName;
        this.email = email;
        this.studentId = studentId;
        this.department = department;
        this.academicYear = academicYear;
        this.phoneNumber = "";
        this.bio = "Undergraduate student passionate about learning and campus engagement.";
        this.avatarUrl = "";
        this.createdAt = System.currentTimeMillis();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName != null ? fullName : "";
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email != null ? email : "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentId() {
        return studentId != null ? studentId : "";
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getDepartment() {
        return department != null ? department : "";
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAcademicYear() {
        return academicYear != null ? academicYear : "";
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getPhoneNumber() {
        return phoneNumber != null ? phoneNumber : "";
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBio() {
        return bio != null ? bio : "";
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarUrl() {
        return avatarUrl != null ? avatarUrl : "";
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}

