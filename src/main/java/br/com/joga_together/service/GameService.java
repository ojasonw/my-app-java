package br.com.joga_together.service;

import br.com.joga_together.dto.game.GameCreateRequestDto;
import br.com.joga_together.dto.game.GameResponseDto;
import br.com.joga_together.dto.game.RawgSearchResultDto;
import br.com.joga_together.dto.game.rawg.RawgApiGameDetailDto;
import br.com.joga_together.exception.GameAlreadyExistsException;
import br.com.joga_together.model.Game;
import br.com.joga_together.repository.GameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private RawgService rawgService;

    @Transactional
    public GameResponseDto createGame(GameCreateRequestDto dto) {
        if (gameExists(dto.title())) {
            throw new GameAlreadyExistsException("Game with title " + dto.title() + " already exists.");
        }
        Game game = gameDtoToEntity(dto);
        gameRepository.save(game);
        return gameEntityToResponseDto(game);
    }

    public List<RawgSearchResultDto> searchGames(String query) {
        return rawgService.search(query);
    }

    @Transactional
    public GameResponseDto importFromRawg(int rawgId) {
        RawgApiGameDetailDto detail = rawgService.getDetail(rawgId);
        if (detail == null) {
            throw new RuntimeException("Game not found on RAWG with id: " + rawgId);
        }

        if (gameExists(detail.name())) {
            return gameRepository.findByTitle(detail.name())
                    .map(this::gameEntityToResponseDto)
                    .orElseThrow();
        }

        String developer = (detail.developers() != null && !detail.developers().isEmpty())
                ? detail.developers().get(0).name()
                : "N/A";
        String genre = (detail.genres() != null && !detail.genres().isEmpty())
                ? detail.genres().get(0).name()
                : "N/A";
        String rawDescription = detail.descriptionRaw() != null ? detail.descriptionRaw() : "";
        String description = rawDescription.length() > 255 ? rawDescription.substring(0, 252) + "..." : rawDescription;
        LocalDate releaseDate = (detail.released() != null && !detail.released().isBlank())
                ? LocalDate.parse(detail.released())
                : null;

        Game game = new Game(null, detail.name(), description, releaseDate, developer, genre, new HashSet<>());
        gameRepository.save(game);
        return gameEntityToResponseDto(game);
    }

    private Game gameDtoToEntity(GameCreateRequestDto dto) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new Game(
                null,
                dto.title(),
                dto.description(),
                LocalDate.from(dateTimeFormatter.parse(dto.releaseDate())),
                dto.developer(),
                dto.genre(),
                null
        );
    }

    private GameResponseDto gameEntityToResponseDto(Game game) {
        return new GameResponseDto(
                game.getId(),
                game.getTitle(),
                game.getDescription(),
                game.getReleaseDate() != null ? game.getReleaseDate().toString() : null,
                game.getDeveloper(),
                game.getGenre()
        );
    }

    public List<GameResponseDto> getAllGames() {
        return gameRepository.findAll().stream()
                .map(this::gameEntityToResponseDto)
                .toList();
    }

    private boolean gameExists(String title) {
        return gameRepository.findByTitle(title).isPresent();
    }
}
