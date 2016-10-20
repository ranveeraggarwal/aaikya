package com.ranveeraggarwal.letrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.microsoft.sonoma.analytics.Analytics;
import com.microsoft.sonoma.core.Sonoma;
import com.microsoft.sonoma.crashes.Crashes;
import com.ranveeraggarwal.letrack.activities.AddPersonActivity;
import com.ranveeraggarwal.letrack.activities.SettingsActivity;
import com.ranveeraggarwal.letrack.adapters.PersonAdapter;
import com.ranveeraggarwal.letrack.models.Person;
import com.ranveeraggarwal.letrack.storage.DatabaseAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.ranveeraggarwal.letrack.utils.RepetitiveUI.shortToastMaker;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton addPersonFab;
    private RecyclerView personList;

    private PersonAdapter personAdapter;
    private DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseAdapter = new DatabaseAdapter(this);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        addPersonFab = (FloatingActionButton)  findViewById(R.id.add_person_fab);
        addPersonFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPersonActivity.class);
                startActivity(intent);
            }
        });

        personList = (RecyclerView) findViewById(R.id.person_list);
        personAdapter = new PersonAdapter(this, databaseAdapter.getPersonList());
        personList.setAdapter(personAdapter);
        personList.setLayoutManager(new LinearLayoutManager(this));

        Sonoma.start(getApplication(), "36f51d49-2178-49b8-bfda-33e9611f1e26", Analytics.class, Crashes.class);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
