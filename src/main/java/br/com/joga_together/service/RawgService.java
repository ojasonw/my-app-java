package br.com.joga_together.service;

import br.com.joga_together.dto.game.RawgSearchResultDto;
import br.com.joga_together.dto.game.rawg.RawgApiGameDetailDto;
import br.com.joga_together.dto.game.rawg.RawgApiSearchResponseDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class RawgService {

    private final RestClient restClient;

    @Value("${rawg.api.key}")
    private String apiKey;

    public RawgService(@Qualifier("rawgRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public List<RawgSearchResultDto> search(String query) {
        RawgApiSearchResponseDto response = restClient.get()
                .uri("/games?search={q}&key={key}&page_size=10&search_precise=true", query, apiKey)
                .retrieve()
                .body(RawgApiSearchResponseDto.class);

        if (response == null || response.results() == null) return List.of();

        return response.results().stream()
                .map(g -> new RawgSearchResultDto(
                        g.id(),
                        g.name(),
                        g.released(),
                        g.backgroundImage(),
                        g.genres() != null
                                ? g.genres().stream().map(genre -> genre.name()).toList()
                                : List.of()
                ))
                .toList();
    }

    public RawgApiGameDetailDto getDetail(int rawgId) {
        return restClient.get()
                .uri("/games/{id}?key={key}", rawgId, apiKey)
                .retrieve()
                .body(RawgApiGameDetailDto.class);
    }
}
