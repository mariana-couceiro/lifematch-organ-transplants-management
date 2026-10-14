package com.lifematch.dto;

public record LoginRequest(
        String username,
        String password
) {
}
