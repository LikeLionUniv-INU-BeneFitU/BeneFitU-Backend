package com.fitu.benefitu.domain.auth.dto;

public record AuthSignupRequest(
        String username,
        String password
) {
}
