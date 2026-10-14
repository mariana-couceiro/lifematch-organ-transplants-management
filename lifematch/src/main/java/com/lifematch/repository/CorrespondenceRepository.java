package com.lifematch.repository;

import java.util.List;

import com.lifematch.model.Correspondence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CorrespondenceRepository {

    @PersistenceContext(unitName = "LifeMatchPU")
    private EntityManager entityManager;


    // CREATE
    @Transactional
    public Correspondence save(Correspondence correspondence) {

        entityManager.persist(correspondence);

        return correspondence;
    }


    // GET ALL
    public List<Correspondence> findAll() {

        return entityManager.createQuery(
                "SELECT c FROM Correspondence c ORDER BY c.id",
                Correspondence.class
        ).getResultList();
    }


    // GET BY ID
    public Correspondence findById(int id) {

        return entityManager.find(Correspondence.class, id);
    }


    // GET BY ORGAN ID
    public List<Correspondence> findByOrganId(int organId) {

        return entityManager.createQuery(
                "SELECT c FROM Correspondence c " +
                "WHERE c.organId = :organId " +
                "ORDER BY c.id",
                Correspondence.class
        )
        .setParameter("organId", organId)
        .getResultList();
    }


    // GET BY CANDIDATE ID
    public List<Correspondence> findByCandidateId(int candidateId) {

        return entityManager.createQuery(
                "SELECT c FROM Correspondence c " +
                "WHERE c.candidateId = :candidateId " +
                "ORDER BY c.id",
                Correspondence.class
        )
        .setParameter("candidateId", candidateId)
        .getResultList();
    }


    // GET PENDING BY ORGAN ID
    public Correspondence findPendingByOrganId(int organId) {

        List<Correspondence> results =
                entityManager.createQuery(
                        "SELECT c FROM Correspondence c " +
                        "WHERE c.organId = :organId " +
                        "AND UPPER(c.status) = :status " +
                        "ORDER BY c.id",
                        Correspondence.class
                )
                .setParameter("organId", organId)
                .setParameter("status", "PENDING")
                .setMaxResults(1)
                .getResultList();

        if (results.isEmpty()) {
            return null;
        }

        return results.get(0);
    }


    // UPDATE
    @Transactional
    public Correspondence update(Correspondence correspondence) {

        return entityManager.merge(correspondence);
    }
}
