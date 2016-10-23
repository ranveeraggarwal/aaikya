package com.ranveeraggarwal.letrack.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ranveeraggarwal.letrack.MainActivity;
import com.ranveeraggarwal.letrack.R;
import com.ranveeraggarwal.letrack.storage.DatabaseAdapter;

import static com.ranveeraggarwal.letrack.utilities.RepetitiveUI.shortToastMaker;

public class SettingsActivity extends AppCompatPreferenceActivity{

    Toolbar toolbar;
    Preference clearData;
    Preference contactUs;
    Preference about;

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
        clearData.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                confirmDatabaseRefresh();
                return false;
            }
        });

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
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
