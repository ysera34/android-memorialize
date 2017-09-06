package com.memorial.altar.util;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by yoon on 2017. 8. 31..
 */

public class UserSharedPreferences {

    private static final String PREF_HOME_INTRO_SLIDE = "com.memorial.altar.home_intro_slide";
    private static final String PREF_STAR_COUNT = "com.memorial.altar.star_count";

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
}
