package com.ranveeraggarwal.letrack.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ranveeraggarwal.letrack.R;

/**
 * Created by raagga on 10-10-2016.
 */

public class PersonViewHolder extends RecyclerView.ViewHolder {

    private TextView name;
    private TextView occupation;
    private TextView dayOfTheMonth;
    private TextView leaves;

    public PersonViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.name);
        occupation = (TextView) itemView.findViewById(R.id.occupation);
        dayOfTheMonth = (TextView) itemView.findViewById(R.id.dayOfTheMonth);
        leaves = (TextView) itemView.findViewById(R.id.leaves);
    }

    public TextView getName() {
        return name;
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public TextView getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation.setText(occupation);
    }

    public TextView getDayOfTheMonth() {
        return dayOfTheMonth;
    }

    public void setDayOfTheMonth(int dayOfTheMonth) {
        if (dayOfTheMonth%10 == 1) {
            this.dayOfTheMonth.setText(dayOfTheMonth + "st of every month");
        } else if (dayOfTheMonth%10 == 2) {
            this.dayOfTheMonth.setText(dayOfTheMonth + "nd of every month");
        } else if (dayOfTheMonth%10 == 3) {
            this.dayOfTheMonth.setText(dayOfTheMonth + "rd of every month");
        } else {
            this.dayOfTheMonth.setText(dayOfTheMonth + "th of every month");
        }
    }

    public TextView getLeaves() {
        return leaves;
    }

    public void setLeaves(int leaves) {
        this.leaves.setText(leaves + "");
    }
}
