package br.com.joga_together.dto.auth;

import java.time.Instant;

public record AuthenticationResponseDto(String token, String type, Instant expiresAt, String username) {
}

