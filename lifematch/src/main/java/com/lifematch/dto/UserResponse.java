package com.lifematch.dto;

public record UserResponse(
        int id,
        String username,
        String role
) {
}
