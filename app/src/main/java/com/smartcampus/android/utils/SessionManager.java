package com.smartcampus.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.smartcampus.android.model.Student;

public class SessionManager {
    private static final String PREF_NAME = "SmartCampusSession";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_IS_DEMO_MODE = "is_demo_mode";
    private static final String KEY_UID = "uid";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_STUDENT_ID = "student_id";
    private static final String KEY_DEPARTMENT = "department";
    private static final String KEY_ACADEMIC_YEAR = "academic_year";
    private static final String KEY_PHONE = "phone_number";
    private static final String KEY_BIO = "bio";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    private static SessionManager instance;

    private SessionManager(Context context) {
        pref = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    public void saveSession(Student student, boolean isDemoMode) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putBoolean(KEY_IS_DEMO_MODE, isDemoMode);
        editor.putString(KEY_UID, student.getUid());
        editor.putString(KEY_FULL_NAME, student.getFullName());
        editor.putString(KEY_EMAIL, student.getEmail());
        editor.putString(KEY_STUDENT_ID, student.getStudentId());
        editor.putString(KEY_DEPARTMENT, student.getDepartment());
        editor.putString(KEY_ACADEMIC_YEAR, student.getAcademicYear());
        editor.putString(KEY_PHONE, student.getPhoneNumber());
        editor.putString(KEY_BIO, student.getBio());
        editor.apply();
    }

    public void updateStudentProfile(Student student) {
        editor.putString(KEY_FULL_NAME, student.getFullName());
        editor.putString(KEY_DEPARTMENT, student.getDepartment());
        editor.putString(KEY_ACADEMIC_YEAR, student.getAcademicYear());
        editor.putString(KEY_PHONE, student.getPhoneNumber());
        editor.putString(KEY_BIO, student.getBio());
        editor.apply();
    }

    public Student getCurrentStudent() {
        if (!isLoggedIn()) return null;
        Student student = new Student();
        student.setUid(pref.getString(KEY_UID, ""));
        student.setFullName(pref.getString(KEY_FULL_NAME, "Student"));
        student.setEmail(pref.getString(KEY_EMAIL, ""));
        student.setStudentId(pref.getString(KEY_STUDENT_ID, ""));
        student.setDepartment(pref.getString(KEY_DEPARTMENT, ""));
        student.setAcademicYear(pref.getString(KEY_ACADEMIC_YEAR, ""));
        student.setPhoneNumber(pref.getString(KEY_PHONE, ""));
        student.setBio(pref.getString(KEY_BIO, ""));
        return student;
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public boolean isDemoMode() {
        return pref.getBoolean(KEY_IS_DEMO_MODE, false);
    }

    public String getCurrentUid() {
        return pref.getString(KEY_UID, "");
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}

