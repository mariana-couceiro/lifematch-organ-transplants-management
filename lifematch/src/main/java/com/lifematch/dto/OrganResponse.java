package com.lifematch.dto;

import java.time.LocalDate;

public record OrganResponse(
    int id,
    String type,
    String bloodType,
    String donorCode,
    String hospital,
    LocalDate availabilityDate,
    String status
) {
}
