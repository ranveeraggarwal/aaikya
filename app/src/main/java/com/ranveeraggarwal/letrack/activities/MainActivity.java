package com.ranveeraggarwal.letrack.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.ranveeraggarwal.letrack.R;
import com.ranveeraggarwal.letrack.adapters.PersonAdapter;
import com.ranveeraggarwal.letrack.storage.DatabaseAdapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    private PersonAdapter personAdapter;
    private DatabaseAdapter databaseAdapter;
    private RecyclerView personList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                boolean isFirstStart = getPrefs.getBoolean("firstRun", true);
                if (isFirstStart) {
                    SharedPreferences.Editor editor = getPrefs.edit();
                    editor.putBoolean("firstRun", false);
                    editor.apply();

                    Intent i = new Intent(MainActivity.this, IntroActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        t.start();

        databaseAdapter = new DatabaseAdapter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.details_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Leave Tracker");

        FloatingActionButton addPersonFab = (FloatingActionButton) findViewById(R.id.add_person_fab);
        addPersonFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPersonActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageView placeholderImage = (ImageView) findViewById(R.id.placeholder_image);

        personList = (RecyclerView) findViewById(R.id.person_list);
        personAdapter = new PersonAdapter(this, databaseAdapter.getPersonList());
        personList.setAdapter(personAdapter);
        personList.setLayoutManager(new LinearLayoutManager(this));

        int personCount = databaseAdapter.getPersonList().size();
        if (personCount > 0) {
            personList.setVisibility(VISIBLE);
            placeholderImage.setVisibility(GONE);
        } else {
            personList.setVisibility(GONE);
            placeholderImage.setVisibility(VISIBLE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseAdapter = new DatabaseAdapter(this);
        personAdapter = new PersonAdapter(this, databaseAdapter.getPersonList());
        personList.setAdapter(personAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
