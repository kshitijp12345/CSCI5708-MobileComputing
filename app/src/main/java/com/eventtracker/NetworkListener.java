package com.eventtracker;

public interface NetworkListener<T> {

    void onSuccess(T t);

    void onFailure(Exception e);
}
