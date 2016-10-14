package com.ranveeraggarwal.letrack.models;

/**
 * Created by raagga on 10-10-2016.
 */

public class Person {
    private String name;
    private int frequency;
    private int leaves;
    private int startDate;
    private String occupation;

    public Person () {}
    public Person (String name, int frequency, String occupation, int startDate) {
        this.setName(name);
        this.setFrequency(frequency);
        this.setOccupation(occupation);
        this.setLeaves(0);
        this.setStartDate(startDate);
    }
    public Person (String name, int frequency, String occupation, int leaves, int startDate) {
        this.setName(name);
        this.setFrequency(frequency);
        this.setOccupation(occupation);
        this.setLeaves(leaves);
        this.setStartDate(startDate);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
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

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }
}
