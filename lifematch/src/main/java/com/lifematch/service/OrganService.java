package com.lifematch.service;

import java.time.LocalDate;
import java.util.List;

import com.lifematch.model.Organ;
import com.lifematch.repository.OrganRepository;

public class OrganService {

    private final OrganRepository repository;

    public OrganService() {
        this.repository = new OrganRepository();
    }

    public Organ createOrgan(Organ organ) {
        validateOrgan(organ);

        Organ savedOrgan = repository.save(organ);

        savedOrgan.setDonorCode(
            "DNR-" + String.format("%04d", savedOrgan.getId())
        );

        return savedOrgan;
    }

    public List<Organ> getAllOrgans() {
        return repository.findAll();
    }

    public Organ getOrganById(int id) {
        return repository.findById(id);
    }

    public Organ updateOrgan(int id, Organ organ) {
        validateOrgan(organ);
        return repository.update(id, organ);
    }

    public boolean deleteOrgan(int id) {
        return repository.delete(id);
    }

    private void validateOrgan(Organ organ) {

        if (organ.getType() == null || organ.getType().isBlank()) {
            throw new IllegalArgumentException("Organ type is required.");
        }

        if (!isValidOrganType(organ.getType())) {
            throw new IllegalArgumentException("Invalid organ type.");
        }

        if (organ.getBloodType() == null || organ.getBloodType().isBlank()) {
            throw new IllegalArgumentException("Blood type is required.");
        }

        if (!isValidBloodType(organ.getBloodType())) {
            throw new IllegalArgumentException("Invalid blood type.");
        }

        if (organ.getDonorName() == null || organ.getDonorName().isBlank()) {
            throw new IllegalArgumentException("Donor name is required.");
        }

        if (organ.getDonorBirthDate() == null) {
            throw new IllegalArgumentException("Donor birth date is required.");
        }

        if (organ.getDonorBirthDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                "Donor birth date cannot be in the future."
            );
        }

        if (organ.getHospital() == null || organ.getHospital().isBlank()) {
            throw new IllegalArgumentException("Hospital is required.");
        }

        if (organ.getAvailabilityDate() == null) {
            throw new IllegalArgumentException(
                "Availability date is required."
            );
        }

        if (organ.getAvailabilityDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                "Availability date cannot be in the future."
            );
        }

        if (organ.getStatus() == null || organ.getStatus().isBlank()) {
            throw new IllegalArgumentException("Organ status is required.");
        }

        if (!isValidStatus(organ.getStatus())) {
            throw new IllegalArgumentException("Invalid organ status.");
        }
    }

    private boolean isValidBloodType(String bloodType) {
        return bloodType.equals("A+") ||
                bloodType.equals("A-") ||
                bloodType.equals("B+") ||
                bloodType.equals("B-") ||
                bloodType.equals("AB+") ||
                bloodType.equals("AB-") ||
                bloodType.equals("0+") ||
                bloodType.equals("0-");
    }

    private boolean isValidOrganType(String type) {
        return type.equals("Kidney") ||
                type.equals("Liver") ||
                type.equals("Heart") ||
                type.equals("Lung") ||
                type.equals("Pancreas") ||
                type.equals("Small Intestine");
    }

    private boolean isValidStatus(String status) {
        return status.equals("AVAILABLE") ||
                status.equals("MATCHED") ||
                status.equals("INACTIVE");
    }
}
