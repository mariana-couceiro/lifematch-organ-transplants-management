package com.lifematch.dto;

public record LoginResponse(
        int id,
        String username,
        String role
) {
}
