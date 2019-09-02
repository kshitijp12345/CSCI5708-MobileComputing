package com.eventtracker.model;


import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.applandeo.materialcalendarview.EventDay;
import com.framgia.library.calendardayview.data.IEvent;
import com.google.firebase.database.Exclude;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class EventDetails implements IEvent {


    long id;
    String serverId;
    String name;
    Date startDay = new Date();
    Date endDay = new Date();
    String address;

    public EventDetails() {
    }

    public EventDetails(Date startDay, Date endDay, int imageResource) {

        this.startDay = startDay;
        this.endDay = endDay;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Date getStartDay() {
        return startDay;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStartDay(Date startDay) {
        this.startDay = startDay;
    }

    public Date getEndDay() {
        return endDay;
    }

    @Exclude
    public Calendar getStartDayCal() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDay);
        return calendar;
    }

    @Exclude
    public Calendar getEndDayCal() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDay);
        return calendar;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
    }


    protected EventDetails(Parcel in) {
        this.name = in.readString();
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getColor() {
        return Color.CYAN;
    }

    @Exclude
    @Override
    public Calendar getStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDay);
        return calendar;
    }

    @Exclude
    @Override
    public Calendar getEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDay);
        return calendar;
    }

    public static final Parcelable.Creator<EventDetails> CREATOR = new Parcelable.Creator<EventDetails>() {
        @Override
        public EventDetails createFromParcel(Parcel source) {
            return new EventDetails(source);
        }

        @Override
        public EventDetails[] newArray(int size) {
            return new EventDetails[size];
        }
    };
}
