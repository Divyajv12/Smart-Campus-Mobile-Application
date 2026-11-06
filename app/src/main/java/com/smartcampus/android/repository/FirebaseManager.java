package com.smartcampus.android.repository;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.smartcampus.android.model.Announcement;
import com.smartcampus.android.model.AttendanceRecord;
import com.smartcampus.android.model.Event;
import com.smartcampus.android.model.Student;
import com.smartcampus.android.model.TimetableSlot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseManager {
    private static final String TAG = "FirebaseManager";

    public static final String COLLECTION_STUDENTS = "students";
    public static final String COLLECTION_ATTENDANCE = "attendance";
    public static final String COLLECTION_TIMETABLE = "timetable";
    public static final String COLLECTION_ANNOUNCEMENTS = "announcements";
    public static final String COLLECTION_EVENTS = "events";

    private final FirebaseAuth auth;
    private final FirebaseFirestore db;
    private static FirebaseManager instance;

    public interface DataCallback<T> {
        void onSuccess(T data);
        void onError(String errorMessage);
    }

    public interface ActionCallback {
        void onSuccess();
        void onError(String errorMessage);
    }

    private FirebaseManager() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public static synchronized FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public FirebaseUser getCurrentUser() {
        try {
            return auth.getCurrentUser();
        } catch (Exception e) {
            return null;
        }
    }

    // ---------------- AUTHENTICATION ---------------- //

    public void login(String email, String password, ActionCallback callback) {
        try {
            auth.signInWithEmailAndPassword(email.trim(), password)
                    .addOnSuccessListener(authResult -> callback.onSuccess())
                    .addOnFailureListener(e -> callback.onError(e.getMessage() != null ? e.getMessage() : "Authentication failed"));
        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }

    public void register(String email, String password, ActionCallback callback) {
        try {
            auth.createUserWithEmailAndPassword(email.trim(), password)
                    .addOnSuccessListener(authResult -> callback.onSuccess())
                    .addOnFailureListener(e -> callback.onError(e.getMessage() != null ? e.getMessage() : "Registration failed"));
        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }

    public void sendPasswordReset(String email, ActionCallback callback) {
        try {
            auth.sendPasswordResetEmail(email.trim())
                    .addOnSuccessListener(aVoid -> callback.onSuccess())
                    .addOnFailureListener(e -> callback.onError(e.getMessage() != null ? e.getMessage() : "Failed to send reset email"));
        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }

    public void logout() {
        try {
            auth.signOut();
        } catch (Exception e) {
            Log.e(TAG, "Logout error: " + e.getMessage());
        }
    }

    // ---------------- STUDENT PROFILE ---------------- //

    public void saveStudentProfile(Student student, ActionCallback callback) {
        try {
            db.collection(COLLECTION_STUDENTS)
                    .document(student.getUid())
                    .set(student)
                    .addOnSuccessListener(aVoid -> callback.onSuccess())
                    .addOnFailureListener(e -> callback.onError(e.getMessage()));
        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }

    public void getStudentProfile(String uid, DataCallback<Student> callback) {
        try {
            db.collection(COLLECTION_STUDENTS).document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Student student = documentSnapshot.toObject(Student.class);
                            callback.onSuccess(student);
                        } else {
                            callback.onError("Student profile not found.");
                        }
                    })
                    .addOnFailureListener(e -> callback.onError(e.getMessage()));
        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }

    // ---------------- ATTENDANCE ---------------- //

    public void getAttendanceRecords(String uid, DataCallback<List<AttendanceRecord>> callback) {
        try {
            db.collection(COLLECTION_STUDENTS).document(uid).collection(COLLECTION_ATTENDANCE)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<AttendanceRecord> records = new ArrayList<>();
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            AttendanceRecord record = doc.toObject(AttendanceRecord.class);
                            if (record != null) {
                                records.add(record);
                            }
                        }
                        callback.onSuccess(records);
                    })
                    .addOnFailureListener(e -> callback.onError(e.getMessage()));
        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }

    public void saveAttendanceRecord(String uid, AttendanceRecord record, ActionCallback callback) {
        try {
            db.collection(COLLECTION_STUDENTS).document(uid).collection(COLLECTION_ATTENDANCE)
                    .document(record.getId())
                    .set(record)
                    .addOnSuccessListener(aVoid -> callback.onSuccess())
                    .addOnFailureListener(e -> callback.onError(e.getMessage()));
        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }

    // ---------------- TIMETABLE ---------------- //

    public void getTimetable(DataCallback<List<TimetableSlot>> callback) {
        try {
            db.collection(COLLECTION_TIMETABLE)
                    .orderBy("slotOrder", Query.Direction.ASCENDING)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<TimetableSlot> slots = new ArrayList<>();
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            TimetableSlot slot = doc.toObject(TimetableSlot.class);
                            if (slot != null) {
                                slots.add(slot);
                            }
                        }
                        callback.onSuccess(slots);
                    })
                    .addOnFailureListener(e -> callback.onError(e.getMessage()));
        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }

    // ---------------- ANNOUNCEMENTS ---------------- //

    public void getAnnouncements(DataCallback<List<Announcement>> callback) {
        try {
            db.collection(COLLECTION_ANNOUNCEMENTS)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Announcement> announcements = new ArrayList<>();
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            Announcement announcement = doc.toObject(Announcement.class);
                            if (announcement != null) {
                                announcements.add(announcement);
                            }
                        }
                        callback.onSuccess(announcements);
                    })
                    .addOnFailureListener(e -> callback.onError(e.getMessage()));
        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }

    // ---------------- EVENTS ---------------- //

    public void getEvents(DataCallback<List<Event>> callback) {
        try {
            db.collection(COLLECTION_EVENTS)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Event> events = new ArrayList<>();
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            Event event = doc.toObject(Event.class);
                            if (event != null) {
                                events.add(event);
                            }
                        }
                        callback.onSuccess(events);
                    })
                    .addOnFailureListener(e -> callback.onError(e.getMessage()));
        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }

    // ---------------- SEED DATA FOR DEMO ---------------- //

    public void seedInitialDataForStudent(String uid, ActionCallback callback) {
        try {
            // Seed attendance
            for (AttendanceRecord record : MockDataProvider.getSampleAttendance()) {
                db.collection(COLLECTION_STUDENTS).document(uid)
                        .collection(COLLECTION_ATTENDANCE).document(record.getId())
                        .set(record);
            }
            // Seed timetable
            for (TimetableSlot slot : MockDataProvider.getSampleTimetable()) {
                db.collection(COLLECTION_TIMETABLE).document(slot.getId()).set(slot);
            }
            // Seed announcements
            for (Announcement announcement : MockDataProvider.getSampleAnnouncements()) {
                db.collection(COLLECTION_ANNOUNCEMENTS).document(announcement.getId()).set(announcement);
            }
            // Seed events
            for (Event event : MockDataProvider.getSampleEvents()) {
                db.collection(COLLECTION_EVENTS).document(event.getId()).set(event);
            }
            callback.onSuccess();
        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }
}

