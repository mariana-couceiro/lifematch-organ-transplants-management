package com.lifematch.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import com.lifematch.model.Candidate;
import com.lifematch.model.OrganType;
import com.lifematch.repository.CandidateRepository;
import com.lifematch.repository.OrganTypeRepository;

public class CandidateService {

    private final CandidateRepository repository;
    private final OrganTypeRepository organTypeRepository;


    public CandidateService() {

        this.repository =
                new CandidateRepository();

        this.organTypeRepository =
                new OrganTypeRepository();
    }


    public Candidate createCandidate(
            Candidate candidate) {

        candidate.setStatus("WAITING");

        validateCandidate(candidate);

        candidate.setPriority(
                calculatePriority(candidate)
        );

        return repository.save(candidate);
    }


    public List<Candidate> getAllCandidates() {

        return repository.findAll();
    }


    public List<Candidate> getCandidatesByName(
            String name) {

        return repository.findByName(name);
    }


    public Candidate getCandidateById(int id) {

        return repository.findById(id);
    }


    public Candidate updateCandidate(
            int id,
            Candidate candidate) {

        Candidate existingCandidate =
                repository.findById(id);

        if (existingCandidate == null) {
            return null;
        }

        candidate.setStatus(
                existingCandidate.getStatus()
        );

        validateCandidate(candidate);

        candidate.setPriority(
                calculatePriority(candidate)
        );

        return repository.update(
                id,
                candidate
        );
    }


    public boolean deleteCandidate(int id) {

        return repository.delete(id);
    }


    private int calculatePriority(
            Candidate candidate) {

        int score = 0;


        // AGE
        int age = Period.between(
                candidate.getBirthDate(),
                LocalDate.now()
        ).getYears();


        if (age <= 17) {

            score += 3;

        } else if (age <= 39) {

            score += 2;

        } else if (age <= 64) {

            score += 1;
        }


        // ORGAN
        OrganType organType =
                organTypeRepository.findByName(
                        candidate.getRequiredOrgan()
                );

        if (organType != null) {

            score += organType.getPriorityPoints();
        }


        // URGENCY
        switch (candidate.getUrgency()) {

            case "MODERATE":

                score += 1;
                break;


            case "HIGH":

                score += 2;
                break;


            case "CRITICAL":

                score += 3;
                break;


            case "LOW":

                break;
        }


        // CONVERT SCORE TO PRIORITY
        if (score >= 7) {
            return 4;
        }

        if (score >= 5) {
            return 3;
        }

        if (score >= 3) {
            return 2;
        }

        return 1;
    }


    private void validateCandidate(
            Candidate candidate) {


        // NAME
        if (candidate.getName() == null ||
            candidate.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Candidate name is required."
            );
        }


        // BIRTH DATE
        if (candidate.getBirthDate() == null) {

            throw new IllegalArgumentException(
                    "Birth date is required."
            );
        }


        if (candidate.getBirthDate()
                .isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Birth date cannot be in the future."
            );
        }


        // BLOOD TYPE
        if (candidate.getBloodType() == null ||
            candidate.getBloodType().isBlank()) {

            throw new IllegalArgumentException(
                    "Blood type is required."
            );
        }


        if (!isValidBloodType(
                candidate.getBloodType())) {

            throw new IllegalArgumentException(
                    "Invalid blood type."
            );
        }


        // REQUIRED ORGAN
        if (candidate.getRequiredOrgan() == null ||
            candidate.getRequiredOrgan().isBlank()) {

            throw new IllegalArgumentException(
                    "Required organ is required."
            );
        }


        if (!isValidOrgan(
                candidate.getRequiredOrgan())) {

            throw new IllegalArgumentException(
                    "Invalid required organ."
            );
        }


        // URGENCY
        if (candidate.getUrgency() == null ||
            candidate.getUrgency().isBlank()) {

            throw new IllegalArgumentException(
                    "Urgency is required."
            );
        }


        if (!isValidUrgency(
                candidate.getUrgency())) {

            throw new IllegalArgumentException(
                    "Invalid urgency."
            );
        }


        // WAITING LIST ENTRY DATE
        if (candidate.getWaitingListEntryDate()
                == null) {

            throw new IllegalArgumentException(
                    "Waiting list entry date is required."
            );
        }


        if (candidate.getWaitingListEntryDate()
                .isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Waiting list entry date cannot be in the future."
            );
        }


        // STATUS
        if (candidate.getStatus() == null ||
            candidate.getStatus().isBlank()) {

            throw new IllegalArgumentException(
                    "Candidate status is required."
            );
        }


        if (!isValidStatus(
                candidate.getStatus())) {

            throw new IllegalArgumentException(
                    "Invalid candidate status."
            );
        }
    }


    private boolean isValidBloodType(
            String bloodType) {

        return bloodType.equals("A+") ||
                bloodType.equals("A-") ||
                bloodType.equals("B+") ||
                bloodType.equals("B-") ||
                bloodType.equals("AB+") ||
                bloodType.equals("AB-") ||
                bloodType.equals("0+") ||
                bloodType.equals("0-");
    }


    private boolean isValidOrgan(
            String organ) {

        OrganType organType =
                organTypeRepository.findByName(
                        organ
                );

        return organType != null &&
                organType.isActive();
    }


    private boolean isValidUrgency(
            String urgency) {

        return urgency.equals("LOW") ||
                urgency.equals("MODERATE") ||
                urgency.equals("HIGH") ||
                urgency.equals("CRITICAL");
    }


    private boolean isValidStatus(
            String status) {

        return status.equals("WAITING") ||
                status.equals("MATCHED") ||
                status.equals("INACTIVE");
    }
}
