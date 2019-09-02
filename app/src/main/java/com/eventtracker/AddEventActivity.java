package com.eventtracker;

/**
 * This class is is responsible for creating and saving event in database.
 */
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

//import com.eventtracker.database.DatabaseHelper;
import com.eventtracker.databinding.ActivityAddEventBinding;
import com.eventtracker.model.EventDetails;
import com.eventtracker.network.FirebaseDbHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddEventActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EVENT_ID = "eventId";
    private static final String TAG = "AddEventActivity";
    ActivityAddEventBinding activityAddEventBinding;
    EventDetails eventDetails = new EventDetails(Calendar.getInstance().getTime(), Calendar.getInstance().getTime(), R.drawable.event);
    long eventId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAddEventBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_event);
        setListeners();
        Calendar calendar = Calendar.getInstance();
        activityAddEventBinding.startDateTextView.setText(getDateStr(calendar));
        activityAddEventBinding.startTimeTextView.setText(getTimeStr(calendar));
        activityAddEventBinding.endDateTextView.setText(getDateStr(calendar));
        activityAddEventBinding.endTimeTextView.setText(getTimeStr(calendar));
        populateDataForEdit();
        Log.d(TAG,"Event Created" );
    }

    /**
     * Populate the data required for an event
     */
    private void populateDataForEdit() {
        eventId = getIntent().getLongExtra(EVENT_ID, 0);
        if (eventId != 0) {
            //fetch event
            FirebaseDbHelper.getInstance().getEventById(eventId, new NetworkListener<EventDetails>() {

                @Override
                public void onSuccess(EventDetails eventDetails) {
                    AddEventActivity.this.eventDetails = eventDetails;
                    if (eventDetails != null) {
                        activityAddEventBinding.titleEditText.setText(eventDetails.getName());
                        activityAddEventBinding.addressEditText.setText(eventDetails.getAddress());
                        activityAddEventBinding.startDateTextView.setText(getDateStr(eventDetails.getStartDayCal()));
                        activityAddEventBinding.endDateTextView.setText(getDateStr(eventDetails.getEndDayCal()));
                        activityAddEventBinding.startTimeTextView.setText(getTimeStr(eventDetails.getStartDayCal()));
                        activityAddEventBinding.endTimeTextView.setText(getTimeStr(eventDetails.getEndDayCal()));
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e(TAG, "Received an exception while creating Event.. " + e.getMessage() );
                }
            });
        }
    }

    private void setListeners() {
        activityAddEventBinding.startDateTextView.setOnClickListener(this);
        activityAddEventBinding.startTimeTextView.setOnClickListener(this);
        activityAddEventBinding.endDateTextView.setOnClickListener(this);
        activityAddEventBinding.endTimeTextView.setOnClickListener(this);
        activityAddEventBinding.map.setOnClickListener(this);
    }

    /**
     * displays all the fields required for saving an event
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_date_text_view:
                showDatePicker(true);
                Log.d(TAG,"Start Date is Clicked" );
                break;

            case R.id.start_time_text_view:
                showTimePicker(true);
                Log.d(TAG,"Start Time is Clicked" );
                break;

            case R.id.end_date_text_view:
                showDatePicker(false);
                Log.d(TAG,"End Date is Clicked" );
                break;

            case R.id.end_time_text_view:
                showTimePicker(false);
                Log.d(TAG,"End Time is Clicked" );
                break;

            case R.id.map:
                openMap();
                Log.d(TAG,"Location is requested." );
                break;
        }
    }

    /**
     * This method call google MAP to set a location for an event.
     */
    private void openMap() {
        String mapUri = "http://maps.google.co.in/maps?q=" + activityAddEventBinding.addressEditText.getText().toString().trim();
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(mapUri));
        startActivity(i);
    }

    /**
     * This method displays the date to be selected
     * @param  isStart  boolean value for showing the date dialogue
     */
    void showDatePicker(final boolean isStart) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Calendar selectDate = Calendar.getInstance();
                selectDate.set(Calendar.YEAR, year);
                selectDate.set(Calendar.MONTH, month);
                selectDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if (isStart) {
                    eventDetails.setStartDay(selectDate.getTime());
                    activityAddEventBinding.startDateTextView.setText(getDateStr(selectDate));
                } else {
                    eventDetails.setEndDay(selectDate.getTime());
                    activityAddEventBinding.endDateTextView.setText(getDateStr(selectDate));
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /**
     * This method displays the time to be selected
     * @param  isStart  boolean value for start date
     */
    void showTimePicker(final boolean isStart) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar selectedCal = isStart ? eventDetails.getStartDayCal() : eventDetails.getEndDayCal();
                selectedCal.set(Calendar.HOUR_OF_DAY, hour);
                selectedCal.set(Calendar.MINUTE, minute);

                if (isStart) {
                    eventDetails.setStartDay(selectedCal.getTime());
                    activityAddEventBinding.startTimeTextView.setText(getTimeStr(selectedCal));
                } else {
                    eventDetails.setEndDay(selectedCal.getTime());
                    activityAddEventBinding.endTimeTextView.setText(getTimeStr(selectedCal));
                }
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    String getDateStr(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        return sdf.format(calendar.getTime());
    }

    String getTimeStr(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(calendar.getTime());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        menu.getItem(0).setTitle(eventId == 0 ? "Save" : "Update");
        menu.getItem(1).setVisible(eventId != 0);
        return true;
    }

    /**
     * This method is responsible for saving the newly created event to FireBase DB and editing an existing event
     * @param  item  Save or Update
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                Log.d(TAG,"Saving an Event." );
                eventDetails.setName(activityAddEventBinding.titleEditText.getText().toString());
                eventDetails.setAddress(activityAddEventBinding.addressEditText.getText().toString());
                if (isValid(eventDetails))
                    saveEvent();
                break;

            case R.id.delete:
                //Delete event
                Log.d(TAG,"Deleting an Event." );
                FirebaseDbHelper.getInstance().deleteEvent(eventDetails.getServerId(), new NetworkListener() {
                    @Override
                    public void onSuccess(Object o) {
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });
                break;
        }
        return true;
    }

    public boolean isValid(EventDetails eventDetails) {
        if (eventDetails.getName() == null || eventDetails.getName().toString().trim().length() == 0) {
            Toast.makeText(AddEventActivity.this, "Please Enter Title", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void saveEvent() {
        setResult(Activity.RESULT_OK, getIntent());
        if (eventId == 0) {
            // insert event
            eventDetails.setId(SharedPreferenceManager.getInstance(this).getGlobalId());
            SharedPreferenceManager.getInstance(this).incrementGlobalId();
            FirebaseDbHelper.getInstance().insertEvent(eventDetails);
            Log.d(TAG,"New Event created." );
             finish();

        } else {
            //update event
            FirebaseDbHelper.getInstance().updateEvent(eventDetails);
            Log.d(TAG,"Old Event updated." );
        }
        finish();
    }
}
