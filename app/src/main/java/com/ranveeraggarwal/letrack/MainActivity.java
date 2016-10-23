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

import com.ranveeraggarwal.letrack.activities.AddPersonActivity;
import com.ranveeraggarwal.letrack.activities.SettingsActivity;
import com.ranveeraggarwal.letrack.adapters.PersonAdapter;
import com.ranveeraggarwal.letrack.storage.DatabaseAdapter;

public class MainActivity extends AppCompatActivity {

    private PersonAdapter personAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseAdapter databaseAdapter = new DatabaseAdapter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.details_app_bar);
        setSupportActionBar(toolbar);

        FloatingActionButton addPersonFab = (FloatingActionButton) findViewById(R.id.add_person_fab);
        addPersonFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPersonActivity.class);
                startActivity(intent);
                finish();
            }
        });

        RecyclerView personList = (RecyclerView) findViewById(R.id.person_list);
        personAdapter = new PersonAdapter(this, databaseAdapter.getPersonList());
        personList.setAdapter(personAdapter);
        personList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        personAdapter.notifyDataSetChanged();
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
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
