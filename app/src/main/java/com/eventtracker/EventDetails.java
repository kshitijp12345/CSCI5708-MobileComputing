package com.eventtracker;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.applandeo.materialcalendarview.EventDay;
import com.framgia.library.calendardayview.data.IEvent;

import java.util.Calendar;

/**
 * This class contains the event details such as : Event Title, date and time.
 */

public class EventDetails extends EventDay implements Parcelable, IEvent {

    String title;
    Calendar day = Calendar.getInstance();

    public EventDetails(Calendar day, int imageResource) {
        super(day, imageResource);
        this.day = day;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Calendar getDay() {
        return day;
    }

    public void setDay(Calendar day) {
        this.day = day;
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public int getColor() {
        return Color.CYAN;
    }

    @Override
    public Calendar getStartTime() {
        return day;
    }

    @Override
    public Calendar getEndTime() {
        return day;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.day);
        dest.writeString(this.title);
    }

    protected EventDetails(Parcel in) {
        super((Calendar) in.readSerializable(), in.readInt());
        this.title = in.readString();
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
