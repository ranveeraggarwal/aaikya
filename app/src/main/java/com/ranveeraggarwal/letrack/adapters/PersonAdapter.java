package com.ranveeraggarwal.letrack.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ranveeraggarwal.letrack.R;
import com.ranveeraggarwal.letrack.activities.PersonDetails;
import com.ranveeraggarwal.letrack.models.Person;
import com.ranveeraggarwal.letrack.storage.DatabaseAdapter;
import com.ranveeraggarwal.letrack.views.PersonViewHolder;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.ranveeraggarwal.letrack.utils.RepetitiveUI.shortToastMaker;

/**
 * Created by raagga on 10-10-2016.
 */

public class PersonAdapter extends RecyclerView.Adapter <PersonViewHolder>{

    private LayoutInflater personInflater;

    private List<Person> data = Collections.emptyList();

    private Context context;

    private long currentDate;
    private DatabaseAdapter databaseAdapter;
    private int currentFrequency;

    public PersonAdapter (Context context, List<Person> data) {
        personInflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;

        // TODO: Instead of storing current time, store current date
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        cal.clear(Calendar.AM_PM);
        currentDate = cal.getTimeInMillis();
        databaseAdapter = new DatabaseAdapter(context);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = personInflater.inflate(R.layout.person_item, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder holder, int position) {
        final Person currentPerson = data.get(position);
        holder.setName(currentPerson.getName());
        holder.setOccupation(currentPerson.getOccupation());
        holder.setFrequency(currentPerson.getFrequency());
        holder.setLeaves(currentPerson.getLeaves());

        holder.addLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentFrequency = 1;
                if (databaseAdapter.checkLeave(currentDate, 1, currentPerson.getId())) currentFrequency = 2;
                if (databaseAdapter.checkLeave(currentDate, 2, currentPerson.getId())) currentFrequency = 3;
                if (databaseAdapter.checkLeave(currentDate, 3, currentPerson.getId())) currentFrequency = 4;
                if (databaseAdapter.checkLeave(currentDate, 4, currentPerson.getId())) currentFrequency = 5;
                if (currentFrequency <= currentPerson.getFrequency()) {
                    int currentLeaves = Integer.valueOf(holder.getLeaves().getText().toString());
                    if (databaseAdapter.insertLeave(currentPerson.getId(), currentDate, currentFrequency) > 0){
                        holder.setLeaves(currentLeaves + 1);
                        shortToastMaker(context, "Leave Added");
                    } else
                    {
                        shortToastMaker(context, "Leave Not Added");
                    }
                }
                else  {
                    holder.addLeave.setEnabled(false);
                }

            }
        });
        holder.cancelLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentLeaves = Integer.valueOf(holder.getLeaves().getText().toString());
                if (currentLeaves != 0) holder.setLeaves(currentLeaves - 1);
                shortToastMaker(context, "Leave Removed");
            }
        });
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PersonDetails.class);
                intent.putExtra("currentPerson", currentPerson);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
