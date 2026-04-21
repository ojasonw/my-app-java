package br.com.joga_together.dto.game.rawg;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RawgApiGameDetailDto(
        int id,
        String name,
        @JsonProperty("description_raw") String descriptionRaw,
        String released,
        @JsonProperty("background_image") String backgroundImage,
        List<RawgApiGenreDto> genres,
        List<RawgApiDeveloperDto> developers
) {}
