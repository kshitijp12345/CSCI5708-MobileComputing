package com.eventtracker;

import android.content.Context;
//import android.support.test.InstrumentationRegistry;
//import android.support.test.espresso.Espresso;
//import android.support.test.espresso.action.ViewActions;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, false, true);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.eventtracker", appContext.getPackageName());

        Espresso.onView(withId(R.id.add_floating_button)).perform(click());

        Espresso.onView(withId(R.id.title_edit_text))
                .perform(ViewActions.typeText("Auto title"));
    }
}
