package com.ranveeraggarwal.letrack.models;

/**
 * Created by raagga on 10-10-2016.
 */

public class Person {
    private String name;
    private int frequency;
    private int leaves;
    private String occupation;

    public Person () {}
    public Person (String name, int frequency, String occupation) {
        this.setName(name);
        this.setFrequency(frequency);
        this.setOccupation(occupation);
        this.setLeaves(0);
    }
    public Person (String name, int frequency, String occupation, int leaves) {
        this.setName(name);
        this.setFrequency(frequency);
        this.setOccupation(occupation);
        this.setLeaves(leaves);
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
}
