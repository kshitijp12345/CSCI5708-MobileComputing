package com.eventtracker;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
//import com.eventtracker.database.DatabaseHelper;
import com.eventtracker.databinding.ActivityDayViewBinding;
import com.eventtracker.model.EventDetails;
import com.eventtracker.network.FirebaseDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DayViewActivity extends AppCompatActivity implements WeekView.EventClickListener {

    ActivityDayViewBinding activityDayViewBinding;
    private static final String TAG = "DayViewActivity";
    List<WeekViewEvent> weekEventList = new ArrayList<>();
    private Calendar startCal, endCal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDayViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_day_view);
        setListeners();
        Calendar calendar = (Calendar) getIntent().getSerializableExtra(CalendarFragment.EVENT_DAY);
        startCal = (Calendar) calendar.clone();
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);

        endCal = (Calendar) startCal.clone();
        endCal.add(Calendar.DAY_OF_MONTH, 1);
        Log.d(TAG,"Day View OnCreate" );

    }

    void loadEvents() {
        // fetch all events by day
        FirebaseDbHelper.getInstance().getAllEventsByDay(startCal.getTime(), endCal.getTime(), new NetworkListener<List<EventDetails>>() {
            @Override
            public void onSuccess(List<EventDetails> eventDetailsList) {
                weekEventList.clear();
                for (EventDetails eventDetails : eventDetailsList) {
                    WeekViewEvent event = new WeekViewEvent(eventDetails.getId(), eventDetails.getName(), eventDetails.getStartDayCal(), eventDetails.getEndDayCal());
                    weekEventList.add(event);
                    Log.d(TAG,"Week View Events.." );
                }
                activityDayViewBinding.dayView.setMonthChangeListener(mMonthChangeListener);
                if (eventDetailsList.size() > 0)
                    activityDayViewBinding.dayView.goToDate(eventDetailsList.get(0).getStartTime());
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Received an exception while loading Event.. " + e.getMessage() );
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        loadEvents();
    }

    private void setListeners() {
        activityDayViewBinding.dayView.setOnEventClickListener(this);
    }

    MonthLoader.MonthChangeListener mMonthChangeListener = new MonthLoader.MonthChangeListener() {
        @Override
        public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
            // Populate the week view with some events.
            if (weekEventList.size() > 0 && newMonth == weekEventList.get(0).getStartTime().get(Calendar.MONTH) + 1)
                return weekEventList;
            return new ArrayList<>();
        }
    };

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Intent editEvent = new Intent(this, AddEventActivity.class);
        editEvent.putExtra(AddEventActivity.EVENT_ID, event.getId());
        startActivity(editEvent);
    }
}
