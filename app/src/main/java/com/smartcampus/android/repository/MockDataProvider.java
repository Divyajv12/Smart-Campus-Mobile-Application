package com.smartcampus.android.repository;

import com.smartcampus.android.model.Announcement;
import com.smartcampus.android.model.AttendanceRecord;
import com.smartcampus.android.model.Event;
import com.smartcampus.android.model.Student;
import com.smartcampus.android.model.TimetableSlot;

import java.util.ArrayList;
import java.util.List;

public class MockDataProvider {

    public static Student getDemoStudent() {
        Student s = new Student(
                "demo_student_uid_123",
                "Alex Morgan",
                "alex.morgan@campus.edu",
                "CS2024-042",
                "Computer Science & Engineering",
                "3rd Year (Semester VI)"
        );
        s.setPhoneNumber("+1 (555) 019-2834");
        s.setBio("Computer Science undergraduate passionate about Mobile Engineering, Distributed Systems, and AI.");
        return s;
    }

    public static List<AttendanceRecord> getSampleAttendance() {
        List<AttendanceRecord> list = new ArrayList<>();
        list.add(new AttendanceRecord("att_1", "CS601", "Data Structures & Algorithms", "Dr. Robert Vance", 36, 32));
        list.add(new AttendanceRecord("att_2", "CS602", "Database Management Systems", "Prof. Jennifer Hayes", 32, 29));
        list.add(new AttendanceRecord("att_3", "CS603", "Computer Networks", "Dr. Alan Turing Jr.", 30, 21)); // 70.0% - Low
        list.add(new AttendanceRecord("att_4", "CS604", "Operating Systems", "Prof. Marcus Brody", 28, 20));  // 71.4% - Low
        list.add(new AttendanceRecord("att_5", "CS605", "Software Engineering", "Dr. Elena Rostova", 26, 24));
        list.add(new AttendanceRecord("att_6", "CS606", "Cloud Computing & DevOps", "Prof. Samuel Clark", 22, 20));
        return list;
    }

    public static List<TimetableSlot> getSampleTimetable() {
        List<TimetableSlot> list = new ArrayList<>();

        // Monday
        list.add(new TimetableSlot("tt_m1", "Monday", "09:00 AM", "10:00 AM", "Data Structures & Algorithms", "CS601", "Lecture Hall 101", "Dr. Robert Vance", 1));
        list.add(new TimetableSlot("tt_m2", "Monday", "10:00 AM", "11:00 AM", "Database Management Systems", "CS602", "Lecture Hall 101", "Prof. Jennifer Hayes", 2));
        list.add(new TimetableSlot("tt_m3", "Monday", "11:15 AM", "01:15 PM", "Algorithms Practical Lab", "CS601L", "Lab 304 (Turing Hall)", "Dr. Robert Vance", 3));
        list.add(new TimetableSlot("tt_m4", "Monday", "02:00 PM", "03:00 PM", "Operating Systems", "CS604", "Lecture Hall 105", "Prof. Marcus Brody", 4));

        // Tuesday
        list.add(new TimetableSlot("tt_t1", "Tuesday", "09:00 AM", "10:00 AM", "Computer Networks", "CS603", "Lecture Hall 102", "Dr. Alan Turing Jr.", 1));
        list.add(new TimetableSlot("tt_t2", "Tuesday", "10:00 AM", "11:00 AM", "Software Engineering", "CS605", "Lecture Hall 102", "Dr. Elena Rostova", 2));
        list.add(new TimetableSlot("tt_t3", "Tuesday", "11:15 AM", "01:15 PM", "Database Systems Lab", "CS602L", "Lab 201 (Codd Lab)", "Prof. Jennifer Hayes", 3));
        list.add(new TimetableSlot("tt_t4", "Tuesday", "02:00 PM", "03:30 PM", "Cloud Computing Seminar", "CS606", "Seminar Room B", "Prof. Samuel Clark", 4));

        // Wednesday
        list.add(new TimetableSlot("tt_w1", "Wednesday", "09:00 AM", "10:00 AM", "Data Structures & Algorithms", "CS601", "Lecture Hall 101", "Dr. Robert Vance", 1));
        list.add(new TimetableSlot("tt_w2", "Wednesday", "10:00 AM", "11:00 AM", "Operating Systems", "CS604", "Lecture Hall 105", "Prof. Marcus Brody", 2));
        list.add(new TimetableSlot("tt_w3", "Wednesday", "11:15 AM", "12:15 PM", "Computer Networks", "CS603", "Lecture Hall 102", "Dr. Alan Turing Jr.", 3));
        list.add(new TimetableSlot("tt_w4", "Wednesday", "01:30 PM", "03:30 PM", "Operating Systems Lab", "CS604L", "Unix Lab 402", "Prof. Marcus Brody", 4));

        // Thursday
        list.add(new TimetableSlot("tt_th1", "Thursday", "09:00 AM", "10:00 AM", "Database Management Systems", "CS602", "Lecture Hall 101", "Prof. Jennifer Hayes", 1));
        list.add(new TimetableSlot("tt_th2", "Thursday", "10:00 AM", "11:00 AM", "Cloud Computing & DevOps", "CS606", "Lecture Hall 204", "Prof. Samuel Clark", 2));
        list.add(new TimetableSlot("tt_th3", "Thursday", "11:15 AM", "01:15 PM", "Networks & Security Lab", "CS603L", "Cisco Lab 305", "Dr. Alan Turing Jr.", 3));
        list.add(new TimetableSlot("tt_th4", "Thursday", "02:00 PM", "03:00 PM", "Software Engineering", "CS605", "Lecture Hall 102", "Dr. Elena Rostova", 4));

        // Friday
        list.add(new TimetableSlot("tt_f1", "Friday", "09:00 AM", "10:00 AM", "Operating Systems", "CS604", "Lecture Hall 105", "Prof. Marcus Brody", 1));
        list.add(new TimetableSlot("tt_f2", "Friday", "10:00 AM", "11:00 AM", "Data Structures & Algorithms", "CS601", "Lecture Hall 101", "Dr. Robert Vance", 2));
        list.add(new TimetableSlot("tt_f3", "Friday", "11:15 AM", "12:15 PM", "Software Engineering Workshop", "CS605", "Design Studio A", "Dr. Elena Rostova", 3));
        list.add(new TimetableSlot("tt_f4", "Friday", "02:00 PM", "04:00 PM", "Capstone Project Mentorship", "CS690", "Innovation Hub", "Faculty Panel", 4));

        // Saturday
        list.add(new TimetableSlot("tt_s1", "Saturday", "09:30 AM", "11:30 AM", "Open Source & Industry Guest Lecture", "CS699", "Auditorium Hall", "Visiting Industry Expert", 1));
        list.add(new TimetableSlot("tt_s2", "Saturday", "11:45 AM", "01:15 PM", "Coding Club & Hackathon Practice", "ACT10", "Activity Center 202", "Student Chapter Leads", 2));

        return list;
    }

    public static List<Announcement> getSampleAnnouncements() {
        List<Announcement> list = new ArrayList<>();
        list.add(new Announcement(
                "anc_1",
                "Mid-Term Examination Schedule & Guidelines Released",
                "The university examination department has finalized the schedule for the Mid-Term Exams. Students must carry valid student ID cards to examination halls.",
                "Today, 10:30 AM",
                System.currentTimeMillis() - 3600000,
                "Exams",
                true,
                "Office of the Controller of Examinations"
        ));

        list.add(new Announcement(
                "anc_2",
                "Smart Campus Hackathon 2026 - Registration Open",
                "Teams of 3 to 4 students can register for the 36-hour flagship hackathon. Exciting cash prizes, mentorship from leading tech firms, and internship opportunities await.",
                "Yesterday",
                System.currentTimeMillis() - 86400000,
                "Events",
                true,
                "Center for Innovation & Entrepreneurship"
        ));

        list.add(new Announcement(
                "anc_3",
                "Central Library Extended Hours for Mid-Term Prep",
                "The Central Library and Digital Resource Center will remain open 24/7 during exam weeks. High-speed Wi-Fi and discussion rooms are available upon prior booking.",
                "2 days ago",
                System.currentTimeMillis() - 172800000,
                "Academic",
                false,
                "University Chief Librarian"
        ));

        list.add(new Announcement(
                "anc_4",
                "Guest Lecture: Microservices & Modern Cloud Architecture",
                "Distinguished engineering leader from Google Cloud will be delivering a hands-on session on Scalable Microservices on Friday in the Main Auditorium.",
                "4 days ago",
                System.currentTimeMillis() - 345600000,
                "Academic",
                false,
                "Department of Computer Science"
        ));

        return list;
    }

    public static List<Event> getSampleEvents() {
        List<Event> list = new ArrayList<>();
        list.add(new Event(
                "evt_1",
                "Smart Campus Hackathon 2026",
                "A 36-hour high-intensity national level hackathon addressing challenges in Smart Cities, AI-driven Healthcare, EdTech, and Sustainable Energy.",
                "Nov 18, 2026",
                "09:00 AM - 09:00 PM",
                "Innovation Hub & Main Auditorium",
                "Technical",
                "Google Developer Student Clubs & ACM"
        ));

        list.add(new Event(
                "evt_2",
                "Vibrance 2026 - Annual Cultural Fest",
                "The biggest college cultural extravaganza featuring music battles, battle of the bands, classical dances, drama, and celebrity guest performances.",
                "Dec 04 - Dec 06, 2026",
                "05:00 PM onwards",
                "University Open Air Theatre",
                "Cultural",
                "Student Cultural Affairs Committee"
        ));

        list.add(new Event(
                "evt_3",
                "Inter-Department Football Championship",
                "The annual intra-college tournament. Support your department teams as they compete for the university championship trophy.",
                "Nov 24, 2026",
                "03:30 PM - 07:00 PM",
                "Campus Sports Complex Ground",
                "Sports",
                "Department of Physical Education"
        ));

        list.add(new Event(
                "evt_4",
                "Google Cloud & Generative AI Workshop",
                "Hands-on workshop covering Gemini APIs, Vector Embeddings, Firebase integration, and cloud-native architecture.",
                "Nov 28, 2026",
                "10:00 AM - 02:00 PM",
                "Computer Center Lab 1",
                "Technical",
                "IEEE Student Branch"
        ));

        return list;
    }
}

