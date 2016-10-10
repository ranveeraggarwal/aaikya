package com.ranveeraggarwal.letrack;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ranveeraggarwal.letrack.adapters.PersonAdapter;
import com.ranveeraggarwal.letrack.models.Person;

import java.util.ArrayList;
import java.util.List;

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
                toaster("Fab");
            }
        });

        personList = (RecyclerView) findViewById(R.id.person_list);
        personAdapter = new PersonAdapter(this, getData());
        personList.setAdapter(personAdapter);
        personList.setLayoutManager(new LinearLayoutManager(this));


    }

    public static List<Person> getData() {
        List<Person> data = new ArrayList<>();

        String[] names = {"Amar Kumar", "Anthony Gonzalves", "Akbar Mohammed", "Gurpreet Kaur"};
        String[] occupations = {"Cook", "Sweeper", "Dog Walker", "Gardener"};
        int[] daysOfTheMonth = {2, 5, 3, 6};
        int[] leaves = {1, 4, 0 , 0};
        for (int i=0; i<names.length && i<occupations.length && i<daysOfTheMonth.length && i<leaves.length; i++) {
            Person person = new Person(names[i], daysOfTheMonth[i], occupations[i], leaves[i]);
            data.add(person);
        }

        return data;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void toaster(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        if (menuItem.getItemId() == R.id.action_settings) {
            toaster("Setting will be implemented soon :)");
            return true;
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
