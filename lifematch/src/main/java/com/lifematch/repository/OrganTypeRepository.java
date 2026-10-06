package com.lifematch.repository;

import java.util.ArrayList;
import java.util.List;

import com.lifematch.model.OrganType;

public class OrganTypeRepository {

    private static final List<OrganType> organTypes =
            new ArrayList<>();

    private static int nextId = 1;


    // CREATE
    public OrganType save(OrganType organType) {

        organType.setId(nextId++);

        organTypes.add(organType);

        return organType;
    }


    // GET ALL
    public List<OrganType> findAll() {

        return organTypes;
    }


    // GET BY ID
    public OrganType findById(int id) {

        for (OrganType organType : organTypes) {

            if (organType.getId() == id) {

                return organType;
            }
        }

        return null;
    }


    // GET BY NAME
    public OrganType findByName(String name) {

        for (OrganType organType : organTypes) {

            if (organType.getName()
                    .equalsIgnoreCase(name)) {

                return organType;
            }
        }

        return null;
    }


    // UPDATE
    public OrganType update(
            int id,
            OrganType updatedOrganType) {

        OrganType existingOrganType =
                findById(id);


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
    public boolean delete(int id) {

        OrganType organType =
                findById(id);


        if (organType == null) {

            return false;
        }


        organTypes.remove(organType);

        return true;
    }
}
