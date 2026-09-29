package com.lifematch.dto;

import java.time.LocalDate;

public record CorrespondenceResponse(
        int id,
        int candidateId,
        String candidateName,
        int organId,
        String organType,
        int compatibilityScore,
        LocalDate correspondenceDate,
        String status,
        String cancellationReason
) {
}