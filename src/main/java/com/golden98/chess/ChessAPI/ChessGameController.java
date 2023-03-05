package com.golden98.chess.ChessAPI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;


@RestController
public class ChessGameController {
    
    private final ChessGameRepository repository;
    private final ChessGameModelAssembler assembler;
    private static final Logger log = LoggerFactory.getLogger(ChessGameController.class);

    ChessGameController(ChessGameRepository repository, ChessGameModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @PostMapping("/chess/api/solo")
    ResponseEntity<?> newGame() {
        // create new game and save it
        EntityModel<SimpleChessGame> entityModel = assembler.toModel(repository.save(new ChessGame()));
        // construct http response
        return ResponseEntity
                    .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(entityModel);
    }

    @GetMapping("/chess/api/solo/{id}")
    EntityModel<SimpleChessGame> one(@PathVariable long id) {
        // get game with this id
        ChessGame chessGame = repository.findById(id).orElseThrow(() -> new ChessGameNotFoundException(id));
        return assembler.toModel(chessGame);
    }

    
    @PutMapping("/chess/api/solo/{id}/{move}")
    ResponseEntity<?> move(@PathVariable long id, @PathVariable String move) {
        // get game from db
        ChessGame chessGame = repository.findById(id).orElseThrow(() -> new ChessGameNotFoundException(id));
        chessGame.doMove(move);
        EntityModel<SimpleChessGame> entityModel = assembler.toModel(repository.save(chessGame));
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(entityModel);
            // TODO check if move is valid
    }
}