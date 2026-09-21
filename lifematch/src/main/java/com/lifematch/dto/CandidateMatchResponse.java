package com.lifematch.dto;

public record CandidateMatchResponse(
        int position,
        int candidateId,
        String candidateName,
        int priority,
        int score
) {
}
