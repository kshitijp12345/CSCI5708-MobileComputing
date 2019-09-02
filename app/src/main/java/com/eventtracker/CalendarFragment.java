package com.eventtracker;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
//import com.eventtracker.database.DatabaseHelper;
import com.eventtracker.databinding.FragmentCalendarBinding;
import com.eventtracker.model.EventDetails;
import com.eventtracker.network.FirebaseDbHelper;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment implements View.OnClickListener, OnDayClickListener {

    public static final String EVENT_DAY = "eventDay";
    private static final String TAG = "CalendarFragment";
    FragmentCalendarBinding fragmentCalendarBinding;
    List<EventDay> eventDayList = new ArrayList<>();

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentCalendarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false);
        setListeners();
        loadEvents();
        Log.d(TAG,"Calendar fragment has been Created" );
        return fragmentCalendarBinding.getRoot();
    }

    private void setListeners() {
        fragmentCalendarBinding.addFloatingButton.setOnClickListener(this);
        fragmentCalendarBinding.calendarView.setOnDayClickListener(this);
        fragmentCalendarBinding.calendarView.showCurrentMonthPage();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_floating_button:
                Log.d(TAG,"Add Event activity.." );
                Intent addEventIntent = new Intent(getContext(), AddEventActivity.class);
                startActivity(addEventIntent);
                Log.d(TAG,"Calendar activity is started.." );
                break;
        }
    }


    public void onResume() {
          super.onResume();
          loadEvents();
    }
    void loadEvents() {
        //fetch all events
        FirebaseDbHelper.getInstance().getAllEvents(new NetworkListener<List<EventDetails>>() {
            @Override
            public void onSuccess(List<EventDetails> eventDetailsList) {
                eventDayList.clear();
                eventDayList.addAll(DayTypeAdapter.getCompatibleList(eventDetailsList));
                try {
                    if (eventDayList.size() > 0)
                        fragmentCalendarBinding.calendarView.setDate(eventDayList.get(0).getCalendar());
                } catch (OutOfDateRangeException e) {
                    Log.e(TAG, "Received an date range exception .. " + e.getMessage() );
                }
                fragmentCalendarBinding.calendarView.setEvents(eventDayList);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Received an exception while loading Event.. " + e.getMessage() );
            }
        });
    }

    @Override
    public void onDayClick(EventDay eventDay) {
        Intent dayView = new Intent(getContext(), DayViewActivity.class);
        dayView.putExtra(EVENT_DAY, eventDay.getCalendar());
        Log.d(TAG,"Add Day View.." );
        startActivity(dayView);
    }
}
