package com.lifematch.service;

import java.util.List;

import com.lifematch.model.OrganType;
import com.lifematch.repository.OrganTypeRepository;

public class OrganTypeService {

    private final OrganTypeRepository repository;

    public OrganTypeService() {
        this.repository =
                new OrganTypeRepository();
    }


    // CREATE
    public OrganType createOrganType(
            OrganType organType) {

        validateOrganType(organType);

        OrganType existingOrganType =
                repository.findByName(
                        organType.getName()
                );

        if (existingOrganType != null) {

            throw new IllegalArgumentException(
                    "Organ type already exists."
            );
        }

        return repository.save(organType);
    }


    // GET ALL
    public List<OrganType> getAllOrganTypes() {

        return repository.findAll();
    }


    // GET BY ID
    public OrganType getOrganTypeById(int id) {

        return repository.findById(id);
    }


    // UPDATE
    public OrganType updateOrganType(
            int id,
            OrganType organType) {

        OrganType existingOrganType =
                repository.findById(id);

        if (existingOrganType == null) {
            return null;
        }

        validateOrganType(organType);


        // Check if another organ type
        // already uses this name
        OrganType sameName =
                repository.findByName(
                        organType.getName()
                );

        if (sameName != null &&
            sameName.getId() != id) {

            throw new IllegalArgumentException(
                    "Organ type already exists."
            );
        }


        return repository.update(
                id,
                organType
        );
    }


    // DELETE
    public boolean deleteOrganType(int id) {

        return repository.delete(id);
    }


    // VALIDATIONS
    private void validateOrganType(
            OrganType organType) {

        if (organType == null) {

            throw new IllegalArgumentException(
                    "Organ type data is required."
            );
        }


        // NAME
        if (organType.getName() == null ||
            organType.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Organ type name is required."
            );
        }


        // PRIORITY POINTS
        if (organType.getPriorityPoints() < 0 ||
            organType.getPriorityPoints() > 3) {

            throw new IllegalArgumentException(
                    "Priority points must be between 0 and 3."
            );
        }
    }
}
