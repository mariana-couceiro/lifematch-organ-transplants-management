package com.lifematch.model;

public class OrganType {

    private int id;
    private String name;
    private int priorityPoints;
    private boolean active;


    public OrganType() {
    }


    public OrganType(
            String name,
            int priorityPoints,
            boolean active) {

        this.name = name;
        this.priorityPoints = priorityPoints;
        this.active = active;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getPriorityPoints() {
        return priorityPoints;
    }

    public void setPriorityPoints(
            int priorityPoints) {

        this.priorityPoints = priorityPoints;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
