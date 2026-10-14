package com.lifematch.repository;

import java.util.List;

import com.lifematch.model.OrganType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrganTypeRepository {

    @PersistenceContext(unitName = "LifeMatchPU")
    private EntityManager entityManager;


    // CREATE
    @Transactional
    public OrganType save(OrganType organType) {

        entityManager.persist(organType);

        return organType;
    }


    // GET ALL
    public List<OrganType> findAll() {

        return entityManager.createQuery(
                "SELECT o FROM OrganType o ORDER BY o.name",
                OrganType.class
        ).getResultList();
    }


    // GET BY ID
    public OrganType findById(int id) {

        return entityManager.find(OrganType.class, id);
    }


    // GET BY NAME
    public OrganType findByName(String name) {

        List<OrganType> results =
                entityManager.createQuery(
                        "SELECT o FROM OrganType o " +
                        "WHERE LOWER(o.name) = LOWER(:name)",
                        OrganType.class
                )
                .setParameter("name", name)
                .getResultList();

        return results.isEmpty() ? null : results.get(0);
    }


    // UPDATE
    @Transactional
    public OrganType update(
            int id,
            OrganType updatedOrganType) {

        OrganType existingOrganType =
                entityManager.find(OrganType.class, id);

        if (existingOrganType == null) {
            return null;
        }

        existingOrganType.setName(
                updatedOrganType.getName()
        );

        existingOrganType.setPriorityPoints(
                updatedOrganType.getPriorityPoints()
        );

        existingOrganType.setActive(
                updatedOrganType.isActive()
        );

        return existingOrganType;
    }


    // DELETE
    @Transactional
    public boolean delete(int id) {

        OrganType organType =
                entityManager.find(OrganType.class, id);

        if (organType == null) {
            return false;
        }

        entityManager.remove(organType);

        return true;
    }
}

