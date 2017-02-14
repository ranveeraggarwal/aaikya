package com.ranveeraggarwal.letrack.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ranveeraggarwal.letrack.R;
import com.ranveeraggarwal.letrack.activities.PersonDetails;
import com.ranveeraggarwal.letrack.models.Person;
import com.ranveeraggarwal.letrack.storage.DatabaseAdapter;
import com.ranveeraggarwal.letrack.views.PersonViewHolder;

import java.util.Collections;
import java.util.List;

import static com.ranveeraggarwal.letrack.utilities.RepetitiveUI.shortToastMaker;
import static com.ranveeraggarwal.letrack.utilities.Utilities.getCurrentDate;


public class PersonAdapter extends RecyclerView.Adapter<PersonViewHolder> {

    private LayoutInflater personInflater;

    private List<Person> data = Collections.emptyList();

    private Context context;

    private DatabaseAdapter databaseAdapter;

    public PersonAdapter(Context context, List<Person> data) {
        personInflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
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
        currentPerson.setLeaves(databaseAdapter.getLeavesForDate(getCurrentDate(), currentPerson.getId()).size());
        holder.setName(currentPerson.getName());
        holder.setDescription(currentPerson.getDescription());
        holder.setFrequency(currentPerson.getFrequency());
        holder.setLeaves(currentPerson.getLeaves());
        if (currentPerson.getLeaves() >= currentPerson.getFrequency()) {
            holder.addLeave.setEnabled(false);
            holder.addLeave.setTextColor(context.getResources().getColor(R.color.colorTextSecondary));
        }
        if (currentPerson.getLeaves() > 0) {
            holder.cancelLeave.setEnabled(true);
            holder.cancelLeave.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        }
        holder.addLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentLeaves = databaseAdapter.getLeavesForDate(getCurrentDate(), currentPerson.getId()).size();
                if (currentLeaves < currentPerson.getFrequency()) {
                    if (databaseAdapter.insertLeave(currentPerson.getId(), getCurrentDate(), currentLeaves + 1) > 0) {
                        currentLeaves++;
                        holder.setLeaves(currentLeaves);
                    } else {
                        shortToastMaker(context, "Leave Not Added");
                    }
                }
                if (currentLeaves >= currentPerson.getFrequency()) {
                    holder.addLeave.setEnabled(false);
                    holder.addLeave.setTextColor(context.getResources().getColor(R.color.colorTextSecondary));
                }
                if (currentLeaves > 0) {
                    holder.cancelLeave.setEnabled(true);
                    holder.cancelLeave.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                }
            }
        });
        holder.cancelLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentLeaves = databaseAdapter.getLeavesForDate(getCurrentDate(), currentPerson.getId()).size();
                if (currentLeaves > 0) {
                    if (databaseAdapter.deleteLeave(currentPerson.getId(), getCurrentDate(), currentLeaves) > 0) {
                        currentLeaves--;
                        holder.setLeaves(currentLeaves);
                    } else {
                        shortToastMaker(context, "Leave Not Removed");
                    }
                }
                if (currentLeaves == 0) {
                    holder.cancelLeave.setEnabled(false);
                    holder.cancelLeave.setTextColor(context.getResources().getColor(R.color.colorTextSecondary));
                }
                if (currentLeaves < currentPerson.getFrequency()) {
                    holder.addLeave.setEnabled(true);
                    holder.addLeave.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                }
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
