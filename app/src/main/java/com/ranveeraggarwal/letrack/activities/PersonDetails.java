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
    long currentDay;

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

        currentDay = getCurrentDate();

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
        setButtons();

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
                currentDay = dateClicked.getTime();
                refreshDayStats();
                setButtons();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

                monthText.setText(getMonthName(firstDayOfNewMonth.getMonth() + 1) + ", " + (firstDayOfNewMonth.getYear() + 1900));
                //TODO: Make this work with different kinds of months.
                long startDate = firstDayOfNewMonth.getTime();
                long endDate = startDate + getCurrentMonthOffset(1) - getCurrentMonth();
                currentDay = startDate;
                refreshDayStats();
                refreshMonthStats(startDate, endDate);
                setButtons();
            }
        });

        addLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentLeaves = databaseAdapter.getLeavesForDate(currentDay, person.getId()).size();
                if (currentLeaves < person.getFrequency()){
                    if (databaseAdapter.insertLeave(person.getId(), currentDay, currentLeaves+1) > 0){
                        leavesDayValue++;
                        leavesMonthValue++;
                        leavesDayText.setText(String.format(Locale.ENGLISH, "%d", leavesDayValue));
                        leavesMonthText.setText(String.format(Locale.ENGLISH, "%d", leavesMonthValue));
                        Event event = new Event(getResources().getColor(R.color.colorPrimaryDark), currentDay);
                        calendarView.addEvent(event, true);
                    }
                    else
                    {
                        shortToastMaker(v.getContext(), "Leave Not Added");
                    }
                }
                setButtons();
            }
        });

        removeLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentLeaves = databaseAdapter.getLeavesForDate(currentDay, person.getId()).size();
                if (currentLeaves > 0 ){
                    if (databaseAdapter.deleteLeave(person.getId(), currentDay, currentLeaves) > 0) {
                        leavesDayValue--;
                        leavesMonthValue--;
                        leavesDayText.setText(String.format(Locale.ENGLISH, "%d", leavesDayValue));
                        leavesMonthText.setText(String.format(Locale.ENGLISH, "%d", leavesMonthValue));
                        List<Event> events = calendarView.getEvents(currentDay);
                        if (events.size() > 0) {
                            events.remove(events.size() - 1);
                            calendarView.invalidate();
                        }
                    }
                    else {
                        shortToastMaker(v.getContext(), "Leave Not Removed");
                    }
                }
                setButtons();
            }
        });
    }

    private void setButtons() {
        int leaves = databaseAdapter.getLeavesForDate(currentDay, person.getId()).size();
        int frequency = person.getFrequency();
        if (leaves < frequency) {
            addLeave.setEnabled(true);
            addLeave.setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));
        } else {
            addLeave.setEnabled(false);
            addLeave.setTextColor(this.getResources().getColor(R.color.colorTextSecondary));
        }
        if (leaves > 0) {
            removeLeave.setEnabled(true);
            removeLeave.setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));
        } else {
            removeLeave.setEnabled(false);
            removeLeave.setTextColor(this.getResources().getColor(R.color.colorTextSecondary));
        }
    }

    private void refreshDayStats() {
        leavesDayValue = databaseAdapter.getLeavesForDate(currentDay, person.getId()).size();
        leavesDayText.setText(String.format(Locale.ENGLISH, "%d", leavesDayValue));
        if (currentDay == getCurrentDate()) {
            leavesDayDesc.setText(R.string.today);
        } else {
            leavesDayDesc.setText(R.string.on_selected_day);
        }
    }

    private void refreshMonthStats(long startDate, long endDate) {
        leavesMonthValue = databaseAdapter.getLeavesInRange(startDate, endDate, person.getId()).size();
        leavesMonthText.setText(String.format(Locale.ENGLISH, "%d", leavesMonthValue));
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
            finish();
            return true;
        } else if (menuItem.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
