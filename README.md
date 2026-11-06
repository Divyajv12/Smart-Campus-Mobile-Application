# Smart Campus - Native Android Application

[![Platform](https://img.shields.io/badge/Platform-Android%20Native-3DDC84.svg?logo=android&logoColor=white)](https://developer.android.com/)
[![Language](https://img.shields.io/badge/Language-Java-ED8B00.svg?logo=openjdk&logoColor=white)](https://www.java.com/)
[![UI](https://img.shields.io/badge/UI-XML%20%7C%20Material%20Design%203-0284C7.svg)](https://m3.material.io/)
[![Backend](https://img.shields.io/badge/Backend-Firebase%20Auth%20%26%20Firestore-FFCA28.svg?logo=firebase&logoColor=black)](https://firebase.google.com/)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-34%20(Android%2014)-0D2C54.svg)](https://developer.android.com/about/versions/14)

**Smart Campus** is a complete, native Android application built for college and university students to manage their academic life, track lecture attendance, view weekly class schedules, stay updated with official circulars, and participate in campus activities.

This solo academic project is built strictly with **native Android technologies (Java, XML, Android SDK, Gradle, and Firebase)** without any hybrid, Flutter, or React Native frameworks.

---

## Table of Contents

1. [Project Overview](#project-overview)
2. [Key Application Features](#key-application-features)
3. [Technology Stack](#technology-stack)
4. [Architecture & Project Structure](#architecture--project-structure)
5. [UI/UX Design System](#uiux-design-system)
6. [Firebase Setup & Configuration](#firebase-setup--configuration)
7. [Firestore Schema & Security Rules](#firestore-schema--security-rules)
8. [Step-by-Step Execution Guide](#step-by-step-execution-guide)
9. [Testing & Verification Checklist](#testing--verification-checklist)

---

## 1. Project Overview

College students often balance multiple lecture schedules, academic regulations (such as mandatory 75% attendance criteria), urgent university notifications, and club events across fragmented platforms. **Smart Campus** consolidates these essentials into an intuitive, responsive mobile application.

### Key Highlights:
- **Zero-Config Demo Mode:** Immediate out-of-the-box evaluation without needing immediate Firebase cloud infrastructure setup.
- **Automated Attendance Mathematics:** Calculates exact percentage, alerts when attendance drops below 75%, and calculates how many consecutive classes are needed to normalize attendance.
- **Dynamic Weekly Timetable:** Organizes class schedules from Monday to Saturday, automatically highlighting today's classes and timing.
- **Enterprise-Grade Security:** Firebase Authentication with token-scoped Cloud Firestore rules.

---

## 2. Key Application Features

### A. Authentication & Onboarding
- **Student Registration:** Captures Full Name, Student ID (Roll No), University Email, Department, Academic Year, and Password.
- **Email & Password Authentication:** Backed by Firebase Authentication with client-side pattern validation and password confirmation checks.
- **One-Click Demo Login:** Allows testing the application instantly with pre-populated academic records (Alex Morgan, CS2024-042).
- **Password Reset:** Generates password reset links dispatched directly to the student's email.
- **Session Management:** Persists login state using `SessionManager` (backed by Android `SharedPreferences`).

### B. Student Dashboard
- **Dynamic Greeting:** Contextual greeting ("Good Morning", "Good Afternoon", "Good Evening") with current date.
- **Student Profile Summary:** Compact card displaying avatar initials, student ID, department, and academic year.
- **Quick-Access Action Grid:** Instant navigation cards to Attendance, Timetable, Notices, and Events.
- **Attendance Gauge:** Real-time percentage indicator with color-coded status badges ("Good Standing" vs "Low Attendance Warning").
- **Today's Lecture Schedule:** Displays ongoing and upcoming lectures with timing and classroom numbers.
- **Latest Notice Snapshot:** Highlights urgent campus announcements with category badges.

### C. Attendance Tracking Module
- **Subject Breakdown:** Lists all registered subjects (e.g. Data Structures, Computer Networks, Operating Systems, DBMS).
- **Conducted vs Attended Counter:** Tracks total lectures held, classes attended, and lectures missed.
- **Low Attendance Warning (&lt; 75%):**
  - High-visibility red warning banner.
  - Computes the recovery formula: `Classes Needed = (3 * Total - 4 * Attended)` to reach 75%.
- **Material Progress Bars:** Dynamically colored (Emerald Green for &ge;75%, Crimson Red for &lt;75%).

### D. Class Timetable Module
- **Weekly Schedule:** Interactive day selector tabs (Monday through Saturday).
- **Current Day Highlighting:** Automatically detects current day of the week and activates its schedule.
- **Rich Class Cards:** Shows start and end time, subject name, course code badge, lecture room / lab number, and faculty in charge.
- **Empty State:** Friendly graphic message when no lectures are scheduled for a specific day.

### E. Notices & Announcements Module
- **Chronological Sorting:** Newest university circulars appear first.
- **Visual Urgency Indicators:** Distinct red **IMPORTANT** badge for priority circulars.
- **Category Filter Chips:** Instant filtering by **Academic**, **Examination**, **Events**, and **Administration**.
- **Search & Empty State:** Graceful fallback when no notices are found.

### F. Campus Events Calendar
- **Event List Interface:** Month/Day calendar badge, event title, category, timings, and campus venue.
- **Interactive Details Modal:** Dialog detailing event overview, organizing committee, and contact information.
- **One-Click Event Registration:** Toggles registration state with visual feedback and toast confirmation.

### G. Student Profile & Edit Profile
- **Academic Record Overview:** Student Name, Roll No, Department, Academic Year, Phone Number, and Bio.
- **Quick Academic Stats:** Active status, enrolled courses count, and overall attendance percentage.
- **Profile Editing:** Allows updating phone number, department, academic year, and personal statement with Firestore and local session synchronization.
- **Safe Sign Out:** Multi-step confirmation dialog clearing local session tokens and Firebase credentials.

---

## 3. Technology Stack

| Component | Technology | Version / Specification |
|---|---|---|
| **IDE** | Android Studio | Hedgehog / Iguana / Jellyfish / Ladybug |
| **Language** | Java | Java 8+ / OpenJDK 17 |
| **UI Framework** | Native Android XML | Material Design 3 (`com.google.android.material:material:1.11.0`) |
| **Minimum SDK** | Android API 24 | Android 7.0 (Nougat) — 96%+ device compatibility |
| **Target / Compile SDK** | Android API 34 | Android 14 (Upside Down Cake) |
| **Architecture** | Repository Pattern | MVVM / Clean Modular Java |
| **Authentication** | Firebase Auth | BoM 32.8.0 |
| **Cloud Database** | Firebase Cloud Firestore | Realtime NoSQL document store |
| **Navigation** | BottomNavigationView | Android Navigation Components & Fragment Transactions |
| **Build System** | Gradle | Gradle 8.2 & Android Gradle Plugin 8.2.2 |

---

## 4. Architecture & Project Structure

The project follows a clean, modular package structure:

```
d:\smart app
├── app
│   ├── build.gradle                          # App module dependencies & configuration
│   ├── google-services.json                  # Firebase configuration file
│   ├── proguard-rules.pro                   # R8 / ProGuard keep rules
│   └── src
│       └── main
│           ├── AndroidManifest.xml           # Activity registry & permissions
│           ├── java/com/smartcampus/android
│           │   ├── SmartCampusApplication.java   # Application class (Firebase init)
│           │   ├── adapter                  # RecyclerView Adapters
│           │   │   ├── AnnouncementAdapter.java
│           │   │   ├── AttendanceAdapter.java
│           │   │   ├── EventAdapter.java
│           │   │   └── TimetableAdapter.java
│           │   ├── model                    # POJO Data Models
│           │   │   ├── Announcement.java
│           │   │   ├── AttendanceRecord.java
│           │   │   ├── Event.java
│           │   │   ├── Student.java
│           │   │   └── TimetableSlot.java
│           │   ├── repository               # Data Repository & Firebase Layer
│           │   │   ├── CampusRepository.java
│           │   │   ├── FirebaseManager.java
│           │   │   └── MockDataProvider.java
│           │   ├── ui                       # Activities & Fragments
│           │   │   ├── announcements
│           │   │   │   └── AnnouncementsActivity.java
│           │   │   ├── attendance
│           │   │   │   └── AttendanceFragment.java
│           │   │   ├── auth
│           │   │   │   ├── ForgotPasswordActivity.java
│           │   │   │   ├── LoginActivity.java
│           │   │   │   ├── RegisterActivity.java
│           │   │   │   └── SplashActivity.java
│           │   │   ├── dashboard
│           │   │   │   └── DashboardFragment.java
│           │   │   ├── events
│           │   │   │   └── EventsFragment.java
│           │   │   ├── main
│           │   │   │   └── MainActivity.java
│           │   │   ├── profile
│           │   │   │   ├── EditProfileActivity.java
│           │   │   │   └── ProfileFragment.java
│           │   │   └── timetable
│           │   │       └── TimetableFragment.java
│           │   └── utils                    # Utilities & Session Helpers
│           │       ├── DateUtils.java
│           │       ├── SessionManager.java
│           │       └── ValidationUtils.java
│           └── res
│               ├── color                    # Selector state lists
│               ├── drawable                 # Vector drawables & background shapes
│               ├── layout                   # 17 responsive XML layout files
│               ├── menu                     # Bottom navigation menu
│               ├── values                   # colors.xml, strings.xml, dimens.xml, themes.xml
│               └── xml                      # Backup and data extraction rules
├── build.gradle                              # Root Gradle build script
├── firestore.rules                           # Cloud Firestore security rules
├── gradle.properties                         # JVM arguments & AndroidX flags
├── sample_firestore_data.json                # Seed data for Firestore
└── settings.gradle                           # Project repositories and module declarations
```

---

## 5. UI/UX Design System

The app adheres to a modern, collegiate color palette designed for high contrast and readability:

| Color Token | Hex Code | Visual Application |
|---|---|---|
| `primary_navy` | `#0D2C54` | App header, primary buttons, bottom navigation selected items |
| `secondary_light_blue` | `#0284C7` | Accent links, icons, chip borders, progress highlights |
| `accent_sky` | `#38BDF8` | Subtitle text on navy backgrounds, active icons |
| `bg_light_gray` | `#F8FAFC` | Scaffold background (off-white for reduced eye strain) |
| `surface_card` | `#FFFFFF` | Elevated cards with rounded 16dp/20dp corners |
| `status_green_success` | `#10B981` | Attendance &ge;75% indicator, good standing badge |
| `status_red_danger` | `#EF4444` | Attendance &lt;75% warning alert, logout action |
| `status_amber_warning` | `#F59E0B` | Priority notice accents, warnings |

### Accessibility & Responsiveness:
- **Dimensions in `dp` & Font sizes in `sp`:** Fully supports Android system font scaling and high-density screens.
- **Touch Target Sizing:** All buttons and interactive elements maintain minimum 48dp &times; 48dp hit targets.
- **Accessibility Attributes:** Explicit `android:contentDescription` labels on icons and action controls for TalkBack screen reader support.

---

## 6. Firebase Setup & Configuration

The application is pre-configured with a valid test `google-services.json` and a graceful fallback layer (`MockDataProvider`). To link your own Firebase project:

### Step 1: Create a Firebase Project
1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Click **Add Project** and name it `Smart Campus`.

### Step 2: Register Android Application
1. Click the **Android** icon to add an Android app.
2. Enter the Package Name:
   ```
   com.smartcampus.android
   ```
3. Enter App Nickname: `Smart Campus`.
4. (Optional) Provide SHA-1 fingerprint for Google Sign-In if adding OAuth later.

### Step 3: Download `google-services.json`
1. Download `google-services.json` from the Firebase Console.
2. Replace the placeholder file located at:
   ```
   d:\smart app\app\google-services.json
   ```

### Step 4: Enable Authentication
1. In Firebase Console, navigate to **Build &rarr; Authentication**.
2. Click **Get Started** and select the **Email/Password** provider.
3. Enable **Email/Password** and click **Save**.

### Step 5: Enable Cloud Firestore
1. Navigate to **Build &rarr; Firestore Database**.
2. Click **Create Database**.
3. Choose **Production Mode** or **Test Mode**.
4. Select a Cloud Firestore location close to your region.

---

## 7. Firestore Schema & Security Rules

### Firestore Collections Structure

```
Firestore Root
│
├── students (Collection)
│   └── {uid} (Document)
│       ├── uid: string
│       ├── fullName: string
│       ├── email: string
│       ├── studentId: string
│       ├── department: string
│       ├── academicYear: string
│       ├── phoneNumber: string
│       ├── bio: string
│       └── attendance (Subcollection)
│           └── {attendanceId} (Document)
│               ├── id: string
│               ├── subjectCode: string
│               ├── subjectName: string
│               ├── facultyName: string
│               ├── totalClasses: number
│               └── attendedClasses: number
│
├── timetable (Collection)
│   └── {slotId} (Document)
│       ├── id: string
│       ├── dayOfWeek: "Monday" | "Tuesday" | "Wednesday" | ...
│       ├── startTime: "09:00 AM"
│       ├── endTime: "10:00 AM"
│       ├── subjectName: string
│       ├── subjectCode: string
│       ├── classroom: string
│       ├── facultyName: string
│       └── slotOrder: number
│
├── announcements (Collection)
│   └── {announcementId} (Document)
│       ├── id: string
│       ├── title: string
│       ├── description: string
│       ├── date: string
│       ├── timestamp: number
│       ├── category: "Academic" | "Exams" | "Events" | "Administrative"
│       ├── isImportant: boolean
│       └── author: string
│
└── events (Collection)
    └── {eventId} (Document)
        ├── id: string
        ├── title: string
        ├── description: string
        ├── date: string
        ├── time: string
        ├── venue: string
        ├── category: "Technical" | "Cultural" | "Sports"
        └── organizer: string
```

### Security Rules (`firestore.rules`)

The included `firestore.rules` file enforces strict role-based access control:
- Students can only read and write their own student document and attendance records.
- College announcements, class timetables, and campus events are readable by all authenticated students.
- Only verified administrators can publish announcements, alter timetables, or create events.

---

## 8. Step-by-Step Execution Guide

### Opening the Project in Android Studio:
1. Launch **Android Studio**.
2. Select **File &rarr; Open...** and browse to `D:\smart app`.
3. Allow Android Studio to sync the project via Gradle.
4. Ensure your project JDK is set to **JDK 17** (`Settings &rarr; Build, Execution, Deployment &rarr; Build Tools &rarr; Gradle &rarr; Gradle JDK`).

### Running the App:
1. Start an Android Virtual Device (AVD) running **Android 10 (API 29)** or newer (recommended: Pixel 7 on API 34).
2. Click the green **Run ('app')** button (or press `Shift + F10`).
3. The app will launch with the animated `SplashActivity` and navigate to `LoginActivity`.

### Quick Demo Testing:
1. On the Login screen, click **Try Demo / Offline Login**.
2. The app will immediately authenticate as **Alex Morgan (CS2024-042)** and load the full suite of sample attendance, timetable, notices, and events.
3. Test editing profile details by going to the **Profile** tab and clicking **Edit Profile**.

---

## 9. Testing & Verification Checklist

- [x] **Student Registration & Validation:** Tested field sanitization, email regex matching, and password confirmation.
- [x] **Authentication Session:** Session persists across app restarts through `SessionManager`.
- [x] **Attendance Calculation:**
  - Automated calculation of percentage (`attended / total * 100`).
  - Low attendance alert triggered for subjects &lt; 75% (CS603 and CS604).
  - Recovery classes formula tested (`classes needed to normalize`).
- [x] **Timetable Current Day Logic:** Auto-selects today's schedule and toggles between Monday-Saturday.
- [x] **Announcements Filtering:** Real-time chip filtering across Exams, Academic, Events, and Administrative.
- [x] **Events Interaction:** Displays bottom dialog modal with event metadata and registration toggle.
- [x] **Profile Modification:** Updates student name, department, year, phone, and bio with immediate UI reflection.
- [x] **Accessibility:** Meaningful `contentDescription` attributes on all icons; touch targets &ge; 48dp.
- [x] **Code Quality:** Zero compilation warnings, strict null safety, try-catch resilience on Firebase network operations.

---

## Author
**Solo Academic Project - Mobile Application Development**  
*Built natively for Android with Java, XML & Firebase.*

