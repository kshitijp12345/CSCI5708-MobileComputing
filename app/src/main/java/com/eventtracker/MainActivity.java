package com.eventtracker;

/**
 * This class is a main class designated for home page and launches 3 main activity as fragment : To-Do, EVent Scheduler, Group To-Do.
 */


import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.eventtracker.R;
//import com.eventtracker.database.DatabaseHelper;
import com.eventtracker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding activityMainBinding;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setListeners();
        loadFragment(new CalendarFragment());
        Log.d(TAG,"load all fragments.." );
    }

    /**
     * loads fragments  such as To-Do and Event calendar launched.
     * The item argument must specify the To-DO/Calendar/Group To-DO fragment.
     * @param  fragment  a bottom navigation selected fragment
     */
    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void setListeners() {
        activityMainBinding.bottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    /**
     * Returns an true or false if navigation Item such as To-Do and Event calendar launched.
     * The item argument must specify the To-DO/Calendar/Group To-DO.
     * @param  item  a bottom navigation selected item
     * @return  boolen returns true/false for the fragments load
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.calendar:
                Log.d(TAG,"load Calendar.." );
                loadFragment(new CalendarFragment());
                break;
            case R.id.todo:
                Log.d(TAG,"load To-Do.." );
                loadFragment(new Personal_ToDo_Fragment());
                break;
            case R.id.group:
                Log.d(TAG,"load Group To-Do.." );
                loadFragment(new Group_ToDo_Fragment());
                break;
        }
        return true;
    }
}