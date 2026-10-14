package com.lifematch.repository;

import java.util.List;

import com.lifematch.model.Candidate;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class CandidateRepository {

        @PersistenceContext(unitName = "LifeMatchPU")
        private EntityManager entityManager;

        // CREATE
        @Transactional
        public Candidate save(Candidate candidate) {

                entityManager.persist(candidate);

                return candidate;
        }

        // GET ALL
        public List<Candidate> findAll() {

                return entityManager.createQuery(
                                "SELECT c FROM Candidate c ORDER BY c.id",
                                Candidate.class).getResultList();
        }

        // GET BY ID
        public Candidate findById(int id) {

                return entityManager.find(Candidate.class, id);
        }

        // GET BY NAME
        public List<Candidate> findByName(String name) {

                return entityManager.createQuery(
                                "SELECT c FROM Candidate c " +
                                                "WHERE LOWER(c.name) LIKE :name " +
                                                "ORDER BY c.id",
                                Candidate.class)
                                .setParameter(
                                                "name",
                                                "%" + name.toLowerCase() + "%")
                                .getResultList();
        }

        // UPDATE
        @Transactional
        public Candidate update(
                        int id,
                        Candidate updatedCandidate) {

                Candidate existingCandidate = entityManager.find(Candidate.class, id);

                if (existingCandidate == null) {
                        return null;
                }

                existingCandidate.setName(
                                updatedCandidate.getName());

                existingCandidate.setBirthDate(
                                updatedCandidate.getBirthDate());

                existingCandidate.setBloodType(
                                updatedCandidate.getBloodType());

                existingCandidate.setRequiredOrgan(
                                updatedCandidate.getRequiredOrgan());

                existingCandidate.setUrgency(
                                updatedCandidate.getUrgency());

                existingCandidate.setPriority(
                                updatedCandidate.getPriority());

                existingCandidate.setWaitingListEntryDate(
                                updatedCandidate.getWaitingListEntryDate());

                existingCandidate.setStatus(
                                updatedCandidate.getStatus());

                return existingCandidate;
        }

        // DELETE
        @Transactional
        public boolean delete(int id) {

                Candidate candidate = entityManager.find(Candidate.class, id);

                if (candidate == null) {
                        return false;
                }

                entityManager.remove(candidate);

                return true;
        }
}
