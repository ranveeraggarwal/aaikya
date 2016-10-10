package com.ranveeraggarwal.letrack.models;

/**
 * Created by raagga on 10-10-2016.
 */

public class Person {
    private String name;
    private int dayOfTheMonth;
    private int leaves;
    private String occupation;

    public Person () {}
    public Person (String name, int dayOfTheMonth, String occupation) {
        this.setName(name);
        this.setDayOfTheMonth(dayOfTheMonth);
        this.setOccupation(occupation);
        this.setLeaves(0);
    }
    public Person (String name, int dayOfTheMonth, String occupation, int leaves) {
        this.setName(name);
        this.setDayOfTheMonth(dayOfTheMonth);
        this.setOccupation(occupation);
        this.setLeaves(leaves);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDayOfTheMonth() {
        return dayOfTheMonth;
    }

    public void setDayOfTheMonth(int dayOfTheMonth) {
        this.dayOfTheMonth = dayOfTheMonth;
    }

    public int getLeaves() {
        return leaves;
    }

    public void setLeaves(int leaves) {
        this.leaves = leaves;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}
