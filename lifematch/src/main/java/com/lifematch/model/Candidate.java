package com.lifematch.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
import jakarta.persistence.Column;



@Entity
@Table(name = "candidate")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "blood_type", nullable = false)
    private String bloodType;

    @Column(name = "required_organ", nullable = false)
    private String requiredOrgan;

    @Column(nullable = false)
    private String urgency;

    @Column(nullable = false)
    private int priority;

    @Column(name = "waiting_list_entry_date", nullable = false)
    private LocalDate waitingListEntryDate;

    @Column(nullable = false)
    private String status;



    // CONSTRUCTOR
    public Candidate(
            String name,
            LocalDate birthDate,
            String bloodType,
            String requiredOrgan,
            String urgency,
            int priority,
            LocalDate waitingListEntryDate,
            String status) {

        this.name = name;
        this.birthDate = birthDate;
        this.bloodType = bloodType;
        this.requiredOrgan = requiredOrgan;
        this.urgency = urgency;
        this.priority = priority;
        this.waitingListEntryDate =
                waitingListEntryDate;
        this.status = status;
    }


    // ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    // NAME
    public String getName() {
        return name;
    }

    public void setName(
            String name) {

        this.name = name;
    }


    // BIRTH DATE
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(
            LocalDate birthDate) {

        this.birthDate = birthDate;
    }


    // BLOOD TYPE
    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(
            String bloodType) {

        this.bloodType = bloodType;
    }


    // REQUIRED ORGAN
    public String getRequiredOrgan() {
        return requiredOrgan;
    }

    public void setRequiredOrgan(
            String requiredOrgan) {

        this.requiredOrgan =
                requiredOrgan;
    }


    // URGENCY
    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(
            String urgency) {

        this.urgency = urgency;
    }


    // PRIORITY
    public int getPriority() {
        return priority;
    }

    public void setPriority(
            int priority) {

        this.priority = priority;
    }


    // WAITING LIST ENTRY DATE
    public LocalDate
            getWaitingListEntryDate() {

        return waitingListEntryDate;
    }

    public void setWaitingListEntryDate(
            LocalDate waitingListEntryDate) {

        this.waitingListEntryDate =
                waitingListEntryDate;
    }


    // STATUS
    public String getStatus() {
        return status;
    }

    public void setStatus(
            String status) {

        this.status = status;
    }
}
