package com.example.ngo2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreference {


        static final String PREF_USER_NAME= "username";

        static SharedPreferences getSharedPreferences(Context ctx) {
            return PreferenceManager.getDefaultSharedPreferences(ctx);
        }

        public static void setEmail(Context ctx, String emailId)
        {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.putString(PREF_USER_NAME, emailId);
            editor.commit();
        }

        public static String getEmail(Context ctx)
        {
            return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
        }

        public static void clearEmail(Context ctx)
        {
            SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
            editor.clear(); //clear all stored data
            editor.commit();
        }
}
