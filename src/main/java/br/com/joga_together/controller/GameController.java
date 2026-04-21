package br.com.joga_together.controller;

import br.com.joga_together.dto.game.GameCreateRequestDto;
import br.com.joga_together.dto.game.GameResponseDto;
import br.com.joga_together.dto.game.RawgSearchResultDto;
import br.com.joga_together.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/create")
    public ResponseEntity<GameResponseDto> createGame(@RequestBody GameCreateRequestDto dto) {
        GameResponseDto game = gameService.createGame(dto);
        return ResponseEntity.created(URI.create("/games/" + game.id())).body(game);
    }

    @GetMapping("/all")
    public ResponseEntity<List<GameResponseDto>> allGames() {
        return ResponseEntity.ok(gameService.getAllGames());
    }

    @GetMapping("/search")
    public ResponseEntity<List<RawgSearchResultDto>> searchGames(@RequestParam("q") String query) {
        return ResponseEntity.ok(gameService.searchGames(query));
    }

    @PostMapping("/import")
    public ResponseEntity<GameResponseDto> importGame(@RequestParam("rawgId") int rawgId) {
        GameResponseDto game = gameService.importFromRawg(rawgId);
        return ResponseEntity.created(URI.create("/games/" + game.id())).body(game);
    }
}
