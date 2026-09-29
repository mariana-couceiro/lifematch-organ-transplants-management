package com.lifematch.service;

import java.time.LocalDate;
import java.util.List;

import com.lifematch.dto.OrganEditResponse;
import com.lifematch.model.Organ;
import com.lifematch.repository.OrganRepository;

public class OrganService {

    private final OrganRepository repository;

    public OrganService() {
        this.repository = new OrganRepository();
    }

    // CREATE
    public Organ createOrgan(Organ organ) {

        validateOrgan(organ);

        Organ savedOrgan = repository.save(organ);

        String donorCode = String.format(
                "DNR-%04d",
                savedOrgan.getId());

        savedOrgan.setDonorCode(donorCode);

        return savedOrgan;
    }

    // GET ALL
    public List<Organ> getAllOrgans() {
        return repository.findAll();
    }

    // GET BY ID
    public Organ getOrganById(int id) {
        return repository.findById(id);
    }

    // UPDATE
    public Organ updateOrgan(
            int id,
            Organ organ) {

        Organ existingOrgan = repository.findById(id);

        if (existingOrgan == null) {
            return null;
        }

        validateOrgan(organ);

        return repository.update(
                id,
                organ);
    }

    // DELETE
    public boolean deleteOrgan(int id) {
        return repository.delete(id);
    }

    // RESPONSE USED FOR EDITING
    public OrganEditResponse toEditResponse(
            Organ organ) {

        return new OrganEditResponse(
                organ.getId(),
                organ.getType(),
                organ.getBloodType(),
                organ.getDonorName(),
                organ.getDonorCode(),
                organ.getDonorBirthDate(),
                organ.getHospital(),
                organ.getAvailabilityDate(),
                organ.getStatus());
    }

    // VALIDATIONS
    private void validateOrgan(Organ organ) {

        if (organ == null) {
            throw new IllegalArgumentException(
                    "Organ data is required.");
        }

        // ORGAN TYPE
        if (organ.getType() == null ||
                organ.getType().isBlank()) {

            throw new IllegalArgumentException(
                    "Organ type is required.");
        }

        if (!isValidOrganType(
                organ.getType())) {

            throw new IllegalArgumentException(
                    "Invalid organ type.");
        }

        // BLOOD TYPE
        if (organ.getBloodType() == null ||
                organ.getBloodType().isBlank()) {

            throw new IllegalArgumentException(
                    "Blood type is required.");
        }

        if (!isValidBloodType(
                organ.getBloodType())) {

            throw new IllegalArgumentException(
                    "Invalid blood type.");
        }

        // DONOR NAME
        if (organ.getDonorName() == null ||
                organ.getDonorName().isBlank()) {

            throw new IllegalArgumentException(
                    "Donor name is required.");
        }

        // DONOR BIRTH DATE
        if (organ.getDonorBirthDate() == null) {

            throw new IllegalArgumentException(
                    "Donor birth date is required.");
        }

        if (organ.getDonorBirthDate()
                .isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Donor birth date cannot be in the future.");
        }

        // HOSPITAL
        if (organ.getHospital() == null ||
                organ.getHospital().isBlank()) {

            throw new IllegalArgumentException(
                    "Hospital is required.");
        }

        // AVAILABILITY DATE
        if (organ.getAvailabilityDate() == null) {

            throw new IllegalArgumentException(
                    "Availability date is required.");
        }

        if (organ.getAvailabilityDate()
                .isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Availability date cannot be in the future.");
        }

        // STATUS
        if (organ.getStatus() == null ||
                organ.getStatus().isBlank()) {

            throw new IllegalArgumentException(
                    "Organ status is required.");
        }

        if (!isValidStatus(
                organ.getStatus())) {

            throw new IllegalArgumentException(
                    "Invalid organ status.");
        }
    }

    // VALID ORGAN TYPES
    private boolean isValidOrganType(
            String type) {

        return type.equalsIgnoreCase("Kidney") ||
                type.equalsIgnoreCase("Liver") ||
                type.equalsIgnoreCase("Heart") ||
                type.equalsIgnoreCase("Lung") ||
                type.equalsIgnoreCase("Pancreas") ||
                type.equalsIgnoreCase("Small Intestine");
    }

    // VALID BLOOD TYPES
    private boolean isValidBloodType(
            String bloodType) {

        return bloodType.equals("A+") ||
                bloodType.equals("A-") ||
                bloodType.equals("B+") ||
                bloodType.equals("B-") ||
                bloodType.equals("AB+") ||
                bloodType.equals("AB-") ||
                bloodType.equals("0+") ||
                bloodType.equals("0-");
    }

    // VALID ORGAN STATUS
    private boolean isValidStatus(
            String status) {

        return status.equalsIgnoreCase("AVAILABLE") ||
                status.equalsIgnoreCase("MATCHED") ||
                status.equalsIgnoreCase("INACTIVE");
    }
}
