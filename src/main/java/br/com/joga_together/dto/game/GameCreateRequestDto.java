package br.com.joga_together.dto.game;

public record GameCreateRequestDto(
        String title,
        String description,
        String releaseDate,
        String developer,
        String genre
) {
}
