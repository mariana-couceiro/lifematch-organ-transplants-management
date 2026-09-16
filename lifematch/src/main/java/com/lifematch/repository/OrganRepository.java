package com.lifematch.repository;

import java.util.ArrayList;
import java.util.List;

import com.lifematch.model.Organ;

public class OrganRepository {

    private static final List<Organ> organs = new ArrayList<>();
    private static int nextId = 1;

    public Organ save(Organ organ) {
        organ.setId(nextId++);
        organs.add(organ);
        return organ;
    }

    public List<Organ> findAll() {
        return organs;
    }

    public Organ findById(int id) {
        for (Organ organ : organs) {
            if (organ.getId() == id) {
                return organ;
            }
        }

        return null;
    }

    public Organ update(int id, Organ updatedOrgan) {

        Organ existingOrgan = findById(id);

        if (existingOrgan == null) {
            return null;
        }

        existingOrgan.setType(updatedOrgan.getType());
        existingOrgan.setBloodType(updatedOrgan.getBloodType());
        existingOrgan.setDonorName(updatedOrgan.getDonorName());
        existingOrgan.setDonorBirthDate(updatedOrgan.getDonorBirthDate());
        existingOrgan.setHospital(updatedOrgan.getHospital());
        existingOrgan.setAvailabilityDate(updatedOrgan.getAvailabilityDate());
        existingOrgan.setStatus(updatedOrgan.getStatus());

        return existingOrgan;
    }

    public boolean delete(int id) {

        Organ organ = findById(id);

        if (organ == null) {
            return false;
        }

        organs.remove(organ);
        return true;
    }
}
