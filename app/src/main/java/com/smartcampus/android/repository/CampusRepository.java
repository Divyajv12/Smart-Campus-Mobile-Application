package com.smartcampus.android.repository;

import android.content.Context;
import com.smartcampus.android.model.Announcement;
import com.smartcampus.android.model.AttendanceRecord;
import com.smartcampus.android.model.Event;
import com.smartcampus.android.model.Student;
import com.smartcampus.android.model.TimetableSlot;
import com.smartcampus.android.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class CampusRepository {
    private static CampusRepository instance;
    private final FirebaseManager firebaseManager;

    public interface RepositoryCallback<T> {
        void onLoaded(T data);
        void onError(String error);
    }

    public interface ActionCallback {
        void onCompleted(boolean success, String message);
    }

    private CampusRepository() {
        firebaseManager = FirebaseManager.getInstance();
    }

    public static synchronized CampusRepository getInstance() {
        if (instance == null) {
            instance = new CampusRepository();
        }
        return instance;
    }

    public void getStudentProfile(Context context, RepositoryCallback<Student> callback) {
        SessionManager session = SessionManager.getInstance(context);
        if (session.isDemoMode()) {
            Student s = session.getCurrentStudent();
            if (s != null) {
                callback.onLoaded(s);
            } else {
                callback.onLoaded(MockDataProvider.getDemoStudent());
            }
            return;
        }

        String uid = session.getCurrentUid();
        if (uid.isEmpty()) {
            Student cached = session.getCurrentStudent();
            callback.onLoaded(cached != null ? cached : MockDataProvider.getDemoStudent());
            return;
        }

        firebaseManager.getStudentProfile(uid, new FirebaseManager.DataCallback<Student>() {
            @Override
            public void onSuccess(Student student) {
                session.updateStudentProfile(student);
                callback.onLoaded(student);
            }

            @Override
            public void onError(String errorMessage) {
                Student cached = session.getCurrentStudent();
                if (cached != null) {
                    callback.onLoaded(cached);
                } else {
                    callback.onLoaded(MockDataProvider.getDemoStudent());
                }
            }
        });
    }

    public void updateStudentProfile(Context context, Student updatedStudent, ActionCallback callback) {
        SessionManager session = SessionManager.getInstance(context);
        session.updateStudentProfile(updatedStudent);

        if (session.isDemoMode()) {
            callback.onCompleted(true, "Profile updated locally (Demo Mode)");
            return;
        }

        firebaseManager.saveStudentProfile(updatedStudent, new FirebaseManager.ActionCallback() {
            @Override
            public void onSuccess() {
                callback.onCompleted(true, "Profile updated in cloud successfully");
            }

            @Override
            public void onError(String errorMessage) {
                // Kept local update, notify user
                callback.onCompleted(true, "Saved locally (Cloud sync failed: " + errorMessage + ")");
            }
        });
    }

    public void getAttendanceRecords(Context context, RepositoryCallback<List<AttendanceRecord>> callback) {
        SessionManager session = SessionManager.getInstance(context);
        if (session.isDemoMode()) {
            callback.onLoaded(MockDataProvider.getSampleAttendance());
            return;
        }

        String uid = session.getCurrentUid();
        firebaseManager.getAttendanceRecords(uid, new FirebaseManager.DataCallback<List<AttendanceRecord>>() {
            @Override
            public void onSuccess(List<AttendanceRecord> records) {
                if (records != null && !records.isEmpty()) {
                    callback.onLoaded(records);
                } else {
                    // Seed initial sample data for new user in Firestore
                    firebaseManager.seedInitialDataForStudent(uid, new FirebaseManager.ActionCallback() {
                        @Override
                        public void onSuccess() {
                            callback.onLoaded(MockDataProvider.getSampleAttendance());
                        }

                        @Override
                        public void onError(String errorMessage) {
                            callback.onLoaded(MockDataProvider.getSampleAttendance());
                        }
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                // Graceful fallback to mock data
                callback.onLoaded(MockDataProvider.getSampleAttendance());
            }
        });
    }

    public void getTimetable(Context context, String dayOfWeek, RepositoryCallback<List<TimetableSlot>> callback) {
        SessionManager session = SessionManager.getInstance(context);
        if (session.isDemoMode()) {
            filterTimetableByDay(MockDataProvider.getSampleTimetable(), dayOfWeek, callback);
            return;
        }

        firebaseManager.getTimetable(new FirebaseManager.DataCallback<List<TimetableSlot>>() {
            @Override
            public void onSuccess(List<TimetableSlot> slots) {
                if (slots != null && !slots.isEmpty()) {
                    filterTimetableByDay(slots, dayOfWeek, callback);
                } else {
                    filterTimetableByDay(MockDataProvider.getSampleTimetable(), dayOfWeek, callback);
                }
            }

            @Override
            public void onError(String errorMessage) {
                filterTimetableByDay(MockDataProvider.getSampleTimetable(), dayOfWeek, callback);
            }
        });
    }

    private void filterTimetableByDay(List<TimetableSlot> allSlots, String dayOfWeek, RepositoryCallback<List<TimetableSlot>> callback) {
        List<TimetableSlot> filtered = new ArrayList<>();
        for (TimetableSlot slot : allSlots) {
            if (slot.getDayOfWeek().equalsIgnoreCase(dayOfWeek)) {
                filtered.add(slot);
            }
        }
        callback.onLoaded(filtered);
    }

    public void getAnnouncements(Context context, RepositoryCallback<List<Announcement>> callback) {
        SessionManager session = SessionManager.getInstance(context);
        if (session.isDemoMode()) {
            callback.onLoaded(MockDataProvider.getSampleAnnouncements());
            return;
        }

        firebaseManager.getAnnouncements(new FirebaseManager.DataCallback<List<Announcement>>() {
            @Override
            public void onSuccess(List<Announcement> announcements) {
                if (announcements != null && !announcements.isEmpty()) {
                    callback.onLoaded(announcements);
                } else {
                    callback.onLoaded(MockDataProvider.getSampleAnnouncements());
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onLoaded(MockDataProvider.getSampleAnnouncements());
            }
        });
    }

    public void getEvents(Context context, RepositoryCallback<List<Event>> callback) {
        SessionManager session = SessionManager.getInstance(context);
        if (session.isDemoMode()) {
            callback.onLoaded(MockDataProvider.getSampleEvents());
            return;
        }

        firebaseManager.getEvents(new FirebaseManager.DataCallback<List<Event>>() {
            @Override
            public void onSuccess(List<Event> events) {
                if (events != null && !events.isEmpty()) {
                    callback.onLoaded(events);
                } else {
                    callback.onLoaded(MockDataProvider.getSampleEvents());
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onLoaded(MockDataProvider.getSampleEvents());
            }
        });
    }
}

