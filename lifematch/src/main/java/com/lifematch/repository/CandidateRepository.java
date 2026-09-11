package com.lifematch.repository;

import java.util.ArrayList;
import java.util.List;

import com.lifematch.model.Candidate;

public class CandidateRepository {

    private static final List<Candidate> candidates = new ArrayList<>();
    private static int nextId = 1;

    public Candidate save(Candidate candidate) {
        candidate.setId(nextId++);
        candidates.add(candidate);
        return candidate;
    }

    public List<Candidate> findAll() {
        return candidates;
    }

    public Candidate findById(int id) {
        for (Candidate candidate : candidates) {
            if (candidate.getId() == id) {
                return candidate;
            }
        }

        return null;
    }

    public Candidate update(int id, Candidate updatedCandidate) {
        Candidate existingCandidate = findById(id);

        if (existingCandidate == null) {
            return null;
        }

        existingCandidate.setName(updatedCandidate.getName());
        existingCandidate.setBirthDate(updatedCandidate.getBirthDate());
        existingCandidate.setBloodType(updatedCandidate.getBloodType());
        existingCandidate.setRequiredOrgan(updatedCandidate.getRequiredOrgan());
        existingCandidate.setPriority(updatedCandidate.getPriority());
        existingCandidate.setWaitingListEntryDate(updatedCandidate.getWaitingListEntryDate());
        existingCandidate.setStatus(updatedCandidate.getStatus());

        return existingCandidate;
    }

    public boolean delete(int id) {
        Candidate candidate = findById(id);

        if (candidate == null) {
            return false;
        }

        candidates.remove(candidate);
        return true;
    }
}
