package com.smartcampus.android.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static String getGreeting() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour >= 4 && hour < 12) {
            return "Good Morning";
        } else if (hour >= 12 && hour < 17) {
            return "Good Afternoon";
        } else {
            return "Good Evening";
        }
    }

    public static String getCurrentDayOfWeek() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.US);
        return sdf.format(new Date());
    }

    public static String getFormattedTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.US);
        return sdf.format(new Date());
    }
}

