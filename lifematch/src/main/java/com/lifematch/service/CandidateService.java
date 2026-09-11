package com.lifematch.service;

import java.time.LocalDate;
import java.util.List;

import com.lifematch.model.Candidate;
import com.lifematch.repository.CandidateRepository;

public class CandidateService {

    private final CandidateRepository repository;

    public CandidateService() {
        this.repository = new CandidateRepository();
    }

    public Candidate createCandidate(Candidate candidate) {
        validateCandidate(candidate);
        return repository.save(candidate);
    }

    public List<Candidate> getAllCandidates() {
        return repository.findAll();
    }

    public Candidate getCandidateById(int id) {
        return repository.findById(id);
    }

    public Candidate updateCandidate(int id, Candidate candidate) {
        validateCandidate(candidate);
        return repository.update(id, candidate);
    }

    public boolean deleteCandidate(int id) {
        return repository.delete(id);
    }

    private void validateCandidate(Candidate candidate) {

        // Nome obrigatório
        if (candidate.getName() == null || candidate.getName().isBlank()) {
            throw new IllegalArgumentException(
                    "Candidate name is required."
            );
        }

        // Data de nascimento obrigatória
        if (candidate.getBirthDate() == null) {
            throw new IllegalArgumentException(
                    "Birth date is required."
            );
        }

        // Data de nascimento não pode ser futura
        if (candidate.getBirthDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Birth date cannot be in the future."
            );
        }

        // Grupo sanguíneo obrigatório
        if (candidate.getBloodType() == null ||
                candidate.getBloodType().isBlank()) {

            throw new IllegalArgumentException(
                    "Blood type is required."
            );
        }

        // Grupo sanguíneo válido
        if (!isValidBloodType(candidate.getBloodType())) {
            throw new IllegalArgumentException(
                    "Invalid blood type."
            );
        }

        // Órgão necessário obrigatório
        if (candidate.getRequiredOrgan() == null ||
                candidate.getRequiredOrgan().isBlank()) {

            throw new IllegalArgumentException(
                    "Required organ is required."
            );
        }

        // Prioridade entre 1 e 4
        if (candidate.getPriority() < 1 ||
                candidate.getPriority() > 4) {

            throw new IllegalArgumentException(
                    "Priority must be between 1 and 4."
            );
        }

        // Data de entrada na lista obrigatória
        if (candidate.getWaitingListEntryDate() == null) {
            throw new IllegalArgumentException(
                    "Waiting list entry date is required."
            );
        }

        // Data de entrada não pode ser futura
        if (candidate.getWaitingListEntryDate()
                .isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Waiting list entry date cannot be in the future."
            );
        }
    }

    private boolean isValidBloodType(String bloodType) {

        return bloodType.equals("A+") ||
                bloodType.equals("A-") ||
                bloodType.equals("B+") ||
                bloodType.equals("B-") ||
                bloodType.equals("AB+") ||
                bloodType.equals("AB-") ||
                bloodType.equals("O+") ||
                bloodType.equals("O-");
    }
}
