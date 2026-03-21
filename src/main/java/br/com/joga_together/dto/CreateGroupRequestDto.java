package br.com.joga_together.dto;

import java.util.UUID;

public record CreateGroupRequestDto(
        String name,
        String description,
        UUID masterId
) {
}
