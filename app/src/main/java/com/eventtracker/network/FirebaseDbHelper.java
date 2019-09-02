package com.eventtracker.network;

/**
 * This class is is responsible creating connection with FIREBASE and for CRUD operations related to events .
 */

import android.util.Log;

import com.eventtracker.NetworkListener;
import com.eventtracker.model.EventDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FirebaseDbHelper {

    DatabaseReference myRef;
    private String TAG = this.getClass().getSimpleName();
    static FirebaseDbHelper firebaseDbHelper;

    public static FirebaseDbHelper getInstance() {
        if (firebaseDbHelper == null) {
            firebaseDbHelper = new FirebaseDbHelper();
        }
        return firebaseDbHelper;
    }

    private FirebaseDbHelper() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        myRef = database.getReference("GetItDone").child("Users").child("UserID").child("events");
        myRef.keepSynced(true);
    }

    public void insertEvent(EventDetails eventDetails) {
        myRef.push().setValue(eventDetails);

    }

    public void updateEvent(EventDetails eventDetails) {
        myRef.child(eventDetails.getServerId()).setValue(eventDetails);
    }

    public void getAllEvents(final NetworkListener<List<EventDetails>> networkListener) {
        final List<EventDetails> eventDetailsList = new ArrayList<>();
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    EventDetails eventDetails = postSnapshot.getValue(EventDetails.class);
                    eventDetails.setServerId(postSnapshot.getKey());
                    eventDetailsList.add(eventDetails);
                }

                networkListener.onSuccess(eventDetailsList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                networkListener.onFailure(error.toException());
            }
        });
    }

    public void getEventById(long id, final NetworkListener networkListener) {
        // Read from the database
        myRef.orderByChild("id").equalTo(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        EventDetails eventDetails = null;
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            eventDetails = postSnapshot.getValue(EventDetails.class);
                            eventDetails.setServerId(postSnapshot.getKey());
                        }

                        networkListener.onSuccess(eventDetails);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                        networkListener.onFailure(error.toException());
                    }
                });
    }

    public void getEventById(String id, final NetworkListener networkListener) {
        // Read from the database
        myRef.child(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        EventDetails eventDetails = null;
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            eventDetails = postSnapshot.getValue(EventDetails.class);
                            eventDetails.setServerId(postSnapshot.getKey());
                        }

                        networkListener.onSuccess(eventDetails);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                        networkListener.onFailure(error.toException());
                    }
                });
    }

    public void getAllEventsByDay(Date startDate, Date endDate, final NetworkListener<List<EventDetails>> networkListener) {
        final List<EventDetails> eventDetailsList = new ArrayList<>();
        // Read from the database
        myRef.orderByChild("startDay/time").startAt(startDate.getTime())
                .endAt(endDate.getTime())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            EventDetails eventDetails = postSnapshot.getValue(EventDetails.class);
                            eventDetails.setServerId(postSnapshot.getKey());
                            eventDetailsList.add(eventDetails);
                        }

                        networkListener.onSuccess(eventDetailsList);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                        networkListener.onFailure(error.toException());
                    }
                });
    }

    public void deleteEvent(String id, final NetworkListener networkListener) {
        try {
            myRef.child(id).removeValue();
            networkListener.onSuccess(null);
        } catch (Exception e) {
            networkListener.onFailure(e);
        }
    }
}
