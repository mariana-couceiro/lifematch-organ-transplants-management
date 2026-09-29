package com.lifematch.dto;

import java.time.LocalDate;

public record OrganEditResponse(
        int id,
        String type,
        String bloodType,
        String donorName,
        String donorCode,
        LocalDate donorBirthDate,
        String hospital,
        LocalDate availabilityDate,
        String status
) {
}
