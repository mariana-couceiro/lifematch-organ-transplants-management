package com.lifematch.repository;

import java.util.ArrayList;
import java.util.List;

import com.lifematch.model.Correspondence;

public class CorrespondenceRepository {

    private static final List<Correspondence> correspondences =
            new ArrayList<>();

    private static int nextId = 1;

    public Correspondence save(Correspondence correspondence) {

        correspondence.setId(nextId++);
        correspondences.add(correspondence);

        return correspondence;
    }

    public List<Correspondence> findAll() {
        return correspondences;
    }

    public Correspondence findById(int id) {

        for (Correspondence correspondence : correspondences) {

            if (correspondence.getId() == id) {
                return correspondence;
            }
        }

        return null;
    }

    public List<Correspondence> findByOrganId(int organId) {

        List<Correspondence> results = new ArrayList<>();

        for (Correspondence correspondence : correspondences) {

            if (correspondence.getOrganId() == organId) {
                results.add(correspondence);
            }
        }

        return results;
    }

    public List<Correspondence> findByCandidateId(int candidateId) {

        List<Correspondence> results = new ArrayList<>();

        for (Correspondence correspondence : correspondences) {

            if (correspondence.getCandidateId() == candidateId) {
                results.add(correspondence);
            }
        }

        return results;
    }

    public Correspondence findPendingByOrganId(int organId) {

        for (Correspondence correspondence : correspondences) {

            if (correspondence.getOrganId() == organId &&
                correspondence.getStatus().equalsIgnoreCase("PENDING")) {

                return correspondence;
            }
        }

        return null;
    }
}
