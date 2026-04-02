package br.com.joga_together.dto.game;

import java.util.UUID;

public record GameResponseDto(
        UUID id,
        String title,
        String description,
        String releaseDate,
        String developer,
        String genre
){
}
