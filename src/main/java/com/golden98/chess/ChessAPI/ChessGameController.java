package com.golden98.chess.ChessAPI;

import java.util.List;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;


@RestController
public class ChessGameController {
    
    private final ChessGameRepository repository;
    private final ChessGameModelAssembler assembler;
    private final GreedyEngine greedyEngine;
    // private static final Logger log = LoggerFactory.getLogger(ChessGameController.class);

    ChessGameController(ChessGameRepository repository, ChessGameModelAssembler assembler, GreedyEngine greedyEngine) {
        this.repository = repository;
        this.assembler = assembler;
        this.greedyEngine = greedyEngine;
    }

    @PostMapping("/chess/api/solo/create")
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

    
    @PatchMapping("/chess/api/solo/{id}/{move}")
    ResponseEntity<?> move(@PathVariable long id, @PathVariable String move) {
        // get game from db
        ChessGame chessGame = repository.findById(id).orElseThrow(() -> new ChessGameNotFoundException(id));
        // is idempotent since repeated requests will not effect board, only the first will be a valid move
        List<String> moves = chessGame.getBoard().legalMoves().stream()
                            .map(m -> m.toString())
                            .toList();
        
        if (moves.contains(move)) {
            chessGame.doMove(move);
            EntityModel<SimpleChessGame> entityModel = assembler.toModel(repository.save(chessGame));
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(entityModel);
        } else {
            // use error defined in rfc7807, TODO add error type to Problem
            return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                    .withTitle("Method not allowed")
                    .withStatus(HttpStatus.METHOD_NOT_ALLOWED)
                    .withDetail("The Move '" + move + "' is invalid for the board id '" + id + "'")
                    .withInstance(linkTo(methodOn(ChessGameController.class).move(chessGame.getId(), move)).toUri()));
        }
    }

    @PatchMapping("/chess/api/solo/{id}/ai")
    ResponseEntity<?> ai(@PathVariable long id) {
        ChessGame chessGame = repository.findById(id).orElseThrow(() -> new ChessGameNotFoundException(id));
        greedyEngine.doBestMove(chessGame);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(assembler.toModel(repository.save(chessGame)));
    }

    @PatchMapping("/chess/api/solo/{id}/undo")
    ResponseEntity<?> undo(@PathVariable long id) {
        ChessGame chessGame = repository.findById(id).orElseThrow(() -> new ChessGameNotFoundException(id));
        if (chessGame.getBoard().getBackup().size() == 0) {
            return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                    .withTitle("Method not allowed")
                    .withStatus(HttpStatus.METHOD_NOT_ALLOWED)
                    .withDetail("Cannot undo a move when no moves have been played")
                    .withInstance(linkTo(methodOn(ChessGameController.class).undo(chessGame.getId())).toUri()));   
        }

        chessGame.undoMove();
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(assembler.toModel(repository.save(chessGame)));
    }
}