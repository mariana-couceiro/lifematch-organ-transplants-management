package com.lifematch.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.lifematch.dto.CandidateMatchResponse;
import com.lifematch.model.Candidate;
import com.lifematch.model.Correspondence;
import com.lifematch.model.Organ;
import com.lifematch.repository.CandidateRepository;
import com.lifematch.repository.CorrespondenceRepository;
import com.lifematch.repository.OrganRepository;

public class CorrespondenceService {

    private final CorrespondenceRepository correspondenceRepository;
    private final CandidateRepository candidateRepository;
    private final OrganRepository organRepository;

    public CorrespondenceService() {
        this.correspondenceRepository =
                new CorrespondenceRepository();

        this.candidateRepository =
                new CandidateRepository();

        this.organRepository =
                new OrganRepository();
    }

    public List<Candidate> findEligibleCandidates(int organId) {

        Organ organ = organRepository.findById(organId);

        if (organ == null) {
            throw new IllegalArgumentException(
                    "Organ not found."
            );
        }

        List<Candidate> eligibleCandidates =
                new ArrayList<>();

        for (Candidate candidate :
                candidateRepository.findAll()) {

            boolean sameOrgan =
                    candidate.getRequiredOrgan()
                            .equalsIgnoreCase(
                                    organ.getType()
                            );

            boolean sameBloodType =
                    candidate.getBloodType()
                            .equalsIgnoreCase(
                                    organ.getBloodType()
                            );

            boolean waiting =
                    candidate.getStatus()
                            .equalsIgnoreCase("WAITING");

            if (sameOrgan &&
                sameBloodType &&
                waiting) {

                eligibleCandidates.add(candidate);
            }
        }

        return eligibleCandidates;
    }

    public int calculateCompatibilityScore(
            Candidate candidate) {

        int priorityScore =
                candidate.getPriority() * 10;

        long monthsWaiting =
                ChronoUnit.MONTHS.between(
                        candidate.getWaitingListEntryDate(),
                        LocalDate.now()
                );

        int waitingScore =
                (int) monthsWaiting;

        return priorityScore + waitingScore;
    }

    public List<Candidate> getRankedCandidates(
            int organId) {

        List<Candidate> candidates =
                findEligibleCandidates(organId);

        candidates.sort(
                Comparator
                        .comparingInt(
                                this::calculateCompatibilityScore
                        )
                        .reversed()
                        .thenComparing(
                                Candidate::getWaitingListEntryDate
                        )
        );

        return candidates;
    }

    public List<CandidateMatchResponse> getRanking(
            int organId) {

        List<Candidate> candidates =
                getRankedCandidates(organId);

        List<CandidateMatchResponse> ranking =
                new ArrayList<>();

        int position = 1;

        for (Candidate candidate : candidates) {

            ranking.add(
                    new CandidateMatchResponse(
                            position,
                            candidate.getId(),
                            candidate.getName(),
                            candidate.getPriority(),
                            calculateCompatibilityScore(
                                    candidate
                            )
                    )
            );

            position++;
        }

        return ranking;
    }

    public Correspondence createProposal(int organId) {

        Organ organ =
                organRepository.findById(organId);

        if (organ == null) {
            throw new IllegalArgumentException(
                    "Organ not found."
            );
        }

        if (!organ.getStatus()
                .equalsIgnoreCase("AVAILABLE")) {

            throw new IllegalArgumentException(
                    "Organ is not available."
            );
        }

        Correspondence existingPending =
                correspondenceRepository
                        .findPendingByOrganId(organId);

        if (existingPending != null) {
            return existingPending;
        }

        List<Candidate> rankedCandidates =
                getRankedCandidates(organId);

        for (Candidate candidate :
                rankedCandidates) {

            if (!wasCandidateCancelled(
                    organId,
                    candidate.getId())) {

                Correspondence correspondence =
                        new Correspondence(
                                candidate.getId(),
                                organId,
                                calculateCompatibilityScore(
                                        candidate
                                ),
                                LocalDate.now(),
                                "PENDING"
                        );

                return correspondenceRepository.save(
                        correspondence
                );
            }
        }

        throw new IllegalArgumentException(
                "No eligible candidates available."
        );
    }

    public Correspondence confirmProposal(
            int correspondenceId) {

        Correspondence correspondence =
                correspondenceRepository
                        .findById(correspondenceId);

        if (correspondence == null) {
            throw new IllegalArgumentException(
                    "Correspondence not found."
            );
        }

        if (!correspondence.getStatus()
                .equalsIgnoreCase("PENDING")) {

            throw new IllegalArgumentException(
                "Correspondence is not pending."
            );
        }

        Organ organ =
                organRepository.findById(
                        correspondence.getOrganId()
                );

        if (organ == null) {
            throw new IllegalArgumentException(
                    "Organ not found."
            );
        }

        Candidate candidate =
                candidateRepository.findById(
                        correspondence.getCandidateId()
                );

        if (candidate == null) {
            throw new IllegalArgumentException(
                    "Candidate not found."
            );
        }

        correspondence.setStatus("SELECTED");

        organ.setStatus("MATCHED");

        candidate.setStatus("MATCHED");

        return correspondence;
    }

    public Correspondence cancelProposal(
            int correspondenceId,
            String reason) {

        Correspondence correspondence =
                correspondenceRepository
                        .findById(correspondenceId);

        if (correspondence == null) {
            throw new IllegalArgumentException(
                    "Correspondence not found."
            );
        }

        if (!correspondence.getStatus()
                .equalsIgnoreCase("PENDING")) {

            throw new IllegalArgumentException(
                    "Correspondence is not pending."
            );
        }

        if (reason == null ||
            reason.isBlank()) {

            throw new IllegalArgumentException(
                    "Cancellation reason is required."
            );
        }

        correspondence.setStatus("CANCELLED");

        correspondence.setCancellationReason(
                reason
        );

        return correspondence;
    }

    public Correspondence cancelAndCreateNext(
            int correspondenceId,
            String reason) {

        Correspondence cancelled =
                cancelProposal(
                        correspondenceId,
                        reason
                );

        return createProposal(
                cancelled.getOrganId()
        );
    }

    public List<Correspondence> getCorrespondencesByOrganId(
            int organId) {

        Organ organ = organRepository.findById(organId);

        if (organ == null) {
            throw new IllegalArgumentException(
                    "Organ not found."
            );
        }

        return correspondenceRepository.findByOrganId(organId);
    }

    private boolean wasCandidateCancelled(
            int organId,
            int candidateId) {

        List<Correspondence> correspondences =
                correspondenceRepository
                        .findByOrganId(organId);

        for (Correspondence correspondence :
                correspondences) {

            if (correspondence.getCandidateId()
                    == candidateId &&
                correspondence.getStatus()
                    .equalsIgnoreCase("CANCELLED")) {

                return true;
            }
        }

        return false;
    }
}
