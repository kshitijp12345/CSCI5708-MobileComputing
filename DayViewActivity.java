package com.eventtracker;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekViewEvent;
import com.eventtracker.R;
import com.eventtracker.databinding.ActivityDayViewBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DayViewActivity extends AppCompatActivity {

    ActivityDayViewBinding activityDayViewBinding;
    List<WeekViewEvent> eventDetailsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDayViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_day_view);
        EventDetails eventDetails = getIntent().getParcelableExtra(CalendarFragment.EVENT_DETAILS);
        Calendar tempCal = (Calendar) eventDetails.getCalendar().clone();
        tempCal.add(Calendar.MINUTE, 20);
        WeekViewEvent event = new WeekViewEvent(1, "Test", eventDetails.getCalendar(), tempCal);
        eventDetailsList.add(event);
//        activityDayViewBinding.dayView.setEvents(eventDetailsList);

        activityDayViewBinding.dayView.setMonthChangeListener(mMonthChangeListener);
        activityDayViewBinding.dayView.goToDate(eventDetailsList.get(0).getStartTime());
    }

    void addSample() {
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
//        startTime.set(Calendar.MONTH, newMonth-1);
//        startTime.set(Calendar.YEAR, newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
//        endTime.set(Calendar.MONTH, newMonth-1);
        WeekViewEvent event = new WeekViewEvent(1, "sample", startTime, endTime);
        event.setColor(Color.RED);
        eventDetailsList.add(event);
    }

    MonthLoader.MonthChangeListener mMonthChangeListener = new MonthLoader.MonthChangeListener() {
        @Override
        public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
            // Populate the week view with some events.
//                List<WeekViewEvent> events = getEvents(newYear, newMonth);
            if (newMonth == eventDetailsList.get(0).getStartTime().get(Calendar.MONTH) + 1)
                return eventDetailsList;
            return new ArrayList<>();
        }
    };
}
