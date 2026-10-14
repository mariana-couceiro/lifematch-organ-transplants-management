package com.lifematch.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
@Table(name = "organ_type")
public class OrganType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "priority_points", nullable = false)
    private int priorityPoints;

    @Column(nullable = false)
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

    public void setPriorityPoints(int priorityPoints) {
        this.priorityPoints = priorityPoints;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

