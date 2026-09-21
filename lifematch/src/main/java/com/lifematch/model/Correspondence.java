package com.lifematch.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Correspondence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int candidateId;
    private int organId;
    private int compatibilityScore;
    private LocalDate correspondenceDate;
    private String status;
    private String cancellationReason;

    public Correspondence() {
    }

    public Correspondence(int candidateId,
                            int organId,
                            int compatibilityScore,
                            LocalDate correspondenceDate,
                            String status) {

        this.candidateId = candidateId;
        this.organId = organId;
        this.compatibilityScore = compatibilityScore;
        this.correspondenceDate = correspondenceDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public int getOrganId() {
        return organId;
    }

    public void setOrganId(int organId) {
        this.organId = organId;
    }

    public int getCompatibilityScore() {
        return compatibilityScore;
    }

    public void setCompatibilityScore(int compatibilityScore) {
        this.compatibilityScore = compatibilityScore;
    }

    public LocalDate getCorrespondenceDate() {
        return correspondenceDate;
    }

    public void setCorrespondenceDate(LocalDate correspondenceDate) {
        this.correspondenceDate = correspondenceDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
}
