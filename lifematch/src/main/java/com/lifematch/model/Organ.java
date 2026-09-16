package com.lifematch.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Organ {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String type;
    private String bloodType;

    private String donorName;
    private String donorCode;
    private LocalDate donorBirthDate;

    private String hospital;
    private LocalDate availabilityDate;
    private String status;

    public Organ() {
    }

    public Organ(String type, String bloodType, String donorName,
                    LocalDate donorBirthDate, String hospital,
                    LocalDate availabilityDate, String status) {

        this.type = type;
        this.bloodType = bloodType;
        this.donorName = donorName;
        this.donorBirthDate = donorBirthDate;
        this.hospital = hospital;
        this.availabilityDate = availabilityDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getDonorCode() {
        return donorCode;
    }

    public void setDonorCode(String donorCode) {
        this.donorCode = donorCode;
    }

    public LocalDate getDonorBirthDate() {
        return donorBirthDate;
    }

    public void setDonorBirthDate(LocalDate donorBirthDate) {
        this.donorBirthDate = donorBirthDate;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public LocalDate getAvailabilityDate() {
        return availabilityDate;
    }

    public void setAvailabilityDate(LocalDate availabilityDate) {
        this.availabilityDate = availabilityDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
