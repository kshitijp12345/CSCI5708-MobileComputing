package com.eventtracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.eventtracker.R;
import com.eventtracker.databinding.ActivityAddEventBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityAddEventBinding activityAddEventBinding;
    EventDetails eventDetails = new EventDetails(Calendar.getInstance(), R.drawable.event);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddEventBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_event);
        setListeners();
        Calendar calendar = Calendar.getInstance();
        activityAddEventBinding.dateTextView.setText(getDateStr(calendar));
        activityAddEventBinding.timeTextView.setText(getTimeStr(calendar));
    }

    private void setListeners() {
        activityAddEventBinding.dateTextView.setOnClickListener(this);
        activityAddEventBinding.timeTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date_text_view:
                showDatePicker();
                break;

            case R.id.time_text_view:
                showTimePicker();
                break;
        }
    }

    void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Calendar selectDate = Calendar.getInstance();
                selectDate.set(Calendar.YEAR, year);
                selectDate.set(Calendar.MONTH, month);
                selectDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                eventDetails.setDay(selectDate);
                activityAddEventBinding.dateTextView.setText(getDateStr(selectDate));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar selectedCal = eventDetails.getDay();
                selectedCal.set(Calendar.HOUR_OF_DAY, hour);
                selectedCal.set(Calendar.MINUTE, minute);
                eventDetails.setDay(selectedCal);
                activityAddEventBinding.timeTextView.setText(getTimeStr(selectedCal));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    String getDateStr(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        return sdf.format(calendar.getTime());
    }

    String getTimeStr(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
        return sdf.format(calendar.getTime());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveEvent();
                break;
        }
        return true;
    }

    private void saveEvent() {
        eventDetails.setTitle(activityAddEventBinding.titleEditText.getText().toString());
        Intent intent = new Intent();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(CalendarFragment.EVENT_DETAILS, eventDetails);
//        intent.putExtras(bundle);
        intent.putExtra(CalendarFragment.EVENT_DETAILS, eventDetails);
//        intent.putExtra(CalendarFragment.EVENT_DETAILS, 1);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
