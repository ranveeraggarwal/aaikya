package com.ranveeraggarwal.letrack.models;

import java.io.Serializable;

/**
 * Created by raagga on 10-10-2016.
 */

public class Person implements Serializable{
    private long id;
    private String name;
    private int frequency;
    private int leaves;
    private int startDate;
    private String occupation;

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    private int salary;

    public Person () {}
    public Person (String name, int frequency, String occupation, int startDate) {
        this.setName(name);
        this.setFrequency(frequency);
        this.setOccupation(occupation);
        this.setLeaves(0);
        this.setStartDate(startDate);
        this.setSalary(0);
    }
    public Person (String name, int frequency, String occupation, int startDate, int salary) {
        this.setName(name);
        this.setFrequency(frequency);
        this.setOccupation(occupation);
        this.setLeaves(0);
        this.setStartDate(startDate);
        this.setSalary(salary);
    }
    public Person (long id, String name, int frequency, String occupation, int leaves, int startDate, int salary) {
        this.setId(id);
        this.setName(name);
        this.setFrequency(frequency);
        this.setOccupation(occupation);
        this.setLeaves(leaves);
        this.setStartDate(startDate);
        this.setSalary(salary);
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
