package br.com.joga_together.dto.game;

import java.util.List;

public record RawgSearchResultDto(
        int rawgId,
        String name,
        String released,
        String imageUrl,
        List<String> genres
) {}
