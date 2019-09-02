package com.eventtracker;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferenceManager {
    private static final String PREFERENCE = "event_tracker_preference";

    private static SharedPreferenceManager instance;
    private Context context;
    private SharedPreferences sharedpreferences;

    private SharedPreferenceManager(Context context) {
        this.context = context;
        sharedpreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceManager(context);
        }
        return instance;
    }

    public SharedPreferences getSharedPrefs() {
        return sharedpreferences;
    }

    public long getGlobalId() {
        return sharedpreferences.getLong("globalId", 1);
    }

    public void incrementGlobalId() {
        long id = sharedpreferences.getLong("globalId", 1);
        sharedpreferences.edit().putLong("globalId", id + 1).commit();
    }
}
