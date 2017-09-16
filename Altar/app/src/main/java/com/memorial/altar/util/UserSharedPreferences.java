package com.memorial.altar.util;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by yoon on 2017. 8. 31..
 */

public class UserSharedPreferences {

    private static final String PREF_HOME_INTRO_SLIDE = "com.memorial.altar.home_intro_slide";
    private static final String PREF_STAR_COUNT = "com.memorial.altar.star_count";
    private static final String PREF_USER_EMAIL = "com.memorial.altar.user_email";
    private static final String PREF_USER_PASSWORD = "com.memorial.altar.user_password";

    public static boolean getStoredHomeIntroSlide(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_HOME_INTRO_SLIDE, false);
    }

    public static void setStoredHomeIntroSlide(Context context, boolean isHomeIntroSlide) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_HOME_INTRO_SLIDE, isHomeIntroSlide)
                .apply();
    }

    public static int getStoredStar(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(PREF_STAR_COUNT, 10);
    }

    public static void setStoredStar(Context context, int starCount) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(PREF_STAR_COUNT, starCount)
                .apply();
    }

    public static String getStoredUserEmail(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_USER_EMAIL, null);
    }

    public static void setStoredUserEmail(Context context, String userEmail) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_USER_EMAIL, userEmail)
                .apply();
    }

    public static String getStoredUserPassword(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_USER_PASSWORD, null);
    }

    public static void setStoredUserPassword(Context context, String userPassword) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_USER_PASSWORD, userPassword)
                .apply();
    }
}
