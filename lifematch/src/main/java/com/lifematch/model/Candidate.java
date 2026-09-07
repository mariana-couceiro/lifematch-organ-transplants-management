package com.lifematch.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private LocalDate birthDate;
    private String bloodType;
    private String requiredOrgan;
    private int priority;
    private LocalDate waitingListEntryDate;
    private String status;

    public Candidate() {
    }

    public Candidate(String name, LocalDate birthDate, String bloodType,
                        String requiredOrgan, int priority,
                        LocalDate waitingListEntryDate, String status) {
        this.name = name;
        this.birthDate = birthDate;
        this.bloodType = bloodType;
        this.requiredOrgan = requiredOrgan;
        this.priority = priority;
        this.waitingListEntryDate = waitingListEntryDate;
        this.status = status;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getRequiredOrgan() {
        return requiredOrgan;
    }

    public void setRequiredOrgan(String requiredOrgan) {
        this.requiredOrgan = requiredOrgan;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public LocalDate getWaitingListEntryDate() {
        return waitingListEntryDate;
    }

    public void setWaitingListEntryDate(LocalDate waitingListEntryDate) {
        this.waitingListEntryDate = waitingListEntryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
