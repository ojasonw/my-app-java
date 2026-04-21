package br.com.joga_together.dto.game.rawg;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RawgApiGameDto(
        int id,
        String name,
        String released,
        @JsonProperty("background_image") String backgroundImage,
        List<RawgApiGenreDto> genres
) {}
