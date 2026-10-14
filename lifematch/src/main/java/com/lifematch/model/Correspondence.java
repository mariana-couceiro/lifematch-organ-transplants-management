package com.lifematch.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
import jakarta.persistence.Column;


@Entity
@Table(name = "correspondence")
public class Correspondence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "candidate_id", nullable = false)
    private int candidateId;

    @Column(name = "organ_id", nullable = false)
    private int organId;

    @Column(name = "compatibility_score", nullable = false)
    private int compatibilityScore;

    @Column(name = "correspondence_date", nullable = false)
    private LocalDate correspondenceDate;

    @Column(nullable = false)
    private String status;

    @Column(name = "cancellation_reason")
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
