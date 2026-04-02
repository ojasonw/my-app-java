package br.com.joga_together.dto.group;

import java.util.UUID;

public record CreateGroupRequestDto(
        String name,
        String description,
        UUID masterId
) {
}
