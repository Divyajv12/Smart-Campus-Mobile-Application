package com.smartcampus.android;

import android.app.Application;
import android.util.Log;
import com.google.firebase.FirebaseApp;

public class SmartCampusApplication extends Application {
    private static final String TAG = "SmartCampusApp";

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            FirebaseApp.initializeApp(this);
            Log.d(TAG, "Firebase initialized successfully.");
        } catch (Exception e) {
            Log.w(TAG, "Firebase initialization warning: " + e.getMessage());
        }
    }
}

