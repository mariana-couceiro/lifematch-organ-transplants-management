package com.lifematch.repository;

import java.util.List;

import com.lifematch.model.Organ;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class OrganRepository {

    @PersistenceContext(unitName = "LifeMatchPU")
    private EntityManager entityManager;


    // CREATE
    @Transactional
    public Organ save(Organ organ) {

        entityManager.persist(organ);

        return organ;
    }


    // GET ALL
    public List<Organ> findAll() {

        return entityManager.createQuery(
                "SELECT o FROM Organ o ORDER BY o.id",
                Organ.class
        ).getResultList();
    }


    // GET BY ID
    public Organ findById(int id) {

        return entityManager.find(Organ.class, id);
    }


    // UPDATE
    @Transactional
    public Organ update(int id, Organ updatedOrgan) {

        Organ existingOrgan =
                entityManager.find(Organ.class, id);

        if (existingOrgan == null) {
            return null;
        }

        existingOrgan.setType(
                updatedOrgan.getType()
        );

        existingOrgan.setBloodType(
                updatedOrgan.getBloodType()
        );

        existingOrgan.setDonorName(
                updatedOrgan.getDonorName()
        );

        existingOrgan.setDonorCode(
                updatedOrgan.getDonorCode()
        );

        existingOrgan.setDonorBirthDate(
                updatedOrgan.getDonorBirthDate()
        );

        existingOrgan.setHospital(
                updatedOrgan.getHospital()
        );

        existingOrgan.setAvailabilityDate(
                updatedOrgan.getAvailabilityDate()
        );

        existingOrgan.setStatus(
                updatedOrgan.getStatus()
        );

        return existingOrgan;
    }


    // DELETE
    @Transactional
    public boolean delete(int id) {

        Organ organ =
                entityManager.find(Organ.class, id);

        if (organ == null) {
            return false;
        }

        entityManager.remove(organ);

        return true;
    }
}
