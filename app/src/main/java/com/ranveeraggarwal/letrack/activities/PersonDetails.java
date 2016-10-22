package com.ranveeraggarwal.letrack.activities;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.ranveeraggarwal.letrack.utilities.RepetitiveUI.shortToastMaker;
import static com.ranveeraggarwal.letrack.utilities.Utilities.getCurrentDate;
import static com.ranveeraggarwal.letrack.utilities.Utilities.getCurrentMonth;
import static com.ranveeraggarwal.letrack.utilities.Utilities.getCurrentMonthOffset;
import static com.ranveeraggarwal.letrack.utilities.Utilities.getMonthName;

public class PersonDetails extends AppCompatActivity {

    DatabaseAdapter databaseAdapter;

    Toolbar toolbar;

    TextView monthText;
    TextView occupationText;
    TextView frequencyText;

    TextView leavesDayText;
    TextView leavesMonthText;
    TextView leavesDayDesc;
    TextView leavesMonthDesc;

    CompactCalendarView calendarView;

    Button addLeave;
    Button removeLeave;

    Person person;

    int leavesDayValue;
    int leavesMonthValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);

        // Initialising all variables
        databaseAdapter = new DatabaseAdapter(this);

        toolbar = (Toolbar) findViewById(R.id.details_app_bar);

        monthText = (TextView) findViewById(R.id.month_text);
        occupationText = (TextView) findViewById(R.id.occupation_text);
        frequencyText = (TextView) findViewById(R.id.frequency_text);
        leavesDayText = (TextView) findViewById(R.id.leaves_day_text);
        leavesMonthText = (TextView) findViewById(R.id.leaves_month_text);
        leavesDayDesc = (TextView) findViewById(R.id.leaves_day_desc);
        leavesMonthDesc = (TextView) findViewById(R.id.leaves_month_desc);

        calendarView = (CompactCalendarView) findViewById(R.id.calendar_view);

        addLeave = (Button) findViewById(R.id.details_add_leave);
        removeLeave = (Button) findViewById(R.id.details_remove_leave);

        Intent intent = getIntent();
        person = (Person) intent.getSerializableExtra("currentPerson");

        leavesDayValue = databaseAdapter.getLeavesForDate(getCurrentDate(), person.getId()).size();
        leavesMonthValue = databaseAdapter.getLeavesInRange(getCurrentMonth(), getCurrentMonthOffset(1), person.getId()).size();

        //Toolbar
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(person.getName());

        // Setting UI elements to current value
        occupationText.setText(person.getOccupation());

        String frequencyText = "Once";
        switch (person.getFrequency()) {
            case 1:
                frequencyText = "Once";
                break;
            case 2:
                frequencyText = "Twice";
                break;
            case 3:
                frequencyText = "Thrice";
                break;
            case 4:
                frequencyText = "Four Times";
                break;
        }

        this.frequencyText.setText(frequencyText);

        leavesDayText.setText(String.format(Locale.ENGLISH, "%d", leavesDayValue));
        leavesMonthText.setText(String.format(Locale.ENGLISH, "%d", leavesMonthValue));

        // Setting current buttons
        if (databaseAdapter.getLeavesForDate(getCurrentDate(), person.getId()).size() >= person.getFrequency())
        {
            addLeave.setEnabled(false);
            addLeave.setTextColor(this.getResources().getColor(R.color.colorTextSecondary));
        }
        if (databaseAdapter.getLeavesForDate(getCurrentDate(), person.getId()).size() > 0) {
            removeLeave.setEnabled(true);
            removeLeave.setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }

        // Calendar initialisation
        calendarView.shouldDrawIndicatorsBelowSelectedDays(true);
        List<Leave> allLeaves = databaseAdapter.getLeavesForPerson(person.getId());
        for (int i=0; i<allLeaves.size(); i++) {
            Event event = new Event(getResources().getColor(R.color.colorPrimaryDark), allLeaves.get(i).getDate());
            calendarView.addEvent(event);
        }

        calendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = calendarView.getEvents(dateClicked);
                //Log.d("Lol", "Day was clicked: " + dateClicked + " with events " + events);
//                long timeInMillis = dateClicked.getTime();
//                int leavesForTheDay = databaseAdapter.getLeavesForDate(timeInMillis, person.getId()).size();
//                leavesDayText.setText(""+leavesForTheDay);
//                leavesDayDesc.setText("On selected date");
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

                monthText.setText(getMonthName(firstDayOfNewMonth.getMonth() + 1) + ", " + (firstDayOfNewMonth.getYear() + 1900));
//                long startDate = firstDayOfNewMonth.getTime();
//                long endDate = startDate + getCurrentMonthOffset(1) - getCurrentMonth();
//                //Log.d("Lol", "Month was scrolled to: " + firstDayOfNewMonth);
//                int leavesForTheMonth = databaseAdapter.getLeavesInRange(startDate, endDate, person.getId()).size();
//                leavesMonthText.setText(""+leavesForTheMonth);
//                leavesMonthDesc.setText("In selected month");
//                int leavesForTheDay = databaseAdapter.getLeavesForDate(startDate, person.getId()).size();
//                leavesDayText.setText(""+leavesForTheDay);
//                leavesDayDesc.setText("On selected date");
            }
        });

        addLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentLeaves = databaseAdapter.getLeavesForDate(getCurrentDate(), person.getId()).size();
                if (currentLeaves < person.getFrequency()){
                    if (databaseAdapter.insertLeave(person.getId(), getCurrentDate(), currentLeaves+1) > 0){
                        currentLeaves++;
                        leavesDayValue++;
                        leavesMonthValue++;
                        leavesDayText.setText(String.format(Locale.ENGLISH, "%d", leavesDayValue));
                        leavesMonthText.setText(String.format(Locale.ENGLISH, "%d", leavesMonthValue));
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
                        leavesDayValue--;
                        leavesMonthValue--;
                        leavesDayText.setText(String.format(Locale.ENGLISH, "%d", leavesDayValue));
                        leavesMonthText.setText(String.format(Locale.ENGLISH, "%d", leavesMonthValue));
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
            return true;
        } else if (menuItem.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
