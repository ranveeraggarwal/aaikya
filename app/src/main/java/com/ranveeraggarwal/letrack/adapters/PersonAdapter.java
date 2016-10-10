package com.ranveeraggarwal.letrack.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ranveeraggarwal.letrack.R;
import com.ranveeraggarwal.letrack.models.Person;
import com.ranveeraggarwal.letrack.views.PersonViewHolder;

import java.util.Collections;
import java.util.List;

/**
 * Created by raagga on 10-10-2016.
 */

public class PersonAdapter extends RecyclerView.Adapter <PersonViewHolder>{

    private LayoutInflater personInflater;

    List<Person> data = Collections.emptyList();

    public PersonAdapter (Context context, List<Person> data) {
        personInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = personInflater.inflate(R.layout.person_item, parent, false);
        PersonViewHolder personViewHolder = new PersonViewHolder(view);
        return personViewHolder;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        Person currentPerson = data.get(position);
        holder.setName(currentPerson.getName());
        holder.setOccupation(currentPerson.getOccupation());
        holder.setDayOfTheMonth(currentPerson.getDayOfTheMonth());
        holder.setLeaves(currentPerson.getLeaves());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
