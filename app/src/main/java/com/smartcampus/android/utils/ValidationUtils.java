package com.smartcampus.android.utils;

import android.text.TextUtils;
import android.util.Patterns;

public class ValidationUtils {

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }

    public static boolean arePasswordsMatching(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) return false;
        return password.equals(confirmPassword);
    }

    public static boolean isNonEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }
}

