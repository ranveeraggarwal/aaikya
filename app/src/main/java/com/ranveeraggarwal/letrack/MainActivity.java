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
import com.ranveeraggarwal.letrack.models.Person;

import java.util.ArrayList;
import java.util.List;

import static com.ranveeraggarwal.letrack.utils.RepetitiveUI.shortToastMaker;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton addPersonFab;
    private RecyclerView personList;

    private PersonAdapter personAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        personAdapter = new PersonAdapter(this, getData());
        personList.setAdapter(personAdapter);
        personList.setLayoutManager(new LinearLayoutManager(this));


    }

    public static List<Person> getData() {
        List<Person> data = new ArrayList<>();

        String[] names = {"Amar Kumar", "Anthony Gonzalves", "Akbar Mohammed", "Gurpreet Kaur", "Dheerendra Dutta"};
        String[] occupations = {"Cook", "Sweeper", "Dog Walker", "Gardener", "Bachcha"};
        int[] frequencies = {2, 1, 3, 2, 1};
        int[] leaves = {1, 4, 0, 0, 10};
        for (int i=0; i<names.length && i<occupations.length && i<frequencies.length && i<leaves.length; i++) {
            Person person = new Person(names[i], frequencies[i], occupations[i], leaves[i], 1);
            data.add(person);
        }

        return data;
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
