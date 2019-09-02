package com.eventtracker;

import android.util.Log;

import com.applandeo.materialcalendarview.EventDay;
import com.eventtracker.model.EventDetails;

import java.util.ArrayList;
import java.util.List;


/**
 * This class is a Adapter for Applandio Calendar library and Android Interface for DayType View.
 */
public class DayTypeAdapter {

    private static final String TAG = "DayTypeAdapter";

    public static List<EventDay> getCompatibleList(List<EventDetails> eventDetailsList) {
        List<EventDay> eventDayList = new ArrayList<>();
        EventDay eventDay = null;
        for (EventDetails eventDetails : eventDetailsList) {
            eventDay = new EventDay(eventDetails.getStartDayCal(), R.drawable.event);
            eventDayList.add(eventDay);
        }
        Log.d(TAG,"Events is retrieved.." );
        return eventDayList;
    }
}
