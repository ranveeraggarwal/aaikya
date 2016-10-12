package com.ranveeraggarwal.letrack.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.ranveeraggarwal.letrack.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class PersonDetails extends AppCompatActivity {

    private Toolbar toolbar;
    Button showCalender;
    TextView currentMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);

        toolbar= (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Amar Akbar");


        final CompactCalendarView compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);

        currentMonth = (TextView) findViewById(R.id.month);

        Calendar ept =  Calendar.getInstance();
        ept.add(ept.DATE, 1);
        ept.setTimeZone(TimeZone.getTimeZone("UTC"));
        Long pcs = ept.getTimeInMillis();
        Date x = ept.getTime();
        Log.d("Yolo", pcs.toString());
        Log.d("Hello", x.toString());
        // Add event 1 on Sun, 07 Jun 2015 18:20:51 GMT
        Event ev1 = new Event(getResources().getColor(R.color.colorAccent), pcs);
        compactCalendar.addEvent(ev1);

//        // Query for events on Sun, 07 Jun 2015 GMT.
//        // Time is not relevant when querying for events, since events are returned by day.
//        // So you can pass in any arbitary DateTime and you will receive all events for that day.
//        List<Event> events = compactCalendar.getEvents(1476111608000L); // can also take a Date object
//
//        // events has size 2 with the 2 events inserted previously
//        Log.d("Lol", "Events: " + events);

        // define a listener to receive callbacks when certain events happen.
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendar.getEvents(dateClicked);
                Log.d("Lol", "Day was clicked: " + dateClicked + " with events " + events);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                currentMonth.setText(firstDayOfNewMonth.getMonth() + 1 + "-" + firstDayOfNewMonth.getYear());
                Log.d("Lol", "Month was scrolled to: " + firstDayOfNewMonth);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void toaster(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        if (menuItem.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            toaster("Setting will be implemented soon :)");
            return true;
        } else if (menuItem.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
