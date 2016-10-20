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
import com.ranveeraggarwal.letrack.models.Person;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.ranveeraggarwal.letrack.utils.RepetitiveUI.shortToastMaker;

public class PersonDetails extends AppCompatActivity {

    TextView currentMonth;
    TextView occupation;
    TextView startDate;
    TextView frequency;

    Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);

        Intent intent = getIntent();
        person = (Person) intent.getSerializableExtra("currentPerson");

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(person.getName());

        occupation = (TextView) findViewById(R.id.person_details_occupation);
        occupation.setText(person.getOccupation());

        startDate = (TextView) findViewById(R.id.person_details_start_date);
        switch (person.getStartDate()){
            case 1:
                startDate.setText("Cycle: 1st of every month");
                break;
            case 21:
                startDate.setText("Cycle: 21st of every month");
                break;
            case 31:
                startDate.setText("Cycle: 31st of every month");
                break;
            case 2:
                startDate.setText("Cycle: 2nd of every month");
                break;
            case 22:
                startDate.setText("Cycle: 22nd of every month");
                break;
            case 3:
                startDate.setText("Cycle: 3rd of every month");
                break;
            case 23:
                startDate.setText("Cycle: 23rd of every month");
                break;
            default:
                startDate.setText("Cycle: " + person.getStartDate() + "th of every month");
                break;
        }

        frequency = (TextView) findViewById(R.id.person_details_frequency);
        frequency.setText(Integer.toString(person.getFrequency()));

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

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        if (menuItem.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            shortToastMaker(this, "Setting will be implemented soon :)");
            return true;
        } else if (menuItem.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
