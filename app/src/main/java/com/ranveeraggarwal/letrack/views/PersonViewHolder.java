package com.ranveeraggarwal.letrack.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ranveeraggarwal.letrack.R;

public class PersonViewHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private TextView description;
    private TextView frequency;
    private TextView leaves;

    public Button addLeave;
    public Button cancelLeave;

    public View root;

    public PersonViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.name);
        description = (TextView) itemView.findViewById(R.id.description);
        frequency = (TextView) itemView.findViewById(R.id.frequency);
        leaves = (TextView) itemView.findViewById(R.id.leaves);
        addLeave = (Button) itemView.findViewById(R.id.add_leave_button);
        cancelLeave = (Button) itemView.findViewById(R.id.cancel_leave_button);
        root = itemView;
    }

    public TextView getName() {
        return name;
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public TextView getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description.setText(description);
    }

    public TextView getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        if (frequency == 1) this.frequency.setText(frequency + " time a day");
        else this.frequency.setText(frequency + " times a day");
    }

    public TextView getLeaves() {
        return leaves;
    }

    public void setLeaves(int leaves) {
        this.leaves.setText(Integer.toString(leaves));
    }
}
