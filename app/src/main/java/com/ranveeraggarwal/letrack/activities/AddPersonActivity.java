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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ranveeraggarwal.letrack.MainActivity;
import com.ranveeraggarwal.letrack.R;
import com.ranveeraggarwal.letrack.models.Person;
import com.ranveeraggarwal.letrack.storage.DatabaseAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.ranveeraggarwal.letrack.utils.RepetitiveUI.shortToastMaker;

public class AddPersonActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView addPersonName;
    Spinner selectOccupation;
    Spinner selectDate;
    Button addPersonSubmit;
    RadioGroup addPersonFrequencyGroup;
    RadioButton addPersonFrequencyCheckedButton;

    String selectedName;
    String selectedOccupation;
    String selectedStartDate;
    String selectedFrequency;

    DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        databaseAdapter = new DatabaseAdapter(this);

        toolbar = (Toolbar) findViewById(R.id.add_person_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Person");

        addPersonName = (TextView) findViewById(R.id.new_person_name);

        selectOccupation = (Spinner) findViewById(R.id.new_person_occupation);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.occupations_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectOccupation.setAdapter(adapter);
        selectOccupation.setSelection(0);
        selectedOccupation = selectOccupation.getSelectedItem().toString();
        selectOccupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOccupation = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        selectDate = (Spinner) findViewById(R.id.new_person_date);
        ArrayAdapter<CharSequence> dateAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_array, android.R.layout.simple_spinner_item);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectDate.setAdapter(dateAdapter);
        selectDate.setSelection(0);
        selectedStartDate = selectDate.getSelectedItem().toString();
        selectDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStartDate = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        addPersonFrequencyGroup = (RadioGroup) findViewById(R.id.new_person_frequency);

        addPersonSubmit = (Button) findViewById(R.id.add_person_submit);
        addPersonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedName = addPersonName.getText().toString();
                int checkedRadioButtonId = addPersonFrequencyGroup.getCheckedRadioButtonId();
                addPersonFrequencyCheckedButton = (RadioButton) addPersonFrequencyGroup.findViewById(checkedRadioButtonId);
                selectedFrequency = addPersonFrequencyCheckedButton.getText().toString();
                long id = databaseAdapter.insertPerson(selectedName, selectedOccupation, Integer.parseInt(selectedFrequency), Integer.parseInt(selectedStartDate), 5000);
                if (id < 0) shortToastMaker(view.getContext(), "Operation unsuccessful");
                else shortToastMaker(view.getContext(), "Person added successfully");
                Intent intent = new Intent(AddPersonActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem settingsItem = menu.findItem(R.id.action_settings);
        settingsItem.setVisible(false);

        MenuItem addItem = menu.findItem(R.id.action_add_person);
        addItem.setVisible(true);

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
        } else if (menuItem.getItemId() == R.id.action_add_person) {
            selectedName = addPersonName.getText().toString();
            int checkedRadioButtonId = addPersonFrequencyGroup.getCheckedRadioButtonId();
            addPersonFrequencyCheckedButton = (RadioButton) addPersonFrequencyGroup.findViewById(checkedRadioButtonId);
            selectedFrequency = addPersonFrequencyCheckedButton.getText().toString();
            long id = databaseAdapter.insertPerson(selectedName, selectedOccupation, Integer.parseInt(selectedFrequency), Integer.parseInt(selectedStartDate), 5000);
            if (id < 0) shortToastMaker(this, "Operation unsuccessful");
            else shortToastMaker(this, "Person added successfully");
            Intent intent = new Intent(AddPersonActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
