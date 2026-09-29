package com.lifematch.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.lifematch.dto.CandidateMatchResponse;
import com.lifematch.dto.CorrespondenceResponse;
import com.lifematch.model.Candidate;
import com.lifematch.model.Correspondence;
import com.lifematch.model.Organ;
import com.lifematch.repository.CandidateRepository;
import com.lifematch.repository.CorrespondenceRepository;
import com.lifematch.repository.OrganRepository;

public class CorrespondenceService {

        private final CandidateRepository candidateRepository;
        private final OrganRepository organRepository;
        private final CorrespondenceRepository correspondenceRepository;

        public CorrespondenceService() {

                this.candidateRepository = new CandidateRepository();

                this.organRepository = new OrganRepository();

                this.correspondenceRepository = new CorrespondenceRepository();
        }

        // GET ALL CORRESPONDENCES

        public List<CorrespondenceResponse> getAllCorrespondences() {

                List<Correspondence> correspondences = correspondenceRepository.findAll();

                List<CorrespondenceResponse> responses = new ArrayList<>();

                for (Correspondence correspondence : correspondences) {

                        responses.add(
                                        toResponse(correspondence));
                }

                return responses;
        }

        // GET CORRESPONDENCES BY ORGAN

        public List<CorrespondenceResponse> getCorrespondencesByOrganId(
                        int organId) {

                List<Correspondence> correspondences = correspondenceRepository
                                .findByOrganId(organId);

                List<CorrespondenceResponse> responses = new ArrayList<>();

                for (Correspondence correspondence : correspondences) {

                        responses.add(
                                        toResponse(correspondence));
                }

                return responses;
        }

        // GET CORRESPONDENCES BY CANDIDATE

        public List<CorrespondenceResponse> getCorrespondencesByCandidateId(
                        int candidateId) {

                List<Correspondence> correspondences = correspondenceRepository
                                .findByCandidateId(candidateId);

                List<CorrespondenceResponse> responses = new ArrayList<>();

                for (Correspondence correspondence : correspondences) {

                        responses.add(
                                        toResponse(correspondence));
                }

                return responses;
        }

        // FIND ELIGIBLE CANDIDATES

        public List<Candidate> findEligibleCandidates(
                        int organId) {

                Organ organ = organRepository.findById(
                                organId);

                if (organ == null) {

                        throw new IllegalArgumentException(
                                        "Organ not found.");
                }

                if (!"AVAILABLE".equals(
                                organ.getStatus())) {

                        throw new IllegalArgumentException(
                                        "Organ is not available.");
                }

                List<Candidate> eligibleCandidates = new ArrayList<>();

                for (Candidate candidate : candidateRepository.findAll()) {

                        boolean sameOrgan = candidate.getRequiredOrgan()
                                        .equalsIgnoreCase(
                                                        organ.getType());

                        boolean sameBloodType = candidate.getBloodType()
                                        .equals(
                                                        organ.getBloodType());

                        boolean waiting = "WAITING".equals(
                                        candidate.getStatus());

                        if (sameOrgan &&
                                        sameBloodType &&
                                        waiting) {

                                eligibleCandidates.add(
                                                candidate);
                        }
                }

                return eligibleCandidates;
        }

        // CALCULATE COMPATIBILITY SCORE

        public int calculateCompatibilityScore(
                        Candidate candidate) {

                int priorityScore = candidate.getPriority() * 10;

                int waitingScore = calculateWaitingMonths(
                                candidate
                                                .getWaitingListEntryDate());

                return priorityScore +
                                waitingScore;
        }

        // CALCULATE WAITING MONTHS

        private int calculateWaitingMonths(
                        LocalDate entryDate) {

                Period period = Period.between(
                                entryDate,
                                LocalDate.now());

                return period.getYears() * 12
                                + period.getMonths();
        }

        // GET RANKED CANDIDATES

        public List<Candidate> getRankedCandidates(
                        int organId) {

                List<Candidate> candidates = findEligibleCandidates(
                                organId);

                candidates.sort(
                                Comparator
                                                .comparingInt(
                                                                this::calculateCompatibilityScore)
                                                .reversed()
                                                .thenComparing(
                                                                Candidate::getWaitingListEntryDate));

                return candidates;
        }

        // GET RANKING RESPONSE

        public List<CandidateMatchResponse> getRanking(
                        int organId) {

                List<Candidate> candidates = getRankedCandidates(
                                organId);

                List<CandidateMatchResponse> ranking = new ArrayList<>();

                int position = 1;

                for (Candidate candidate : candidates) {

                        CandidateMatchResponse response = new CandidateMatchResponse(
                                        position,
                                        candidate.getId(),
                                        candidate.getName(),
                                        candidate.getPriority(),
                                        calculateCompatibilityScore(
                                                        candidate));

                        ranking.add(response);

                        position++;
                }

                return ranking;
        }

        // CREATE PROPOSAL

        public CorrespondenceResponse createProposal(
                        int organId) {

                Organ organ = organRepository.findById(
                                organId);

                if (organ == null) {

                        throw new IllegalArgumentException(
                                        "Organ not found.");
                }

                if (!"AVAILABLE".equals(
                                organ.getStatus())) {

                        throw new IllegalArgumentException(
                                        "Organ is not available.");
                }

                Correspondence existingPending = correspondenceRepository
                                .findPendingByOrganId(
                                                organId);

                if (existingPending != null) {

                        return toResponse(
                                        existingPending);
                }

                List<Candidate> rankedCandidates = getRankedCandidates(
                                organId);

                for (Candidate candidate : rankedCandidates) {

                        if (wasCandidateCancelled(
                                        candidate.getId(),
                                        organId)) {

                                continue;
                        }

                        int score = calculateCompatibilityScore(
                                        candidate);

                        Correspondence correspondence = new Correspondence(
                                        candidate.getId(),
                                        organId,
                                        score,
                                        LocalDate.now(),
                                        "PENDING");

                        Correspondence saved = correspondenceRepository
                                        .save(
                                                        correspondence);

                        return toResponse(saved);
                }

                throw new IllegalArgumentException(
                                "No eligible candidates available.");
        }

        // CONFIRM PROPOSAL

        public CorrespondenceResponse confirmProposal(
                        int correspondenceId) {

                Correspondence correspondence = correspondenceRepository
                                .findById(
                                                correspondenceId);

                if (correspondence == null) {

                        throw new IllegalArgumentException(
                                        "Correspondence not found.");
                }

                if (!"PENDING".equals(
                                correspondence.getStatus())) {

                        throw new IllegalArgumentException(
                                        "Only pending correspondences can be confirmed.");
                }

                Candidate candidate = candidateRepository.findById(
                                correspondence
                                                .getCandidateId());

                Organ organ = organRepository.findById(
                                correspondence
                                                .getOrganId());

                if (candidate == null) {

                        throw new IllegalArgumentException(
                                        "Candidate not found.");
                }

                if (organ == null) {

                        throw new IllegalArgumentException(
                                        "Organ not found.");
                }

                correspondence.setStatus(
                                "SELECTED");

                candidate.setStatus(
                                "MATCHED");

                organ.setStatus(
                                "MATCHED");

                return toResponse(
                                correspondence);
        }

        // CANCEL PROPOSAL

        public CorrespondenceResponse cancelProposal(
                        int correspondenceId,
                        String reason) {

                Correspondence correspondence = correspondenceRepository
                                .findById(
                                                correspondenceId);

                if (correspondence == null) {

                        throw new IllegalArgumentException(
                                        "Correspondence not found.");
                }

                if (!"PENDING".equals(
                                correspondence.getStatus())) {

                        throw new IllegalArgumentException(
                                        "Only pending correspondences can be cancelled.");
                }

                if (reason == null ||
                                reason.isBlank()) {

                        throw new IllegalArgumentException(
                                        "Cancellation reason is required.");
                }

                correspondence.setStatus(
                                "CANCELLED");

                correspondence.setCancellationReason(
                                reason);

                return toResponse(
                                correspondence);
        }

        // CANCEL AND CREATE NEXT PROPOSAL

        public CorrespondenceResponse cancelAndCreateNext(
                        int correspondenceId,
                        String reason) {

                CorrespondenceResponse cancelled = cancelProposal(
                                correspondenceId,
                                reason);

                try {

                        return createProposal(
                                        cancelled.organId());

                } catch (IllegalArgumentException e) {

                        if ("No eligible candidates available."
                                        .equals(e.getMessage())) {

                                return cancelled;
                        }

                        throw e;
                }
        }

        // CHECK IF CANDIDATE WAS CANCELLED

        private boolean wasCandidateCancelled(
                        int candidateId,
                        int organId) {

                List<Correspondence> correspondences = correspondenceRepository
                                .findByOrganId(
                                                organId);

                for (Correspondence correspondence : correspondences) {

                        if (correspondence
                                        .getCandidateId() == candidateId
                                        &&
                                        "CANCELLED".equals(
                                                        correspondence
                                                                        .getStatus())) {

                                return true;
                        }
                }

                return false;
        }

        // CONVERT TO RESPONSE

        private CorrespondenceResponse toResponse(
                        Correspondence correspondence) {

                Candidate candidate = candidateRepository.findById(
                                correspondence
                                                .getCandidateId());

                Organ organ = organRepository.findById(
                                correspondence
                                                .getOrganId());

                String candidateName = candidate != null
                                ? candidate.getName()
                                : "Unknown Candidate";

                String organType = organ != null
                                ? organ.getType()
                                : "Unknown Organ";

                return new CorrespondenceResponse(
                                correspondence.getId(),
                                correspondence.getCandidateId(),
                                candidateName,
                                correspondence.getOrganId(),
                                organType,
                                correspondence
                                                .getCompatibilityScore(),
                                correspondence
                                                .getCorrespondenceDate(),
                                correspondence.getStatus(),
                                correspondence
                                                .getCancellationReason());
        }
}
