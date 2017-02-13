package com.ranveeraggarwal.letrack.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ranveeraggarwal.letrack.R;
import com.ranveeraggarwal.letrack.storage.DatabaseAdapter;

import static com.ranveeraggarwal.letrack.utilities.Utilities.composeEmail;

public class SettingsActivity extends AppCompatPreferenceActivity {

    Toolbar toolbar;
    Preference clearData;
    Preference contactUs;
    Preference about;
    Preference credits;

    DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = (Toolbar) findViewById(R.id.settings_app_bar);

        databaseAdapter = new DatabaseAdapter(this);

        //Toolbar
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");

        addPreferencesFromResource(R.xml.settings_preferences);

        clearData = findPreference("DatabaseRefresh");
        contactUs = findPreference("ContactUs");
        about = findPreference("About");
        credits = findPreference("Credits");

        clearData.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                confirmDatabaseRefresh();
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = getPrefs.edit();
                editor.putBoolean("firstRun", true);
                editor.apply();
                return false;
            }
        });

        contactUs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String[] devEmail = {"ranveerag2arwal@gmail.com"};
                composeEmail(devEmail, "Hello!", preference.getContext());
                return false;
            }
        });

        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                aboutDialog();
                return false;
            }
        });

        credits.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                creditsDialog();
                return false;
            }
        });
    }

    private void aboutDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About");
        builder.setMessage("This app was built as an 'Android Hello World' experiment by me, with the " +
                "help of some of my friends. You can freely use this app for eternity, it'll never " +
                "pivot to a paid app. Promise :)\n - Ranveer Aggarwal");
        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void creditsDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Credits");
        builder.setMessage("APIs: Folks at Google\n" +
                "Icons: materialdesignicons.com\n" +
                "Calendar: SundeepK - CompactCalendarView\n" +
                "Intro Screen: PaoloRotolo - AppIntro\n" +
                "Special thanks: Bijoy Singh");
        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void confirmDatabaseRefresh() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setMessage("Clicking yes will reset your database. This means " +
                "that all the people you've added, all the leaves you have added will be deleted.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                databaseAdapter.refreshDatabase();
                dialog.dismiss();
                Intent intent = new Intent(builder.getContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem settingsItem = menu.findItem(R.id.action_settings);
        settingsItem.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
