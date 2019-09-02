package com.eventtracker;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * This class initialise the event with default values.
 */


public class EventTrackerApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
