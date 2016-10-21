package com.ranveeraggarwal.letrack.activities;

import android.content.Intent;
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

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.ranveeraggarwal.letrack.R;
import com.ranveeraggarwal.letrack.models.Leave;
import com.ranveeraggarwal.letrack.models.Person;
import com.ranveeraggarwal.letrack.storage.DatabaseAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.ranveeraggarwal.letrack.utilities.RepetitiveUI.shortToastMaker;
import static com.ranveeraggarwal.letrack.utilities.Utilities.getCurrentDate;
import static com.ranveeraggarwal.letrack.utilities.Utilities.getCurrentMonth;
import static com.ranveeraggarwal.letrack.utilities.Utilities.getCurrentMonthOffset;
import static com.ranveeraggarwal.letrack.utilities.Utilities.getMonthName;

public class PersonDetails extends AppCompatActivity {

    DatabaseAdapter databaseAdapter;

    Toolbar toolbar;

    TextView currentMonth;
    TextView occupation;
    TextView startDate;
    TextView frequency;
    TextView leavesToday;
    TextView leavesThisMonth;
    TextView leavesTodayDesc;
    TextView leavesThisMonthDesc;

    Button addLeave;
    Button removeLeave;

    Person person;

    int todayLeaves;
    int thisMonthLeaves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);

        databaseAdapter = new DatabaseAdapter(this);

        toolbar = (Toolbar) findViewById(R.id.app_bar);

        currentMonth = (TextView) findViewById(R.id.month);
        occupation = (TextView) findViewById(R.id.person_details_occupation);
        startDate = (TextView) findViewById(R.id.person_details_start_date);
        frequency = (TextView) findViewById(R.id.person_details_frequency);
        leavesToday = (TextView) findViewById(R.id.person_details_leaves_today);
        leavesThisMonth = (TextView) findViewById(R.id.person_details_leaves_this_month);
        leavesTodayDesc = (TextView) findViewById(R.id.person_details_leaves_today_desc);
        leavesThisMonthDesc = (TextView) findViewById(R.id.person_details_leaves_this_month_desc);

        addLeave = (Button) findViewById(R.id.person_details_add_leave);
        removeLeave = (Button) findViewById(R.id.person_details_remove_leave);

        Intent intent = getIntent();
        person = (Person) intent.getSerializableExtra("currentPerson");

        todayLeaves = databaseAdapter.getLeavesForDate(getCurrentDate(), person.getId()).size();
        thisMonthLeaves = databaseAdapter.getLeavesInRange(getCurrentMonth(), getCurrentMonthOffset(1), person.getId()).size();

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(person.getName());

        occupation.setText(person.getOccupation());

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

        frequency.setText(Integer.toString(person.getFrequency()));

        leavesToday.setText(todayLeaves+"");
        leavesThisMonth.setText(thisMonthLeaves+"");

        if (databaseAdapter.getLeavesForDate(getCurrentDate(), person.getId()).size() >= person.getFrequency())
        {
            addLeave.setEnabled(false);
            addLeave.setTextColor(this.getResources().getColor(R.color.colorTextSecondary));
        }
        if (databaseAdapter.getLeavesForDate(getCurrentDate(), person.getId()).size() > 0) {
            removeLeave.setEnabled(true);
            removeLeave.setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        addLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentLeaves = databaseAdapter.getLeavesForDate(getCurrentDate(), person.getId()).size();
                if (currentLeaves < person.getFrequency()){
                    if (databaseAdapter.insertLeave(person.getId(), getCurrentDate(), currentLeaves+1) > 0){
                        currentLeaves++;
                        todayLeaves++;
                        thisMonthLeaves++;
                        leavesToday.setText(todayLeaves+"");
                        leavesThisMonth.setText(thisMonthLeaves+"");
                    }
                    else
                    {
                        shortToastMaker(v.getContext(), "Leave Not Added");
                    }
                }
                if (currentLeaves >= person.getFrequency())
                {
                    addLeave.setEnabled(false);
                    addLeave.setTextColor(v.getContext().getResources().getColor(R.color.colorTextSecondary));
                }
                if (currentLeaves > 0) {
                    removeLeave.setEnabled(true);
                    removeLeave.setTextColor(v.getContext().getResources().getColor(R.color.colorPrimaryDark));
                }
            }
        });

        removeLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentLeaves = databaseAdapter.getLeavesForDate(getCurrentDate(), person.getId()).size();
                if (currentLeaves > 0 ){
                    if (databaseAdapter.deleteLeave(person.getId(), getCurrentDate(), currentLeaves) > 0) {
                        currentLeaves--;
                        todayLeaves--;
                        thisMonthLeaves--;
                        leavesToday.setText(todayLeaves+"");
                        leavesThisMonth.setText(thisMonthLeaves+"");
                    }
                    else {
                        shortToastMaker(v.getContext(), "Leave Not Removed");
                    }
                }
                if (currentLeaves == 0) {
                    removeLeave.setEnabled(false);
                    removeLeave.setTextColor(v.getContext().getResources().getColor(R.color.colorTextSecondary));
                }
                if (currentLeaves < person.getFrequency()) {
                    addLeave.setEnabled(true);
                    addLeave.setTextColor(v.getContext().getResources().getColor(R.color.colorPrimaryDark));
                }
            }
        });



        final CompactCalendarView compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        
        long something = getCurrentMonth();
        Event event = new Event(getResources().getColor(R.color.colorAccent), something);
        compactCalendar.addEvent(event);

        List<Leave> allLeaves = databaseAdapter.getLeavesForPerson(person.getId());
        for (int i=0; i<allLeaves.size(); i++) {
            Event eventa = new Event(getResources().getColor(R.color.colorAccent), allLeaves.get(i).getDate());
            compactCalendar.addEvent(eventa);
        }

        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendar.getEvents(dateClicked);
                //Log.d("Lol", "Day was clicked: " + dateClicked + " with events " + events);
//                long timeInMillis = dateClicked.getTime();
//                int leavesForTheDay = databaseAdapter.getLeavesForDate(timeInMillis, person.getId()).size();
//                leavesToday.setText(""+leavesForTheDay);
//                leavesTodayDesc.setText("On selected date");
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

                currentMonth.setText(getMonthName(firstDayOfNewMonth.getMonth() + 1) + ", " + (firstDayOfNewMonth.getYear() + 1900));
//                long startDate = firstDayOfNewMonth.getTime();
//                long endDate = startDate + getCurrentMonthOffset(1) - getCurrentMonth();
//                //Log.d("Lol", "Month was scrolled to: " + firstDayOfNewMonth);
//                int leavesForTheMonth = databaseAdapter.getLeavesInRange(startDate, endDate, person.getId()).size();
//                leavesThisMonth.setText(""+leavesForTheMonth);
//                leavesThisMonthDesc.setText("In selected month");
//                int leavesForTheDay = databaseAdapter.getLeavesForDate(startDate, person.getId()).size();
//                leavesToday.setText(""+leavesForTheDay);
//                leavesTodayDesc.setText("On selected date");
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
