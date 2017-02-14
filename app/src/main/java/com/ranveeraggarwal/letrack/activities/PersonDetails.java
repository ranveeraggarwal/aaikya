package com.ranveeraggarwal.letrack.activities;

import android.content.Intent;
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
import static com.ranveeraggarwal.letrack.utilities.Utilities.addDays;
import static com.ranveeraggarwal.letrack.utilities.Utilities.addMonths;
import static com.ranveeraggarwal.letrack.utilities.Utilities.getCurrentDate;
import static com.ranveeraggarwal.letrack.utilities.Utilities.getCurrentMonth;
import static com.ranveeraggarwal.letrack.utilities.Utilities.getMonth;
import static com.ranveeraggarwal.letrack.utilities.Utilities.getMonthName;

public class PersonDetails extends AppCompatActivity {

    DatabaseAdapter databaseAdapter;

    Toolbar toolbar;

    TextView monthText;
    TextView descriptionText;
    TextView frequencyText;
    TextView startDateText;

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
    long currentMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);

        // Initialising all variables
        databaseAdapter = new DatabaseAdapter(this);

        toolbar = (Toolbar) findViewById(R.id.details_app_bar);

        monthText = (TextView) findViewById(R.id.month_text);
        descriptionText = (TextView) findViewById(R.id.description_text);
        frequencyText = (TextView) findViewById(R.id.frequency_text);
        startDateText = (TextView) findViewById(R.id.start_day_text);
        leavesDayText = (TextView) findViewById(R.id.leaves_day_text);
        leavesMonthText = (TextView) findViewById(R.id.leaves_month_text);
        leavesDayDesc = (TextView) findViewById(R.id.leaves_day_desc);
        leavesMonthDesc = (TextView) findViewById(R.id.leaves_month_desc);

        calendarView = (CompactCalendarView) findViewById(R.id.calendar_view);

        addLeave = (Button) findViewById(R.id.details_add_leave);
        removeLeave = (Button) findViewById(R.id.details_remove_leave);

        Intent intent = getIntent();
        person = (Person) intent.getSerializableExtra("currentPerson");
        person.setStartDate(person.getStartDate());

        currentDay = getCurrentDate();
        currentMonth = addDays(getCurrentMonth(), person.getStartDate());

        leavesDayValue = databaseAdapter.getLeavesForDate(currentDay, person.getId()).size();
        leavesMonthValue = databaseAdapter.getLeavesInRange(currentMonth, addMonths(currentMonth, 1), person.getId()).size();

        Date dateToday = new Date();

        //Toolbar
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(person.getName());

        monthText.setText(getMonthName(dateToday.getMonth() + 1) + ", " + (dateToday.getYear() + 1900));

        // Setting UI elements to current value
        descriptionText.setText(person.getDescription());

        switch (person.getStartDate()){
            case 1:
                startDateText.setText("Cycle: 1st of every month");
                break;
            case 21:
                startDateText.setText("Cycle: 21st of every month");
                break;
            case 31:
                startDateText.setText("Cycle: 31st of every month");
                break;
            case 2:
                startDateText.setText("Cycle: 2nd of every month");
                break;
            case 22:
                startDateText.setText("Cycle: 22nd of every month");
                break;
            case 3:
                startDateText.setText("Cycle: 3rd of every month");
                break;
            case 23:
                startDateText.setText("Cycle: 23rd of every month");
                break;
            default:
                startDateText.setText("Cycle: " + person.getStartDate() + "th of every month");
                break;
        }

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

        leavesMonthDesc.setText(String.format(Locale.ENGLISH, "%d %s - %d %s",
                person.getStartDate(), getMonthName(getMonth(currentDay)).substring(0, 3), person.getStartDate(),
                getMonthName(getMonth(currentDay) + 1).substring(0, 3)));

        // Setting current buttons
        setButtons();

        // Calendar initialisation
        calendarView.shouldDrawIndicatorsBelowSelectedDays(true);
        List<Leave> allLeaves = databaseAdapter.getLeavesForPerson(person.getId());
        for (int i = 0; i < allLeaves.size(); i++) {
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
                long startDate = firstDayOfNewMonth.getTime();
                if (startDate < currentMonth) {
                    currentMonth = addMonths(currentMonth, -1);
                } else if (startDate > currentMonth) {
                    currentMonth = addMonths(currentMonth, 1);
                }
                currentDay = startDate;
                refreshDayStats();
                refreshMonthStats();
                setButtons();
            }
        });

        addLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentLeaves = databaseAdapter.getLeavesForDate(currentDay, person.getId()).size();
                if (currentLeaves < person.getFrequency()) {
                    if (databaseAdapter.insertLeave(person.getId(), currentDay, currentLeaves + 1) > 0) {
                        leavesDayValue++;
                        leavesMonthValue++;
                        leavesDayText.setText(String.format(Locale.ENGLISH, "%d", leavesDayValue));
                        leavesMonthText.setText(String.format(Locale.ENGLISH, "%d", leavesMonthValue));
                        Event event = new Event(getResources().getColor(R.color.colorPrimaryDark), currentDay);
                        calendarView.addEvent(event, true);
                    } else {
                        shortToastMaker(v.getContext(), "Leave Not Added");
                    }
                }
                refreshMonthStats();
                setButtons();
            }
        });

        removeLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentLeaves = databaseAdapter.getLeavesForDate(currentDay, person.getId()).size();
                if (currentLeaves > 0) {
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
                    } else {
                        shortToastMaker(v.getContext(), "Leave Not Removed");
                    }
                }
                refreshMonthStats();
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

    private void refreshMonthStats() {
        leavesMonthValue = databaseAdapter.getLeavesInRange(currentMonth, addMonths(currentMonth, 1), person.getId()).size();
        leavesMonthText.setText(String.format(Locale.ENGLISH, "%d", leavesMonthValue));
        leavesMonthDesc.setText(String.format(Locale.ENGLISH, "%d %s - %d %s",
                person.getStartDate(), getMonthName(getMonth(currentDay)).substring(0, 3), person.getStartDate(),
                getMonthName(getMonth(currentDay) + 1).substring(0, 3)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem settingsItem = menu.findItem(R.id.action_settings);
        settingsItem.setVisible(false);

        MenuItem editItem = menu.findItem(R.id.action_edit_person);
        editItem.setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (menuItem.getItemId() == R.id.action_edit_person) {
            Intent intent = new Intent(this, EditPersonActivity.class);
            intent.putExtra("currentPerson", person);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
