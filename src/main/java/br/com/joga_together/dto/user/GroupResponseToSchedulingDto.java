package br.com.joga_together.dto.user;

import java.util.UUID;

public record GroupResponseToSchedulingDto(
        UUID id,
        String name
) {
}
