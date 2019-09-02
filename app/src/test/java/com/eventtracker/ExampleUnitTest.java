package com.eventtracker;

import com.eventtracker.model.EventDetails;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void validation_isCorrect() throws Exception {
        EventDetails eventDetails = new EventDetails();
        assertFalse(new AddEventActivity().isValid(eventDetails));
        eventDetails.setName("Valid Name");
        assertTrue(new AddEventActivity().isValid(eventDetails));
    }
}