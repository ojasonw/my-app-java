package br.com.joga_together.dto.game.rawg;

import java.util.List;

public record RawgApiSearchResponseDto(
        int count,
        List<RawgApiGameDto> results
) {}
