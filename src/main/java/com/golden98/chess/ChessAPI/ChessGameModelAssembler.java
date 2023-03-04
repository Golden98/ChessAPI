package com.golden98.chess.ChessAPI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;


@Component
public class ChessGameModelAssembler implements RepresentationModelAssembler<ChessGame, EntityModel<SimpleChessGame>>{



    @Override
    public EntityModel<SimpleChessGame> toModel (ChessGame chessGame) {
        return EntityModel.of(new SimpleChessGame(chessGame), 
            linkTo(methodOn(ChessGameController.class).one(chessGame.getId())).withSelfRel());
        //TODO add links to each available move
    }
}