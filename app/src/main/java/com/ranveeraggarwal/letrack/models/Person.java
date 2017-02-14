package com.ranveeraggarwal.letrack.models;

import java.io.Serializable;

public class Person implements Serializable {
    private long id;
    private String name;
    private int frequency;
    private int leaves;
    private int startDate;
    private String description;

    public Person() {
    }

    public Person(String name, int frequency, String description, int startDate) {
        this.setName(name);
        this.setFrequency(frequency);
        this.setDescription(description);
        this.setLeaves(0);
        this.setStartDate(startDate);
    }

    public Person(long id, String name, int frequency, String description, int leaves, int startDate) {
        this.setId(id);
        this.setName(name);
        this.setFrequency(frequency);
        this.setDescription(description);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
