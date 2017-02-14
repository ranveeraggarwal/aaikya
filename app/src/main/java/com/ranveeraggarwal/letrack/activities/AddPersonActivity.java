package com.ranveeraggarwal.letrack.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.TextView;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.ranveeraggarwal.letrack.R;
import com.ranveeraggarwal.letrack.storage.DatabaseAdapter;

import static com.ranveeraggarwal.letrack.utilities.RepetitiveUI.shortToastMaker;

public class AddPersonActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    Toolbar toolbar;
    TextView nameField;
    TextView descriptionField;
    ImageView chooseFromContacts;
    Button submitButton;
    RadioGroup frequencyFieldGroup;
    RadioButton frequencyField;
    AppCompatSpinner startDateField;

    String selectedName;
    String selectedDescription;
    String selectedFrequency;
    String selectedStartDate;

    int REQUEST_CONTACTS = 1;
    String TAG = "AddPersonActivity";
    boolean hasContactPermission = false;

    DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_person);

        databaseAdapter = new DatabaseAdapter(this);

        toolbar = (Toolbar) findViewById(R.id.add_app_bar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Person");

        nameField = (TextView) findViewById(R.id.name_field);

        chooseFromContacts = (ImageView) findViewById(R.id.choose_from_contacts);
        chooseFromContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNameOnClickListener();
            }
        });

        descriptionField = (TextView) findViewById(R.id.description_field);

        frequencyFieldGroup = (RadioGroup) findViewById(R.id.frequency_field);

        startDateField = (AppCompatSpinner) findViewById(R.id.start_date_field);
        ArrayAdapter<CharSequence> dateAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_array, android.R.layout.simple_spinner_item);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        startDateField.setAdapter(dateAdapter);
        startDateField.setSelection(0);
        selectedStartDate = startDateField.getSelectedItem().toString();
        startDateField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStartDate = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitButtonOnClickListener();
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
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (menuItem.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (menuItem.getItemId() == R.id.action_add_person) {
            submitButtonOnClickListener();
        }

        return super.onOptionsItemSelected(menuItem);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CONTACTS) {
            hasContactPermission = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            addNameOnClickListener();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_CONTACTS) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                Uri uri = data.getData();
                String[] projection = { Phone.DISPLAY_NAME };

                try (Cursor cursor = getContentResolver().query(uri, projection,
                        null, null, null)) {
                    if (cursor == null || cursor.getCount() <= 0) {
                        Log.e(TAG, "Null cursor found. Weird! Probably no contact was picked");
                        return;
                    }
                    cursor.moveToFirst();

                    int nameColumnIndex = cursor.getColumnIndex(Phone.DISPLAY_NAME);
                    String name = cursor.getString(nameColumnIndex);
                    nameField.setText(name);
                }
            }
        }
    }

    private void addNameOnClickListener() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, REQUEST_CONTACTS);
        } else {
            hasContactPermission = true;
        }

        if (!hasContactPermission) return;

        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, REQUEST_CONTACTS);

    }

    private void submitButtonOnClickListener() {
        selectedName = nameField.getText().toString();

        selectedDescription = descriptionField.getText().toString();

        int checkedRadioButtonId = frequencyFieldGroup.getCheckedRadioButtonId();
        frequencyField = (RadioButton) frequencyFieldGroup.findViewById(checkedRadioButtonId);
        selectedFrequency = frequencyField.getText().toString();

        if (selectedName.equals("")) {
            shortToastMaker(this, "Name cannot be empty!");
        } else if (selectedDescription.equals("")) {
            shortToastMaker(this, "Description cannot be empty!");
        } else {
            long id = databaseAdapter.insertPerson(selectedName, selectedDescription, Integer.parseInt(selectedFrequency),
                    Integer.parseInt(selectedStartDate));
            if (id < 0) shortToastMaker(this, "Operation unsuccessful");
            else shortToastMaker(this, "Person added successfully");
            Intent intent = new Intent(AddPersonActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
